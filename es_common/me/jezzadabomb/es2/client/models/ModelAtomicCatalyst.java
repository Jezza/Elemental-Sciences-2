package me.jezzadabomb.es2.client.models;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.lib.Models;
import net.minecraft.client.model.ModelBase;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelAtomicCatalyst extends ModelBase{
    
    public ArrayList<IModelCustom> parts = new ArrayList<IModelCustom>();

    
    public ModelAtomicCatalyst() {
        parts.add(AdvancedModelLoader.loadModel(Models.ATOMIC_CATALYST_MAIN));
        parts.add(AdvancedModelLoader.loadModel(Models.ATOMIC_CATALYST_ELECTRON1));
        parts.add(AdvancedModelLoader.loadModel(Models.ATOMIC_CATALYST_ELECTRON2));
        parts.add(AdvancedModelLoader.loadModel(Models.ATOMIC_CATALYST_ELECTRON3));
    }

    public void renderAll() {
        for(IModelCustom m : parts){
            m.renderAll();
        }
    }
    
    public void renderPart(String part){
        switch(part){
            case "Main":
                parts.get(0).renderAll();
                break;
            case "Electron1":
                parts.get(1).renderAll();
                break;
            case "Electron2":
                parts.get(2).renderAll();
                break;
            case "Electron3":
                parts.get(3).renderAll();
                break;
            default:
                return;
        }
    }
}
