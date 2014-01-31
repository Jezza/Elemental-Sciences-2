package me.jezzadabomb.es2.client.utils;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex3d;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtils {

    static RenderBlocks renderBlocksInstance = new RenderBlocks();

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

    public static void resetHUDColour() {
        glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
    }

    public static float[] translateToWorldCoordsShifted(Entity entity, double frame, double x, double y, double z) {
        double interpPosX = MathHelper.interpolate(entity.lastTickPosX, entity.posX, frame);
        double interpPosY = MathHelper.interpolate(entity.lastTickPosY, entity.posY, frame);
        double interpPosZ = MathHelper.interpolate(entity.lastTickPosZ, entity.posZ, frame);

        GL11.glTranslated(-interpPosX + x, -interpPosY + y, -interpPosZ + z);

        float[] temp = new float[3];
        temp[0] = (float) (interpPosX - (x + 0.5D));
        temp[1] = (float) (interpPosZ - (z + 0.5D));
        temp[2] = (float) (interpPosY - (y + 1.8D));
        return temp;
    }

    public static void drawLineFrom(double x, double y, double z, double targetX, double targetY, double targetZ) {
        glPushMatrix();
        glBegin(GL_LINES);
        glVertex3d(x, y, z);
        glVertex3d(targetX, targetY, targetZ);
        glEnd();
        glPopMatrix();
    }

    public static float[] worldCoordsShifted(Entity entity, double frame, double x, double y, double z) {
        double interpPosX = MathHelper.interpolate(entity.lastTickPosX, entity.posX, frame);
        double interpPosY = MathHelper.interpolate(entity.lastTickPosY, entity.posY, frame);
        double interpPosZ = MathHelper.interpolate(entity.lastTickPosZ, entity.posZ, frame);

        float[] temp = new float[3];
        temp[0] = (float) (interpPosX - (x + 0.5D));
        temp[1] = (float) (interpPosZ - (z + 0.5D));
        temp[2] = (float) (interpPosY - (y + 1.8D));
        return temp;
    }

    // Individual translations if I want.
    private static void translateWithRowAndColumn(int indexNum, int rowNum, boolean itemBlock, boolean specialRenderer) {
        if (itemBlock) {
            switch (indexNum) {
                case 0:
                    glTranslated(2.0D, 0.0D, 0.0D);
                    break;
                case 1:
                    break;
                case 2:
                    glTranslated(-2.0D, 0.0D, 0.0D);
                    break;
                default:
                    return;
            }

            switch (rowNum) {
                case 0:
                    glTranslated(0.0D, 3.0D, 0.0D);
                    break;
                case 1:
                    glTranslated(0.0D, 2.0D, 0.0D);
                    break;
                case 2:
                    break;
                default:
                    return;
            }
        } else {
            switch (indexNum) {
                case 0:
                    glTranslated(1.0D, 0.0D, 0.0D);
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    return;
            }

            switch (rowNum) {
                case 0:
                    break;
                case 1:
                    // glTranslated(0.0D, -2.0D, 0.0D);
                    break;
                case 2:
                    break;
                default:
                    return;
            }
        }

    }

    private static void translateSpecialRender(int indexRow, int rowNum, boolean neg) {
        int tempNeg = neg ? 1 : -1;
        switch (rowNum) {
            case 0:
                glTranslated(0.0D, -1.0D * tempNeg, 0.0D);
                break;
            case 2:
                glTranslated(0.0D, 1.0D * tempNeg, 0.0D);
                break;
            default:
                return;
        }
        switch (indexRow) {
            case 0:
                glTranslated(-1.0D * tempNeg, 0.0D, 0.0D);
                break;
            case 2:
                glTranslated(0.0D, 0.0D, 1.0D * tempNeg);
                break;
            default:
                return;
        }
    }

    public static void drawItemStack(int x, int y, ItemStack itemStack, RenderItem customItemRenderer, int zLevel, int indexNum, int rowNum) {
        if (itemStack == null)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        TextureManager textureManager = mc.getTextureManager();
        FontRenderer fontRenderer = mc.fontRenderer;

        glPushMatrix();
        glDisable(GL_CULL_FACE);

        glTranslated(x + 10, y, zLevel);

        glScalef(0.96F, 1.2F, -1.2F);

        translateWithRowAndColumn(indexNum, rowNum, itemStack.getItem() instanceof ItemBlock, false);

        if (itemStack.getItem() instanceof ItemBlock) {
            glTranslated(-3.0D, 8.0D, 0.0D);
            glScaled(2.2D, 2.2D, 2.2D);

            EntityItem entityItem = new EntityItem(mc.thePlayer.worldObj);
            entityItem.setEntityItemStack(itemStack);

            glDisable(GL_BLEND);
            if (!ForgeHooksClient.renderInventoryItem(renderBlocksInstance, textureManager, itemStack, true, zLevel, 0, 0)) {
                glEnable(GL_BLEND);
                customItemRenderer.renderItemIntoGUI(fontRenderer, textureManager, itemStack, 0, 0);
            }
            glEnable(GL_BLEND);
            glDisable(GL_LIGHTING);
        } else {
            glTranslated(-9, 5, 0);

            glScaled(2.8D, 2.8D, 2.8D);

            translateSpecialRender(indexNum, rowNum, false);
            if (!ForgeHooksClient.renderInventoryItem(renderBlocksInstance, textureManager, itemStack, true, zLevel, 0, 0)) {
                translateSpecialRender(indexNum, rowNum, true);
                customItemRenderer.renderItemIntoGUI(fontRenderer, textureManager, itemStack, 0, 0);
            }
            glDisable(GL_LIGHTING);
        }
        resetHUDColour();
        glEnable(GL_CULL_FACE);
        glPopMatrix();
    }

    public static void drawTextureSlot(int x, int y, double zLevel) {
        glPushMatrix();
        bindTexture(TextureMaps.HUD_INVENTORY);
        drawTexturedQuad(x, y, 175, 0, 49, 73, zLevel);
        glPopMatrix();
    }

    public static void drawItemAndSlot(int x, int y, ItemStack itemStack, RenderItem customItemRenderer, int zLevel, int indexNum, int rowNum) {
        glDisable(GL_LIGHTING);
        if (Reference.DRAW_TEXTURED_SLOTS)
            drawTextureSlot(x, y, zLevel + 1);
        drawItemStack(x, y, itemStack, customItemRenderer, zLevel, indexNum, rowNum);
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

    // Thanks to Player for this. :D
    public static void renderColouredBox(RenderWorldLastEvent event, InventoryPacket p, boolean underBlock) {
        double partialTicks = event.partialTicks;

        EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;
        double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        double offset = 0.02;
        double delta = 1 + 2 * offset;

        double x = p.coordSet.getX() - px - offset;
        double y = p.coordSet.getY() - py - offset;
        double z = p.coordSet.getZ() - pz - offset;
        glPushMatrix();
        glPushAttrib(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        if (underBlock) {
            tessellator.setColorRGBA(0, 255, 0, 75);
        } else {
            tessellator.setColorRGBA(255, 0, 0, 150);
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
