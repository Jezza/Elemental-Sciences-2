package me.jezzadabomb.es2.client.models;

import me.jezzadabomb.es2.common.lib.Models;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelDroneBay {

    IModelCustom droneFrame;

    public ModelDroneBay() {
        droneFrame = AdvancedModelLoader.loadModel(Models.DRONE_BAY);
    }

    public void render() {
        droneFrame.renderAll();
    }

}
