package cc.littleBits.cloudmod.proxy;

import cc.littleBits.cloudmod.init.CloudModBlocks;
import cc.littleBits.cloudmod.models.ModelCloud;
import cc.littleBits.cloudmod.rendering.ItemRenderCloud;
import cc.littleBits.cloudmod.rendering.RenderCloud;
import cc.littleBits.cloudmod.tileentities.TileEntityCloud;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy{
	@Override
	public void registerRenders() {
		ModelCloud model = new ModelCloud();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCloud.class, new RenderCloud(model));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(CloudModBlocks.blockCloud), new ItemRenderCloud(model));
	}
}
