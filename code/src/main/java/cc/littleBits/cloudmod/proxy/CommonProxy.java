package cc.littleBits.cloudmod.proxy;

import cc.littleBits.cloudmod.tileentities.TileEntityCloud;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {
	public void registerRenders(){
		
	}
		
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityCloud.class, TileEntityCloud.publicName);
	}
}
