package me.jezzadabomb.es2.common.items.equipment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;

public class ItemSelectiveEMPTrigger extends ItemES {

    public ItemSelectiveEMPTrigger(String name) {
        super(name);
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        CoordSet coordSet = new CoordSet(x, y, z);

        if (!world.isRemote && coordSet.isDroneBay(world))
            ((TileDroneBay) coordSet.getTileEntity(world)).recallDrones(-1);

        return false;
    }
}
