package me.jezzadabomb.es2.client.models;

import me.jezzadabomb.es2.client.lib.Models;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelCrystalObelisk {

    IModelCustom modelCrystalObelisk;
    IModelCustom modelCrystalObeliskTop;

    public ModelCrystalObelisk() {
        modelCrystalObelisk = AdvancedModelLoader.loadModel(Models.CRYSTAL_OBELISK);
        modelCrystalObeliskTop = AdvancedModelLoader.loadModel(Models.CRYSTAL_TOP_OBELISK);
    }

    public void renderUpper() {
        modelCrystalObeliskTop.renderAll();
    }

    public void renderLower() {
        modelCrystalObelisk.renderAll();
    }

}
