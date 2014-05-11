package me.jezzadabomb.es2.client.renderers;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import me.jezzadabomb.es2.client.hud.StoredQueues;
import me.jezzadabomb.es2.client.utils.Colour;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.api.HUDBlackLists;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.core.utils.Identifier;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.helpers.DebugHelper;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import me.jezzadabomb.es2.common.core.utils.helpers.PlayerHelper;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HUDRenderer {

    // TODO REWRITE THIS
    
    private ArrayList<InventoryPacket> packetList = new ArrayList<InventoryPacket>();
    private ArrayList<PacketTimeout> ignoreList = new ArrayList<PacketTimeout>();
    public static final Colour hudColour = new Colour(1.0F, 1.0F, 1.0F, 0.6F);

    public void addPacketToList(InventoryPacket p) {
        World world = Minecraft.getMinecraft().theWorld;

        if (world == null)
            return;

        CoordSet packetSet = p.coordSet;
        if (isIgnoring(p) || !Identifier.isIInventory(world, packetSet.getX(), packetSet.getY(), packetSet.getZ()))
            return;

        if (isPacketAtXYZ(p))
            packetList.set(getPosInList(p), p);
        else
            packetList.add(p);
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

    public int getPosInList(InventoryPacket p) {
        if (p == null)
            return -1;
        return packetList.indexOf(p);
    }

    public InventoryPacket getPacketAtXYZ(String loc) {
        CoordSet coordSet = UtilMethods.getArrayFromString(loc);
        if (coordSet == null)
            return null;
        return getPacketAtXYZ(coordSet.getX(), coordSet.getY(), coordSet.getZ());
    }

    public void removePacketAtXYZ(int x, int y, int z) {
        InventoryPacket packet = getPacketAtXYZ(x, y, z);
        if (packet != null) {
            packetList.remove(packet);
            ignoreList.add(new PacketTimeout(x, y, z));
        }
    }

    public boolean isIgnoring(InventoryPacket packet) {
        for (PacketTimeout timeout : ignoreList)
            if (timeout.isAtXYZ(packet))
                return true;
        return false;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        ArrayList<PacketTimeout> utilList = new ArrayList<PacketTimeout>();
        utilList.addAll(ignoreList);

        for (PacketTimeout packet : utilList)
            if (packet.tickTimeout())
                ignoreList.remove(packet);

        if (packetList.isEmpty())
            return;

        ArrayList<InventoryPacket> packetUtilList = new ArrayList<InventoryPacket>();
        packetUtilList.addAll(packetList);

        // TODO Yep.
        for (InventoryPacket packet : packetUtilList) {
            if (PlayerHelper.isWearingItem(ModItems.glasses)) {
                if (!StoredQueues.getInstance().isAtXYZ(packet.coordSet))
                    packetList.remove(packet);
            } else {
                if (packet.tickTiming > 100)
                    packetList.remove(packet);
            }
        }

        packetUtilList.clear();
        packetUtilList.addAll(packetList);

        Collections.sort(packetUtilList, packetListComparator);

        for (InventoryPacket p : packetUtilList) {
            renderInfoScreen(p, event.partialTicks);

            p.tickTiming++;
        }
    }

    private void renderInfoScreen(InventoryPacket p, double partialTicks) {
        boolean underBlock = false;
        if ((Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().renderViewEntity;
            World world = player.worldObj;

            CoordSet coordSet = p.coordSet;

            double x = coordSet.getX();
            double y = coordSet.getY();
            double z = coordSet.getZ();

            underBlock = (!world.isAirBlock((int) Math.floor(x), (int) Math.floor(y) + 1, (int) Math.floor(z)) && !HUDBlackLists.ignoreListContains(world.getBlock((int) Math.floor(x), (int) Math.floor(y) + 1, (int) Math.floor(z))));

            glPushMatrix();
            glDisable(GL_LIGHTING);
            glDisable(GL_CULL_FACE);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            hudColour.doGL();

            if (underBlock) {
                glClear(GL_DEPTH_BUFFER_BIT);
            }

            double[] num = RenderUtils.translateToWorldCoordsShifted(player, partialTicks, x, y, z);

            if (num == null) {
                glEnable(GL_CULL_FACE);
                glDisable(GL_BLEND);
                glPopMatrix();

                if (DebugHelper.canShowDebugHUD())
                    RenderUtils.renderDebugBox(partialTicks, p);
                return;
            }

            glTranslated(0.5D, 1.5D, 0.5D);

            double xd = num[0];
            double zd = num[1];
            double yd = num[2];

            int xTextureOffset = 11;
            int yTextureOffset = 18;
            int xInventoryPos = -87;
            int yInventoryPos = -130;

            if (underBlock) {
                yInventoryPos = 174;
                yd += 1.2F;
            }

            float rotYaw = (float) (Math.atan2(xd, zd) * 180.0D / 3.141592653589793D);
            glRotatef(rotYaw + 180.0F, 0.0F, 1.0F, 0.0F);

            if (Reference.HUD_VERTICAL_ROTATION || underBlock) {
                float rotPitch = (float) (Math.atan2(yd, MathHelper.pythagoras(xd, zd)) * 180.0D / 3.141592653589793D);
                glRotatef(rotPitch, 1.0F, 0.0F, 0.0F);
            }

            glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            glScalef(0.006F, 0.0036F, 0.006F);

            RenderUtils.bindTexture(TextureMaps.HUD_INVENTORY);
            RenderUtils.drawTexturedQuad(xInventoryPos, yInventoryPos, 0, 0, 174, 256, 0);
            glTranslated(xInventoryPos + xTextureOffset, yInventoryPos + yTextureOffset, 0.0D);

            ArrayList<ItemStack> itemStacks = p.getItemStacks();
            Collections.sort(itemStacks, itemStackComparator);

            drawItemStacks(itemStacks);

            glEnable(GL_CULL_FACE);
            glDisable(GL_BLEND);
            glPopMatrix();
        }
        if (underBlock) {
            if (DebugHelper.canShowDebugHUD())
                RenderUtils.renderColouredBox(partialTicks, p, underBlock);
        }
    }

    private void drawItemStacks(ArrayList<ItemStack> itemStacks) {
        int xOffset = 52;
        int yOffset = 74;

        int indexNum = -1;
        int rowNum = 0;
        int totalSlots = 0;

        for (ItemStack itemStack : itemStacks) {
            if (totalSlots > 8)
                break;

            if (++indexNum >= 3) {
                indexNum = 0;
                rowNum++;
            }

            RenderUtils.drawItemAndSlot(indexNum * xOffset, rowNum * yOffset, itemStack, -2, indexNum, rowNum);
            totalSlots++;
        }
    }

    public static Comparator<ItemStack> itemStackComparator = new Comparator<ItemStack>() {
        @Override
        public int compare(ItemStack stack1, ItemStack stack2) {
            return stack2.stackSize - stack1.stackSize;
        }
    };

    public static Comparator<InventoryPacket> packetListComparator = new Comparator<InventoryPacket>() {
        @Override
        public int compare(InventoryPacket packet1, InventoryPacket packet2) {
            return (int) Math.floor(packet2.distanceToPlayer() - packet1.distanceToPlayer());
        }
    };

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
