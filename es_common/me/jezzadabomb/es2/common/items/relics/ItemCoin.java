package me.jezzadabomb.es2.common.items.relics;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.items.framework.ItemES;

public class ItemCoin extends ItemES {

    private String[] lore = null;

    public ItemCoin(String name) {
        super(name);
    }

    public ItemCoin setLore(String... lore) {
        this.lore = lore;
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
        for (String line : lore)
            information.addToBothLists(line);
    }

}
