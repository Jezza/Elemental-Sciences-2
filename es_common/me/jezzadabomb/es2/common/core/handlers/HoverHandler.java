package me.jezzadabomb.es2.common.core.handlers;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class HoverHandler {

    ArrayList<String> playerList;

    public HoverHandler() {
        playerList = new ArrayList<String>();
    }

    public void addPlayer(EntityPlayer player, boolean beginning) {
        if (beginning) {
            playerList.add(player.username);
        } else {
            playerList.remove(player.username);
        }
    }

    // TODO Deal with hovering in a better way.
    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        EntityPlayer renderView = (EntityPlayer) Minecraft.getMinecraft().renderViewEntity;
        for (String name : playerList) {
            if (name.equals(renderView.username))
                continue;
            RenderUtils.translateToOtherPlayer(renderView.worldObj.getPlayerEntityByName(name), event.partialTicks);

            glTranslated(0.0D, 1.55D, 0.0D);
            // Flips it upright
            glRotated(90, 1.0D, 0.0D, 0.0D);
            glRotatef(-(float) (2880.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL), 0.0F, 0.0F, 1.0F);
            // Push it under the player
            glTranslated(-1.3D, -1.3D, 0.0D);
            // Scale it, so it isn't massive.
            glScaled(0.01D, 0.01D, 0.01D);

            glDisable(GL_LIGHTING);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glDisable(GL_CULL_FACE);

            RenderUtils.drawTexturedQuadAtPlayer(TextureMaps.HOVER_TEXTURE, 0, 0, 0, 0, 256, 256, 161);

            glEnable(GL_CULL_FACE);
            glDisable(GL_BLEND);
        }

    }
}
