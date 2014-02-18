package me.jezzadabomb.es2.common.core.network;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.network.packet.IPacket;
import me.jezzadabomb.es2.common.network.packet.client.ConsoleInfoPacket;
import me.jezzadabomb.es2.common.network.packet.client.InventoryRequestPacket;
import me.jezzadabomb.es2.common.network.packet.client.SetBlockChunkPacket;
import me.jezzadabomb.es2.common.network.packet.server.HoverHandlerPacket;
import me.jezzadabomb.es2.common.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.network.packet.server.NeighbourChangedPacket;

public class PacketHandler {

    public static void init() {
        registerPacket(InventoryRequestPacket.class);
        registerPacket(SetBlockChunkPacket.class);
        registerPacket(HoverHandlerPacket.class);
        registerPacket(InventoryPacket.class);
        registerPacket(NeighbourChangedPacket.class);
        registerPacket(ConsoleInfoPacket.class);
    }

    private static void registerPacket(Class<? extends IPacket> clazz) {
        ElementalSciences2.packetPipeline.registerPacket(clazz);
    }

}
