package me.jezzadabomb.es2.common.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;

public class ContainerAtomicCatalystDebug extends ContainerES {

    EntityPlayer player;
    ItemStack itemStack;

    public ContainerAtomicCatalystDebug(EntityPlayer player, ItemStack itemStack) {
        super(new CoordSet(player), 8);
        this.player = player;
        this.itemStack = itemStack;
    }

}
