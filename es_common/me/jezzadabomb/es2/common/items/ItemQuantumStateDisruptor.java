package me.jezzadabomb.es2.common.items;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.helpers.PlayerHelper;
import me.jezzadabomb.es2.common.items.framework.ItemES;
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
            PlayerHelper.decrCurrentItem(player);
        }
        return true;
    }

    @Override
    protected void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
        information.defaultInfoList();
        information.addShiftList("The war between Life and Death");
        information.addShiftList("has never been a war.");
    }
}
