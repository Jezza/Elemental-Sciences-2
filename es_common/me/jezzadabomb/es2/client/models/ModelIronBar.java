package me.jezzadabomb.es2.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelIronBar extends ModelBase {
    ModelRenderer ironBar;
    
    public ModelIronBar() {
        textureWidth = 64;
        textureHeight = 128;
        
        ironBar = new ModelRenderer(this, 60, 0);
        ironBar.addBox(0F, 0F, 0F, 1, 14, 1);
        ironBar.setRotationPoint(0F, 9F, 0F);
        ironBar.setTextureSize(64, 128);
        ironBar.mirror = true;
    }
    
    public void render() {
        float f5 = 0.0625F;
        super.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f5);
        setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f5, (Entity) null);
        ironBar.render(f5);
    }
    
    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
