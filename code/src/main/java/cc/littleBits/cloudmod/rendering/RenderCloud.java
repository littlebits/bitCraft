package cc.littleBits.cloudmod.rendering;

import org.lwjgl.opengl.GL11;

import cc.littleBits.cloudmod.models.ModelCloud;
import cc.littleBits.cloudmod.tileentities.TileEntityCloud;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class RenderCloud extends TileEntitySpecialRenderer{

	private ModelCloud model;
	
	private ResourceLocation textureRcvNew = new ResourceLocation("lbcm:textures/blocks/CloudRcvNew.png");
	private ResourceLocation textureRcvBad = new ResourceLocation("lbcm:textures/blocks/CloudRcvBad.png");
	private ResourceLocation textureRcvEhh = new ResourceLocation("lbcm:textures/blocks/CloudRcvEhh.png");
	private ResourceLocation textureRcvOff = new ResourceLocation("lbcm:textures/blocks/CloudRcvOff.png");
	private ResourceLocation textureRcvOn = new ResourceLocation("lbcm:textures/blocks/CloudRcvOn.png");

	private ResourceLocation textureXmitNew = new ResourceLocation("lbcm:textures/blocks/CloudXmitNew.png");
	private ResourceLocation textureXmitBad = new ResourceLocation("lbcm:textures/blocks/CloudXmitBad.png");
	private ResourceLocation textureXmitEhh = new ResourceLocation("lbcm:textures/blocks/CloudXmitEhh.png");
	private ResourceLocation textureXmitOff = new ResourceLocation("lbcm:textures/blocks/CloudXmitOff.png");
	private ResourceLocation textureXmitOn = new ResourceLocation("lbcm:textures/blocks/CloudXmitOn.png");
	
	public RenderCloud(ModelCloud newModel) {
		this.model = newModel;
	}
	
	private void adjustRotatePivotViaMeta(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x,y,z);
		GL11.glPushMatrix();
		GL11.glRotatef(meta*(-90), 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {				
		GL11.glPushMatrix();
		GL11.glTranslated(x+0.5, y+1.5, z+0.5);
		switch (((TileEntityCloud)te).getConnected()) {
			case 0:
				if(((TileEntityCloud)te).getDirection()) {this.bindTexture(textureRcvNew);}
				else {this.bindTexture(textureXmitNew);}
				break;
			case 1:
				if(((TileEntityCloud)te).getDirection()) {this.bindTexture(textureRcvOn);}
				else {this.bindTexture(textureXmitOn);}
				break;
			case 2:
				if(((TileEntityCloud)te).getDirection()) {this.bindTexture(textureRcvOff);}
				else {this.bindTexture(textureXmitOff);}
				break;
			case 3:
				if(((TileEntityCloud)te).getDirection()) {this.bindTexture(textureRcvBad);}
				else {this.bindTexture(textureXmitBad);}
				break;
		}
		
		GL11.glPushMatrix();
		GL11.glRotated(180, 0, 0, 1);
		
		int rotation = 0;
		switch (te.getBlockMetadata()) {
			case 4:
				rotation = 90;
				break;
			case 5:
				rotation = 270;
				break;
			case 2:
				rotation = 180;
				break;
			case 3:
				rotation = 0;
				break;
		}
		GL11.glRotatef(rotation,0.0F, 1.0F, 0.0F);
		
		this.model.render((Entity)null, 0, 0, -0.1F, 0, 0, 0.0625f);
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

}
