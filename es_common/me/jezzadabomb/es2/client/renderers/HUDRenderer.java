package me.jezzadabomb.es2.client.renderers;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.tickers.PlayerTicker;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilHelpers;
import me.jezzadabomb.es2.common.hud.StoredQueues;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HUDRenderer {
	private ArrayList<InventoryPacket> packetList = new ArrayList<InventoryPacket>();
	private ArrayList<InventoryPacket> removeList = new ArrayList<InventoryPacket>();

	private final RenderItem customItemRenderer;

	public HUDRenderer() {
		customItemRenderer = new RenderItem() {
			@Override
			public boolean shouldBob() {
				return false;
			}

			@Override
			public boolean shouldSpreadItems() {
				return false;
			}
		};
		customItemRenderer.setRenderManager(RenderManager.instance);
	}

	public InventoryPacket getPacket(int x, int y, int z) {
		for (InventoryPacket packet : packetList) {
			if (packet.x == x && packet.y == y && packet.z == z) {
				return packet;
			}
		}
		return null;
	}

	public void printPacketList() {
		for (InventoryPacket packet : packetList) {
			System.out.println(packet);
		}
	}

	public void addPacketToList(InventoryPacket p) {
		if (doesPacketAlreadyExistAtXYZ(p)) {
			packetList.set(getPosInList(p), p);
		} else {
			packetList.add(p);
		}
	}

	private boolean doesPacketAlreadyExistAtXYZ(InventoryPacket p) {
		for (InventoryPacket packet : packetList) {
			if (p.equals(packet)) {
				return true;
			}
		}
		return false;
	}

	public int getPosInList(InventoryPacket p) {
		for (InventoryPacket packet : packetList) {
			if (p.equals(packet)) {
				return packetList.indexOf(packet);
			}
		}
		return -1;
	}

	@ForgeSubscribe
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if (packetList.isEmpty())
			return;

		for (InventoryPacket packet : packetList) {
			if (!StoredQueues.instance().getStrXYZ(packet.inventoryTitle, packet.x, packet.y, packet.z)) {
				removeList.add(packet);
			}
		}

		packetList.removeAll(removeList);
		removeList.clear();

		for (InventoryPacket p : packetList) {
			if (UtilHelpers.canShowDebugHUD()) {
				RenderUtils.renderRedBox(event, p);
				RenderUtils.drawTextInAir(p.x, p.y + 0.5F, p.z, event.partialTicks, p.inventoryTitle);
			}
			renderInfoScreen(p.x, p.y, p.z, event.partialTicks, p);
		}
	}

	private void renderInfoScreen(double x, double y, double z, double partialTicks, InventoryPacket p) {
		if ((Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().renderViewEntity;
			double iPX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
			double iPY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
			double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

			glPushMatrix();
			glDisable(GL_LIGHTING);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glColor4f(1.0F, 1.0F, 1.0F, 0.6F);

			float[] num = RenderUtils.translateToWorldCoordsShifted(player, partialTicks, x, y, z);
			if (num == null) {
				return;
			}

			float xd = num[0];
			float zd = num[1];
			float yd = num[2];

			int xTextureOffset = 11;
			int yTextureOffset = 18;
			int xInventoryPos = -80;
			int yInventoryPos = -90;
			boolean renderOnBlock = !player.worldObj.isAirBlock(p.x, p.y + 1, p.z);
			if (renderOnBlock) {
				yInventoryPos = 190;
				yd += 1.0F;
			}

			float rotYaw = (float) (Math.atan2(xd, zd) * 180.0D / 3.141592653589793D);
			float rotPitch = (float) (Math.atan2(yd, Math.sqrt(xd * xd + zd * zd)) * 180.0D / 3.141592653589793D);

			glRotatef(rotYaw + 180.0F, 0.0F, 1.0F, 0.0F);
			if (Reference.HUD_VERTICAL_ROTATION) {
				glRotatef(rotPitch + 0.0F, 1.0F, 0.0F, 0.0F);
			}

			glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			glScalef(0.01F, 0.006F, 0.01F);
			glScalef(0.6F, 0.6F, 0.6F);

			// Inventory background
			RenderUtils.bindTexture(TextureMaps.HUD_INVENTORY);
			RenderUtils.drawTexturedQuad(xInventoryPos, yInventoryPos, 0, 0, 172, 250, 0);
			glTranslated(xInventoryPos + xTextureOffset, yInventoryPos + yTextureOffset, 0.0D);
			int xOffset = 52;
			int yOffset = 74;

			int indexNum = -1;
			int rowNum = 0;
			int totalSlots = 0;
			for (ItemStack itemStack : p.getItemStacks()) {
				if (totalSlots > 8) {
					break;
				}
				if (indexNum < 2) {
					indexNum++;
				} else {
					indexNum = 0;
					rowNum++;
				}
				RenderUtils.drawItemAndSlot(indexNum * xOffset, rowNum * yOffset, itemStack, customItemRenderer, -2, indexNum, rowNum);
				totalSlots++;
			}
			glDisable(GL_BLEND);
			glPopMatrix();
		}
	}
}
