package me.jezzadabomb.es2.common.items.equipment;

import java.util.Random;

import me.jezzadabomb.es2.common.entities.EntityCombatDrone;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCombatDrone extends ItemES {

    public ItemCombatDrone(String name) {
        super(name);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            EntityCombatDrone combatDrone = new EntityCombatDrone(world);
            Random rand = new Random();

            float dX = rand.nextFloat() - 0.5F;
            float dZ = rand.nextFloat() - 0.5F;

            combatDrone.setPosition(player.posX - dX, player.posY + 1.5, player.posZ - dZ);
            combatDrone.setOwner(player);

            world.spawnEntityInWorld(combatDrone);
        }
        --itemStack.stackSize;
        return itemStack;
    }

}
