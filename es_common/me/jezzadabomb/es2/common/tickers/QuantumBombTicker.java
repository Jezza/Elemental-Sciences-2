package me.jezzadabomb.es2.common.tickers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileQuantumStateDisruptor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class QuantumBombTicker {

    public static String DEFAULT_TIMING = "default";
    public static int customTiming = -1;

    private static HashSet<QuantumBombState> watchList, watchUtilList;
    private static ArrayList<EntityItem> itemList, utilList;

    public QuantumBombTicker() {
        itemList = new ArrayList<EntityItem>();
        utilList = new ArrayList<EntityItem>();

        watchList = new HashSet<QuantumBombState>();
        watchUtilList = new HashSet<QuantumBombState>();
    }

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        // Item watch list.
        if (!itemList.isEmpty()) {
            utilList.clear();
            utilList.addAll(itemList);
            for (EntityItem item : utilList) {
                if (item.isCollidedVertically) {
                    item.setDead();
                    itemList.remove(item);
                    World world = item.worldObj;
                    world.setBlock((int) Math.floor(item.posX), (int) Math.floor(item.posY), (int) Math.floor(item.posZ), ModBlocks.quantumStateDisrupter);
                }
            }
        }

        if (watchList.isEmpty())
            return;

        watchUtilList.clear();
        watchUtilList.addAll(watchList);

        for (QuantumBombState quantumState : watchUtilList) {

            CoordSet coordSet = quantumState.coordSet;

            ArrayList<EntityLivingBase> entityList = quantumState.getNearbyEntities();

            for (EntityLivingBase entity : entityList) {
                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;

                    if (UtilMethods.hasItemInInventory(player, ModItems.getPlaceHolderStack("lifeCoin"), false, null))
                        continue;

                    if (player.capabilities.isFlying)
                        player.capabilities.isFlying = false;
                    player.inventory.dropAllItems();
                }

                double xVelocity = (entity.posX - (coordSet.getX() + 0.5F));
                double yVelocity = (entity.posY - (coordSet.getY() + 0.5F));
                double zVelocity = (entity.posZ - (coordSet.getZ() + 0.5F));

                entity.addVelocity(xVelocity, yVelocity, zVelocity);
                entity.velocityChanged = true;
            }

            if (quantumState.tick()) {
                quantumState.tileQuantum.removeSelf();
                watchList.remove(quantumState);
                killPlayers(MinecraftServer.getServer().getConfigurationManager().playerEntityList);
            }
        }

    }

    private void killPlayers(List playerEntities) {
        for (Object object : playerEntities) {
            if (object instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) object;
                if (UtilMethods.hasItemInInventory(player, ModItems.getPlaceHolderStack("lifeCoin"), true, ModItems.getPlaceHolderStack("deadCoin"))) {
                    UtilMethods.addChatMessage(player, "You're lucky you managed to play a perfect game.");
                    continue;
                }

                player.inventory.clearInventory(null, -1);

                String name = DamageSource.outOfWorld.damageType;
                DamageSource.outOfWorld.damageType = "quantumDisruption";
                player.attackEntityFrom(DamageSource.outOfWorld, 25000.0F);
                DamageSource.outOfWorld.damageType = name;
            }
        }
    }

    public static void addToWatchList(TileQuantumStateDisruptor tileQuantum) {
        watchList.add(new QuantumBombState(tileQuantum));
    }

    public static void addItemEntityToList(EntityItem item) {
        itemList.add(item);
    }

    public static void parseTime(String time) {
        String[] stringParts = time.split(",");
        if (time.equals(DEFAULT_TIMING) || stringParts.length != 4) {
            ESLogger.info("Quantum Bomb Timer is set to default.");
            return;
        }

        switch (stringParts.length) {
            case 4:
                int hours = Integer.parseInt(stringParts[0]);
                int minutes = Integer.parseInt(stringParts[1]);
                int seconds = Integer.parseInt(stringParts[2]);
                int ticks = Integer.parseInt(stringParts[3]);
                ESLogger.info("Quantum Bomb Timer: Hours: " + hours + ", Minutes: " + minutes + ", Seconds: " + seconds + ", Ticks: " + ticks);
                customTiming = UtilMethods.getTimeInTicks(hours, minutes, seconds, ticks);
        }
    }

    public static class QuantumBombState {

        TileQuantumStateDisruptor tileQuantum;
        World world;
        CoordSet coordSet;
        int timing, totalTiming;

        public QuantumBombState(TileQuantumStateDisruptor tileQuantum) {
            this.tileQuantum = tileQuantum;
            world = tileQuantum.getWorldObj();
            coordSet = tileQuantum.getCoordSet();
            timing = 0;
            totalTiming = UtilMethods.getTimeInTicks(0, new Random().nextInt(15) + 5, 0, 0);
        }

        public boolean tick() {
            return ++timing > totalTiming;
        }

        public ArrayList<EntityLivingBase> getNearbyEntities() {
            ArrayList<EntityLivingBase> entityList = new ArrayList<EntityLivingBase>();

            ArrayList<Object> loadedEntityList = new ArrayList<Object>();
            loadedEntityList.addAll(world.loadedEntityList);

            for (Object object : loadedEntityList) {
                if (!(object instanceof EntityLivingBase))
                    continue;

                EntityLivingBase entity = (EntityLivingBase) object;

                if (UtilMethods.isEntityWithin(entity, coordSet, 30))
                    entityList.add(entity);
            }
            return entityList;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof QuantumBombState))
                return false;
            return coordSet.equals(((QuantumBombState) obj).coordSet);
        }
    }
}
