package me.jezzadabomb.es2.client.models;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.client.lib.Models;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelAtomicConstructor extends ModelCustomAbstract {

    public ModelAtomicConstructor() {
        super(Models.ATOMIC_CONSTRUCTOR);
    }

    public void render(boolean[] checkState) {
        if (checkState == null || checkState.length < 1)
            return;

        if (checkState[0])
            renderPart("BasePlate");

        if (checkState[1])
            // NEG X && POS Z
            renderPart("NegXPosZNegY");
        if (checkState[2])
            // POS X && POS Z
            renderPart("PosXPosZNegY");
        if (checkState[3])
            // POS X && NEG Z
            renderPart("PosXNegZNegY");
        if (checkState[4])
            // NEG X && NEG Z
            renderPart("NegXNegZNegY");

        if (checkState[5])
            // POS Z
            renderPart("UpperPosZ");
        if (checkState[6])
            // NEG X
            renderPart("UpperNegX");
        if (checkState[7])
            // POS X
            renderPart("UpperPosX");
        if (checkState[8])
            // NEG Z
            renderPart("UpperNegZ");

        if (checkState[9])
            // POS X && NEG Z
            renderPart("SupportPosXNegZ");
        if (checkState[10])
            // NEG X && POS Z
            renderPart("SupportNegXPosZ");
        if (checkState[11])
            // NEG X && NEG Z
            renderPart("SupportNegXNegZ");
        if (checkState[12])
            // POS X && POS Z
            renderPart("SupportPosXPosZ");

        if (checkState[13])
            // NEG X && NEG Z
            renderPart("NegXNegZPosY");
        if (checkState[14])
            // NEG X && POS Z
            renderPart("NegXPosZPosY");
        if (checkState[15])
            // POS X && NEG Z
            renderPart("PosXNegZPosY");
        if (checkState[16])
            // POS X && POS Z
            renderPart("PosXPosZPosY");

        if (checkState[17])
            // POS Z
            renderPart("BasePosZ");
        if (checkState[18])
            // POS X
            renderPart("BasePosX");
        if (checkState[19])
            // NEG Z
            renderPart("BaseNegZ");
        if (checkState[20])
            // NEG X
            renderPart("BaseNegX");
    }

    public void renderFrame() {
        renderAllExcept("BasePlate");
    }
}
