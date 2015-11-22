package cc.littleBits.cloudmod.guis;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

public class FutureTestAuthToken<T> implements FutureCallback<T>{
	private GuiCloudSetup gui;
	
	public FutureTestAuthToken (GuiCloudSetup newGui) {
		super();
		gui = newGui;
	}
	
	@Override
	public void completed(final T res) {
		 try{
			 int statusCode = ((HttpResponse)res).getStatusLine().getStatusCode();
			 gui.authTestResponse = String.valueOf(statusCode);
			 
		 } catch (Throwable t) {gui.authTestResponse = String.valueOf("ERR");}
	 }
	 
	@Override
	 public void failed(final Exception e) {
		gui.authTestResponse = String.valueOf("ERR");
	 }
	 
	@Override
	 public void cancelled() {
		gui.authTestResponse = String.valueOf("ERR");
	 }
}
