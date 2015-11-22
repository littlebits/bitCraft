// Handles all requests for GUIs. Currently, there is only one GUI. It's important to separate the client
// and server side, as a GUI call on the server will crash the server

package cc.littleBits.cloudmod.guis;

import cc.littleBits.cloudmod.tileentities.TileEntityCloud;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
	// Never generate a GUI on the server
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
	
	// Check id of GUI requested. It's always the same, because there's only one GUI (right now)
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntityCloud te = (TileEntityCloud)(world.getTileEntity(x,y,z));
		if(id==2) {
			return new GuiCloudSetup(player, te);
		}
		return null;
	}
}
