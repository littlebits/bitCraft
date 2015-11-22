package cc.littleBits.cloudmod.worldevents;

import cc.littleBits.cloudmod.CloudMod;
import cc.littleBits.cloudmod.websocket.CloudConnectionManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;


public class WorldEventLBCMHandler {
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		if(!event.world.isRemote)
		{
			if(CloudMod.cloudManager == null) {
				CloudMod.cloudManager = new CloudConnectionManager();
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		if(!event.world.isRemote) {
			if(CloudMod.cloudManager != null) {
				CloudMod.cloudManager.purgeAll();
				CloudMod.cloudManager = null;
			}
		}
	}
}
