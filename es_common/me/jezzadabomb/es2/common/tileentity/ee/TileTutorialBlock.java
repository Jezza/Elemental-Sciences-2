package me.jezzadabomb.es2.common.tileentity.ee;

import net.minecraft.init.Blocks;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.BlockState;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Layer;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Row;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;

public class TileTutorialBlock extends TileES {

    int timeTicked = 0;
    DimensionalPattern dimensionalPattern = null;

    public TileTutorialBlock() {
        Row row1 = DimensionalPattern.createRow("###");
        Row row2 = DimensionalPattern.createRow("#C#");
        Row row3 = DimensionalPattern.createRow("###");

        Row row4 = DimensionalPattern.createRow("#O#");
        Row row5 = DimensionalPattern.createRow("GLO");
        Row row6 = DimensionalPattern.createRow("#O#");

        Row row7 = DimensionalPattern.createRow("###");
        Row row8 = DimensionalPattern.createRow("#O#");
        Row row9 = DimensionalPattern.createRow("###");

        Layer layer1 = DimensionalPattern.createLayer(row1, row2, row3);
        Layer layer2 = DimensionalPattern.createLayer(row4, row5, row6);
        Layer layer3 = DimensionalPattern.createLayer(row7, row8, row9);

        BlockState netherBrick = DimensionalPattern.createBlockState(Character.valueOf('#'), Blocks.nether_brick, 0);
        BlockState core = DimensionalPattern.createBlockState(Character.valueOf('C'), ModBlocks.atomicShredderDummyCore, 3);
        BlockState lava = DimensionalPattern.createBlockState(Character.valueOf('L'), Blocks.lava, 0);
        BlockState ironBar = DimensionalPattern.createBlockState(Character.valueOf('G'), Blocks.iron_bars, 0);
        BlockState obsidian = DimensionalPattern.createBlockState(Character.valueOf('O'), Blocks.obsidian, 0);

        dimensionalPattern = DimensionalPattern.createPattern("CrappyFurnace", layer1, layer2, layer3, netherBrick, core, lava, ironBar, obsidian);
    }

    @Override
    public void updateEntity() {

        if (worldObj.isRemote)
            return;

        if (++timeTicked > UtilMethods.getTimeInTicks(0, 0, 2, 0)) {
            ESLogger.info("Searching");
            if (dimensionalPattern.hasFormed(worldObj, xCoord - 1, yCoord, zCoord - 1)) {
                ESLogger.info("Formed");
            }
            ESLogger.info("");
            timeTicked = 0;
        }

    }

}
