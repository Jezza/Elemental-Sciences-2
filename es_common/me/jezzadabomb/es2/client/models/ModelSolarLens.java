package me.jezzadabomb.es2.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.lib.Models;
import net.minecraft.client.model.ModelBase;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

@SideOnly(Side.CLIENT)
public class ModelSolarLens extends ModelBase {
	IModelCustom lens;
	IModelCustom loop;

	public ModelSolarLens() {
		lens = AdvancedModelLoader.loadModel(Models.SOLAR_LENS);
		loop = AdvancedModelLoader.loadModel(Models.SOLAR_LENS_LOOP);
	}

	public void renderAll() {
		lens.renderAll();
		loop.renderAll();
	}

	public void renderPart(String part) {
		switch (part) {
		case "lens":
			lens.renderAll();
			break;
		case "loop":
			loop.renderAll();
			break;
		default:
			return;
		}
	}
}
