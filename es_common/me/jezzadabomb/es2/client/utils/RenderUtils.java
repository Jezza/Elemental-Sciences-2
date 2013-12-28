package me.jezzadabomb.es2.client.utils;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class RenderUtils {

	static RenderBlocks renderBlocksInstance = new RenderBlocks();

	public static void bindTexture(ResourceLocation rl) {
		Minecraft.getMinecraft().renderEngine.bindTexture(rl);
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
		double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
		double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
		double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;

		GL11.glTranslated(-interpPosX + x + 0.5D, -interpPosY + y + 1.5D, -interpPosZ + z + 0.5D);

		float[] temp = new float[3];
		temp[0] = (float) (interpPosX - (x + 0.5D));
		temp[1] = (float) (interpPosZ - (z + 0.5D));
		temp[2] = (float) (interpPosY - (y + 1.8D));
		return temp;
	}

	public static float[] worldCoordsShifted(Entity entity, double frame, double x, double y, double z) {
		double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
		double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
		double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;

		float[] temp = new float[3];
		temp[0] = (float) (interpPosX - (x + 0.5D));
		temp[1] = (float) (interpPosZ - (z + 0.5D));
		temp[2] = (float) (interpPosY - (y + 1.8D));
		return temp;
	}

	private static void translateWithRowAndColumn(int indexNum, int rowNum, boolean itemBlock) {
		if (itemBlock) {
			switch (indexNum) {
			case 0:
				// glTranslated(-2.0D, 0.0D, 0.0D);
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
				// glTranslated(0.0D, 5.0D, 0.0D);
				break;
			case 1:
				// glTranslated(0.0D, -10.0D, 0.0D);
				break;
			case 2:
				// glTranslated(0.0D, -22.0D, 0.0D);
				break;
			default:
				return;
			}
		} else {
			switch (indexNum) {
			case 0:
				// glTranslated(-3.0D, -5.0D, 0.0D);
				break;
			case 1:
				// glTranslated(-2.0D, -5.0D, 0.0D);
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

	public static void drawItemInSlot(int x, int y, ItemStack itemStack, RenderItem customItemRenderer, int zLevel, int indexNum, int rowNum) {
		Minecraft mc = Minecraft.getMinecraft();
		TextureManager textureManager = mc.getTextureManager();
		FontRenderer fontRenderer = mc.fontRenderer;

		glPushMatrix();
		glDisable(GL_CULL_FACE);

		glTranslated(x + 10, y, zLevel);

		glScalef(0.8F, 1.0F, 1.0F);
		glScalef(1.2F, 1.2F, -1.2F);

		translateWithRowAndColumn(indexNum, rowNum, itemStack.getItem() instanceof ItemBlock);

		if (itemStack.getItem() instanceof ItemBlock) {
			glTranslated(-3.0D, 8.0D, 0.0D);
			glScaled(2.2D, 2.2D, 2.2D);

			EntityItem entityItem = new EntityItem(mc.thePlayer.worldObj);
			entityItem.hoverStart = 0.0F;
			entityItem.setEntityItemStack(itemStack);

			glDisable(GL_BLEND);
			if (!ForgeHooksClient.renderInventoryItem(renderBlocksInstance, textureManager, itemStack, true, zLevel, 0, 0)) {
				customItemRenderer.renderItemIntoGUI(fontRenderer, textureManager, itemStack, 0, 0);
			}
			glEnable(GL_BLEND);
			glDisable(GL_LIGHTING);
		} else {
			glTranslated(-7, 4, 0);

			glScaled(2.8D, 2.8D, 2.8D);

			customItemRenderer.renderItemIntoGUI(fontRenderer, textureManager, itemStack, 0, 0);
			ForgeHooksClient.renderInventoryItem(renderBlocksInstance, textureManager, itemStack, true, zLevel, 0, 0);
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
		drawItemInSlot(x, y, itemStack, customItemRenderer, zLevel, indexNum, rowNum);
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

		double x = p.x - px - offset;
		double y = p.y - py - offset;
		double z = p.z - pz - offset;
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

	// Thanks to Azanor ;)
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
			float rotYaw = (float) (Math.atan2(xd, zd) * 180.0D / 3.141592653589793D);

			glRotatef(rotYaw + 180.0F, 0.0F, 1.0F, 0.0F);

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

}
