package me.jezzadabomb.es2.common.tileentity;

import me.jezzadabomb.es2.common.core.interfaces.IBlockNotifier;
import me.jezzadabomb.es2.common.core.interfaces.IPylon;
import me.jezzadabomb.es2.common.tileentity.framework.TilePylon;

public class TilePylonCrystal extends TilePylon implements IPylon, IBlockNotifier {

    @Override
    public int getPowerLevel() {
        return 3;
    }
}
