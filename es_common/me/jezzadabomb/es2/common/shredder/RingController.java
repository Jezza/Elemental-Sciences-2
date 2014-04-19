package me.jezzadabomb.es2.common.shredder;

import cpw.mods.fml.common.FMLCommonHandler;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.SteppingObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

public class RingController extends SteppingObject {

    int ringNum;

    public RingController(int ringNum, float startingAmount, float stepAmount, float upperLimit, float lowerLimit) {
        super(startingAmount, stepAmount, upperLimit, lowerLimit);
        this.ringNum = ringNum;
    }

    @Override
    public void preTick() {
        if (ringNum != 1)
            return;

//        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
//            ESLogger.info(getAmount());
//        }
        // NBTTagCompound tag = new NBTTagCompound();
        // writeToNBT(tag);
        // ESLogger.info(tag);
    }

    @Override
    public void postTick() {
        if (ringNum != 1)
            return;

    }

    @Override
    public String getIdentifier() {
        return "Ring" + ringNum;
    }

}
