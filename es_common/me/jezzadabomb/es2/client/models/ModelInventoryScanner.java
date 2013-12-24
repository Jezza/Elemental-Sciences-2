package me.jezzadabomb.es2.client.models;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.lib.Models;
import net.minecraft.client.model.ModelBase;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelInventoryScanner extends ModelBase {

	public ArrayList<IModelCustom> parts = new ArrayList<IModelCustom>();

	public ModelInventoryScanner() {
        parts.add(AdvancedModelLoader.loadModel(Models.INVENTORY_SCANNER_BASE_PLATE));
        parts.add(AdvancedModelLoader.loadModel(Models.INVENTORY_SCANNER_SPINNER));
    }
	
	public void renderAll() {
        for(IModelCustom m : parts){
        	ESLogger.debug(m);
            m.renderAll();
        }
    }
	
	public void renderPart(String part){
        switch(part){
            case "Base":
                parts.get(0).renderAll();
                break;
            case "Spinner":
                parts.get(1).renderAll();
                break;
            default:
                return;
        }
    }
	
}
