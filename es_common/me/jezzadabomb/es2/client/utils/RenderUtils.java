package me.jezzadabomb.es2.client.utils;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.renderers.HUDRenderer;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.EffectRenderer;
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
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtils {

    static RenderBlocks renderBlocksInstance = new RenderBlocks();

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
        translateToWorldCoordsShifted(renderView, partialTicks, player.posX, player.posY, player.posZ);
    }

    public static void drawTexturedQuadAtPlayer(ResourceLocation rl, int x, int y, int u, int v, int uLength, int vLength, double zLevel) {
        bindTexture(rl);
        drawTexturedQuad(x, y, u, v, uLength, vLength, zLevel);
    }

    public static void drawTexturedQuad(int x, int y, int u, int v, int uLength, int vLength, double zLevel) {
        float var7 = 0.0039063F;
        float var8 = 0.0039063F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + vLength, zLevel, (u + 0) * var7, (v + vLength) * var8);
        tessellator.addVertexWithUV(x + uLength, y + vLength, zLevel, (u + uLength) * var7, (v + vLength) * var8);
        tessellator.addVertexWithUV(x + uLength, y + 0, zLevel, (u + uLength) * var7, (v + 0) * var8);
        tessellator.addVertexWithUV(x + 0, y + 0, zLevel, (u + 0) * var7, (v + 0) * var8);
        tessellator.draw();
    }

    public static double[] translateToWorldCoordsShifted(Entity entity, double frame, double x, double y, double z) {
        double interpPosX = MathHelper.interpolate(entity.lastTickPosX, entity.posX, frame);
        double interpPosY = MathHelper.interpolate(entity.lastTickPosY, entity.posY, frame);
        double interpPosZ = MathHelper.interpolate(entity.lastTickPosZ, entity.posZ, frame);

        GL11.glTranslated(-interpPosX + x, -interpPosY + y, -interpPosZ + z);

        double[] temp = new double[3];
        temp[0] = interpPosX - (x + 0.5D);
        temp[1] = interpPosZ - (z + 0.5D);
        temp[2] = interpPosY - (y + entity.getEyeHeight());
        return temp;
    }

    public static double[] worldCoordsShifted(Entity entity, double frame, double x, double y, double z) {
        double interpPosX = MathHelper.interpolate(entity.lastTickPosX, entity.posX, frame);
        double interpPosY = MathHelper.interpolate(entity.lastTickPosY, entity.posY, frame);
        double interpPosZ = MathHelper.interpolate(entity.lastTickPosZ, entity.posZ, frame);

        double[] temp = new double[3];
        temp[0] = interpPosX - (x + 0.5D);
        temp[1] = interpPosZ - (z + 0.5D);
        temp[2] = interpPosY - (y + entity.getEyeHeight());
        return temp;
    }

    public static boolean isPlayerRendering(String username) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        return (player != null && player.getDisplayName().equals(username));
    }

    // Individual translations if I want.

    private static void translateSpecialRender(int indexNum, int rowNum) {
        indexNum -= 1.0D;
        glTranslated(indexNum < 0.0D ? indexNum : 0.0D, 0.0D, indexNum > 0.0D ? indexNum : 0.0D);

        rowNum -= 1.0D;
        glTranslated(0.0D, rowNum, 0.0D);
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
        HUDRenderer.HUDCOLOUR.doGL();
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

    public static void drawItemAndSlot(int x, int y, ItemStack itemStack, int zLevel, int indexNum, int rowNum) {
        glDisable(GL_LIGHTING);
        if (Reference.DRAW_TEXTURED_SLOTS)
            drawTextureSlot(x, y, zLevel + 1);
        draw2DItemStack(x, y, itemStack, zLevel, indexNum, rowNum);
    }

    public static void drawTextInAir(double x, double y, double z, double partialTicks, String text) {
        if ((Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().renderViewEntity;
            double iPX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
            double iPY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
            double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

            glPushMatrix();

            glTranslated(-iPX + x + 0.5D, -iPY + y + 1.5D, -iPZ + z + 0.5D);

            float xd = (float) (iPX - (x + 0.5D));
            float zd = (float) (iPZ - (z + 0.5D));
            float yd = (float) (iPY - (y + 1.5D));

            float rotYaw = (float) (Math.atan2(xd, zd) * 180.0D / 3.141592653589793D);

            glRotatef(rotYaw + 180.0F, 0.0F, 1.0F, 0.0F);
            if (Reference.HUD_VERTICAL_ROTATION) {
                glTranslated(0.0D, -0.7D, 0.0D);
                float rotPitch = (float) (Math.atan2(yd, MathHelper.pythagoras(xd, zd)) * 180.0D / 3.141592653589793D);
                glRotatef(rotPitch, 1.0F, 0.0F, 0.0F);
                glTranslated(0.0D, 0.7D, 0.0D);
            }

            glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            glScalef(0.02F, 0.02F, 0.02F);
            int sw = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
            glEnable(GL_BLEND);
            Minecraft.getMinecraft().fontRenderer.drawString(text, 1 - sw / 2, 1, 1118481);
            glTranslated(0.0D, 0.0D, -0.1D);
            Minecraft.getMinecraft().fontRenderer.drawString(text, -sw / 2, 0, 16777215);
            glDisable(GL_BLEND);
            glPopMatrix();
        }
    }

    public static void renderColouredBox(double partialTicks, InventoryPacket p, boolean underBlock) {
        renderColouredBox(partialTicks, p.coordSet.getX(), p.coordSet.getY(), p.coordSet.getZ(), underBlock ? 1 : 0);
    }

    public static void renderDebugBox(double partialTicks, InventoryPacket p) {
        renderColouredBox(partialTicks, p.coordSet.getX(), p.coordSet.getY(), p.coordSet.getZ(), 2);
    }

    // Thanks to Player for this. :D
    public static void renderColouredBox(double partialTicks, int posX, int posY, int posZ, int type) {

        EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;
        double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        double offset = 0.02;
        double delta = 1 + 2 * offset;

        double x = posX - px - offset;
        double y = posY - py - offset;
        double z = posZ - pz - offset;
        glPushMatrix();
        glPushAttrib(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        switch (type) {
            case 0:
                tessellator.setColorRGBA(255, 0, 0, 150);
                break;
            case 1:
                tessellator.setColorRGBA(0, 255, 0, 75);
                break;
            case 2:
                tessellator.setColorRGBA(0, 0, 255, 75);
                break;
        }

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
