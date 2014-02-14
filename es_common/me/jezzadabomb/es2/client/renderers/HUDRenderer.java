package me.jezzadabomb.es2.client.renderers;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import me.jezzadabomb.es2.client.hud.StoredQueues;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.api.HUDBlackLists;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.network.packet.server.InventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HUDRenderer {

    // TODO On/Off animations
    private ArrayList<InventoryPacket> packetList = new ArrayList<InventoryPacket>();
    private ArrayList<PacketTimeout> ignoreList = new ArrayList<PacketTimeout>();

    private final RenderItem customItemRenderer;

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

    private void debugPacketMode(Object object) {
        ESLogger.debug(object.toString(), UtilMethods.getDebugString("Extreme Packet Monitoring"));
    }

    public void addPacketToList(InventoryPacket p) {
        World world = Minecraft.getMinecraft().theWorld;

        if (world == null) {
            ESLogger.severe("World has not loaded yet.");
            return;
        }

        CoordSet packetSet = p.coordSet;
        if (!UtilMethods.isIInventory(world, packetSet.getX(), packetSet.getY(), packetSet.getZ()) || isIgnoring(p)) {
            debugPacketMode("Ignored Packet: " + p);
            return;
        }

        if (isPacketAtXYZ(p)) {
            debugPacketMode("Setting packet in list " + p);
            packetList.set(getPosInList(p), p);
        } else {
            debugPacketMode("Adding packet to list " + p);
            packetList.add(p);
        }
        debugPacketMode("Added packet: " + p);
    }

    public boolean isPacketAtXYZ(InventoryPacket packet) {
        CoordSet packetSet = packet.coordSet;
        return isPacketAtXYZ(packetSet.getX(), packetSet.getY(), packetSet.getZ());
    }

    public boolean isPacketAtXYZ(int x, int y, int z) {
        return getPacketAtXYZ(x, y, z) != null;
    }

    public InventoryPacket getPacketAtXYZ(int x, int y, int z) {
        for (InventoryPacket packet : packetList)
            if (packet.coordSet.isAtXYZ(x, y, z))
                return packet;
        return null;
    }

    public InventoryPacket getPacketAtXYZ(String loc) {
        int[] coord = UtilMethods.getArrayFromString(loc);
        if (coord == null)
            return null;
        return getPacketAtXYZ(coord[0], coord[1], coord[2]);
    }

    public int getPosInList(InventoryPacket p) {
        for (InventoryPacket packet : packetList)
            if (p.equals(packet))
                return packetList.indexOf(packet);
        return -1;
    }

    // This removes a packet, and sets a timeout for it.
    public void removePacketAtXYZ(int x, int y, int z) {
        InventoryPacket packet = getPacketAtXYZ(x, y, z);
        if (packet != null) {
            packetList.remove(packet);
            ignoreList.add(new PacketTimeout(x, y, z));
        }
    }

    public boolean isIgnoring(InventoryPacket packet) {
        for (PacketTimeout timeout : ignoreList) {
            if (timeout.isAtXYZ(packet))
                return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        ArrayList<PacketTimeout> utilList = new ArrayList<PacketTimeout>();
        utilList.addAll(ignoreList);

        for (PacketTimeout packet : utilList)
            if (packet.tickTimeout())
                ignoreList.remove(packet);

        if (packetList.isEmpty()) {
            ESLogger.debugFlood("PacketList is empty");
            return;
        }

        ESLogger.debugFlood(packetList);

        ArrayList<InventoryPacket> packetUtilList = new ArrayList<InventoryPacket>();
        packetUtilList.addAll(packetList);

        for (InventoryPacket packet : packetUtilList) {
            if (UtilMethods.isWearingItem(ModItems.glasses)) {
                if (!StoredQueues.getInstance().isAtXYZ(packet.coordSet))
                    packetList.remove(packet);
            } else {
                if (packet.tickTiming > 120)
                    packetList.remove(packet);
            }
        }

        packetUtilList.clear();
        packetUtilList.addAll(packetList);

        for (InventoryPacket p : packetUtilList) {
            boolean underBlock = renderInfoScreen(p.coordSet.getX(), p.coordSet.getY(), p.coordSet.getZ(), event.partialTicks, p);
            if (UtilMethods.canShowDebugHUD())
                RenderUtils.renderColouredBox(event, p, underBlock);
            p.tickTiming++;
        }
    }

    private boolean renderInfoScreen(double x, double y, double z, double partialTicks, InventoryPacket p) {
        boolean underBlock = false;
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
                return underBlock;

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

            if (!world.isAirBlock(packetX, packetY + 1, packetZ) && !HUDBlackLists.ignoreListContains(world.getBlock(packetX, packetY + 1, packetZ))) {
                yInventoryPos = 190;
                yd += 1.0F;
                // TODO Add support for blocks on top of inventory.
                glEnable(GL_CULL_FACE);
                glDisable(GL_BLEND);
                glPopMatrix();
                underBlock = true;
                return underBlock;
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

            for (ItemStack tempStack : sortedList)
                if (HUDBlackLists.renderBlackListContains(tempStack))
                    utilList.add(tempStack);

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
        return underBlock;
    }

    private static class PacketTimeout {
        public int x, y, z, timeout;

        public PacketTimeout(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            timeout = 30;
        }

        public boolean isAtXYZ(InventoryPacket packet) {
            CoordSet packetSet = packet.coordSet;
            return this.x == packetSet.getX() && this.y == packetSet.getY() && this.z == packetSet.getZ();
        }

        public boolean tickTimeout() {
            return --timeout <= 0;
        }
    }

}
