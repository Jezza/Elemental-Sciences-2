package me.jezzadabomb.es2.common.core.utils;

import net.minecraft.world.World;

/**
 * This entire class is thanks the CoFH team.
 * @author The CoFH Team
 *
 */
public class TimeTracker {

    private long lastMark = Long.MIN_VALUE;
    private final long delay = -1;

    public boolean hasDelayPassed(World world, int delay) {

            long currentTime = world.getTotalWorldTime();

            if (currentTime < lastMark) {
                    lastMark = currentTime;
                    return false;
            } else if (lastMark + delay <= currentTime) {
                    lastMark = currentTime;
                    return true;
            } else {
                    return false;
            }

    }

    public void markTime(World world) {

            lastMark = world.getTotalWorldTime();
    }

}