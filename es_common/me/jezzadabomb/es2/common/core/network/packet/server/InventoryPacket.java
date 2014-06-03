package me.jezzadabomb.es2.common.core.network.packet.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.ArrayList;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketUtils;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSetF;
import me.jezzadabomb.es2.common.core.utils.helpers.InventoryHelper;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class InventoryPacket implements IPacket {

    public ArrayList<ItemStack> itemStacks;
    public String loc = null;
    public int tickTiming;
    public CoordSet coordSet;

    public InventoryPacket(TileEntity tileEntity, String loc) {
        if (tileEntity == null || !(tileEntity instanceof IInventory || tileEntity instanceof ISidedInventory))
            return;

        itemStacks = new ArrayList<ItemStack>();
        this.loc = loc;
        IInventory inventory = ((IInventory) tileEntity);
        for (int i = 0; i < inventory.getSizeInventory(); i++)
            if (inventory.getStackInSlot(i) != null)
                itemStacks.add(inventory.getStackInSlot(i));
    }

    public InventoryPacket() {
    }

    public boolean tick() {
        ESLogger.info(tickTiming);
        return ++tickTiming >= 100;
    }

    public boolean canRemove() {
        int dis = Reference.HUD_BLOCK_RANGE;
        return distanceToPlayer() > dis * dis;
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        PacketUtils.writeStringByteBuffer(buffer, loc);
        buffer.writeShort(itemStacks != null ? (itemStacks.isEmpty() ? (short) 0 : (short) itemStacks.size()) : (short) 0);
        if (itemStacks != null)
            for (ItemStack i : itemStacks)
                PacketUtils.writeItemStack(buffer, i);
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        loc = PacketUtils.readStringByteBuffer(buffer);
        short length = buffer.readShort();
        itemStacks = new ArrayList<ItemStack>(length);
        for (int i = 0; i < length; i++)
            itemStacks.add(PacketUtils.readItemStack(buffer));
    }

    @Override
    public void executeClientSide(EntityPlayer player) {
        World world = player.worldObj;
        CoordSet tempSet = UtilMethods.getArrayFromString(loc);
        if (tempSet == null)
            return;
        coordSet = tempSet;
        ClientProxy.getHUDRenderer().addPacketToList(this);
        tickTiming = 0;
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        ESLogger.severe("Tried to send a packet to the wrong side!");
    }

    public String getItemStacksInfo() {
        StringBuilder temp = new StringBuilder();
        for (ItemStack tempStack : getItemStacks()) {
            temp.append(tempStack.getUnlocalizedName() + ":" + tempStack.stackSize + ",");
        }
        return temp.toString();
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
        return loc + " " + getItemStacksInfo();
    }

    @Override
    public boolean equals(Object other) {
        return equals(other, false);
    }

    public boolean equals(Object other, boolean includeItemStacks) {
        if (other == null || !(other instanceof InventoryPacket))
            return false;

        InventoryPacket tempPacket = (InventoryPacket) other;
        if (!includeItemStacks)
            return coordSet.equals(tempPacket.coordSet);
        return coordSet.equals(tempPacket.coordSet) && this.itemStacks.equals(tempPacket.itemStacks);
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
