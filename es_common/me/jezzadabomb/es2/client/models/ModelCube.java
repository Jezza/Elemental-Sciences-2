package me.jezzadabomb.es2.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.common.lib.Models;
import net.minecraft.client.model.ModelBase;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

@SideOnly(Side.CLIENT)
public class ModelCube extends ModelBase {
    IModelCustom cube;

    public ModelCube() {
        cube = AdvancedModelLoader.loadModel(Models.CUBE);
    }

    public void renderAll() {
        cube.renderAll();
    }
}
