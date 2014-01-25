package me.jezzadabomb.es2.common.tickers;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.packets.HoverHandlerPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HoverHandler implements ITickHandler {

    private boolean hovering = false;
    private boolean hitGround = false;
    private boolean sentPacket = false;
    private boolean justStartedHovering = true;
    private float WAIT_TIME = 80;
    private float timeHovering = WAIT_TIME;

    ArrayList<HoveringPlayer> playerList = new ArrayList<HoveringPlayer>();

    public HoverHandler() {
    }

    public void addPlayer(String username) {
        if (!doesListContain(username))
            playerList.add(new HoveringPlayer(username));
    }

    public void removePlayer(String username) {
        boolean found = false;
        int index = 0;

        for (HoveringPlayer player : playerList) {
            if (player.username.equals(username)) {
                found = true;
                break;
            }
            index++;
        }

        playerList.remove(index);
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        List players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for (Object obj : players) {
            if (obj instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) obj;
                if (UtilMethods.isPlayerWearing(player, ModItems.hoverBoots)) {
                    addPlayer(player.username);
                } else {
                    if (doesListContain(player.username))
                        removePlayer(player.username);
                }
            }
        }
    }

    @ForgeSubscribe
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entity == null || !(event.entity instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer) event.entity;
        // Check if should be hovering.

        // Start hovering methods.
    }

    private void startHovering(LivingUpdateEvent event, EntityPlayer player) {
        if (justStartedHovering) {
            PacketDispatcher.sendPacketToServer(new HoverHandlerPacket(player, true).makePacket());
            justStartedHovering = false;
            player.moveEntity(0, 0.08F, 0);
        }
        if (player.motionY < 0)
            player.motionY = 0;
        double slipperness = 0.95;
        player.motionX *= slipperness;
        player.motionZ *= slipperness;
        player.moveFlying((float) player.motionX, 0.0F, (float) player.motionZ);
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.SERVER);
    }

    @Override
    public String getLabel() {
        return "ES2-HoverHandler";
    }

    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        EntityPlayer renderView = Minecraft.getMinecraft().thePlayer;
        if (renderView == null)
            return;
        for (HoveringPlayer player : playerList) {
            if (player.equals(renderView.username)) {
                glPushMatrix();
                RenderUtils.bindTexture(TextureMaps.HOVER_TEXTURE);

                glRotated(90, 1.0D, 0.0D, 0.0D);
                glRotatef(-(float) (2880.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL), 0.0F, 0.0F, 1.0F);

                glTranslated(-1.291D, -1.281D, 0.0D);

                glScaled(0.01D, 0.01D, 0.01D);

                glColor4f(1.0F, 1.0F, 1.0F, (timeHovering / (WAIT_TIME * 2)) + 0.068F);
//                if (--timeHovering == 0)
//                    timeHovering = WAIT_TIME;

                glDisable(GL_LIGHTING);
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glDisable(GL_CULL_FACE);

                RenderUtils.drawTexturedQuad(0, 0, 0, 0, 256, 256, 161);
                glPopMatrix();
                continue;
            }
            glPushMatrix();
            RenderUtils.translateToOtherPlayer(renderView.worldObj.getPlayerEntityByName(player.username), event.partialTicks);

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

    private boolean doesListContain(String username) {
        boolean found = false;
        for (HoveringPlayer temp : playerList)
            if (temp.equals(username)) {
                found = true;
            }

        return found;
    }

    public static class HoveringPlayer {
        String username;
        int timeLeft;

        public HoveringPlayer(String username) {
            this.username = username;
            timeLeft = 80;
        }

        public void decrementTime() {
            timeLeft--;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof HoveringPlayer))
                return false;
            HoveringPlayer player = (HoveringPlayer) obj;
            return equals(player.username);
        }

        public boolean equals(String username) {
            return this.username.equals(username);
        }
    }
}
