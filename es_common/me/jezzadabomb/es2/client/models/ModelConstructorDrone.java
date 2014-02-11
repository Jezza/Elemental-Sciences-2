package me.jezzadabomb.es2.client.models;

import me.jezzadabomb.es2.common.lib.Models;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelConstructorDrone {
	
	IModelCustom constructorDrone;

	public ModelConstructorDrone(){
	    // 10
	    // 10
	    // 13
	    // 4
	    // 6
	    // 4
	    // 13
	    // 10
	    // 10
		constructorDrone = AdvancedModelLoader.loadModel(Models.CONSTRUCTOR_DRONE);
	}
	
	public void render(){
		constructorDrone.renderAll();
	}
}
