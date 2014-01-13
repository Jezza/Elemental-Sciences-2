package me.jezzadabomb.es2.common.tickers;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.packets.PlayerBombPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

public class QuantumBombTicker implements ITickHandler {

	private String player;
	private String prevPlayer;
	private EntityPlayer entityPlayer;
	private int tickTiming = 0;
	private double lastTickPosX, lastTickPosY, lastTickPosZ;
	private boolean ticked = false;
	ArrayList<EntityItem> itemList, removeList;
	
	public QuantumBombTicker(){
	    itemList = new ArrayList<EntityItem>();
	    removeList = new ArrayList<EntityItem>();
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.SERVER))) {
		    for(EntityItem item : itemList){
		        if(item.isCollided){
		            item.setDead();
		            removeList.add(item);
		        }
		    }
		    itemList.removeAll(removeList);
		    removeList.clear();
		    
			if (player != null) {
				if (++tickTiming > (Reference.CAN_DEBUG ? 60 : Reference.QUANTUM_STATE_DISRUPTER_WAIT_TIMER)) {
					tickTiming = 0;
					beginExplosion(MinecraftServer.getServer().getConfigurationManager().playerEntityList);
					entityPlayer = null;
					player = null;
					ticked = false;
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

	public void addItemEntityToList(EntityItem item){
	    itemList.add(item);
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
				if (UtilMethods.hasItemInInventory(player, new ItemStack(ModItems.placeHolders, 1, 0), true)) {
					continue;
				}
				player.inventory.clearInventory(-1, -1);
				damageEntity(player);
			}
		}
	}

	private void damageEntity(EntityPlayer entityPlayer) {
		String name = DamageSource.outOfWorld.damageType;
		DamageSource.outOfWorld.damageType = "quantumDisruption";
		entityPlayer.attackEntityFrom(DamageSource.outOfWorld, 25000.0F);
		DamageSource.outOfWorld.damageType = name;
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
