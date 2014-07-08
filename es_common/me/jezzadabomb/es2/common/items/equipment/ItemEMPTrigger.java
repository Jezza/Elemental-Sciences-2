package me.jezzadabomb.es2.common.items.equipment;

import java.util.List;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemEMPTrigger extends ItemES {

    public ItemEMPTrigger(String name) {
        super(name);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        float range = 5.0F; // Around the player.
        List<Object> droneList = world.getEntitiesWithinAABB(EntityConstructorDrone.class, AxisAlignedBB.getBoundingBox(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
        for (Object object : droneList) {
            if (object instanceof EntityConstructorDrone) {
                EntityConstructorDrone drone = (EntityConstructorDrone) object;
                drone.setDead();
                ItemStack droneStack = new ItemStack(ModItems.constructorDrone);
                if (!player.capabilities.isCreativeMode && !player.inventory.addItemStackToInventory(droneStack))
                    world.spawnEntityInWorld(new EntityItem(world, drone.posX, drone.posY, drone.posZ, droneStack));
            }
        }
        if (!world.isRemote)
            UtilMethods.addChatMessage(player, "Removed " + droneList.size() + " drones.");

        boolean flag = !droneList.isEmpty();

        if (flag) {
            player.swingItem();
            if (!player.capabilities.isCreativeMode)
                --itemStack.stackSize;
        }
        return itemStack;
    }

}
