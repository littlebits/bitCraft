// Defines the GUI and interactions for cloud setup. Called when the player
// activates a cloud gateway

package cc.littleBits.cloudmod.guis;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cc.littleBits.cloudmod.CloudMod;
import cc.littleBits.cloudmod.network.NBTMessage;
import cc.littleBits.cloudmod.tileentities.TileEntityCloud;
import cc.littleBits.cloudmod.url.LbcmLocalServer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GuiCloudSetup extends GuiScreen {
	// Global GUI dimensions
	 int guiWidth = 210;
	 int guiHeight = 165;
	 
	 // Defines usable width of GUI for text, and the default text height
	 int txtWidth = 190;
	 int txtHeight = 20;
	 
	 // Default prompts in GUI directing player to cloudcontrol. Sets color
	 // to black.
	 private String authPromptText1 = "Click the button below to connect";
	 private String authPromptText2 = "to your cloudBit. This will launch";
	 private String authPromptText3 = "a browser and ask you to log into";
	 private String authPromptText4 = "your littleBits account."; 
	 
	 private String tokenPaste1 = "Follow the instructions in";
	 private String tokenPaste2 = "the browser to connect";
	 private String tokenPaste3 = "to your cloudBit.";
	 
	 private String devLabel = "Device:";
	 private String dirLabel = "Direction:";
	 
	 private String loadingText = "Loading...";
	 
	 private int promptColor = 0x000000;
	 
	 // The cloudBit which called the GUI
	 TileEntityCloud tileCloud;
	 // The player calling the GUI
	 EntityPlayer player;
	 
	 // Cloud params default to empty strings
	 private String authToken = "";
	 private String devID = "";
	 private String ownerID = "";
	 private String owner = "";
	 private String cloudName = "";
	 private boolean direction = false;
	 
	 // Instantiate the "save" button, and two text fields
	 private GuiButton saveToken;
	 private GuiButton devButton;
	 private GuiButton modeButton;
	 private GuiButton resetButton;
	 private GuiTextField authField;
	 private GuiButton connectButton;
	 
	 private int guiMode = 0;
	 private ArrayList<DevPair> deviceArrayList;
	 
	 public String authTestResponse = "";
	 private boolean testing = false;
	 private LbcmLocalServer server;
	 
	 private int defaultDev = 0;
	 
	 public GuiCloudSetup(EntityPlayer newPlayer, TileEntityCloud te) {
		 super();
		 
		 player = newPlayer;
		 tileCloud = te;
		 
		 if(te.getAuth().length() == 64) {
			 authToken = te.getAuth();
			 devID = te.getDevID();
			 ownerID = te.getOwnerID();
			 owner = te.getOwner();
			 cloudName = te.getCloudName();
			 direction = te.getDirection();
			 guiMode = 2;
			 
			 CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		     httpclient.start();
		     final HttpGet nameReq = new HttpGet("https://api-http.littlebitscloud.cc/v3/devices");
		     nameReq.addHeader("Authorization", "Bearer "+authToken);
		     httpclient.execute(nameReq, new FutureAccountDevList<HttpResponse>(this));
		 }
		 else {
			 if(CloudMod.playerManager.isPlayerInDB(player.getUniqueID())) {
					NBTTagCompound nbt = new NBTTagCompound();
					authToken = CloudMod.playerManager.getAuth(player.getPersistentID());
					devID = CloudMod.playerManager.getDevID(player.getPersistentID());
					ownerID = player.getUniqueID().toString();
					owner = player.getDisplayName();
	
					CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
				    httpclient.start();
				    final HttpGet nameReq = new HttpGet("https://api-http.littlebitscloud.cc/v3/devices");
				    nameReq.addHeader("Authorization", "Bearer "+authToken);
				    httpclient.execute(nameReq, new FutureAccountDevList<HttpResponse>(this));
				    
				    guiMode = 2;
				}
			 else {guiMode = 0;}
		 }
		 Keyboard.enableRepeatEvents(true);
	 }
	 
	 // When GUI is first called, set it up nicely
	 @Override
	 public void initGui() {
		 super.initGui();
		 
	   	 // Defines location of upper-left corner of the GUI 
		 int guiX = (width - guiWidth)/2;
		 int guiY = (height - guiHeight)/2;
		 
		 // Start with an empty list of buttons, and add the "SAVE" button
		 buttonList.clear();
		 saveToken = new GuiButton(1,guiX+guiWidth/2-60, guiY+120, 120, 20, "Paste Token");
		 devButton = new GuiButton(2,guiX+guiWidth/2-60, guiY+40, 120, 20, "");
		 modeButton = new GuiButton(3,guiX+guiWidth/2-60, guiY+80, 120, 20, "");
		 resetButton = new GuiButton(4,guiX+guiWidth/2-60, guiY+120, 120, 20, "Reset Block");
		 connectButton = new GuiButton(5,guiX+guiWidth/2-60, guiY+100, 120, 20, "Connect");
		 
		 
		 if(guiMode == 0) {
			 buttonList.add(connectButton);
		 }
		 else if (guiMode ==  1) {
			 buttonList.clear();
			 buttonList.add(saveToken);
		 }
		 
		 // Define the location of the auth token field, don't focus it, and 
		 // set the max length to the length of the token.
		 authField = new GuiTextField(this.fontRendererObj, guiX+(guiWidth-txtWidth)/2, guiY+90, txtWidth, txtHeight);
		 authField.setFocused(true);
		 authField.setMaxStringLength(64);
	 }
	 
	 // Defines how the GUI is drawn
	 @Override
	 public void drawScreen(int x, int y, float ticks) {
		 // Defines location of upper-left corner of the GUI
		 int guiX = (width - guiWidth)/2;
		 int guiY = (height - guiHeight)/2;
		 
		 GL11.glColor4f(1,1,1,1);
		 
		 try {
			 // Grey the world screen behind the GUI
			 drawDefaultBackground();
			 // Get the GUI texture from resources
			 this.mc.getTextureManager().bindTexture(new ResourceLocation("lbcm:textures/gui/cloud_gui.png"));
			 // Draw the GUI rectangle with the defined texture
			 this.drawTexturedModalRect(guiX, guiY, 0, 0, guiWidth, guiHeight);
			 
			 if(guiMode == 0) {
				 // Start with Auth prompt screen
				 int prompt1XLoc = guiX+(guiWidth-(fontRendererObj.getStringWidth(authPromptText1)))/2;
				 int prompt2XLoc = guiX+(guiWidth-(fontRendererObj.getStringWidth(authPromptText2)))/2;
				 int prompt3XLoc = guiX+(guiWidth-(fontRendererObj.getStringWidth(authPromptText3)))/2;
				 int prompt4XLoc = guiX+(guiWidth-(fontRendererObj.getStringWidth(authPromptText4)))/2;
				 fontRendererObj.drawString(authPromptText1, prompt1XLoc, guiY+30, promptColor);
				 fontRendererObj.drawString(authPromptText2, prompt2XLoc, guiY+42, promptColor);
				 fontRendererObj.drawString(authPromptText3, prompt3XLoc, guiY+54, promptColor);
				 fontRendererObj.drawString(authPromptText4, prompt4XLoc, guiY+66, promptColor);
			 }
			 else if(guiMode == 1) {
				 int tokenPaste1XLoc = guiX+(guiWidth-(fontRendererObj.getStringWidth(tokenPaste1)))/2;
				 int tokenPaste2XLoc = guiX+(guiWidth-(fontRendererObj.getStringWidth(tokenPaste2)))/2;
				 int tokenPaste3XLoc = guiX+(guiWidth-(fontRendererObj.getStringWidth(tokenPaste3)))/2;
				 fontRendererObj.drawString(tokenPaste1, tokenPaste1XLoc, guiY+36, promptColor);
				 fontRendererObj.drawString(tokenPaste2, tokenPaste2XLoc, guiY+48, promptColor);
				 fontRendererObj.drawString(tokenPaste3, tokenPaste3XLoc, guiY+60, promptColor);
				 
				 authField.drawTextBox();
				if(authToken != null && authToken.length() == 64 && !testing) {
					this.testToken(authToken);
					testing = true;
				}
				
				if(!authTestResponse.equals("")) {
					testing = false;
					if(authTestResponse.equals("200")) {
						if(server != null) {
							try {
								server.stopServer();
							} catch (Exception e) {e.printStackTrace();}
						}
						guiMode = 2;
						buttonList.clear();
						
						// Get list of Devices
						CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
				        httpclient.start();
				        final HttpGet nameReq = new HttpGet("https://api-http.littlebitscloud.cc/v3/devices");
				        nameReq.addHeader("Authorization", "Bearer "+authToken);
				        httpclient.execute(nameReq, new FutureAccountDevList<HttpResponse>(this));
					}
					else {
						authToken = "";
						tokenPaste1 = "Hmm, looks like there's a problem,";
						tokenPaste2 = "check your internet connection";
						tokenPaste3 = "and try again.";
					}
					authTestResponse = "";
				}
			 }
			 else if(guiMode == 2) {
				int prompt1XLoc = guiX+(guiWidth-(fontRendererObj.getStringWidth(loadingText)))/2;
				fontRendererObj.drawString(loadingText, prompt1XLoc, guiY+70, promptColor);
				
				if(deviceArrayList != null) {
					if(deviceArrayList.size()>0) {
						if(devID == "") {
							defaultDev=0;
						}
						else {
							defaultDev = 0;
							for(int i=0; i<deviceArrayList.size(); i++) {
								if(deviceArrayList.get(i).getDevID().equals(devID)) {
									defaultDev = i;
									break;
								}
							}
						}
						guiMode = 3;
						 
						buttonList.add(devButton);
						buttonList.add(modeButton);
						buttonList.add(resetButton);
					}
					else {
						tokenPaste1 = "Hmm, looks like you don't have";
						tokenPaste2 = "any cloudBits. Check out ";
						tokenPaste3 = "littlebits.cc/cloud for more info";
						
						buttonList.clear();
						buttonList.add(saveToken);
						authToken = "";
						guiMode = 1;
					}
				}
			 }
			 else if(guiMode == 3) {
				 int devLabelXLoc = guiX+(guiWidth-(fontRendererObj.getStringWidth(devLabel)))/2;
				 int dirLabelXLoc = guiX+(guiWidth-(fontRendererObj.getStringWidth(dirLabel)))/2;
				 fontRendererObj.drawString(devLabel, devLabelXLoc, guiY+30, promptColor);
				 fontRendererObj.drawString(dirLabel, dirLabelXLoc, guiY+70, promptColor);
				 
				devButton.displayString = deviceArrayList.get(defaultDev).getDevName();
				devID = deviceArrayList.get(defaultDev).getDevID();
				if(direction) {modeButton.displayString = "Receive";}
				else {modeButton.displayString = "Transmit";}
			 }
			 else {
				 guiMode = 0;
			 }
			 super.drawScreen(x, y, ticks);
		 }
		 // Catch an NPE if it happens. Was sporadically occurring. Race condition?
		 catch(NullPointerException e) {
			 e.printStackTrace();
		 }
	 }
	 
	 public void keyTyped(char c, int i) {
		 
		 // If escape key pressed, don't type it in a text box. This way, the GUI will close.
		 if(i == Keyboard.KEY_ESCAPE) { }
			 
		 // If the auth token field is focused, send any typed characters there
		 if(authField.isFocused()) {
			 authField.textboxKeyTyped(c, i);
		 }
		 super.keyTyped(c, i);
	 }
	 
	 // Handle any button presses
	 @Override
	 protected void actionPerformed(GuiButton button)  {
		 switch(button.id) {
		 	case 1:
		 		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		 	    Transferable t = c.getContents(this);
		 	    if (t == null)
		 	        return;
		 	    try {
		 	        authField.setText((String) t.getTransferData(DataFlavor.stringFlavor));
		 	    } catch (Exception e){
		 	        e.printStackTrace();
		 	    }
		 	
		 		authToken = authField.getText();
		 		break;
		 		
		 	case 2:
		 		defaultDev = (defaultDev+1)%deviceArrayList.size();
		 		devID = deviceArrayList.get(defaultDev).getDevID();
		 		break;
		 		
		 	case 3:
		 		direction = !direction;
		 		break;
		 		
		 	case 4:
		 		// Create a new NBT compound with no cloud info
		 		authToken = "";
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("posX", Integer.toString(tileCloud.xCoord));
				nbt.setString("posY", Integer.toString(tileCloud.yCoord));
				nbt.setString("posZ", Integer.toString(tileCloud.zCoord));
				nbt.setString("authToken", "");
				nbt.setString("devID", "");
				nbt.setString("ownerID", "");
				nbt.setString("owner", "");
				nbt.setString("setTime", "");
				nbt.setBoolean("direction", direction);
				nbt.setInteger("dimension", tileCloud.getWorldObj().provider.dimensionId);
			
				// Send the NBT data to the server so it updates first
				CloudMod.network.sendToServer(new NBTMessage(nbt));
				
				tileCloud.setConnected(0);
				
				tokenPaste1 = "Follow the instructions in";
				tokenPaste2 = "the browser to connect";
				tokenPaste3 = "to your cloudBit.";
				
				buttonList.clear();
				buttonList.add(connectButton);
				
				guiMode = 0;
				break;
		 		
		 	case 5:
		 		
		 		if(Desktop.isDesktopSupported()) {
		 		  try {
		 			Desktop.getDesktop().browse(new URI("http://littlebits.cc/auth/minecraft"));
		 		  } catch (Exception e) {
		 			  e.printStackTrace();
		 		  }
		 		}
		 		
		 		// Launch server listening for client ID from LB Server
				if(server == null) {
					server= new LbcmLocalServer();
				}
				try {
					server.startServer(this);
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				// Transition to next GUI screen, "loading..."
				guiMode = 1;
				buttonList.clear();
				buttonList.add(saveToken);
				break;
				
		 	default:
		 		break;
			}
			
			super.actionPerformed(button);
	 }
	 
	 @Override
	 public void onGuiClosed() {
		if(server != null) {
			try {
				server.stopServer();
			} catch (Exception e) {e.printStackTrace();}
		}
		
		if(guiMode == 3) {
			// Create a new NBT compound with all of the cloud info inside
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("posX", Integer.toString(tileCloud.xCoord));
			nbt.setString("posY", Integer.toString(tileCloud.yCoord));
			nbt.setString("posZ", Integer.toString(tileCloud.zCoord));
			nbt.setString("authToken", authToken);
			nbt.setString("devID", devID);
			nbt.setString("ownerID", player.getUniqueID().toString());
			nbt.setString("owner", player.getDisplayName());
			nbt.setString("setTime", String.valueOf(System.currentTimeMillis()));
			nbt.setBoolean("direction", direction);
			nbt.setInteger("dimension", tileCloud.getWorldObj().provider.dimensionId);
		
			// Send the NBT data to the server so it updates first
			CloudMod.network.sendToServer(new NBTMessage(nbt));
			CloudMod.playerManager.addPlayerSettings(player.getUniqueID().toString(), authToken, devID);
		}
	 }
	 
	 private void testToken(String newToken) {
		 CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
	     httpclient.start();
	     final HttpGet nameReq = new HttpGet("https://api-http.littlebitscloud.cc/v3/devices");
	     nameReq.addHeader("Authorization", "Bearer "+authToken);
	     httpclient.execute(nameReq, new FutureTestAuthToken<HttpResponse>(this));
	 }
	 
	 public void setAuth(String newAuth) {
		 authToken = newAuth;
	 }
	 
	 public void setDevArraylist(ArrayList<DevPair> newDeviceArrayList) {
		 deviceArrayList = newDeviceArrayList;
	 }
}