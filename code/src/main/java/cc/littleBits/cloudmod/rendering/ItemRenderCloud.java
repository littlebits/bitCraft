package cc.littleBits.cloudmod.rendering;

import org.lwjgl.opengl.GL11;

import cc.littleBits.cloudmod.models.ModelCloud;
import cc.littleBits.cloudmod.tileentities.TileEntityCloud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class ItemRenderCloud implements IItemRenderer {

	private ModelCloud model;
	private ResourceLocation texture = new ResourceLocation("lbcm:textures/blocks/CloudRcvNew.png");

   public ItemRenderCloud(ModelCloud newModel) {
      this.model = newModel;
   }

   @Override
   public boolean handleRenderType(ItemStack item, ItemRenderType type) {
      return true;
   }

   @Override
   public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
      return true;
   }

   @Override
   public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
	  
	  GL11.glPushMatrix();

	  GL11.glTranslated(0.5, 1.5, 0.5);
      GL11.glScalef(-1F, -1F, 1F);
      Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
      model.render(null, 0, 0, 0, 0, 0, 0.0625F);

      GL11.glPopMatrix();
   }
}