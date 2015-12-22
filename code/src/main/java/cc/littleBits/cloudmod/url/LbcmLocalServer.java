package cc.littleBits.cloudmod.url;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import cc.littleBits.cloudmod.guis.GuiCloudSetup;

public class LbcmLocalServer {
    HttpServer server;
    
	public void startServer(GuiCloudSetup gui) throws Exception{
        int port=8277;
        
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new LbcmContentHandler(gui));
        server.setExecutor(null);
        
        server.start();
    }
    
    public void stopServer() throws Exception{
    	server.stop(0);
    }
}
