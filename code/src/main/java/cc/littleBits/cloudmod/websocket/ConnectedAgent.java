package cc.littleBits.cloudmod.websocket;

import java.net.URI;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import cc.littleBits.cloudmod.tileentities.TileEntityCloud;
import net.minecraft.server.MinecraftServer;


public class ConnectedAgent {
	private CloudHandler socketHandler;
	private TileEntityCloud te;
	
	// Constructs a new connected agent, and tries to connect
	public ConnectedAgent(TileEntityCloud t) {
		te = t;
		
		socketHandler = new CloudHandler(te);
		
		socketHandler.setConnecting(true);
		String destUri = "wss://api-stream.littlebitscloud.cc/primus/?access_token=" + te.getAuth();
		SslContextFactory sslContextFactory = new SslContextFactory();
		WebSocketClient client = new WebSocketClient(sslContextFactory);
		try {
			client.start();
			URI echoUri = new URI(destUri);
			ClientUpgradeRequest request = new ClientUpgradeRequest();
			client.connect(socketHandler, echoUri, request);
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
