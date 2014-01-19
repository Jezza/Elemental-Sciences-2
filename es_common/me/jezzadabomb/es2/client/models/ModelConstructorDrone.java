package me.jezzadabomb.es2.client.models;

import me.jezzadabomb.es2.common.lib.Models;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelConstructorDrone {
	
	IModelCustom constructorDrone;

	public ModelConstructorDrone(){
		constructorDrone = AdvancedModelLoader.loadModel(Models.ATOMIC_CATALYST_MAIN);
	}
	
	public void render(){
		constructorDrone.renderAll();
	}
}
