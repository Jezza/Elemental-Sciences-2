package me.jezzadabomb.es2.common.core.handlers;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.network.packet.IPacket;
import me.jezzadabomb.es2.common.core.network.packet.server.HoverHandlerPacket;
import me.jezzadabomb.es2.common.core.utils.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class HoverHandler {

    public ArrayList<HoveringPlayer> playerList;

    static HoverHandler INSTANCE = new HoverHandler();

    public HoverHandler() {
        playerList = new ArrayList<HoveringPlayer>();
    }

    public static HoverHandler getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entity == null || !(event.entity instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) event.entity;

        if (!PlayerHelper.isPlayerWearing(player, ModItems.hoverBoots)) {
            if (isInList(player))
                playerList.remove(getHoveringPlayer(player));
            return;
        }

        if (!isInList(player))
            playerList.add(new HoveringPlayer(player));

        HoveringPlayer hoveringPlayer = getHoveringPlayer(player);

        if (hoveringPlayer == null)
            return;

        if (player.isCollidedVertically || player.onGround || player.isDead) {
            playerList.remove(getHoveringPlayer(player));
            return;
        }

        if (hoveringPlayer.isWaiting())
            return;

        if (player.capabilities.isFlying || player.isSneaking()) {
            hoveringPlayer.timeLeft = 0;
            hoveringPlayer.setWaiting(true);
            hoveringPlayer.setHovering(false);
            sendPacket(hoveringPlayer);
        }

        if (!hoveringPlayer.isHovering()) {
            if (player.fallDistance > 0.0F || player.isAirBorne || !player.onGround || !player.isCollidedVertically) {
                hoveringPlayer.setHovering(true);
                sendPacket(hoveringPlayer);
            }
        } else {
            hoveringPlayer.hoverTick();
        }

        if (hoveringPlayer.tickPlayer() <= 0) {
            hoveringPlayer.setWaiting(true);
            hoveringPlayer.setHovering(false);
            sendPacket(hoveringPlayer);
        }

    }

    private void sendPacket(HoveringPlayer hoveringPlayer) {
        EntityPlayer player = hoveringPlayer.getPlayer();
        PacketDispatcher.sendToAllAround(getPacket(hoveringPlayer), new TargetPoint(player.dimension, (int) player.posX, player.posY, player.posZ, 64));
    }

    private IPacket getPacket(HoveringPlayer hoveringPlayer) {
        return new HoverHandlerPacket(hoveringPlayer);
    }

    public void updatePlayer(EntityPlayer player, int time, boolean hovering, boolean waiting, int texture) {
        if (player == null)
            return;
        HoveringPlayer hoveringPlayer = getHoveringPlayer(player);
        if (hoveringPlayer != null) {
            hoveringPlayer.setTimeLeft(time);
            hoveringPlayer.setHovering(hovering);
            hoveringPlayer.setWaiting(waiting);
            hoveringPlayer.setTexture(texture);
        }
    }

    private HoveringPlayer getHoveringPlayer(EntityPlayer player) {
        String username = player.getDisplayName();
        for (HoveringPlayer temp : playerList)
            if (temp.getUsername().equals(username))
                return temp;
        return null;
    }

    private boolean isInList(EntityPlayer player) {
        return getHoveringPlayer(player) != null;
    }

    public static class HoveringPlayer {
        public static final int MAX_HOVER_TIME = 80;

        EntityPlayer player;
        int timeLeft, texture;
        boolean waiting, hovering, justStarted;

        public HoveringPlayer(EntityPlayer player) {
            this.player = player;
            timeLeft = MAX_HOVER_TIME;
            waiting = false;
            justStarted = true;
        }

        public int tickPlayer() {
            return hovering ? --timeLeft : timeLeft;
        }

        public int tickCount() {
            return timeLeft;
        }

        public void setTimeLeft(int timeLeft) {
            this.timeLeft = timeLeft;
        }

        public void setJustStarted(boolean justStarted) {
            this.justStarted = justStarted;
        }

        public boolean isJustStarted() {
            return justStarted;
        }

        public void setHovering(boolean bool) {
            hovering = bool;
        }

        public boolean isHovering() {
            return hovering;
        }

        public void setWaiting(boolean bool) {
            waiting = bool;
        }

        public boolean isWaiting() {
            return waiting;
        }

        public String getUsername() {
            return player.getDisplayName();
        }

        public EntityPlayer getPlayer() {
            return player;
        }

        public void resetState() {
            timeLeft = MAX_HOVER_TIME;
            waiting = false;
            justStarted = true;
        }

        public int getTexture() {
            return texture;
        }

        public void setTexture(int texture) {
            this.texture = texture;
        }

        public void hoverTick() {
            if (justStarted) {
                justStarted = false;
                player.motionY = 0.30F;
                // player.moveEntity(0, 0.30F, 0);
            } else {
                if (player.motionY < 0)
                    player.motionY = 0;
            }
            double slipperness = 0.95;
            player.motionX *= slipperness;
            player.motionZ *= slipperness;
            player.moveEntity((float) player.motionX, 0.0F, (float) player.motionZ);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof HoveringPlayer) {
                HoveringPlayer hoveringPlayer = (HoveringPlayer) obj;
                return equals(hoveringPlayer.getUsername());
            } else if (obj instanceof EntityPlayer) {
                EntityPlayer hoveringPlayer = (EntityPlayer) obj;
                return equals(hoveringPlayer.getDisplayName());
            }
            return false;
        }

        public boolean equals(String username) {
            return getUsername().equals(username);
        }
    }
}
