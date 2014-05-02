package me.jezzadabomb.es2.common.tileentity;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import me.jezzadabomb.es2.common.core.interfaces.IPylon;
import me.jezzadabomb.es2.common.core.interfaces.IPylonReceiver;
import me.jezzadabomb.es2.common.core.utils.Identifier;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;

public class TilePylonCrystal extends TileES implements IPylon {

    private int tier = 0;
    private boolean broadcasted = false;

    public TilePylonCrystal(int tier) {
        this.tier = tier;
    }

    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;

        if (!broadcasted) {
            searchAndAlertNearby(true);
            broadcasted = true;
        }
    }

    public void searchAndAlertNearby(boolean joining) {
        int range = (tier + 1) * 12;
        for (int i = -range; i <= range; i++)
            for (int j = -range; j <= range; j++)
                for (int k = -range; k <= range; k++) {
                    int x = xCoord + i;
                    int y = yCoord + j;
                    int z = zCoord + k;
                    if (!worldObj.blockExists(x, y, z))
                        continue;
                    if (Identifier.isPylonReciever(worldObj, x, y, z))
                        if (joining)
                            ((IPylonReceiver) worldObj.getTileEntity(x, y, z)).pylonEnteredArea(tier);
                        else
                            ((IPylonReceiver) worldObj.getTileEntity(x, y, z)).pylonLeftArea(tier);
                }
    }
}
