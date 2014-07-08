package me.jezzadabomb.es2.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

@SideOnly(Side.CLIENT)
public abstract class ModelCustomAbstract {

    private IModelCustom customModel;

    public ModelCustomAbstract(ResourceLocation customModelLocation) {
        this.customModel = AdvancedModelLoader.loadModel(customModelLocation);
    }

    public void renderAll() {
        customModel.renderAll();
    }

    public void renderAllExcept(String... excludedGroupNames) {
        customModel.renderAllExcept(excludedGroupNames);
    }

    public void renderOnly(String... groupNames) {
        customModel.renderOnly(groupNames);
    }

    public void renderPart(String part) {
        customModel.renderPart(part);
    }
}
