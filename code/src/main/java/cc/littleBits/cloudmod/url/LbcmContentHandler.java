package cc.littleBits.cloudmod.url;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import cc.littleBits.cloudmod.guis.GuiCloudSetup;

class LbcmContentHandler extends AbstractHandler{
	GuiCloudSetup gui;
	
	public LbcmContentHandler(GuiCloudSetup newGui) {
		super();
		gui = newGui;
	}
	
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException{
    	if(request.getMethod() == "POST") {
        	String token = request.getReader().readLine().substring(6);
        	gui.setAuth(token);
        }
    	try {
    		response.setContentType("text/html;charset=utf-8");
    		response.setStatus(HttpServletResponse.SC_OK);
    		baseRequest.setHandled(true);
    		response.addHeader("Access-Control-Allow-Origin", "*");
    		response.addHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
    		response.addHeader("Access-Control-Allow-Headers", "Content-Type");
    		response.addHeader("Access-Control-Max-Age", "86400");
    	} catch(Exception  e) {
    		System.out.println("response exception :(");
    	}
    }
}