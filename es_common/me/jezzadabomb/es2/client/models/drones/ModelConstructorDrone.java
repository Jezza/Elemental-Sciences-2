package me.jezzadabomb.es2.client.models.drones;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.client.lib.Models;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

@SideOnly(Side.CLIENT)
public class ModelConstructorDrone extends ModelDrone {
	
	IModelCustom constructorDrone;

	public ModelConstructorDrone(){
		constructorDrone = AdvancedModelLoader.loadModel(Models.CONSTRUCTOR_DRONE);
	}
	
	@Override
    public void render(){
		constructorDrone.renderAll();
	}
}
