package me.jezzadabomb.es2.common.core.utils.helpers;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.BlockState;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Layer;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Row;

public class DimensionalPatternHelper {

    public static DimensionalPattern getAtomicShredder() {
        Row row1 = DimensionalPattern.createRow("###");
        Row row2 = DimensionalPattern.createRow("#G#");
        Row row3 = DimensionalPattern.createRow("###");

        Row row4 = DimensionalPattern.createRow("#G#");
        Row row5 = DimensionalPattern.createRow("GCG");
        Row row6 = DimensionalPattern.createRow("#G#");

        Layer layer1 = DimensionalPattern.createLayer(row1, row2, row3);
        Layer layer2 = DimensionalPattern.createLayer(row4, row5, row6);
        Layer layer3 = DimensionalPattern.createLayer(row1, row2, row3);

        BlockState shredderFrame = DimensionalPattern.createBlockState(Character.valueOf('#'), ModBlocks.atomicShredderDummyCore, 0);
        BlockState shredderGlass = DimensionalPattern.createBlockState(Character.valueOf('G'), ModBlocks.atomicShredderDummy, 1);
        BlockState shredderDummyCore = DimensionalPattern.createBlockState(Character.valueOf('C'), ModBlocks.atomicShredderDummyCore, 1);

        return DimensionalPattern.createPattern("AtomicShredderDummyCore", layer1, layer2, layer3, shredderFrame, shredderGlass, shredderDummyCore);
    }

}
