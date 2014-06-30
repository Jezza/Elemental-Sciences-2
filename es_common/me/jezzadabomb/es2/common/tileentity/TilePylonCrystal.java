package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.tileentity.framework.TilePylon;

public class TilePylonCrystal extends TilePylon {

    private int powerLevel = 1;

    public TilePylonCrystal(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public TilePylonCrystal() {
    }

    @Override
    public int getPowerLevel() {
        return powerLevel;
    }
}
