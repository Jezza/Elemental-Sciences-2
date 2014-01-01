package me.jezzadabomb.es2.common.tickers;

import java.util.EnumSet;
import java.util.List;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.packets.PlayerBombPacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

public class QuantumBombTicker implements ITickHandler {

	private String player;
	private String prevPlayer;
	private EntityPlayer entityPlayer;
	private int tickTiming = 0;
	private double lastTickPosX, lastTickPosY, lastTickPosZ;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.SERVER))) {
			if (player != null) {
				stopMovement();
				if (++tickTiming > 160) {
					tickTiming = 0;
					beginExplosion(MinecraftServer.getServer().getConfigurationManager().playerEntityList);
					entityPlayer = null;
					player = null;
				}
			}
			if (player != prevPlayer) {
				if (player == null) {
					player = "null";
				}

				PacketDispatcher.sendPacketToAllPlayers(new PlayerBombPacket(player).makePacket());

				if (player.equals("null")) {
					player = null;
				}
			}
			prevPlayer = player;
		}
	}

	public boolean canAdd(EntityPlayer player) {
		if (this.player != null || player == null) {
			return false;
		}
		entityPlayer = player;
		setPlayer(player.username);
		return true;
	}

	public String getPlayer() {
		return player;
	}

	private void stopMovement() {
		entityPlayer.velocityChanged = true;
		entityPlayer.motionX = entityPlayer.motionZ = entityPlayer.motionY = 0.00000000000D;
	}

	public void setPlayer(String name) {
		if (name.equals("null")) {
			player = null;
		} else {
			player = name;
		}
	}

	private void beginExplosion(List playerEntities) {
		for (Object object : playerEntities) {
			if (object instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) object;
				if (UtilMethods.hasItemInInventory(player, ModItems.lifeCoin)) {
					continue;
				}
				player.inventory.clearInventory(-1, -1);
				String name = DamageSource.outOfWorld.damageType;
				DamageSource.outOfWorld.damageType = "quantumDisruption";
				player.attackEntityFrom(DamageSource.outOfWorld, 25000.0F);
				DamageSource.outOfWorld.damageType = name;
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return "ES2-BombTicker";
	}

}
