package me.jezzadabomb.es2.common.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

import me.jezzadabomb.es2.common.lib.Reference;

public abstract class CentralPacket {

    public static final String CHANNEL = Reference.CHANNEL_NAME;

    private static final BiMap<Integer, Class<? extends CentralPacket>> idMap;

    static {
        ImmutableBiMap.Builder<Integer, Class<? extends CentralPacket>> builder = ImmutableBiMap.builder();

        builder.put(Integer.valueOf(0), TestPacket.class);
        builder.put(Integer.valueOf(1), InventoryRequestPacket.class);
        
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
