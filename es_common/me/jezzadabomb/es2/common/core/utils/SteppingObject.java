package me.jezzadabomb.es2.common.core.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraft.nbt.NBTTagCompound;

public abstract class SteppingObject {

    private float stepAmount, amount, upperLimit, lowerLimit;
    private boolean upDirection = false;
    private boolean downDirection = false;

    public SteppingObject(int startingAmount, int stepAmount, int upperLimit, int lowerLimit) {
        this.amount = startingAmount;
        this.stepAmount = stepAmount;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    public SteppingObject(float startingAmount, float stepAmount, float upperLimit, float lowerLimit) {
        this.amount = startingAmount;
        this.stepAmount = stepAmount;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
    }

    public void processingTick() {
        preTick();
        if (upDirection)
            amount += stepAmount;
        if (downDirection)
            amount -= stepAmount;

        if (hasReachedUpperLimit()) {
            upDirection = false;
            amount = upperLimit;
        }

        if (hasReachedLowerLimit()) {
            downDirection = false;
            amount = lowerLimit;
        }
        postTick();
    }

    public void goUp() {
        upDirection = true;
        downDirection = false;
    }

    public void goDown() {
        upDirection = false;
        downDirection = true;
    }

    public float getAmount() {
        return amount;
    }

    public boolean isGoingUp() {
        return upDirection;
    }

    public boolean isGoingDown() {
        return downDirection;
    }

    public boolean hasReachedUpperLimit() {
        return amount >= upperLimit;
    }

    public boolean hasReachedLowerLimit() {
        return amount <= lowerLimit;
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setBoolean("upDirection:" + getIdentifier(), upDirection);
        tag.setBoolean("downDirection:" + getIdentifier(), downDirection);

        tag.setFloat("amount" + getIdentifier(), amount);
        tag.setFloat("stepAmount" + getIdentifier(), stepAmount);
        tag.setFloat("upperLimit" + getIdentifier(), upperLimit);
        tag.setFloat("lowerLimit" + getIdentifier(), lowerLimit);
    }

    public void readFromNBT(NBTTagCompound tag) {
        upDirection = tag.getBoolean("upDirection:" + getIdentifier());
        downDirection = tag.getBoolean("downDirection:" + getIdentifier());

        amount = tag.getFloat("amount" + getIdentifier());
        stepAmount = tag.getFloat("stepAmount" + getIdentifier());
        upperLimit = tag.getFloat("upperLimit" + getIdentifier());
        lowerLimit = tag.getFloat("lowerLimit" + getIdentifier());
    }

    public void preTick() {
    };

    public void postTick() {
    };

    public abstract String getIdentifier();
}
