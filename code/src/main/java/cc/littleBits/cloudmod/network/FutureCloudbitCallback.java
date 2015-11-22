package cc.littleBits.cloudmod.network;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import cc.littleBits.cloudmod.tileentities.TileEntityCloud;

public class FutureCloudbitCallback<T> implements FutureCallback<T>{
	TileEntityCloud te;
	
	public FutureCloudbitCallback (TileEntityCloud t) {
		te = t;
	}
	
	@Override
	public void completed(final T res) {
		 try{
			 String resString = EntityUtils.toString(((HttpResponse)res).getEntity());
			 JSONObject resJSON = new JSONObject(resString);
			 String cloudName = (String)resJSON.get("label");
			 te.setCloudName(cloudName);
		 } catch (Throwable t) { }
	 }
	 
	@Override
	public void failed(final Exception e) {
		 System.out.println("GET failed");
		 te.setCloudName("???");
	}
	 
	@Override
	 public void cancelled() {
		 System.out.println("GET cancelled");
		 te.setCloudName("???");
	 }
}
