package cc.littleBits.cloudmod.websocket;

import java.net.URI;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.java_websocket.WebSocketFactory;
import org.java_websocket.client.DefaultSSLWebSocketClientFactory;

import cc.littleBits.cloudmod.tileentities.TileEntityCloud;
import net.minecraft.server.MinecraftServer;


public class ConnectedAgent {
	private CloudHandler socketHandler;
	private TileEntityCloud te;
	
	// Constructs a new connected agent, and tries to connect
	public ConnectedAgent(TileEntityCloud t) {
		te = t;
		
		String destUri = "wss://api-stream.littlebitscloud.cc/primus/?access_token=" + te.getAuth();
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		
		try {
			SSLContext ssl = SSLContext.getDefault();
			DefaultSSLWebSocketClientFactory socketFactory = new DefaultSSLWebSocketClientFactory(ssl);
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("User-Agent", "bitCraft-0.1.4");
			
			socketHandler = new CloudHandler(new URI(destUri), headers, te);
			socketHandler.setWebSocketFactory(socketFactory);
			socketHandler.setConnecting(true);
			socketHandler.connect();
		} 
		catch (Throwable err) {
			err.printStackTrace();
		}
	}
	
	public TileEntityCloud getTileEntity() {
		te = (TileEntityCloud)(MinecraftServer.getServer().worldServerForDimension(te.getDimension()).getTileEntity(te.xCoord, te.yCoord, te.zCoord));
		return te;
	}
		
	public CloudHandler getSocket() {
		return socketHandler;
	}
}
