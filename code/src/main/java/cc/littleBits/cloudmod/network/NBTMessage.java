// Class defines and manages the NBT packet that updates the server with cloudBit info. 
// When a player adds or changes the info stored in a cloudBit, the data must be
// transferred to the server. This class defines the form of the packet, as well as a
// callback function which executes server-side when a packet is received. The callback
// stores the cloud info on the server-side cloudBit, and then briefly opens a websocket
// to get the name of the cloudBit. The websocket is then closed, and the server updates
// the clients with the cloud info.

package cc.littleBits.cloudmod.network;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import cc.littleBits.cloudmod.CloudMod;
import cc.littleBits.cloudmod.tileentities.TileEntityCloud;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class NBTMessage implements IMessage 
{
	private String text;
	
	public NBTMessage() { 
		return;
	}
	
	public NBTMessage(String text) {
		this.text = text;
	}
	
	// Converts the NBT tag to a String, which is how information is transferred to the server.
	// The packet contains the auth token. device ID, owner name, and owner UUID.
	public NBTMessage(NBTTagCompound syncData) {
		this.text = syncData.getString("posX") + "," + syncData.getString("posY") + "," + syncData.getString("posZ") + "," + syncData.getString("authToken") + "," + syncData.getString("devID") + "," + syncData.getString("ownerID") + "," + syncData.getString("owner") + "," + syncData.getString("setTime") + "," + syncData.getBoolean("direction") + "," + syncData.getInteger("dimension");
	}
	
	@Override
    public void fromBytes(ByteBuf buf) {
        text = ByteBufUtils.readUTF8String(buf);
    }
	
	@Override
	public void toBytes(ByteBuf buf) {
	    ByteBufUtils.writeUTF8String(buf, text);
	}
	 
	// Defines how the packet is handled once it's received by the server. 
	public static class Handler implements IMessageHandler<NBTMessage, IMessage> {
		
		@Override
	    public IMessage onMessage(NBTMessage message, MessageContext ctx) {
	        WorldServer mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
	        
	        // Extract the new cloud info
	        String[] nbtParams = message.text.split(",");
	        int posX = Integer.parseInt(nbtParams[0]);
	        int posY = Integer.parseInt(nbtParams[1]);
	        int posZ = Integer.parseInt(nbtParams[2]);
	        String authToken = nbtParams[3];
	        String devID = nbtParams[4];
	        String ownerID = nbtParams[5];
	        String owner = nbtParams[6];
	        String setTime = nbtParams[7];
	        String dirString = nbtParams[8];
	        int dimension = Integer.valueOf(nbtParams[9]);
	        boolean direction;
	        if(dirString.equals("true")) {direction = true;}
	        else {direction = false;}
	                 
	        // Get the cloudBit ItemStack for the new info
	        TileEntityCloud te = (TileEntityCloud)(MinecraftServer.getServer().worldServerForDimension(dimension).getTileEntity(posX, posY, posZ));
	        
	        if(devID != "") {
	        	// Query LB for the name of the cloudBit
	        	CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
	        	httpclient.start();
	        	final HttpGet nameReq = new HttpGet("https://api-http.littlebitscloud.cc/v3/devices/" + devID);
	        	nameReq.addHeader("Authorization", "Bearer "+authToken);
	        	httpclient.execute(nameReq, new FutureCloudbitCallback<HttpResponse>(te));
	        }
	        
	        if(!te.getAuth().equals(authToken) || !te.getDevID().equals(devID) || te.getDirection() != direction) {
	        	if(te.getDirection() == false && direction == true) {te.setPower(0);}
	        	CloudMod.cloudManager.deRegister(te);
	        }
	        // Put info in server-side TE
			te.setAuth(authToken);
			te.setDevID(devID);
			te.setOwnerID(ownerID);
			te.setOwner(owner);
			te.setSetTime(setTime);
			te.setDirection(direction);
			te.setDimension(dimension);
			
			if(!CloudMod.cloudManager.isRegistered(te) && authToken.length()==64) {
				CloudMod.cloudManager.register(te);		// Register with connection manager
			}
			
			if(authToken.length()!=64) {
				te.setConnected(0);
				MinecraftServer.getServer().worldServerForDimension(te.getDimension()).markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
			}
			
	        return null;
	     }
	 }
}