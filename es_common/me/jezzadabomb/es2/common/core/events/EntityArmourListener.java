package me.jezzadabomb.es2.common.core.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class EntityArmourListener {
    HashMap<Integer, ArrayList<ItemStack>> inventoryMap = new HashMap<Integer, ArrayList<ItemStack>>();

    @ForgeSubscribe
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entity == null)
            return;

        EntityLivingBase entity = event.entityLiving;


        ArrayList<ItemStack> curInventory = null;
        if (entity instanceof EntityPlayer) {
            curInventory = new ArrayList<ItemStack>(Arrays.asList(((EntityPlayer) entity).inventory.armorInventory));
        } else if (entity instanceof EntityLiving) {
            curInventory = new ArrayList<ItemStack>(Arrays.asList(((EntityLiving) entity).getLastActiveItems()));
        }

        if (curInventory == null)
            return;

        int ID = entity.entityId;
        if (!inventoryMap.containsKey(ID)) {
            inventoryMap.put(ID, curInventory);
            return;
        }

        ArrayList<ItemStack> prevInventory = inventoryMap.get(ID);

        if (prevInventory == null)
            return;

        if (detectChanges(entity, curInventory, prevInventory)) {
            inventoryMap.remove(ID);
            inventoryMap.put(ID, curInventory);
        }
    }

    private boolean detectChanges(EntityLivingBase entity, ArrayList<ItemStack> curInventory, ArrayList<ItemStack> prevInventory) {
        boolean changed = false;
        boolean isMob = entity instanceof EntityLiving;
        BitSet updateChunk = new BitSet(4);

        for (int index = (isMob ? 1 : 0); index < curInventory.size(); index++)
            if (hasChanged(curInventory.get(index), prevInventory.get(index))) {
                updateChunk.set(index);
                changed = true;
            }

        if (changed)
            if (isMob) {
                MinecraftForge.EVENT_BUS.post(new MobArmourEvent((EntityLiving) entity, updateChunk));
            } else {
                MinecraftForge.EVENT_BUS.post(new PlayerArmourEvent((EntityPlayer) entity, updateChunk));
            }

        return changed;
    }

    private boolean hasChanged(ItemStack itemStack1, ItemStack itemStack2) {
        if (itemStack1 == null && itemStack2 == null)
            return false;

        if (itemStack1 == null && itemStack2 != null || itemStack1 != null && itemStack2 == null)
            return true;

        return !(itemStack1 != null && itemStack2 != null && itemStack1.getItem().itemID == itemStack2.getItem().itemID);
    }
}
