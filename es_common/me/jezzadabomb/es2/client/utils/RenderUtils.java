package me.jezzadabomb.es2.client.utils;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.renderers.HUDRenderer;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtils {

    private static RenderBlocks renderBlocksInstance = new RenderBlocks();

    public static double hoverEquation(float inAmplitude, float scale, float exAmplitude) {
        return hoverEquation(inAmplitude, scale, exAmplitude, 0.0F);
    }

    public static double hoverEquation(float inAmplitude, float scale, float exAmplitude, float translation) {
        return ((inAmplitude * (Math.sin((scale * Math.PI * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) + translation))) / exAmplitude);
    }

    public static double rotationEquation(int speed, float externalAddition) {
        return (Math.PI * 114.591559F * speed * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) + externalAddition;
    }

    public static RenderItem createItemRenderer() {
        RenderItem renderItem = new RenderItem();
        renderItem.setRenderManager(RenderManager.instance);
        return renderItem;
    }

    public static void bindTexture(ResourceLocation rl) {
        Minecraft.getMinecraft().renderEngine.bindTexture(rl);
    }

    public static void translateToOtherPlayer(EntityPlayer player, double partialTicks) {
        EntityPlayer renderView = (EntityPlayer) Minecraft.getMinecraft().renderViewEntity;
        worldCoordShifted(renderView, partialTicks, player.posX, player.posY, player.posZ, true);
    }

    public static void drawTexturedQuadAtPlayer(ResourceLocation rl, int x, int y, int u, int v, int uLength, int vLength, double zLevel) {
        bindTexture(rl);
        drawTexturedQuad(x, y, u, v, uLength, vLength, zLevel);
    }

    public static void drawTexturedQuad(int x, int y, int u, int v, int uLength, int vLength, double zLevel) {
        float constant = 0.0039063F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        // @formatter:off
        tessellator.addVertexWithUV(x + 0,       y + vLength,   zLevel, (u + 0)       * constant,   (v + vLength) * constant);
        tessellator.addVertexWithUV(x + uLength, y + vLength,   zLevel, (u + uLength) * constant,   (v + vLength) * constant);
        tessellator.addVertexWithUV(x + uLength, y + 0,         zLevel, (u + uLength) * constant,   (v + 0)       * constant);
        tessellator.addVertexWithUV(x + 0,       y + 0,         zLevel, (u + 0)       * constant,   (v + 0)       * constant);
        // @formatter:on
        tessellator.draw();
    }

    public static double[] worldCoordShifted(Entity entity, double frame, double x, double y, double z, boolean shouldTranslate) {
        double interpPosX = MathHelper.interpolate(entity.lastTickPosX, entity.posX, frame);
        double interpPosY = MathHelper.interpolate(entity.lastTickPosY, entity.posY, frame);
        double interpPosZ = MathHelper.interpolate(entity.lastTickPosZ, entity.posZ, frame);

        if (shouldTranslate)
            glTranslated(-interpPosX + x, -interpPosY + y, -interpPosZ + z);

        double[] temp = new double[3];
        temp[0] = interpPosX - (x + 0.5D);
        temp[1] = interpPosZ - (z + 0.5D);
        temp[2] = interpPosY - (y + entity.getEyeHeight());
        return temp;
    }

    public static boolean isPlayerRendering(String username) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        return (player != null && player.getCommandSenderName().equals(username));
    }

    public static void drawItemAndSlot(int x, int y, ItemStack itemStack, int zLevel, int indexNum, int rowNum) {
        glDisable(GL_LIGHTING);
        if (Reference.DRAW_TEXTURED_SLOTS)
            drawTextureSlot(x, y, zLevel + 1);
        draw2DItemStack(x, y, itemStack, zLevel, indexNum, rowNum);
    }

    public static void draw2DItemStack(int x, int y, ItemStack itemStack, int zLevel, int indexNum, int rowNum) {
        if (itemStack == null)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        TextureManager textureManager = mc.getTextureManager();
        FontRenderer fontRenderer = mc.fontRenderer;

        RenderItem customItemRenderer = createItemRenderer();

        glPushMatrix();
        glDisable(GL_CULL_FACE);

        glTranslated(x + 10, y, zLevel);

        glScalef(0.96F, 1.2F, -1.2F);

        boolean flag = itemStack.getItem() instanceof ItemBlock;

        HUDUtils.translateWithRowAndColumn(indexNum, rowNum, flag);

        if (flag) {
            glTranslated(-3.0D, 8.0D, 0.0D);

            double scale = 2.2D;

            glScaled(scale, scale, scale);

            EntityItem entityItem = new EntityItem(mc.thePlayer.worldObj);
            entityItem.setEntityItemStack(itemStack);

            glDisable(GL_BLEND);
            if (!ForgeHooksClient.renderInventoryItem(renderBlocksInstance, textureManager, itemStack, true, zLevel, 0, 0)) {
                glEnable(GL_BLEND);
                customItemRenderer.renderItemIntoGUI(fontRenderer, textureManager, itemStack, 0, 0);
            }
            glEnable(GL_BLEND);
        } else {
            glTranslated(-9, 5, 0);

            double scale = 2.8D;

            glScaled(scale, scale, scale);

            glPushMatrix();

            boolean pop = false;
            translateSpecialRender(indexNum, rowNum);
            if (!ForgeHooksClient.renderInventoryItem(renderBlocksInstance, textureManager, itemStack, true, zLevel, 0, 0)) {
                pop = true;
                glPopMatrix();
                customItemRenderer.renderItemIntoGUI(fontRenderer, textureManager, itemStack, 0, 0);
            }
            if (!pop)
                glPopMatrix();
        }
        glEnable(GL_ALPHA_TEST);
        glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
        glDisable(GL_LIGHTING);
        glEnable(GL_CULL_FACE);
        glPopMatrix();
    }

    public static void drawTextureSlot(int x, int y, double zLevel) {
        glPushMatrix();
        bindTexture(TextureMaps.HUD_INVENTORY);
        drawTexturedQuad(x, y, 175, 0, 49, 73, zLevel);
        glPopMatrix();
    }

    private static void translateSpecialRender(int indexNum, int rowNum) {
        indexNum -= 1.0D;
        glTranslated(indexNum < 0.0D ? indexNum : 0.0D, 0.0D, indexNum > 0.0D ? indexNum : 0.0D);

        rowNum -= 1.0D;
        glTranslated(0.0D, rowNum, 0.0D);
    }

    // Thanks to Player for this. :D
    public static void renderColouredBox(double partialTicks, CoordSet coordSet) {
        EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;
        double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        double offset = 0.02;
        double delta = 1 + 2 * offset;

        double x = coordSet.getX() - px - offset;
        double y = coordSet.getY() - py - offset;
        double z = coordSet.getZ() - pz - offset;
        glPushMatrix();
        glPushAttrib(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        // colour.setTesselator(tessellator);

        // BOTTOM
        tessellator.addVertex(x, y, z);
        tessellator.addVertex(x + delta, y, z);
        tessellator.addVertex(x + delta, y, z + delta);
        tessellator.addVertex(x, y, z + delta);
        // TOP
        tessellator.addVertex(x, y + delta, z);
        tessellator.addVertex(x, y + delta, z + delta);
        tessellator.addVertex(x + delta, y + delta, z + delta);
        tessellator.addVertex(x + delta, y + delta, z);
        // Neg Z
        tessellator.addVertex(x, y, z);
        tessellator.addVertex(x, y + delta, z);
        tessellator.addVertex(x + delta, y + delta, z);
        tessellator.addVertex(x + delta, y, z);
        // Pos Z
        tessellator.addVertex(x, y, z + delta);
        tessellator.addVertex(x + delta, y, z + delta);
        tessellator.addVertex(x + delta, y + delta, z + delta);
        tessellator.addVertex(x, y + delta, z + delta);
        // Neg X
        tessellator.addVertex(x, y, z);
        tessellator.addVertex(x, y, z + delta);
        tessellator.addVertex(x, y + delta, z + delta);
        tessellator.addVertex(x, y + delta, z);
        // Pos X
        tessellator.addVertex(x + delta, y, z);
        tessellator.addVertex(x + delta, y + delta, z);
        tessellator.addVertex(x + delta, y + delta, z + delta);
        tessellator.addVertex(x + delta, y, z + delta);

        tessellator.draw();

        glEnable(GL_TEXTURE_2D);

        glPopAttrib();
        glPopMatrix();
    }
}
