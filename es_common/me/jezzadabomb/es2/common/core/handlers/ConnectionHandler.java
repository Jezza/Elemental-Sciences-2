package me.jezzadabomb.es2.common.core.handlers;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.packets.PlayerBombPacket;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class ConnectionHandler implements IConnectionHandler {
	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) // client: remove server
	{
		onClientConnection();
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) // client: local server
	{
		onClientConnection();
	}

	public void onClientConnection() {
		ElementalSciences2.proxy.quantumBomb.setPlayer("null");
	}

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
		//TODO Add packet to send the info for the player.
		PacketDispatcher.sendPacketToPlayer(new PlayerBombPacket(ElementalSciences2.proxy.quantumBomb.getPlayer()).makePacket(), player);
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
		return null;
	}

	@Override
	public void connectionClosed(INetworkManager manager) // both
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			onClientConnection();
		}
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {

	}
}
