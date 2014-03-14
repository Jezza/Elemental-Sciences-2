package me.jezzadabomb.es2.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.common.lib.Models;
import net.minecraft.client.model.ModelBase;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

@SideOnly(Side.CLIENT)
public class ModelPixel extends ModelBase {
	IModelCustom pixel;

	public ModelPixel() {
		pixel = AdvancedModelLoader.loadModel(Models.ATOMIC_CATALYST_MAIN);
	}

	public void renderAll() {
		pixel.renderAll();
	}
}
