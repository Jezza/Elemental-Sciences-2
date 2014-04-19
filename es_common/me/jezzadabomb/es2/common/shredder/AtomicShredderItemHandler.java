package me.jezzadabomb.es2.common.shredder;

import cpw.mods.fml.common.FMLCommonHandler;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.helpers.InventoryHelper;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class AtomicShredderItemHandler {
    RingController ring1;
    RingController ring2;
    ItemCasingController casingController;

    int timeTicked = 40;

    private boolean destroyedTick = false;

    ItemStack[] inventory = new ItemStack[2];
    float hoverHeight = 0.0F;

    public AtomicShredderItemHandler() {
        ring1 = new RingController(1, 0.0F, 0.1F, 15.0F, 0.0F);
        ring2 = new RingController(2, 0.0F, 0.1F, 15.0F, 0.0F);
        casingController = new ItemCasingController(1.0F, 0.01F, 1.0F, 0.0F);
    }

    public void processingTick() {
        hoverTick();

        ring1.processingTick();
        ring2.processingTick();
        casingController.processingTick();
    }

    public void hoverTick() {
        if (hasCasedItem())
            destroyTick();

        if (!hasHoverItem() || hasCasedItem()) {
            hoverHeight = 0.0F;
            return;
        }

        if (hoverHeight >= 0.8F) {
            inventory[1] = inventory[0];
            inventory[0] = null;
            hoverHeight = 0.0F;

            ring1.goUp();
            ring2.goUp();

            casingController.goDown();
        }

        hoverHeight += 0.01F;
    }

    public void destroyTick() {
        if (!ring1.isGoingUp())
            ring1.goUp();

        if (!ring2.isGoingUp())
            ring2.goUp();

        if (ring1.hasReachedUpperLimit() && ring2.hasReachedUpperLimit() && casingController.hasReachedLowerLimit())
            destroyCasedItem(false);
    }

    public void destroyCasedItem(boolean simulate) {
        // TODO Particle effects
        if (!simulate)
            inventory[1] = null;

        ring1.goDown();
        ring2.goDown();

        casingController.goUp();
    }

    public boolean addItemStack(ItemStack itemStack) {
        if (inventory[0] != null)
            return false;

        inventory[0] = itemStack;
        itemStack = null;
        return true;
    }

    public boolean dropSlot(EntityPlayer player, int slot) {
        ItemStack tempStack = inventory[slot];

        if (InventoryHelper.addItemStackToInventory(player, tempStack))
            inventory[slot] = null;
        else {
            if (!player.worldObj.isRemote)
                player.entityDropItem(tempStack, 0.1F);
            inventory[slot] = null;
        }

        return inventory[slot] == null;
    }

    public boolean hasHoverItem() {
        return inventory[0] != null;
    }

    public ItemStack getHoverItem() {
        return inventory[0];
    }

    public float getHoverHeight() {
        return hoverHeight;
    }

    public boolean hasCasedItem() {
        return inventory[1] != null;
    }

    public ItemStack getCasedItem() {
        return inventory[1];
    }

    public float getCasingDistance() {
        return casingController.getAmount();
    }

    public boolean isCaseOpen() {
        return casingController.hasReachedUpperLimit();
    }

    public RingController getRing(int ringNum) {
        switch (ringNum) {
            case 1:
                return ring1;
            case 2:
                return ring2;
        }
        return null;
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setFloat("hoverHeight", hoverHeight);

        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
            if (inventory[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        tag.setTag("Items", tagList);

        ring1.writeToNBT(tag);
        ring2.writeToNBT(tag);

        casingController.writeToNBT(tag);
    }

    public void readFromNBT(NBTTagCompound tag) {
        hoverHeight = tag.getFloat("hoverHeight");

        NBTTagList tagList = tag.getTagList("Items", 10);
        inventory = new ItemStack[2];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = (NBTTagCompound) tagList.getCompoundTagAt(i);
            byte slotIndex = tagCompound.getByte("Slot");
            if (slotIndex >= 0 && slotIndex < inventory.length) {
                inventory[slotIndex] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }

        ring1.readFromNBT(tag);
        ring2.readFromNBT(tag);

        casingController.readFromNBT(tag);
    }
}
