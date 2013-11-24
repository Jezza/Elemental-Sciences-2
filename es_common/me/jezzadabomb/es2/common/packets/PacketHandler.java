package me.jezzadabomb.es2.common.packets;

import java.util.logging.Logger;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import me.jezzadabomb.es2.common.packets.CentralPacket.ProtocolException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
            try {
                    EntityPlayer entityPlayer = (EntityPlayer)player;
                    ByteArrayDataInput in = ByteStreams.newDataInput(packet.data);
                    int packetId = in.readUnsignedByte(); // Assuming your packetId is between 0 (inclusive) and 256 (exclusive). If you need more you need to change this
                    
                    CentralPacket centralPacket = CentralPacket.constructPacket(packetId);
                    centralPacket.read(in);
                    centralPacket.execute(entityPlayer, entityPlayer.worldObj.isRemote ? Side.CLIENT : Side.SERVER);
            } catch (ProtocolException e) {
                    if (player instanceof EntityPlayerMP) {
                            ((EntityPlayerMP) player).playerNetServerHandler.kickPlayerFromServer("Protocol Exception!");
                            Logger.getLogger("ElementalSciences2").warning("Player " + ((EntityPlayer)player).username + " caused a Protocol Exception!");
                    }
            } catch (ReflectiveOperationException e) {
                    throw new RuntimeException("Unexpected Reflection exception during Packet construction!", e);
            }
    }

}
