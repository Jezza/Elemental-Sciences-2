package me.jezzadabomb.es2.client.models;

import me.jezzadabomb.es2.common.lib.Models;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelAtomicShredder extends ModelBase {

    IModelCustom modelAtomicShredder;

    public ModelAtomicShredder() {
        modelAtomicShredder = AdvancedModelLoader.loadModel(Models.ATOMIC_SHREDDER);
    }

    public void render() {
        modelAtomicShredder.renderAll();
    }

    public void renderPart(String part) {
        modelAtomicShredder.renderPart(part);
    }

    public void renderCasing(int casingNumber) {
        casingNumber = MathHelper.clamp_int(casingNumber, 1, 8);
        modelAtomicShredder.renderPart("InnerCasing" + casingNumber);
    }

    public IModelCustom getModel() {
        return modelAtomicShredder;
    }
}
