package cc.littleBits.cloudmod;

import cc.littleBits.cloudmod.guis.GuiHandler;
import cc.littleBits.cloudmod.init.CloudModBlocks;
import cc.littleBits.cloudmod.network.NBTMessage;
import cc.littleBits.cloudmod.playerdb.PlayerDB;
import cc.littleBits.cloudmod.proxy.CommonProxy;
import cc.littleBits.cloudmod.websocket.CloudConnectionManager;
import cc.littleBits.cloudmod.worldevents.WorldEventLBCMHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class CloudMod {
	
	@Instance("lbcm")
	public static CloudMod instance = new CloudMod();
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final CloudModTab tabCloudMod = new CloudModTab("tabCloudMod");
	private GuiHandler gui_handler = new GuiHandler();
	public static SimpleNetworkWrapper network;
	public static CloudConnectionManager cloudManager;
	public static PlayerDB playerManager;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Register network packet handler for updating the cloud gateway
		network = NetworkRegistry.INSTANCE.newSimpleChannel("NBTChannel");
		network.registerMessage(NBTMessage.Handler.class, NBTMessage.class, 0, Side.SERVER);
		
		CloudModBlocks.init();
		CloudModBlocks.register();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Register TEs and renderers on appropriate side
		proxy.registerTileEntities();
		proxy.registerRenders();
		
		ModRecipies.addRecipies();
		
		gui_handler = new GuiHandler();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, gui_handler);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Register world load and unload event handlers
		MinecraftForge.EVENT_BUS.register(new WorldEventLBCMHandler());
		// Create new player database
		playerManager = new PlayerDB();		
	}
}
