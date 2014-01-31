package me.jezzadabomb.es2.common.core.events;

import java.util.BitSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.IEventListener;

public class PlayerArmourEvent extends Event {

    EntityPlayer player;
    BitSet updateChunk;

    public PlayerArmourEvent(EntityPlayer player, BitSet updateChunk) {
        this.player = player;
        this.updateChunk = updateChunk;
    }

    public boolean isBootsChanged() {
        return updateChunk.get(0);
    }

    public boolean isLeggingsChanged() {
        return updateChunk.get(1);
    }

    public boolean isChestPlateChanged() {
        return updateChunk.get(2);
    }

    public boolean isHelmetChanged() {
        return updateChunk.get(3);
    }

    public ItemStack getBoots() {
        return player.inventory.armorInventory[0];
    }

    public ItemStack getLeggings() {
        return player.inventory.armorInventory[1];
    }

    public ItemStack getChestPlate() {
        return player.inventory.armorInventory[2];
    }

    public ItemStack getHelmet() {
        return player.inventory.armorInventory[3];
    }
}
