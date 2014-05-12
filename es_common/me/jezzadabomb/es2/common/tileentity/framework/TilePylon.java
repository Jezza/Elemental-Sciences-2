package me.jezzadabomb.es2.common.tileentity.framework;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.BlockState;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Layer;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Row;

public class TilePylon extends TileES {

    private Row row1 = DimensionalPattern.createRow("#");
    private Row row2 = DimensionalPattern.createRow("*");

    private Layer layer1 = DimensionalPattern.createLayer(row1);
    private Layer layer2 = DimensionalPattern.createLayer(row2);

    private BlockState strengthenedIronBlock = DimensionalPattern.createBlockState('#', ModBlocks.strengthenedIronBlock);
    private BlockState pylonObelisk = DimensionalPattern.createBlockState('#', ModBlocks.crystalObelisk);

    private DimensionalPattern strengthenedIronPattern = DimensionalPattern.createPattern("PylonCrystalObeliskRecipe", layer1, layer1, layer1, layer2, strengthenedIronBlock);
    private DimensionalPattern pylonObeliskPattern = DimensionalPattern.createPattern("PylonCrystalObelisk", layer1, layer1, layer1, layer2, pylonObelisk);
    
    public DimensionalPattern getStrengthenedIronPattern() {
        return strengthenedIronPattern;
    }
    
    public DimensionalPattern getPylonObeliskPattern() {
        return pylonObeliskPattern;
    }
}
