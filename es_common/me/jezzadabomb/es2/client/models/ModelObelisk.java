package me.jezzadabomb.es2.client.models;

import me.jezzadabomb.es2.client.lib.Models;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelObelisk {

    IModelCustom modelObelisk;

    public ModelObelisk() {
        modelObelisk = AdvancedModelLoader.loadModel(Models.OBELISK);
    }

    public void renderAll() {
        modelObelisk.renderAll();
    }

}
