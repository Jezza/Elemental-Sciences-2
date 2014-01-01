package me.jezzadabomb.es2.common.packets.handler;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import me.jezzadabomb.es2.common.packets.InventoryRequestPacket;
import me.jezzadabomb.es2.common.packets.InventoryTerminatePacket;
import me.jezzadabomb.es2.common.packets.PlayerBombPacket;

public abstract class CentralPacket {

    public static final String CHANNEL = Reference.CHANNEL_NAME;

    private static final BiMap<Integer, Class<? extends CentralPacket>> idMap;

    static {
        ImmutableBiMap.Builder<Integer, Class<? extends CentralPacket>> builder = ImmutableBiMap.builder();

        builder.put(Integer.valueOf(1), InventoryRequestPacket.class);
        builder.put(Integer.valueOf(2), InventoryPacket.class);
        builder.put(Integer.valueOf(3), InventoryTerminatePacket.class);
        builder.put(Integer.valueOf(4), PlayerBombPacket.class);

        idMap = builder.build();
    }

    public static CentralPacket constructPacket(int packetId) throws ProtocolException, ReflectiveOperationException {
        Class<? extends CentralPacket> clazz = idMap.get(Integer.valueOf(packetId));
        if (clazz == null) {
            throw new ProtocolException("Unknown Packet Id!");
        } else {
            return clazz.newInstance();
        }
    }

    @SuppressWarnings("serial")
    public static class ProtocolException extends Exception {

        public ProtocolException() {
        }

        public ProtocolException(String message, Throwable cause) {
            super(message, cause);
        }

        public ProtocolException(String message) {
            super(message);
        }

        public ProtocolException(Throwable cause) {
            super(cause);
        }
    }

    public final int getPacketId() {
        if (idMap.inverse().containsKey(getClass())) {
            return idMap.inverse().get(getClass()).intValue();
        } else {
            throw new RuntimeException("Packet " + getClass().getSimpleName() + " is missing a mapping!");
        }
    }

    public static void writeItemStack(ItemStack par0ItemStack, DataOutput par1DataOutput) throws IOException {
        if (par0ItemStack == null) {
            par1DataOutput.writeShort(-1);
        } else {
            par1DataOutput.writeShort(par0ItemStack.itemID);
            par1DataOutput.writeByte(par0ItemStack.stackSize);
            par1DataOutput.writeShort(par0ItemStack.getItemDamage());
            NBTTagCompound nbttagcompound = null;

            if (par0ItemStack.getItem().isDamageable() || par0ItemStack.getItem().getShareTag()) {
                nbttagcompound = par0ItemStack.stackTagCompound;
            }

            writeNBTTagCompound(nbttagcompound, par1DataOutput);
        }
    }

    protected static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutput par1DataOutput) throws IOException {
        if (par0NBTTagCompound == null) {
            par1DataOutput.writeShort(-1);
        } else {
            byte[] abyte = CompressedStreamTools.compress(par0NBTTagCompound);
            par1DataOutput.writeShort((short) abyte.length);
            par1DataOutput.write(abyte);
        }
    }

    public static ItemStack readItemStack(DataInput par0DataInput) throws IOException {
        ItemStack itemstack = null;
        short short1 = par0DataInput.readShort();

        if (short1 >= 0) {
            byte b0 = par0DataInput.readByte();
            short short2 = par0DataInput.readShort();
            itemstack = new ItemStack(short1, b0, short2);
            itemstack.stackTagCompound = readNBTTagCompound(par0DataInput);
        }

        return itemstack;
    }

    public static NBTTagCompound readNBTTagCompound(DataInput par0DataInput) throws IOException {
        short short1 = par0DataInput.readShort();

        if (short1 < 0) {
            return null;
        } else {
            byte[] abyte = new byte[short1];
            par0DataInput.readFully(abyte);
            return CompressedStreamTools.decompress(abyte);
        }
    }

    public final Packet makePacket() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeByte(getPacketId());
        write(out);
        return PacketDispatcher.getPacket(CHANNEL, out.toByteArray());
    }

    public abstract void write(ByteArrayDataOutput out);

    public abstract void read(ByteArrayDataInput in) throws ProtocolException;

    public abstract void execute(EntityPlayer player, Side side) throws ProtocolException;
}
