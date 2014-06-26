package me.jezzadabomb.es2.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelInventoryScanner extends ModelBase {
    // fields
    ModelRenderer base;
    ModelRenderer hold1;
    ModelRenderer hold2;
    ModelRenderer hold3;
    ModelRenderer hold4;
    ModelRenderer projector1;
    ModelRenderer projector2;
    ModelRenderer projector3;
    ModelRenderer projector4;

    public ModelInventoryScanner() {
        textureWidth = 64;
        textureHeight = 32;

        base = new ModelRenderer(this, 0, 0);
        base.addBox(0F, 0F, 0F, 16, 1, 16);
        base.setRotationPoint(-8F, 23F, -8F);
        base.setTextureSize(64, 32);
        base.mirror = true;
        setRotation(base, 0F, 0F, 0F);
        hold1 = new ModelRenderer(this, 0, 17);
        hold1.addBox(0F, 0F, 0F, 1, 1, 1);
        hold1.setRotationPoint(-8F, 22F, -8F);
        hold1.setTextureSize(64, 32);
        hold1.mirror = true;
        setRotation(hold1, 0F, 0F, 0F);
        hold2 = new ModelRenderer(this, 0, 17);
        hold2.addBox(0F, 0F, 0F, 1, 1, 1);
        hold2.setRotationPoint(7F, 22F, -8F);
        hold2.setTextureSize(64, 32);
        hold2.mirror = true;
        setRotation(hold2, 0F, 0F, 0F);
        hold3 = new ModelRenderer(this, 0, 17);
        hold3.addBox(0F, 0F, 0F, 1, 1, 1);
        hold3.setRotationPoint(-8F, 22F, 7F);
        hold3.setTextureSize(64, 32);
        hold3.mirror = true;
        setRotation(hold3, 0F, 0F, 0F);
        hold4 = new ModelRenderer(this, 0, 17);
        hold4.addBox(0F, 0F, 0F, 1, 1, 1);
        hold4.setRotationPoint(7F, 22F, 7F);
        hold4.setTextureSize(64, 32);
        hold4.mirror = true;
        setRotation(hold4, 0F, 0F, 0F);
        projector1 = new ModelRenderer(this, 4, 17);
        projector1.addBox(0F, 0F, 0F, 1, 1, 2);
        projector1.setRotationPoint(-8F, 22F, -7.3F);
        projector1.setTextureSize(64, 32);
        projector1.mirror = true;
        setRotation(projector1, 0.669215F, 0.7853982F, 0F);
        projector2 = new ModelRenderer(this, 4, 17);
        projector2.addBox(0F, 0F, 0F, 1, 1, 2);
        projector2.setRotationPoint(-7.28F, 22F, 7.966667F);
        projector2.setTextureSize(64, 32);
        projector2.mirror = true;
        setRotation(projector2, 0.669215F, 2.356194F, 0F);
        projector3 = new ModelRenderer(this, 4, 17);
        projector3.addBox(0F, 0F, 0F, 1, 1, 2);
        projector3.setRotationPoint(8F, 22F, 7.3F);
        projector3.setTextureSize(64, 32);
        projector3.mirror = true;
        setRotation(projector3, 0.669215F, 3.926991F, 0F);
        projector4 = new ModelRenderer(this, 4, 17);
        projector4.addBox(0F, 0F, 0F, 1, 1, 2);
        projector4.setRotationPoint(7.3F, 22F, -8F);
        projector4.setTextureSize(64, 32);
        projector4.mirror = true;
        setRotation(projector4, 0.669215F, -0.7853982F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        base.render(f5);
        hold1.render(f5);
        hold2.render(f5);
        hold3.render(f5);
        hold4.render(f5);
        projector1.render(f5);
        projector2.render(f5);
        projector3.render(f5);
        projector4.render(f5);
    }

    public void render() {
        Entity entity = null;
        float f5 = 0.0625F;
        super.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f5);
        setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f5, entity);
        base.render(f5);
        hold1.render(f5);
        hold2.render(f5);
        hold3.render(f5);
        hold4.render(f5);
        projector1.render(f5);
        projector2.render(f5);
        projector3.render(f5);
        projector4.render(f5);

    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}
