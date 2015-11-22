package cc.littleBits.cloudmod.guis;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class FutureAccountDevList<T> implements FutureCallback<T>{
	private GuiCloudSetup gui;
	
	public FutureAccountDevList (GuiCloudSetup newGui) {
		super();
		gui = newGui;
	}
	
	@Override
	public void completed(final T res) {
		try{
			String resString = EntityUtils.toString(((HttpResponse)res).getEntity());
			JSONArray resJSON = new JSONArray(resString);
			 
			ArrayList<DevPair> devList = new ArrayList<DevPair>(); 
			boolean done = false;
			int ind = 0;
			while(!done) {
				try{
					 JSONObject cloudDev = resJSON.getJSONObject(ind);
					 String devName = cloudDev.getString("label");
					 String devID = cloudDev.getString("id");
					 devList.add(new DevPair(devID, devName));
					 ind++;
				 } catch (Throwable e){
					 done = true;
				 }
			 }
			 gui.setDevArraylist(devList);
		 } catch (Throwable t) { t.printStackTrace();}
	 }
	 
	@Override
	 public void failed(final Exception e) {
		 System.out.println("GET failed");
		 ArrayList<DevPair> devList = new ArrayList<DevPair>(); 
		 gui.setDevArraylist(devList);
	 }
	 
	@Override
	 public void cancelled() {
		 System.out.println("GET cancelled");
		 ArrayList<DevPair> devList = new ArrayList<DevPair>(); 
		 gui.setDevArraylist(devList);
	 }
}
