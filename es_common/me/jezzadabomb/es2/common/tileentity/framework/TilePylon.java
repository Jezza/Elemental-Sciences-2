package me.jezzadabomb.es2.common.tileentity.framework;

import me.jezzadabomb.es2.api.multiblock.DimensionalPattern;
import me.jezzadabomb.es2.api.multiblock.DimensionalPattern.BlockState;
import me.jezzadabomb.es2.api.multiblock.DimensionalPattern.Layer;
import me.jezzadabomb.es2.api.multiblock.DimensionalPattern.Row;
import me.jezzadabomb.es2.common.ModBlocks;

public class TilePylon extends TileES {

    private Row row1 = DimensionalPattern.createRow("#");
    private Row row2 = DimensionalPattern.createRow("A");
    private Row row3 = DimensionalPattern.createRow("B");
    private Row row4 = DimensionalPattern.createRow("C");
    private Row crystal = DimensionalPattern.createRow("*");

    private Layer layer1 = DimensionalPattern.createLayer(row1);

    private Layer layer2 = DimensionalPattern.createLayer(row2);
    private Layer layer3 = DimensionalPattern.createLayer(row3);
    private Layer layer4 = DimensionalPattern.createLayer(row4);

    private Layer crystalLayer = DimensionalPattern.createLayer(crystal);

    private BlockState strengthenedIronBlock = DimensionalPattern.createBlockState('#', ModBlocks.strengthenedIronBlock);

    private BlockState pylonObelisk0 = DimensionalPattern.createBlockState('A', ModBlocks.crystalObelisk, 0);
    private BlockState pylonObelisk1 = DimensionalPattern.createBlockState('B', ModBlocks.crystalObelisk, 1);
    private BlockState pylonObelisk2 = DimensionalPattern.createBlockState('C', ModBlocks.crystalObelisk, 2);

    private DimensionalPattern strengthenedIronPattern = DimensionalPattern.createPattern("PylonCrystalObeliskRecipe", layer1, layer1, layer1, crystalLayer, strengthenedIronBlock);
    private DimensionalPattern pylonObeliskPattern = DimensionalPattern.createPattern("PylonCrystalObelisk", layer2, layer3, layer4, crystal, pylonObelisk0, pylonObelisk1, pylonObelisk2);

    public DimensionalPattern getStrengthenedIronPattern() {
        return strengthenedIronPattern;
    }

    public DimensionalPattern getPylonObeliskPattern() {
        return pylonObeliskPattern;
    }
}
