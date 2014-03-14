package me.jezzadabomb.es2.common.core.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.network.packet.client.*;
import me.jezzadabomb.es2.common.core.network.packet.server.*;

public class PacketDispatcher {

    private static final PacketPipeline packetPipeline = ElementalSciences2.packetPipeline;

    public static void sendToAll(IPacket message) {
        packetPipeline.sendToAll(message);
    }

    public static void sendTo(IPacket message, EntityPlayerMP player) {
        packetPipeline.sendTo(message, player);
    }

    public static void sendPacketToPlayer(IPacket message, EntityPlayerMP player) {
        sendTo(message, player);
    }

    public static void sendToAllAround(IPacket message, NetworkRegistry.TargetPoint point) {
        packetPipeline.sendToAllAround(message, point);
    }

    public static void sendToDimension(IPacket message, int dimensionId) {
        packetPipeline.sendToDimension(message, dimensionId);
    }

    public static void sendToServer(IPacket message) {
        packetPipeline.sendToServer(message);
    }
}
