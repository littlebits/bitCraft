package cc.littleBits.cloudmod.websocket;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import org.json.JSONObject;

import cc.littleBits.cloudmod.CloudMod;
import cc.littleBits.cloudmod.Blocks.BlockCloud;
import cc.littleBits.cloudmod.tileentities.TileEntityCloud;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;

public class CloudHandler extends WebSocketClient{
	protected TileEntityCloud te;
	protected boolean connectingFlag;
	protected boolean isClosing = false;
	protected static HashMap<String, String> header = new HashMap<String, String>();

	public CloudHandler(URI serverUri, Map<String, String> header, TileEntityCloud t) {
		super(serverUri, new Draft_10(), header, 0);
		te = t;
	}
	
	@Override
	public void onClose(int statusCode, String reason, boolean remote) {
        connectingFlag = false;
        try {
        	te.setInputPower(0);
			te.setPower(0);
        	Block block = MinecraftServer.getServer().worldServerForDimension(te.getDimension()).getBlock(te.xCoord, te.yCoord, te.zCoord);
        	if (block instanceof BlockCloud && !te.getDisabled()) {te.setConnected(3);}
        } catch (Throwable t) {
        	System.out.println("ERR");
        }
        MinecraftServer.getServer().worldServerForDimension(te.getDimension()).markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
        isClosing = false; 
	}
	
	@Override
    public void onOpen(ServerHandshake handshakedata) {
		te = CloudMod.cloudManager.getAgent(te).getTileEntity();
        connectingFlag = false;
        this.send("{\"name\":\"subscribe\",\"args\":{\"device_id\":\"" + te.getDevID() + "\"}}");
	}
	
	@Override
    public void onError(Exception ex) {
		try {
			te.setPower(0); 
			ex.printStackTrace();
			connectingFlag = false;
		}
		catch (Throwable t) {
			//t.printStackTrace();
			System.out.println("wse");
		}
    }
	
	public void disconnect() {
		isClosing = true;
		if(!te.getDirection()) {
			this.sendPower(0);
		}
		if(!this.getConnection().isClosed()) {
			this.close();
		}
		connectingFlag = false;
	}
	
	public boolean isOpen() {
		return !this.getConnection().isClosed();
	}
	
	@Override
	public void onMessage(String msg) {
		try{
			te = CloudMod.cloudManager.getAgent(te).getTileEntity();
			JSONObject res = new JSONObject(msg.replace("\\","").substring(1));
			if(res.getString("type").equals("connection_change")) {
				if(res.getInt("state") == 0) {
					te.setConnected(3);
					te.setInputPower(0);
				}
				else if(res.getInt("state") == 2 || res.getInt("state") == 1)
	        		{te.setConnected(1);}
			
				MinecraftServer.getServer().worldServerForDimension(te.getDimension()).markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
			}
		
			if(te.getDirection()) {	// If te is an input
				if(res.getString("type").equals("input")) {
					int bitOut = (int)(Math.round(15*res.getInt("percent")/100.0));
					te.setInputPower(bitOut);
				}
			}
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void keepAlive() {
		this.send("Hi");
	}
	
	public void sendPower(int power) {
		te = CloudMod.cloudManager.getAgent(te).getTileEntity();
		try {
			this.send("{\"name\":\"output\",\"args\":{\"device_id\":\"" + te.getDevID() + "\",\"duration_ms\": -1, \"percent\": " + Integer.toString(power) + "}}");
		} 
		catch (Throwable t)
		{System.out.println("wse");}
	}
	
	public boolean isConnecting() {
		return connectingFlag;
	}
	
	public void setConnecting(boolean state) {
		connectingFlag = state;
	}
}
