package me.jezzadabomb.es2.common.shredder;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.nbt.NBTTagCompound;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.SteppingObject;

public class ItemCasingController extends SteppingObject {

    public ItemCasingController(float startingAmount, float stepAmount, float upperLimit, float lowerLimit) {
        super(startingAmount, stepAmount, upperLimit, lowerLimit);
    }

    @Override
    public String getIdentifier() {
        return "ItemCasingController";
    }

}
