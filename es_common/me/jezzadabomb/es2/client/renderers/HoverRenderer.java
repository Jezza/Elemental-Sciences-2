package me.jezzadabomb.es2.client.renderers;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.util.ArrayList;

import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.core.handlers.HoverHandler;
import me.jezzadabomb.es2.common.core.handlers.HoverHandler.HoveringPlayer;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HoverRenderer {

    private static HoverRenderer INSTANCE = new HoverRenderer();

    private HoverRenderer() {
    }

    public static HoverRenderer getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        EntityPlayer renderView = Minecraft.getMinecraft().thePlayer;
        ArrayList<HoveringPlayer> playerList = new ArrayList<HoveringPlayer>();
        playerList.addAll(HoverHandler.getInstance().playerList);
        if (renderView == null || playerList.isEmpty())
            return;
        for (HoveringPlayer player : playerList)
            if (player != null && player.isHovering()) {
                glPushMatrix();
                glDisable(GL_LIGHTING);
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                glDisable(GL_CULL_FACE);

                boolean flag = !player.equals(renderView.getCommandSenderName());
                if (flag) {
                    RenderUtils.translateToOtherPlayer(renderView.worldObj.getPlayerEntityByName(player.getUsername()), event.partialTicks);
                    glTranslated(0.0D, 1.55D, 0.0D);
                }

                glRotated(90, 1.0D, 0.0D, 0.0D);
                glRotatef(-(float) (2880.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL), 0.0F, 0.0F, 1.0F);

                if (flag)
                    glTranslated(-1.3D, -1.3D, 0.0D);
                else
                    glTranslated(-1.291D, -1.281D, 0.0D);

                glScaled(0.01D, 0.01D, 0.01D);
                glColor4f(1.0F, 1.0F, 1.0F, ((float) player.tickCount() / ((float) player.MAX_HOVER_TIME)) + 0.1F);

                RenderUtils.drawTexturedQuadAtPlayer(TextureMaps.HOVER_TEXTURES[player.getTexture()], 0, 0, 0, 0, 256, 256, 161);

                glEnable(GL_CULL_FACE);
                glDisable(GL_BLEND);
                glPopMatrix();
            }

    }
}
