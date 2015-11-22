package cc.littleBits.cloudmod.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import cc.littleBits.cloudmod.CloudMod;
import cc.littleBits.cloudmod.Blocks.BlockCloud;
import cc.littleBits.cloudmod.tileentities.TileEntityCloud;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class CloudHandler {
	protected Session session;
	protected TileEntityCloud te;
	protected boolean connectingFlag;
	protected boolean isClosing = false;

	public CloudHandler(TileEntityCloud t) {
		super();
		te = t;
	}
	
	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		this.session = null;
        connectingFlag = false;
        try {
        	te = CloudMod.cloudManager.getAgent(te).getTileEntity();
        	Block block = MinecraftServer.getServer().worldServerForDimension(te.getDimension()).getBlock(te.xCoord, te.yCoord, te.zCoord);
        	if (block instanceof BlockCloud && !te.getDisabled()) {te.setConnected(3);}
        } catch (Throwable t) {
        	System.out.println("ERR");
        }
        MinecraftServer.getServer().worldServerForDimension(te.getDimension()).markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
        isClosing = false; 
	}
	
	@OnWebSocketConnect
    public void onConnect(Session session) {
		te = CloudMod.cloudManager.getAgent(te).getTileEntity();
		this.session = session;
        connectingFlag = false;
        session.getRemote().sendStringByFuture("{\"name\":\"subscribe\",\"args\":{\"device_id\":\"" + te.getDevID() + "\"}}");
	}
	
	@OnWebSocketError
    public void onError(Throwable t) {
		te = CloudMod.cloudManager.getAgent(te).getTileEntity();
		te.setPower(0);
		t.printStackTrace();
        connectingFlag = false;
    }
	
	public void disconnect() {
		isClosing = true;
		if(session != null) {
			session.close(StatusCode.NORMAL, "I'm done");
		}
		connectingFlag = false;
	}
	
	public boolean isOpen() {
		try {
			if(session == null) {
				return false;
			}
			return session.isOpen();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			return false;
		}
	}
	
	@OnWebSocketMessage
	public void onMessage(String msg) {
		te = CloudMod.cloudManager.getAgent(te).getTileEntity();
		JSONObject res = new JSONObject(msg.replace("\\","").substring(1));
		if(res.getString("type").equals("connection_change")) {
			if(res.getInt("state") == 1 || res.getInt("state") == 0) {
				te.setConnected(3);
		 		te.setInputPower(0);
		 	}
	        else if(res.getInt("state") == 2)
	        	{te.setConnected(1);}
			
			MinecraftServer.getServer().worldServerForDimension(te.getDimension()).markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
	    }
		
		if(te.getDirection()) {	// If te is an input
	        if(res.getString("type").equals("input")) {
	        	int bitOut = (int)(Math.round(15*res.getInt("percent")/100.0));
	        	te.setInputPower(bitOut);
	        }
		}
	}
	
	public void keepAlive() {
		session.getRemote().sendStringByFuture("Hi");
	}
	
	public Session getSession() {
		return session;
	}
	
	public void sendPower(int power) {
		te = CloudMod.cloudManager.getAgent(te).getTileEntity();
		try {
			session.getRemote().sendStringByFuture("{\"name\":\"output\",\"args\":{\"device_id\":\"" + te.getDevID() + "\",\"duration_ms\": -1, \"percent\": " + Integer.toString(power) + "}}");
		} 
		catch (WebSocketException wse)
		{System.out.println("wse");}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isConnecting() {
		return connectingFlag;
	}
	
	public void setConnecting(boolean state) {
		connectingFlag = state;
	}
}
