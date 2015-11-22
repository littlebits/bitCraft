package cc.littleBits.cloudmod.Blocks;

import java.util.Random;

import cc.littleBits.cloudmod.CloudMod;
import cc.littleBits.cloudmod.network.NBTMessage;
import cc.littleBits.cloudmod.tileentities.TileEntityCloud;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCloud extends Block implements ITileEntityProvider{
	
	int ticksPerUpdate = 0;

	public BlockCloud(Material material) {
		super(material);
 		// Hardness is the same as stone
 		this.setHardness(1.5F);
 		// Explosion resistance is the same as stone
 		this.setResistance(10.0F);
 		// Block is transparent to light
 		this.setLightOpacity(0);
 		// Pickaxe the preferred tool, no tool level requirement
 		this.setHarvestLevel("pickaxe", 0);
 		//Set bounding box to be closer to the model dimensions
 		this.setBlockBounds(0F, 0F, 0F, 1F, 0.6F, 1F);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		// Set metadata of block to correspond with placement orientation
		int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if(l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}
		else if(l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}
		else if(l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}
		else if(l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
		
		// Pass changes to client and notify neighbors
		world.scheduleBlockUpdate(x,y,z, this, ticksPerUpdate);
		this.notifyNeighbors(world, x, y, z);
		
		// If the player has already configured a cloud gateway, apply their default values to the new block
		if(CloudMod.playerManager.isPlayerInDB(player.getUniqueID())) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("posX", Integer.toString(x));
			nbt.setString("posY", Integer.toString(y));
			nbt.setString("posZ", Integer.toString(z));
			nbt.setString("authToken", CloudMod.playerManager.getAuth(player.getPersistentID()));
			nbt.setString("devID", CloudMod.playerManager.getDevID(player.getPersistentID()));
			nbt.setString("ownerID", player.getUniqueID().toString());
			nbt.setString("setTime", String.valueOf(System.currentTimeMillis()));
			nbt.setBoolean("direction", true);
			nbt.setInteger("dimension", world.provider.dimensionId);
		
			// Send the NBT data to the server so it updates first
			CloudMod.network.sendToServer(new NBTMessage(nbt));
		}
	}
	
	@Override
	public void breakBlock(World world, int posX, int posY, int posZ, Block block, int meta) {
		// Run only server-side
 		if(!world.isRemote) {
  			// Get the tileEntity associated with this block
 			int dimension = world.provider.dimensionId;
 			TileEntityCloud t = (TileEntityCloud)(MinecraftServer.getServer().worldServerForDimension(dimension).getTileEntity(posX, posY, posZ));
 			
  			// If the tileEntity is being managed by the connection manager, remove it
 			t.setPower(0);
 			if(CloudMod.cloudManager.isRegistered(t)) {
  				CloudMod.cloudManager.deRegister(t);
  			}
  		}
 		this.notifyNeighbors(world, posX, posY, posZ);
		super.breakBlock(world, posX, posY, posZ, block , meta);
	}
	
	// Returns the redstone power level provided by this block. Up-to-date power levels
	// are stored in the associated tileEntity.
	@Override
    public int isProvidingWeakPower(IBlockAccess world, int posX, int posY, int posZ, int side) {
		// Check if the requested side is the same as the bitSnap side
		
		TileEntityCloud te = (TileEntityCloud) world.getTileEntity(posX, posY, posZ);
		if(te.getDirection()) {
			if (world.getBlockMetadata(posX, posY, posZ) == side) {
				// if bitSnap side, return current power level
				return ((TileEntityCloud) world.getTileEntity(posX, posY, posZ)).getInputPower();
			}
			else {
				// if not bitSnap side, return 0 power
				return 0;
			}
		}
		return super.isProvidingWeakPower(world, posX, posY, posZ, side);
    }
	
	@Override
    public boolean canProvidePower() {
		return true;
    }
	
	// When called, notifies all neighboring blocks of a change, e.g., redstone signal
	// change.
	public void notifyNeighbors(World world, int posX, int posY, int posZ) {
		world.notifyBlockOfNeighborChange(posX+1, posY, posZ ,this);
	    world.notifyBlockOfNeighborChange(posX-1, posY, posZ ,this);
	    world.notifyBlockOfNeighborChange(posX, posY+1, posZ ,this);
	    world.notifyBlockOfNeighborChange(posX, posY-1, posZ ,this);
	    world.notifyBlockOfNeighborChange(posX, posY, posZ+1 ,this);
	    world.notifyBlockOfNeighborChange(posX, posY, posZ-1 ,this);
	}
	
	// Every tick, update outgoing power if configured as an xmitter
	@Override
    public void updateTick(World world, int posX, int posY, int posZ, Random random){
     	super.updateTick(world, posX, posY, posZ, null);
    	this.notifyNeighbors(world, posX, posY, posZ);
    	world.scheduleBlockUpdate(posX, posY, posZ, this, ticksPerUpdate);
    	
    	int dimension = world.provider.dimensionId;
		TileEntityCloud te = (TileEntityCloud)(MinecraftServer.getServer().worldServerForDimension(dimension).getTileEntity(posX, posY, posZ));
    	
    	if(!te.getDirection()) {
    		float amplitude = ( world.getStrongestIndirectPower(posX, posY, posZ) / 15.0F) * 100.0F; 
    		te.setPower(Math.round(amplitude));
    	}
    	
    }
	
	@Override
	public TileEntity createNewTileEntity(World world, int var1) {
		return new TileEntityCloud();
	}
	
	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}
	
	// Don't render block, we define the rendering ourselves
	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	public boolean renderAsNormalBlock() {
		return false;
	}

	protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
	
	// If the player owns the block, open a GUI. If not, deny and notify
	@Override
 	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			TileEntityCloud te = (TileEntityCloud) world.getTileEntity(x, y, z);
			
			if(te.getOwnerID().equals("")) {
				player.openGui(CloudMod.instance, 2, world, x, y, z);
			}
			else {
				if(te.getOwnerID().equals(player.getUniqueID().toString())) {
					player.openGui(CloudMod.instance, 2, world, x, y, z);
				}
				else {
					player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "You are not the owner of this cloudBit"));
				}
			}
		}
		return true;
 	}
}
