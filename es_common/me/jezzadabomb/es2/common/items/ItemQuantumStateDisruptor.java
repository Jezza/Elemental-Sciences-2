package me.jezzadabomb.es2.common.items;

import me.jezzadabomb.es2.CommonProxy;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import me.jezzadabomb.es2.common.tileentity.TileQuantumBomb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemQuantumStateDisruptor extends ItemES {

    public ItemQuantumStateDisruptor(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (side != 1 || world.isAirBlock(x, y, z))
            return false;
        y += 1;
        if (!world.isRemote && !(new CoordSet(player).isAtXYZ(x, y, z))) {
            world.setBlock(x, y, z, ModBlocks.quantumStateDisrupter);
            UtilMethods.decrCurrentItem(player);
        }
        return true;
    }

    @Override
    protected void addInformation(EntityPlayer player, ItemStack stack) {
        defaultInfoList();
        shiftList.add("The war between Life and Death");
        shiftList.add("has never been a victory.");
    }
}
