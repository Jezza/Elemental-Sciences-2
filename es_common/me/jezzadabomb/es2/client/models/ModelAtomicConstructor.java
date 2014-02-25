package me.jezzadabomb.es2.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelAtomicConstructor extends ModelBase {
    ModelRenderer Base;
    ModelRenderer Pillars1;
    ModelRenderer Pillars2;
    ModelRenderer Pillars3;
    ModelRenderer Pillars4;
    ModelRenderer Brace1;
    ModelRenderer Brace2;
    ModelRenderer Brace4;
    ModelRenderer Brace3;
    ModelRenderer Wall1;
    ModelRenderer Wall2;
    ModelRenderer Wall3;
    ModelRenderer Wall4;
    ModelRenderer BottomCorner1;
    ModelRenderer BottomCorner2;
    ModelRenderer BottomCorner3;
    ModelRenderer BottomCorner4;
    ModelRenderer TopCorner1;
    ModelRenderer TopCorner2;
    ModelRenderer TopCorner3;
    ModelRenderer TopCorner4;
    ModelRenderer BottomBrace1;
    ModelRenderer BottomBrace2;
    ModelRenderer BottomBrace3;
    ModelRenderer BottomBrace4;

    public ModelAtomicConstructor() {
        textureWidth = 64;
        textureHeight = 128;

        Base = new ModelRenderer(this, 0, 0);
        Base.addBox(0F, 0F, 0F, 14, 1, 14);
        Base.setRotationPoint(-7F, 23F, -7F);
        Base.setTextureSize(64, 128);
        Base.mirror = true;
        setRotation(Base, 0F, 0F, 0F);
        Pillars1 = new ModelRenderer(this, 60, 0);
        Pillars1.addBox(0F, 0F, 0F, 1, 14, 1);
        Pillars1.setRotationPoint(7F, 9F, -8F);
        Pillars1.setTextureSize(64, 128);
        Pillars1.mirror = true;
        setRotation(Pillars1, 0F, 0F, 0F);
        Pillars2 = new ModelRenderer(this, 60, 0);
        Pillars2.addBox(0F, 0F, 0F, 1, 14, 1);
        Pillars2.setRotationPoint(-8F, 9F, 7F);
        Pillars2.setTextureSize(64, 128);
        Pillars2.mirror = true;
        setRotation(Pillars2, 0F, 0F, 0F);
        Pillars3 = new ModelRenderer(this, 60, 0);
        Pillars3.addBox(0F, 0F, 0F, 1, 14, 1);
        Pillars3.setRotationPoint(-8F, 9F, -8F);
        Pillars3.setTextureSize(64, 128);
        Pillars3.mirror = true;
        setRotation(Pillars3, 0F, 0F, 0F);
        Pillars4 = new ModelRenderer(this, 60, 0);
        Pillars4.addBox(0F, 0F, 0F, 1, 14, 1);
        Pillars4.setRotationPoint(7F, 9F, 7F);
        Pillars4.setTextureSize(64, 128);
        Pillars4.mirror = true;
        setRotation(Pillars4, 0F, 0F, 0F);
        Brace1 = new ModelRenderer(this, 0, 17);
        Brace1.addBox(0F, 0F, 0F, 14, 1, 1);
        Brace1.setRotationPoint(-7F, 8F, 7F);
        Brace1.setTextureSize(64, 128);
        Brace1.mirror = true;
        setRotation(Brace1, 0F, 0F, 0F);
        Brace2 = new ModelRenderer(this, 15, 19);
        Brace2.addBox(0F, 0F, 0F, 1, 1, 14);
        Brace2.setRotationPoint(-8F, 8F, -7F);
        Brace2.setTextureSize(64, 128);
        Brace2.mirror = true;
        setRotation(Brace2, 0F, 0F, 0F);
        Brace4 = new ModelRenderer(this, 0, 17);
        Brace4.addBox(0F, 0F, 0F, 14, 1, 1);
        Brace4.setRotationPoint(-7F, 8F, -8F);
        Brace4.setTextureSize(64, 128);
        Brace4.mirror = true;
        setRotation(Brace4, 0F, 0F, 0F);
        Brace3 = new ModelRenderer(this, 15, 19);
        Brace3.addBox(0F, 0F, 0F, 1, 1, 14);
        Brace3.setRotationPoint(7F, 8F, -7F);
        Brace3.setTextureSize(64, 128);
        Brace3.mirror = true;
        setRotation(Brace3, 0F, 0F, 0F);
        Wall1 = new ModelRenderer(this, 0, 19);
        Wall1.addBox(0F, 0F, 0F, 14, 14, 0);
        Wall1.setRotationPoint(-7F, 9F, 7F);
        Wall1.setTextureSize(64, 128);
        Wall1.mirror = true;
        setRotation(Wall1, 0F, 0F, 0F);
        Wall2 = new ModelRenderer(this, 0, 19);
        Wall2.addBox(0F, 0F, 0F, 14, 14, 0);
        Wall2.setRotationPoint(-7F, 9F, -7F);
        Wall2.setTextureSize(64, 128);
        Wall2.mirror = true;
        setRotation(Wall2, 0F, 0F, 0F);
        Wall3 = new ModelRenderer(this, 0, 5);
        Wall3.addBox(0F, 0F, 0F, 0, 14, 14);
        Wall3.setRotationPoint(7F, 9F, -7F);
        Wall3.setTextureSize(64, 128);
        Wall3.mirror = true;
        setRotation(Wall3, 0F, 0F, 0F);
        Wall4 = new ModelRenderer(this, 0, 5);
        Wall4.addBox(0F, 0F, 0F, 0, 14, 14);
        Wall4.setRotationPoint(-7F, 9F, -7F);
        Wall4.setTextureSize(64, 128);
        Wall4.mirror = true;
        setRotation(Wall4, 0F, 0F, 0F);
        BottomCorner1 = new ModelRenderer(this, 60, 3);
        BottomCorner1.addBox(0F, 0F, 0F, 1, 1, 1);
        BottomCorner1.setRotationPoint(-8F, 23F, 7F);
        BottomCorner1.setTextureSize(64, 128);
        BottomCorner1.mirror = true;
        setRotation(BottomCorner1, 0F, 0F, 0F);
        BottomCorner2 = new ModelRenderer(this, 60, 3);
        BottomCorner2.addBox(0F, 0F, 0F, 1, 1, 1);
        BottomCorner2.setRotationPoint(7F, 23F, 7F);
        BottomCorner2.setTextureSize(64, 128);
        BottomCorner2.mirror = true;
        setRotation(BottomCorner2, 0F, 0F, 0F);
        BottomCorner4 = new ModelRenderer(this, 60, 3);
        BottomCorner4.addBox(0F, 0F, 0F, 1, 1, 1);
        BottomCorner4.setRotationPoint(-8F, 23F, -8F);
        BottomCorner4.setTextureSize(64, 128);
        BottomCorner4.mirror = true;
        setRotation(BottomCorner4, 0F, 0F, 0F);
        BottomCorner3 = new ModelRenderer(this, 60, 3);
        BottomCorner3.addBox(0F, 0F, 0F, 1, 1, 1);
        BottomCorner3.setRotationPoint(7F, 23F, -8F);
        BottomCorner3.setTextureSize(64, 128);
        BottomCorner3.mirror = true;
        setRotation(BottomCorner3, 0F, 0F, 0F);
        TopCorner1 = new ModelRenderer(this, 60, 3);
        TopCorner1.addBox(0F, 0F, 0F, 1, 1, 1);
        TopCorner1.setRotationPoint(-8F, 8F, -8F);
        TopCorner1.setTextureSize(64, 128);
        TopCorner1.mirror = true;
        setRotation(TopCorner1, 0F, 0F, 0F);
        TopCorner2 = new ModelRenderer(this, 60, 3);
        TopCorner2.addBox(0F, 0F, 0F, 1, 1, 1);
        TopCorner2.setRotationPoint(-8F, 8F, 7F);
        TopCorner2.setTextureSize(64, 128);
        TopCorner2.mirror = true;
        setRotation(TopCorner2, 0F, 0F, 0F);
        TopCorner3 = new ModelRenderer(this, 60, 3);
        TopCorner3.addBox(0F, 0F, 0F, 1, 1, 1);
        TopCorner3.setRotationPoint(7F, 8F, -8F);
        TopCorner3.setTextureSize(64, 128);
        TopCorner3.mirror = true;
        setRotation(TopCorner3, 0F, 0F, 0F);
        TopCorner4 = new ModelRenderer(this, 60, 3);
        TopCorner4.addBox(0F, 0F, 0F, 1, 1, 1);
        TopCorner4.setRotationPoint(7F, 8F, 7F);
        TopCorner4.setTextureSize(64, 128);
        TopCorner4.mirror = true;
        setRotation(TopCorner4, 0F, 0F, 0F);
        BottomBrace1 = new ModelRenderer(this, 0, 17);
        BottomBrace1.addBox(0F, 0F, 0F, 14, 1, 1);
        BottomBrace1.setRotationPoint(-7F, 23F, 7F);
        BottomBrace1.setTextureSize(64, 128);
        BottomBrace1.mirror = true;
        setRotation(BottomBrace1, 0F, 0F, 0F);
        BottomBrace2 = new ModelRenderer(this, 15, 19);
        BottomBrace2.addBox(0F, 0F, 0F, 1, 1, 14);
        BottomBrace2.setRotationPoint(7F, 23F, -7F);
        BottomBrace2.setTextureSize(64, 128);
        BottomBrace2.mirror = true;
        setRotation(BottomBrace2, 0F, 0F, 0F);
        BottomBrace3 = new ModelRenderer(this, 0, 17);
        BottomBrace3.addBox(0F, 0F, 0F, 14, 1, 1);
        BottomBrace3.setRotationPoint(-7F, 23F, -8F);
        BottomBrace3.setTextureSize(64, 128);
        BottomBrace3.mirror = true;
        setRotation(BottomBrace3, 0F, 0F, 0F);
        BottomBrace4 = new ModelRenderer(this, 15, 19);
        BottomBrace4.addBox(0F, 0F, 0F, 1, 1, 14);
        BottomBrace4.setRotationPoint(-8F, 23F, -7F);
        BottomBrace4.setTextureSize(64, 128);
        BottomBrace4.mirror = true;
        setRotation(BottomBrace4, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Base.render(f5);
        Pillars1.render(f5);
        Pillars2.render(f5);
        Pillars3.render(f5);
        Pillars4.render(f5);
        Brace1.render(f5);
        Brace2.render(f5);
        Brace3.render(f5);
        Brace4.render(f5);
//        Wall1.render(f5);
//        Wall2.render(f5);
//        Wall3.render(f5);
//        Wall4.render(f5);
        TopCorner1.render(f5);
        TopCorner2.render(f5);
        TopCorner3.render(f5);
        TopCorner4.render(f5);
        BottomBrace1.render(f5);
        BottomBrace2.render(f5);
        BottomBrace3.render(f5);
        BottomBrace4.render(f5);
        BottomCorner1.render(f5);
        BottomCorner2.render(f5);
        BottomCorner3.render(f5);
        BottomCorner4.render(f5);
    }

    public void renderFrame() {
        float f5 = 0.0625F;

        Pillars1.render(f5);
        Pillars2.render(f5);
        Pillars3.render(f5);
        Pillars4.render(f5);
        Brace1.render(f5);
        Brace2.render(f5);
        Brace3.render(f5);
        Brace4.render(f5);
        // Wall1.render(f5);
        // Wall2.render(f5);
        // Wall3.render(f5);
        // Wall4.render(f5);
        TopCorner1.render(f5);
        TopCorner2.render(f5);
        TopCorner3.render(f5);
        TopCorner4.render(f5);
        BottomBrace1.render(f5);
        BottomBrace2.render(f5);
        BottomBrace3.render(f5);
        BottomBrace4.render(f5);
        BottomCorner1.render(f5);
        BottomCorner2.render(f5);
        BottomCorner3.render(f5);
        BottomCorner4.render(f5);
    }

    public void renderColumn() {
        Pillars2.render(0.0625F);
    }

    public void render(boolean[] checkState) {
        if (checkState == null || checkState.length < 1)
            return;
        Entity entity = (Entity) null;
        float f5 = 0.0625F;
        super.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f5);
        setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f5, entity);
        if (checkState[0])
            Base.render(f5);
        if (checkState[1])
            // NEG X && NEG Z
            BottomCorner1.render(f5);
        if (checkState[2])
            // POS X && NEG Z
            BottomCorner2.render(f5);
        if (checkState[3])
            // POS X && POS Z
            BottomCorner3.render(f5);
        if (checkState[4])
            // NEG X && POS Z
            BottomCorner4.render(f5);
        if (checkState[5])
            // NEG Z
            Brace1.render(f5);
        if (checkState[6])
            // NEG X
            Brace2.render(f5);
        if (checkState[7])
            // POS X
            Brace3.render(f5);
        if (checkState[8])
            // POS Z
            Brace4.render(f5);
        if (checkState[9])
            // POS X && POS Z
            Pillars1.render(f5);
        if (checkState[10])
            // NEG X && NEG Z
            Pillars2.render(f5);
        if (checkState[11])
            // NEG X && POS Z
            Pillars3.render(f5);
        if (checkState[12])
            // POS X && NEG Z
            Pillars4.render(f5);

        // TODO Decide whether or not to have walls.
        // Wall1.render(f5);
        // Wall2.render(f5);
        // Wall3.render(f5);
        // Wall4.render(f5);

        // If ANY two are being rendered, you render.
        if (checkState[13])
            // NEG X && POS Z
            TopCorner1.render(f5);
        if (checkState[14])
            // NEG X && NEG Z
            TopCorner2.render(f5);
        if (checkState[15])
            // POS X && POS Z
            TopCorner3.render(f5);
        if (checkState[16])
            // POS X && NEG Z
            TopCorner4.render(f5);
        if (checkState[17])
            // NEG Z
            BottomBrace1.render(f5);
        if (checkState[18])
            // POS X
            BottomBrace2.render(f5);
        if (checkState[19])
            // POS Z
            BottomBrace3.render(f5);
        if (checkState[20])
            // NEG X
            BottomBrace4.render(f5);
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
