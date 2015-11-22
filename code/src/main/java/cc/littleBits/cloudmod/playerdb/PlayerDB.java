package cc.littleBits.cloudmod.playerdb;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerDB {
	private ArrayList<PlayerData> managedPlayers;
	
	public PlayerDB() {
		managedPlayers = new ArrayList<PlayerData>();
	}
	
	public int playerDBIndex(String uuid) {
		if(managedPlayers.size() == 0) {return -1;}
		for(int i=0; i<managedPlayers.size(); i++) {
			if(uuid.equals(managedPlayers.get(i).getPlayerUUID())) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean isPlayerInDB(String uuid) {
		return playerDBIndex(uuid) != -1;
	}
	
	public boolean isPlayerInDB(UUID uuid) {
		return playerDBIndex(uuid.toString()) != -1;
	}
	
	public void addPlayerSettings(String newPlayer, String newToken, String newDev) {
		int ind = playerDBIndex(newPlayer);
		if(ind == -1) {
			managedPlayers.add(new PlayerData(newPlayer, newToken, newDev));
		}
		else {
			PlayerData oldPlayer = managedPlayers.get(ind);
			oldPlayer.setDevID(newDev);
			oldPlayer.setToken(newToken);
		}
	}
	
	public void removePlayer(String player) {
		int ind = playerDBIndex(player);
		if(ind == -1) {return;}
		else {
			managedPlayers.remove(ind);
		}
	}
	
	public String getAuth(String player) {
		int ind = playerDBIndex(player);
		if(ind == -1) {return null;}
		else {
			return managedPlayers.get(ind).getToken();
		}
	}
	
	public String getAuth(UUID player) {
		int ind = playerDBIndex(player.toString());
		if(ind == -1) {return null;}
		else {
			return managedPlayers.get(ind).getToken();
		}
	}
	
	public String getDevID(String player) {
		int ind = playerDBIndex(player);
		if(ind == -1) {return null;}
		else {
			return managedPlayers.get(ind).getDefaultDevID();
		}
	}
	
	public String getDevID(UUID player) {
		int ind = playerDBIndex(player.toString());
		if(ind == -1) {return null;}
		else {
			return managedPlayers.get(ind).getDefaultDevID();
		}
	}
}
