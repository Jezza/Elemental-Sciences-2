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

    private DimensionalPattern stregthenedIronPattern;
    private DimensionalPattern obeliskPattern;

    public TilePylonCrystal(int tier) {
        this.tier = tier;

        Row row1 = DimensionalPattern.createRow("#");
        Row row2 = DimensionalPattern.createRow("*");

        Layer layer1 = DimensionalPattern.createLayer(row1);
        Layer layer2 = DimensionalPattern.createLayer(row2);

        BlockState strengthenedIronBlock = DimensionalPattern.createBlockState('#', ModBlocks.strengthenedIronBlock);
        BlockState pylonCrystalPattern = DimensionalPattern.createBlockState('#', ModBlocks.crystalObelisk);

        stregthenedIronPattern = DimensionalPattern.createPattern("PylonCrystalObeliskRecipe", layer1, layer1, layer1, layer2, strengthenedIronBlock);
        obeliskPattern = DimensionalPattern.createPattern("PylonCrystalObelisk", layer1, layer1, layer1, layer2, pylonCrystalPattern);
    }

    public TilePylonCrystal() {
    }

    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;
        
//        boolean formed = obeliskPattern.hasFormed(worldObj, xCoord, yCoord - 3, zCoord);

//        if (formed) {
            if (!registered)
                registered = IPylonRegistry.registerPylon(worldObj, getCoordSet(), tier);
//        } else {
//            if (registered)
//                registered = !IPylonRegistry.removePylon(worldObj, getCoordSet());
//
//            boolean shouldForm = stregthenedIronPattern.hasFormed(worldObj, xCoord, yCoord - 3, zCoord);
//            if (shouldForm)
//                obeliskPattern.convert(worldObj, xCoord, yCoord - 3, zCoord, Flag.IGNORE);
//        }
    }

    private boolean shouldForm() {
        return !obeliskPattern.hasFormed(worldObj, xCoord, yCoord - 3, zCoord) || stregthenedIronPattern.hasFormed(worldObj, xCoord, yCoord - 3, zCoord);
    }

    public void notifyBlockBroken() {
        registered = !IPylonRegistry.removePylon(worldObj, getCoordSet());
    }

    // public void searchAndAlertNearby(boolean joining) {
    // int range = (tier + 1) * 12;
    // for (int i = -range; i <= range; i++)
    // for (int j = -range; j <= range; j++)
    // for (int k = -range; k <= range; k++) {
    // int x = xCoord + i;
    // int y = yCoord + j;
    // int z = zCoord + k;
    // if (!worldObj.blockExists(x, y, z))
    // continue;
    // if (Identifier.isPylonReciever(worldObj, x, y, z))
    // if (joining)
    // ((IPylonReceiver) worldObj.getTileEntity(x, y, z)).pylonEnteredArea(tier);
    // else
    // ((IPylonReceiver) worldObj.getTileEntity(x, y, z)).pylonLeftArea(tier);
    // }
    // }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

    }
}
