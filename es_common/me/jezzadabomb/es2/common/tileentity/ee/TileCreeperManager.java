package me.jezzadabomb.es2.common.tileentity.ee;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Blocks;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Layer;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Row;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.BlockState;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;

public class TileCreeperManager extends TileES {

    int ticked = 0;
    DimensionalPattern dimensionalPattern;

    public TileCreeperManager() {
        Row row1 = DimensionalPattern.createRow("#*#");
        Row row2 = DimensionalPattern.createRow("*G*");
        Row row3 = DimensionalPattern.createRow("#*#");

        Row row4 = DimensionalPattern.createRow("***");
        Row row5 = DimensionalPattern.createRow("*G*");
        Row row6 = DimensionalPattern.createRow("***");

        Row row7 = DimensionalPattern.createRow("***");
        Row row8 = DimensionalPattern.createRow("*C*");
        Row row9 = DimensionalPattern.createRow("***");

        Layer layer1 = DimensionalPattern.createLayer(row1, row2, row3);
        Layer layer2 = DimensionalPattern.createLayer(row4, row5, row6);
        Layer layer3 = DimensionalPattern.createLayer(row7, row8, row9);

        BlockState greenCarpet = DimensionalPattern.createBlockState(Character.valueOf('#'), Blocks.carpet, 13);
        BlockState greenWool = DimensionalPattern.createBlockState(Character.valueOf('G'), Blocks.wool, 13);
        BlockState creeperCore = DimensionalPattern.createBlockState(Character.valueOf('C'), ModBlocks.atomicShredderDummyCore, 2);

        dimensionalPattern = DimensionalPattern.createPattern("CreeperManager", layer1, layer2, layer3, greenCarpet, greenWool, creeperCore);
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote)
            return;

        if (++ticked > UtilMethods.getTimeInTicks(0, 0, 5, 0)) {
            ESLogger.info("Ticking");
            if (dimensionalPattern.hasFormed(worldObj, xCoord - 1, yCoord - 2, zCoord - 1))
                convert();
            ticked = 0;
        }
    }

    public void convert() {
        for (int i = -1; i <= 1; i++) {
            for (int j = -2; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    worldObj.setBlockToAir(xCoord + i, yCoord + j, zCoord + k);
                }
            }
        }
        EntityCreeper creeper = new EntityCreeper(worldObj);
        creeper.setPosition(xCoord + 0.5F, yCoord - 2.0F, zCoord + 0.5F);
        worldObj.spawnEntityInWorld(creeper);
    }

}
