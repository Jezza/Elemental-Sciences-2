package me.jezzadabomb.es2.client.models;

import me.jezzadabomb.es2.common.lib.Models;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelDroneBay {

    IModelCustom droneBay;

    public ModelDroneBay() {
        droneBay = AdvancedModelLoader.loadModel(Models.DRONE_BAY);
    }

    public void renderPart(String part){
        droneBay.renderPart(part);
    }
    
    public void renderDoor(int door){
        renderPart("Door" + door);
    }
    
    public void render() {
        droneBay.renderAll();
    }

}
