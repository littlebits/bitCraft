package cc.littleBits.cloudmod.guis;

public class DevPair {
	private String devID;
	private String devName;
	
	public DevPair(String newID, String newName) {
		devID = newID;
		devName = newName;
	}
	
	public String getDevID() {
		return devID;
	}
	
	public String getDevName() {
		return devName;
	}
}
