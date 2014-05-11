package me.jezzadabomb.es2.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.IPylonRegistry;
import me.jezzadabomb.es2.common.core.interfaces.IPylon;
import me.jezzadabomb.es2.common.core.interfaces.IPylonReceiver;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern;
import me.jezzadabomb.es2.common.core.utils.SteppingObject;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.BlockState;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Flag;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Layer;
import me.jezzadabomb.es2.common.core.utils.DimensionalPattern.Row;
import me.jezzadabomb.es2.common.core.utils.Identifier;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;

public class TilePylonCrystal extends TileES implements IPylon {

    private int tier = 0;

    private boolean registered = false;

    private DimensionalPattern strengthenedIronPattern;
    private DimensionalPattern pylonObeliskPattern;

    public TilePylonCrystal(int tier) {
        this.tier = tier;

        Row row1 = DimensionalPattern.createRow("#");
        Row row2 = DimensionalPattern.createRow("*");

        Layer layer1 = DimensionalPattern.createLayer(row1);
        Layer layer2 = DimensionalPattern.createLayer(row2);

        BlockState strengthenedIronBlock = DimensionalPattern.createBlockState('#', ModBlocks.strengthenedIronBlock);
        BlockState pylonObelisk = DimensionalPattern.createBlockState('#', ModBlocks.crystalObelisk);

        strengthenedIronPattern = DimensionalPattern.createPattern("PylonCrystalObeliskRecipe", layer1, layer1, layer1, layer2, strengthenedIronBlock);
        pylonObeliskPattern = DimensionalPattern.createPattern("PylonCrystalObelisk", layer1, layer1, layer1, layer2, pylonObelisk);
    }

//    public TilePylonCrystal() {
//    }

    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;

        if (!registered)
            registered = IPylonRegistry.registerPylon(worldObj, getCoordSet(), tier);

        if (!pylonObeliskPattern.hasFormed(worldObj, xCoord, yCoord - 3, zCoord)) {
            strengthenedIronPattern.convert(worldObj, xCoord, yCoord - 3, zCoord, Flag.IGNORE);
            worldObj.setBlock(xCoord, yCoord, zCoord, ModBlocks.pylonCrystal, tier + 3, 3);
        }
    }

    public void notifyBlockBroken() {
        registered = !IPylonRegistry.removePylon(worldObj, getCoordSet(), tier);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

    }
}
