package me.jezzadabomb.es2.common.items.research;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemResearchTool extends ItemES {

    private int guiID;

    public ItemResearchTool(String name, int guiID) {
        super(name);
        this.guiID = guiID;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote)
            player.openGui(ElementalSciences2.instance, guiID, world, 0, 0, 0);
        return itemStack;
    }

}
