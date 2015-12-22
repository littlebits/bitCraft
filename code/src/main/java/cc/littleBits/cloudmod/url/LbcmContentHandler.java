package cc.littleBits.cloudmod.url;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import cc.littleBits.cloudmod.guis.GuiCloudSetup;

class LbcmContentHandler implements HttpHandler{
	GuiCloudSetup gui;
	
	public LbcmContentHandler(GuiCloudSetup newGui) {
		super();
		gui = newGui;
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
		t.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
		t.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
		t.getResponseHeaders().set("Access-Control-Max-Age", "86400");
		
		if(t.getRequestMethod().equals("POST")) {
			String token = IOUtils.toString(t.getRequestBody(), "UTF-8").substring(6);
			gui.setAuth(token);
		}

		String response = "OK";
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
	}
}