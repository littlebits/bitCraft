package cc.littleBits.cloudmod.playerdb;

public class PlayerData {
	private String playerUUID;
	private String authToken;
	private String devID;
	
	public PlayerData(String newPlayer, String newToken, String newDev) {
		playerUUID = newPlayer;
		authToken = newToken;
		devID = newDev;
	}
	
	public String getPlayerUUID() {
		return playerUUID;
	}
	
	public String getToken() {
		return authToken;
	}
	
	public String getDefaultDevID() {
		return devID;
	}
	
	public void setToken(String newToken) {
		authToken = newToken;
	}
	
	public void setDevID(String newDev) {
		devID = newDev;
	}
}
