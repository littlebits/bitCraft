package cc.littleBits.cloudmod.init;

import cc.littleBits.cloudmod.CloudMod;
import cc.littleBits.cloudmod.Blocks.BlockCloud;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class CloudModBlocks {
	public static Block blockCloud;
	
	// Instantiate the new blocks, set the names of them, and put them in the creative inventory tab for this mod
	public static void init() {
		blockCloud = new BlockCloud(Material.cloth).setBlockName("BlockCloud").setCreativeTab(CloudMod.tabCloudMod).setBlockTextureName("lbcm:particle");
	}
	
	// Register the new blocks with Minecraft
	public static void register() {
		GameRegistry.registerBlock(blockCloud, blockCloud.getUnlocalizedName().substring(5));
	}
}
