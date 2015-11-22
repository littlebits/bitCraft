// Defines the recipes for the mod blocks and items 

package cc.littleBits.cloudmod;

import cc.littleBits.cloudmod.init.CloudModBlocks;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModRecipies {
	public static void addRecipies()
	{
		// Recipe for the Cloud Gateway
		GameRegistry.addRecipe(new ItemStack(CloudModBlocks.blockCloud,1), new Object[]{
				"   ",
				"ADB",
				"C C",
				'A', Items.redstone,
				'B', Items.iron_ingot,
				'C', new ItemStack(Blocks.cobblestone,1,0),
				'D', Items.paper}
		);
	}
}