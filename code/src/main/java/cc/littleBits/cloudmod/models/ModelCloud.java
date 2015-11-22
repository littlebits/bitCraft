package cc.littleBits.cloudmod.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * CloudGateway - sokrasins
 * Created using Tabula 4.1.1
 */
public class ModelCloud extends ModelBase {
    public ModelRenderer RLeg;
    public ModelRenderer LLeg;
    public ModelRenderer Front;
    public ModelRenderer RFoot;
    public ModelRenderer LFoot;
    public ModelRenderer Brd;
    public ModelRenderer SigPin;
    public ModelRenderer GndPin;
    public ModelRenderer VccPin;
    public ModelRenderer RBase;
    public ModelRenderer LBase;
    public ModelRenderer RAnt;
    public ModelRenderer LAnt;
    public ModelRenderer RAntTop;
    public ModelRenderer LAntTop;

    public ModelCloud() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.RLeg = new ModelRenderer(this, 0, 0);
        this.RLeg.setRotationPoint(5.0F, 18.0F, -7.0F);
        this.RLeg.addBox(0.0F, 0.0F, 0.0F, 2, 6, 2, 0.0F);
        this.LFoot = new ModelRenderer(this, 0, 20);
        this.LFoot.setRotationPoint(-4.0F, 22.0F, 4.0F);
        this.LFoot.addBox(0.0F, 0.0F, 0.0F, 2, 2, 2, 0.0F);
        this.RAntTop = new ModelRenderer(this, 8, 12);
        this.RAntTop.setRotationPoint(3.85F, 14.6F, 5.15F);
        this.RAntTop.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(RAntTop, 0.0F, -0.7853981633974483F, 0.0F);
        this.LBase = new ModelRenderer(this, 8, 20);
        this.LBase.setRotationPoint(-4.5F, 20.0F, 4.5F);
        this.LBase.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
        this.Front = new ModelRenderer(this, 0, 15);
        this.Front.setRotationPoint(-5.0F, 18.0F, -7.0F);
        this.Front.addBox(0.0F, 0.0F, 0.0F, 10, 3, 2, 0.0F);
        this.RFoot = new ModelRenderer(this, 0, 20);
        this.RFoot.setRotationPoint(2.0F, 22.0F, 4.0F);
        this.RFoot.addBox(0.0F, 0.0F, 0.0F, 2, 2, 2, 0.0F);
        this.Brd = new ModelRenderer(this, 0, 0);
        this.Brd.setRotationPoint(-5.0F, 21.0F, -7.0F);
        this.Brd.addBox(0.0F, 0.0F, 0.0F, 10, 1, 14, 0.0F);
        this.RBase = new ModelRenderer(this, 8, 20);
        this.RBase.setRotationPoint(2.5F, 20.0F, 4.5F);
        this.RBase.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2, 0.0F);
        this.LAnt = new ModelRenderer(this, 8, 0);
        this.LAnt.setRotationPoint(-4.0F, 15.0F, 5.1F);
        this.LAnt.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1, 0.0F);
        this.GndPin = new ModelRenderer(this, 0, 12);
        this.GndPin.setRotationPoint(-2.5F, 19.0F, -8.0F);
        this.GndPin.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.LAntTop = new ModelRenderer(this, 8, 12);
        this.LAntTop.setRotationPoint(-3.15F, 14.6F, 6.05F);
        this.LAntTop.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
        this.setRotateAngle(LAntTop, 0.0F, -2.356194490192345F, 0.0F);
        this.VccPin = new ModelRenderer(this, 0, 12);
        this.VccPin.setRotationPoint(1.5F, 19.0F, -8.0F);
        this.VccPin.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.SigPin = new ModelRenderer(this, 0, 12);
        this.SigPin.setRotationPoint(-0.5F, 19.0F, -8.0F);
        this.SigPin.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
        this.RAnt = new ModelRenderer(this, 8, 0);
        this.RAnt.setRotationPoint(3.0F, 15.0F, 5.0F);
        this.RAnt.addBox(0.0F, 0.0F, 0.0F, 1, 5, 1, 0.0F);
        this.LLeg = new ModelRenderer(this, 0, 0);
        this.LLeg.setRotationPoint(-7.0F, 18.0F, -7.0F);
        this.LLeg.addBox(0.0F, 0.0F, 0.0F, 2, 6, 2, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.RLeg.render(f5);
        this.LFoot.render(f5);
        this.RAntTop.render(f5);
        this.LBase.render(f5);
        this.Front.render(f5);
        this.RFoot.render(f5);
        this.Brd.render(f5);
        this.RBase.render(f5);
        this.LAnt.render(f5);
        this.GndPin.render(f5);
        this.LAntTop.render(f5);
        this.VccPin.render(f5);
        this.SigPin.render(f5);
        this.RAnt.render(f5);
        this.LLeg.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
