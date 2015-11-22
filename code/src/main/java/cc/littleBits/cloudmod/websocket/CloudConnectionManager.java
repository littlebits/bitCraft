package cc.littleBits.cloudmod.websocket;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cc.littleBits.cloudmod.CloudMod;
import cc.littleBits.cloudmod.tileentities.TileEntityCloud;

public class CloudConnectionManager {
	private ArrayList<ConnectedAgent> managedAgents; 
	ScheduledExecutorService checkConn;
	
	public CloudConnectionManager() {
		managedAgents = new ArrayList<ConnectedAgent>();
		checkConn = Executors.newSingleThreadScheduledExecutor();
		checkConn.scheduleAtFixedRate(new Runnable() {
			public void run() {
				CloudMod.cloudManager.pokeAndReconnect();
			}
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	public void register(TileEntityCloud t) {
		if(!this.isRegistered(t)) {
			managedAgents.add(new ConnectedAgent(t));
		}
	}
	
	public void deRegister(TileEntityCloud t) {
		for(int i=0; i<managedAgents.size(); i++) {
			if(t.posEquals(managedAgents.get(i).getTileEntity())) {
				CloudHandler closingSocket = managedAgents.get(i).getSocket();
				closingSocket.disconnect();
				managedAgents.remove(i);
			}
		}
	}
	
	public void pokeAndReconnect() {
		try{
			for(int i=0; i<managedAgents.size(); i++) {
				TileEntityCloud tc = managedAgents.get(i).getTileEntity();
				CloudHandler agentSocket = managedAgents.get(i).getSocket();
			
				if(!agentSocket.isOpen() && !agentSocket.isConnecting() && !tc.getDisabled()) {
					TileEntityCloud errantTE = managedAgents.get(i).getTileEntity();
				
					this.deRegister(errantTE);
					this.register(errantTE);
				}
				else if(!agentSocket.isConnecting() && agentSocket.isOpen() && !tc.getDisabled()) {
					try{
						agentSocket.keepAlive();
					} catch (Throwable t) {}
				}
				else if(agentSocket.isOpen() && (tc.getDisabled())) {
					if(!tc.getDirection()) {
						tc.setPower(0);
					}
					agentSocket.disconnect();
				}
			}
		} catch (Throwable t)
		{t.printStackTrace();}
	}
	
	public boolean isRegistered(TileEntityCloud t) {
		for(int i=0; i<managedAgents.size(); i++) {
			if(t.posEquals(managedAgents.get(i).getTileEntity())) {return true;}
		}
		return false;
	}
	
	public void updateOutput(TileEntityCloud te) {
		for(int i=0; i<managedAgents.size(); i++) {
			if(te.posEquals(managedAgents.get(i).getTileEntity())) {
				managedAgents.get(i).getSocket().sendPower(managedAgents.get(i).getTileEntity().getPower());
			}
		}
	}
	
	public int findInd(TileEntityCloud t) {
		for(int i=0; i<managedAgents.size(); i++) {
			if(t.posEquals(managedAgents.get(i).getTileEntity())) {
				return i;
			}
		}
		return -1;
	}
	
	public ConnectedAgent getAgent(TileEntityCloud t) {
		for(int i=0; i<managedAgents.size(); i++) {
			if(t.posEquals(managedAgents.get(i).getTileEntity())) {
				return managedAgents.get(i);
			}
		}
		return null;
	}
	
	public void purgeAll() {
		int arraySize = managedAgents.size();
		for(int i=0; i<arraySize; i++) {
			if(!managedAgents.get(0).getTileEntity().getDirection()) {
				managedAgents.get(0).getTileEntity().setPower(0);
			}
			managedAgents.get(0).getSocket().disconnect();
			managedAgents.remove(0);
		}
	}
	
	public void updateSetTimes(String authToken, String devID, String newSetTime) {
		for(int i=0; i<managedAgents.size(); i++) {
			TileEntityCloud tc = managedAgents.get(i).getTileEntity();
			if(tc.getAuth().equals(authToken) && tc.getDevID().equals(devID)) {
				tc.setSetTime(newSetTime);
			}
		}
	}
}
