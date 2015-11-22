package cc.littleBits.cloudmod.url;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;

import cc.littleBits.cloudmod.guis.GuiCloudSetup;

public class LbcmLocalServer {
    Server server;
    
	public void startServer(GuiCloudSetup gui) throws Exception{
        int port=8277;
       
        server= new Server();
        ServerConnector connector=new ServerConnector(server);
        connector.setPort(port);
        connector.setHost("localhost");
        server.setConnectors(new Connector[]{connector});
        
        ContextHandler context = new ContextHandler();
        context.setContextPath("/");
        context.setAllowNullPathInfo(true);
        context.setResourceBase(".");
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        context.setHandler(new LbcmContentHandler(gui));
        
        server.setHandler(context);
        server.start();
    }
    
    public void stopServer() throws Exception{
    	server.stop();
    }
}
