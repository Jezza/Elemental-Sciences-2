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

    HashMap<String, ArrayList<ItemStack>> playerInventoryMap = new HashMap<String, ArrayList<ItemStack>>();
    HashMap<Integer, ArrayList<ItemStack>> mobInventoryMap = new HashMap<Integer, ArrayList<ItemStack>>();

    @ForgeSubscribe
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entity == null)
            return;
        if (event.entity instanceof EntityPlayer) {
            handlePlayerUpdate(event);
        } else if (event.entity instanceof EntityLiving) {
            handleMobUpdate(event);
        }
    }

    private void handlePlayerUpdate(LivingUpdateEvent event) {
        EntityPlayer player = (EntityPlayer) event.entity;

        String username = player.username;

        ArrayList<ItemStack> curInventory = new ArrayList<ItemStack>(Arrays.asList(player.inventory.armorInventory));

        if (!playerInventoryMap.containsKey(username)) {
            playerInventoryMap.put(username, curInventory);
            return;
        }

        ArrayList<ItemStack> prevInventory = playerInventoryMap.get(username);

        if (curInventory == null || prevInventory == null)
            return;

        if (detectChanges(player, curInventory, prevInventory)) {
            playerInventoryMap.remove(username);
            playerInventoryMap.put(username, curInventory);
        }
    }

    private void handleMobUpdate(LivingUpdateEvent event) {
        EntityLiving mob = (EntityLiving) event.entity;

        int ID = mob.entityId;

        ArrayList<ItemStack> curInventory = new ArrayList<ItemStack>(Arrays.asList(mob.getLastActiveItems()));

        if (!mobInventoryMap.containsKey(ID)) {
            mobInventoryMap.put(ID, curInventory);
            return;
        }

        ArrayList<ItemStack> prevInventory = mobInventoryMap.get(ID);

        if (curInventory == null || prevInventory == null)
            return;

        if (detectChanges(mob, curInventory, prevInventory)) {
            mobInventoryMap.remove(ID);
            mobInventoryMap.put(ID, curInventory);
        }

    }

    private boolean detectChanges(EntityLivingBase entity, ArrayList<ItemStack> curInventory, ArrayList<ItemStack> prevInventory) {
        boolean changed = false;
        boolean isMob = entity instanceof EntityPlayer;
        BitSet updateChunk = new BitSet(4);

        for (int index = (isMob ? 1 : 0); index < curInventory.size(); index++)
            if (hasChanged(curInventory.get(index), prevInventory.get(index))) {
                updateChunk.set(index);
                changed = true;
            }

        if (changed) {
            if (isMob) {
                MinecraftForge.EVENT_BUS.post(new MobArmourEvent((EntityLiving) entity, updateChunk));
            } else {
                MinecraftForge.EVENT_BUS.post(new PlayerArmourEvent((EntityPlayer) entity, updateChunk));
            }
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
