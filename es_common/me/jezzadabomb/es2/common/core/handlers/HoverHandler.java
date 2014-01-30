package me.jezzadabomb.es2.common.core.handlers;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HoverHandler {

    private boolean hovering = false;
    private boolean hitGround = false;
    private boolean sentPacket = false;

    // List containing players wearing the boots.
    ArrayList<HoveringPlayer> playerList;

    public HoverHandler() {
        playerList = new ArrayList<HoveringPlayer>();
    }

    @ForgeSubscribe
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entity == null || !(event.entity instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) event.entity;

        // Check for boots.
        if (!UtilMethods.isPlayerWearing(player, ModItems.hoverBoots)) {
            if (isInList(player))
                playerList.remove(getHoveringPlayer(player));
            return;
        }

        if (!isInList(player))
            playerList.add(new HoveringPlayer(player));

        // Get the hovering instance of the player.
        HoveringPlayer hoveringPlayer = getHoveringPlayer(player);

        if (player.capabilities.isFlying) {
            hoveringPlayer.timeLeft = 0;
            hoveringPlayer.setWaiting(true);
            hoveringPlayer.setHovering(false);
        }

        if (player.onGround) {
            if (player.isCollidedVertically || player.onGround || player.isDead || !player.isAirBorne) {
                playerList.remove(getHoveringPlayer(player));
            }
            return;
        }
        if (hoveringPlayer.isWaiting())
            return;

        // Check if should be hovering.

        if (!hoveringPlayer.isHovering())
            if (player.fallDistance > 0.0F || player.isAirBorne || !player.onGround || !player.isCollidedVertically)
                hoveringPlayer.setHovering(true);
        // Make them hover.
        hoveringPlayer.hoverTick();

        // Decrement the time, so when we finish hovering, drop them.
        if (hoveringPlayer.tickPlayer() <= 0) {
            hoveringPlayer.setWaiting(true);
            hoveringPlayer.setHovering(false);
        }
    }

    private void startHovering(HoveringPlayer hoveringPlayer) {

        // player.moveFlying((float) player.motionX, 0.0F, (float) player.motionZ);
    }

    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        EntityPlayer renderView = Minecraft.getMinecraft().thePlayer;
        if (renderView == null || playerList.isEmpty())
            return;
        ArrayList<HoveringPlayer> tempList = new ArrayList<HoveringPlayer>();
        tempList.addAll(playerList);
        for (HoveringPlayer player : tempList) {
            if (!player.isHovering())
                continue;
            if (player.equals(renderView.username)) {
                glPushMatrix();
                RenderUtils.bindTexture(TextureMaps.HOVER_TEXTURE);

                glRotated(90, 1.0D, 0.0D, 0.0D);
                glRotatef(-(float) (2880.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL), 0.0F, 0.0F, 1.0F);

                glTranslated(-1.291D, -1.281D, 0.0D);

                glScaled(0.01D, 0.01D, 0.01D);

                glColor4f(1.0F, 1.0F, 1.0F, (player.timeLeft / (player.MAX_HOVER_TIME * 2)) + 0.068F);
                // if (--timeHovering == 0)
                // timeHovering = WAIT_TIME;

                glDisable(GL_LIGHTING);
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glDisable(GL_CULL_FACE);

                RenderUtils.drawTexturedQuad(0, 0, 0, 0, 256, 256, 161);
                glPopMatrix();
                continue;
            }
            glPushMatrix();
            RenderUtils.translateToOtherPlayer(renderView.worldObj.getPlayerEntityByName(player.getUsername()), event.partialTicks);

            glTranslated(0.0D, 1.55D, 0.0D);

            glRotated(90, 1.0D, 0.0D, 0.0D);
            glRotatef(-(float) (2880.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL), 0.0F, 0.0F, 1.0F);

            glTranslated(-1.3D, -1.3D, 0.0D);

            glScaled(0.01D, 0.01D, 0.01D);

            glDisable(GL_LIGHTING);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glDisable(GL_CULL_FACE);

            RenderUtils.drawTexturedQuadAtPlayer(TextureMaps.HOVER_TEXTURE, 0, 0, 0, 0, 256, 256, 161);

            glPopMatrix();
            glEnable(GL_CULL_FACE);
            glDisable(GL_BLEND);
        }
    }

    public void updatePlayer(EntityPlayer player, boolean waiting) {
        HoveringPlayer hoveringPlayer = getHoveringPlayer(player);
        if (hoveringPlayer != null)
            hoveringPlayer.setWaiting(waiting);
    }

    private HoveringPlayer getHoveringPlayer(EntityPlayer player) {
        String username = player.username;
        for (HoveringPlayer temp : playerList)
            if (temp.getUsername().equals(username))
                return temp;
        return null;
    }

    private boolean isInList(EntityPlayer player) {
        return getHoveringPlayer(player) != null;
    }

    public static class HoveringPlayer {
        int MAX_HOVER_TIME = 80;

        EntityPlayer player;
        int timeLeft;
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
            return player.username;
        }

        public EntityPlayer getPlayer() {
            return player;
        }

        public void hoverTick() {
            if (justStarted) {
                justStarted = false;
                player.moveEntity(0, 0.08F, 0);
            }
            if (player.motionY < 0)
                player.motionY = 0;
            double slipperness = 0.95;
            player.motionX *= slipperness;
            player.motionZ *= slipperness;
            player.moveEntity((float) player.motionX, 0.0F, (float) player.motionZ);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (obj instanceof HoveringPlayer) {
                HoveringPlayer hoveringPlayer = (HoveringPlayer) obj;
                return equals(hoveringPlayer.getUsername());
            } else if (obj instanceof EntityPlayer) {
                EntityPlayer hoveringPlayer = (EntityPlayer) obj;
                return equals(player.username);
            } else {
                return false;
            }
        }

        public boolean equals(String username) {
            return getUsername().equals(username);
        }
    }
}
