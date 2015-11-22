// Defines the inventory tab in creative mode for the littleBits mod

package cc.littleBits.cloudmod;

import cc.littleBits.cloudmod.init.CloudModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CloudModTab extends CreativeTabs{

	public CloudModTab(String arg0) {
		super(arg0);
	}
	
	@Override
	public Item getTabIconItem() {
		// Set the cloud gateway as the creative inventory tab
		return Item.getItemFromBlock(CloudModBlocks.blockCloud);
	}
	
}