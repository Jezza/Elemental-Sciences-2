package me.jezzadabomb.es2.client.renderers;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import me.jezzadabomb.es2.client.utils.CoordSet;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.api.HUDBlackLists;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilHelpers;
import me.jezzadabomb.es2.common.hud.StoredQueues;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import me.jezzadabomb.es2.common.packets.InventoryTerminatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HUDRenderer {

	// TODO On/Off animations
	private ArrayList<InventoryPacket> packetList = new ArrayList<InventoryPacket>();
	private ArrayList<InventoryPacket> removeList = new ArrayList<InventoryPacket>();
	private ArrayList<CoordSet> ignoreList = new ArrayList<CoordSet>();

	private final RenderItem customItemRenderer;

	private boolean underBlock = false;
	private int tickTiming = 0;

	public HUDRenderer() {
		customItemRenderer = new RenderItem();
		customItemRenderer.setRenderManager(RenderManager.instance);
	}

	public InventoryPacket getPacket(int x, int y, int z) {
		for (InventoryPacket packet : packetList) {
			if (packet.coordSet.isAtXYZ(x, y, z)) {
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
		ESLogger.debug("Started method to add: " + p, 1);
		if (ignorePacket(p)) {
			ESLogger.debug("Ignored Packet: " + p, 1);
			return;
		}
		if (doesPacketAlreadyExistAtXYZ(p)) {
			ESLogger.debug("Setting packet in list " + p, 1);
			packetList.set(getPosInList(p), p);
		} else {
			ESLogger.debug("Adding packet to list " + p, 1);
			packetList.add(p);
		}
		ESLogger.debug("Added packet: " + p, 1);
	}

	private boolean ignorePacket(InventoryPacket p) {
		boolean found = false;
		CoordSet tempSet = null;
		for (CoordSet set : ignoreList) {
			if (set.isPacket(p)) {
				found = true;
				tempSet = set;
				break;
			}
		}
		if (found && tempSet != null) {
			ignoreList.remove(tempSet);
		}
		return found;
	}

	private boolean doesPacketAlreadyExistAtXYZ(InventoryPacket p) {
		ESLogger.debug("Testing for previous packet.", 1);
		for (InventoryPacket packet : packetList) {
			if (p.equals(packet)) {
				ESLogger.debug("Found previous packet.", 1);
				return true;
			}
		}
		ESLogger.debug("No previous packet found.", 1);
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

	public InventoryPacket getPacketAtXYZ(String loc) {
		int[] coord = UtilHelpers.getArrayFromString(loc);
		for (InventoryPacket p : packetList) {
			if (p.coordSet.isAtXYZ(coord[0], coord[1], coord[2])) {
				return p;
			}
		}
		return null;
	}

	@ForgeSubscribe
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if (packetList.isEmpty()) {
			ESLogger.debugFlood("PacketList is empty");
			return;
		}
		ESLogger.debugFlood(packetList);
		tickTiming++;

		for (InventoryPacket packet : packetList) {
			if (UtilHelpers.isWearingItem(ModItems.glasses)) {
				if (!StoredQueues.instance().getStrXYZ(packet.inventoryTitle, packet.coordSet.getX(), packet.coordSet.getY(), packet.coordSet.getZ())) {
					removeList.add(packet);
				}
			} else {
				if (packet.tickTiming > 120) {
					removeList.add(packet);
				}
			}
		}
		
		packetList.removeAll(removeList);
		removeList.clear();

		for (InventoryPacket p : packetList) {
			renderInfoScreen(p.coordSet.getX(), p.coordSet.getY(), p.coordSet.getZ(), event.partialTicks, p);
			if (UtilHelpers.canShowDebugHUD()) {
				RenderUtils.renderColouredBox(event, p, underBlock);
				if (!underBlock)
					RenderUtils.drawTextInAir(p.coordSet.getX(), p.coordSet.getY() + 0.63F, p.coordSet.getZ(), event.partialTicks, p.inventoryTitle);
				underBlock = false;
			}
			p.tickTiming++;
		}
	}

	private void renderInfoScreen(double x, double y, double z, double partialTicks, InventoryPacket p) {
		if ((Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().renderViewEntity;
			World world = player.worldObj;

			glPushMatrix();
			glDisable(GL_LIGHTING);
			glDisable(GL_CULL_FACE);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			RenderUtils.resetHUDColour();

			float[] num = RenderUtils.translateToWorldCoordsShifted(player, partialTicks, x, y, z);
			glTranslated(0.5D, 1.5D, 0.5D);

			if (num == null)
				return;

			float xd = num[0];
			float zd = num[1];
			float yd = num[2];

			int xTextureOffset = 11;
			int yTextureOffset = 18;
			int xInventoryPos = -87;
			int yInventoryPos = -130;

			int packetX = p.coordSet.getX();
			int packetY = p.coordSet.getY();
			int packetZ = p.coordSet.getZ();

			if (!world.isAirBlock(packetX, packetY + 1, packetZ) && !HUDBlackLists.IgnoreListContains(UtilHelpers.getBlockFromWorld(world, packetX, packetY + 1, packetZ))) {
				yInventoryPos = 190;
				yd += 1.0F;
				// TODO Add support for blocks on top of inventory.
				glEnable(GL_CULL_FACE);
				glDisable(GL_BLEND);
				glPopMatrix();
				underBlock = true;
				return;
			}

			float rotYaw = (float) (Math.atan2(xd, zd) * 180.0D / 3.141592653589793D);

			glRotatef(rotYaw + 180.0F, 0.0F, 1.0F, 0.0F);

			if (Reference.HUD_VERTICAL_ROTATION) {
				float rotPitch = (float) (Math.atan2(yd, MathHelper.pythagoras(xd, zd)) * 180.0D / 3.141592653589793D);
				glRotatef(rotPitch, 1.0F, 0.0F, 0.0F);
			}

			glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			glScalef(0.006F, 0.0036F, 0.006F);

			// Inventory background
			RenderUtils.bindTexture(TextureMaps.HUD_INVENTORY);
			RenderUtils.drawTexturedQuad(xInventoryPos, yInventoryPos, 0, 0, 174, 250, 0);
			glTranslated(xInventoryPos + xTextureOffset, yInventoryPos + yTextureOffset, 0.0D);
			int xOffset = 52;
			int yOffset = 74;

			int indexNum = -1;
			int rowNum = 0;
			int totalSlots = 0;
			ArrayList<ItemStack> sortedList = new ArrayList<ItemStack>();
			ArrayList<ItemStack> utilList = new ArrayList<ItemStack>();

			boolean added = false;
			for (ItemStack itemStack : p.getItemStacks()) {
				for (ItemStack tempStack : sortedList) {
					if (itemStack.stackSize > tempStack.stackSize) {
						utilList.add(sortedList.indexOf(tempStack), itemStack);
						added = true;
						break;
					}
				}
				if (!added) {
					utilList.add(itemStack);
				}

				sortedList.clear();
				sortedList.addAll(utilList);
				added = false;
			}
			utilList.clear();

			for (ItemStack tempStack : sortedList) {
				for (Item item : HUDBlackLists.getRenderBlackList()) {
					if (tempStack.getItem().equals(item)) {
						utilList.add(tempStack);
					}
				}
			}
			sortedList.removeAll(utilList);

			for (ItemStack itemStack : sortedList) {
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
			glEnable(GL_CULL_FACE);
			glDisable(GL_BLEND);
			glPopMatrix();
		}
	}

	public void addToRemoveList(InventoryTerminatePacket inventoryTerminatePacket) {
		packetList.clear();
		removeList.add(getPacketAtXYZ(inventoryTerminatePacket.loc));
	}

	public void addToRemoveList(int x, int y, int z) {
		ESLogger.debug("Found", 1);
		InventoryPacket p = getPacket(x, y, z);
		if (p == null)
			return;
		ESLogger.debug(p, 1);
		removeList.add(p);
		ignoreList.add(new CoordSet(x, y, z));
	}
}
