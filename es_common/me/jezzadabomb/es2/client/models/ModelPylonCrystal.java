package me.jezzadabomb.es2.client.models;

import me.jezzadabomb.es2.client.lib.Models;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelPylonCrystal {

    IModelCustom model;

    public ModelPylonCrystal() {
        model = AdvancedModelLoader.loadModel(Models.PYLON_CRYSTAL);
    }

    public void render() {
        model.renderAll();
    }

}
