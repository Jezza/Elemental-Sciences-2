package me.jezzadabomb.es2.common.network.packet.client;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.jezzadabomb.es2.client.hud.InventoryInstance;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.network.PacketDispatcher;
import me.jezzadabomb.es2.common.network.PacketPipeline;
import me.jezzadabomb.es2.common.network.PacketUtils;
import me.jezzadabomb.es2.common.network.packet.IPacket;
import me.jezzadabomb.es2.common.network.packet.server.InventoryPacket;

public class InventoryRequestPacket implements IPacket {

    private String loc;

    public InventoryRequestPacket(InventoryInstance inventory) {
        loc = new CoordSet(inventory.getX(), inventory.getY(), inventory.getZ()).toPacketString();
    }

    public InventoryRequestPacket() {
    }

    @Override
    public void writeBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        PacketUtils.writeStringByteBuffer(buffer, loc);
    }

    @Override
    public void readBytes(ChannelHandlerContext ctx, ByteBuf buffer) throws IOException {
        loc = PacketUtils.readStringByteBuffer(buffer);
    }

    @Override
    public void executeClientSide(EntityPlayer player) {
        ESLogger.severe("Tried sending packet to wrong side!");
    }

    @Override
    public void executeServerSide(EntityPlayer player) {
        World world = player.worldObj;

        int[] coords = UtilMethods.getArrayFromString(loc);

        int x = coords[0];
        int y = coords[1];
        int z = coords[2];

        if (UtilMethods.isIInventory(world, x, y, z))
            PacketDispatcher.sendTo(new InventoryPacket(world.getTileEntity(x, y, z), loc), (EntityPlayerMP) player);
    }
}
