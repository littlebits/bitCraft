package cc.littleBits.cloudmod.tileentities;

import java.util.UUID;

import cc.littleBits.cloudmod.CloudMod;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityCloud extends TileEntity{
	public static final String publicName = "tileEntityCloud";
	protected String name;
	protected String owner = "";
	protected String ownerID = "";
	protected String authToken = "";
	protected String devID = "";
	protected int power = 0;
	protected int oldPower = 0;
	protected int inputPower = 0;
	protected String setTime = "";
	protected boolean disabled = false;
	protected String cloudName = "";
	protected boolean direction = true; // true = input to Minecraft, false == output to littlebits
	protected int connectionStatus = 0;
	protected int dimension;
	
	public TileEntityCloud(){
		super();
	}
	
	public String getName() {
		return name;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public void setDimension(int newDim) {
		dimension = newDim;
	}
	
	public int getConnected() {
		return connectionStatus;
	}
	
	public void setConnected(int newStatus) {
		connectionStatus = newStatus;
	}

	public void readFromNBT(NBTTagCompound nbt) {
	    this.ownerID = nbt.getString("ownerID");
	    this.owner = nbt.getString("owner");
	    this.authToken = nbt.getString("authToken");
	    this.devID = nbt.getString("devID");
	    this.setTime = nbt.getString("setTime");
	    this.disabled = nbt.getBoolean("disabled");
	    this.cloudName = nbt.getString("cloudName");
	    this.connectionStatus = nbt.getInteger("connectionStatus");
	    this.direction = nbt.getBoolean("direction");
	    this.dimension= nbt.getInteger("dimension");
		super.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
	   nbt.setString("ownerID", ownerID);
	   nbt.setString("owner", owner);
	   nbt.setString("authToken", authToken);
	   nbt.setString("devID", devID);
	   nbt.setString("setTime", setTime);
	   nbt.setBoolean("disabled", disabled);
	   nbt.setString("cloudName", cloudName);
	   nbt.setInteger("connectionStatus", connectionStatus);
	   nbt.setBoolean("direction", direction);
	   nbt.setInteger("dimension", dimension);
	   super.writeToNBT(nbt);
	}
	
	public void setOwner(String placer) {
		owner = placer;
	}
	
	public void setOwnerID(UUID id) {
		ownerID = id.toString();
	}
	
	public void setOwnerID(String id) {
		ownerID = id;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public String getOwnerID() {
		return ownerID;
	}
	
	public boolean getDirection() {
		return direction;
	}
	
	public void setDirection(boolean newDir) {
		direction = newDir;
	}
	
	public String getAuth() {
		return authToken;
	}
	public String getDevID() {
		return devID;
	}
	
	public void setAuth(String token) {
		authToken = token;
	}
	
	public void setDevID(String id) {
		devID = id;
	}
	
	@Override
	public Packet getDescriptionPacket() {		
		this.connect();
		NBTTagCompound syncData = new NBTTagCompound();
		
		syncData.setString("ownerID", ownerID);
		syncData.setString("owner", owner);
		syncData.setString("cloudName", cloudName);
		syncData.setInteger("connectionStatus", connectionStatus);
		syncData.setBoolean("direction", direction);
		syncData.setString("authToken", authToken);
		syncData.setString("devID", devID);
		syncData.setInteger("dimension", dimension);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound syncData = pkt.func_148857_g();
		this.ownerID = syncData.getString("ownerID");
	    this.owner = syncData.getString("owner");
	    this.cloudName = syncData.getString("cloudName");
	    this.connectionStatus = syncData.getInteger("connectionStatus");
	    this.direction = syncData.getBoolean("direction");
	    this.authToken = syncData.getString("authToken");
	    this.devID = syncData.getString("devID");
	    this.dimension = syncData.getInteger("dimension");
	}
	
	
	@Override
	public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z) {
	    return (oldMeta != newMeta);
	}
	
	
	public void setPower(int newPower) {
		power = newPower;
		if(!direction) {
			if(CloudMod.cloudManager.isRegistered(this) && CloudMod.cloudManager.getAgent(this).getSocket()!=null && !CloudMod.cloudManager.getAgent(this).getSocket().isConnecting() && CloudMod.cloudManager.getAgent(this).getSocket().isOpen()) {
				if(oldPower != newPower) {
					CloudMod.cloudManager.updateOutput(this);
				}
				oldPower = newPower;
			}
		}
	}
	
	public int getPower() {
		return power;
	}
	
	public String getSetTime() {
		return setTime;
	}
	
	public void setSetTime(String newTime) {
		setTime = newTime;
	}
	
	public boolean getDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean newState) {
		disabled = newState;
	}
	
	public void setCloudName(String newName) {
		cloudName = newName;
	}
	
	public String getCloudName() {
		return cloudName;
	}
	
	public boolean posEquals(TileEntityCloud te) {
		return (this.xCoord == te.xCoord) && (this.yCoord == te.yCoord) && (this.zCoord == te.zCoord);
	}
	
	public int getInputPower() {
		return inputPower;
	}
	
	public void setInputPower(int newInputPower) {
		inputPower = newInputPower;
	}
	
	public void connect() {
		if(!disabled && this.getAuth().length() == 64 && !CloudMod.cloudManager.isRegistered(this)) {
			 CloudMod.cloudManager.register(this);		// Register with connection manager at world load
			 CloudMod.playerManager.addPlayerSettings(ownerID, authToken, devID); 		// Catalog existing settings in DB on startup 
		 }
	}
}
