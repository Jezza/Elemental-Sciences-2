package me.jezzadabomb.es2.common.core.network.packet.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.ArrayList;
import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketUtils;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.helpers.InventoryHelper;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class InventoryPacket implements IPacket {

    public ArrayList<ItemStack> itemStacks;
    public String loc = null;
    public int tickTiming;
    public CoordSet coordSet;
    private boolean inventoryScanner = false;

    public InventoryPacket(IInventory inventory, String loc, boolean inventoryScanner) {
        this.loc = loc;
        this.inventoryScanner = inventoryScanner;

        itemStacks = new ArrayList<ItemStack>();
        for (int i = 0; i < inventory.getSizeInventory(); i++)
            if (inventory.getStackInSlot(i) != null)
                itemStacks.add(inventory.getStackInSlot(i));
    }

    public InventoryPacket() {
    }

    public boolean tick() {
        return ++tickTiming >= 100;
    }

    public boolean canRemove() {
        int dis = Reference.HUD_BLOCK_RANGE;
        if (inventoryScanner)
            dis *= 2;
        return distanceToPlayer() > dis * dis;
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        PacketUtils.writeStringByteBuffer(buffer, loc);
        buffer.writeBoolean(inventoryScanner);
        buffer.writeShort(itemStacks != null ? (itemStacks.isEmpty() ? (short) 0 : (short) itemStacks.size()) : (short) 0);
        if (itemStacks != null)
            for (ItemStack i : itemStacks)
                PacketUtils.writeItemStack(buffer, i);
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        loc = PacketUtils.readStringByteBuffer(buffer);
        inventoryScanner = buffer.readBoolean();
        short length = buffer.readShort();
        itemStacks = new ArrayList<ItemStack>();
        for (int i = 0; i < length; i++)
            itemStacks.add(PacketUtils.readItemStack(buffer));
    }

    @Override
    public void executeClientSide(EntityPlayer player) {
        World world = player.worldObj;
        CoordSet tempSet = CoordSet.getArrayFromString(loc);
        if (tempSet == null)
            return;
        coordSet = tempSet;
        ClientProxy.getHUDRenderer().addPacket(this);
        tickTiming = 0;
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        ESLogger.severe("Tried to send a packet to the wrong side!");
    }

    public ArrayList<ItemStack> getItemStacks() {
        ArrayList<ItemStack> tempStacks = new ArrayList<ItemStack>();
        for (ItemStack itemStack : itemStacks) {
            boolean added = false;
            for (ItemStack tempStack : tempStacks) {
                if (InventoryHelper.areItemStacksEqual(itemStack, tempStack)) {
                    InventoryHelper.mergeItemStacks(tempStack, itemStack, true);
                    added = true;
                }
            }
            if (!added)
                tempStacks.add(itemStack);
        }
        return tempStacks;
    }

    @Override
    public String toString() {
        return coordSet.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof InventoryPacket))
            return false;

        InventoryPacket tempPacket = (InventoryPacket) other;
        return coordSet.equals(tempPacket.coordSet);
    }

    @Override
    public int hashCode() {
        return coordSet.hashCode();
    }

    public double distanceToPlayer() {
        EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().renderViewEntity;
        CoordSetF playerSet = new CoordSet(player).toCoordSetF();

        return Math.abs(playerSet.distanceToSq(coordSet.toCoordSetF()));
    }
}
