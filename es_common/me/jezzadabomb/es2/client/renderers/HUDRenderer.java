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

import me.jezzadabomb.es2.api.HUDBlackLists;
import me.jezzadabomb.es2.client.hud.StoredQueues;
import me.jezzadabomb.es2.client.utils.Colour;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.helpers.DebugHelper;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import me.jezzadabomb.es2.common.core.utils.helpers.PlayerHelper;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HUDRenderer {

    private HashSet<InventoryPacket> packetList = new HashSet<InventoryPacket>();
    private HashSet<PacketTimeout> ignoreList = new HashSet<PacketTimeout>();
    public static final Colour HUDCOLOUR = new Colour(1.0F, 1.0F, 1.0F, 0.6F);

    public void addPacketToList(InventoryPacket packet) {
        World world = Minecraft.getMinecraft().theWorld;
        CoordSet coordSet = packet.coordSet;

        if (ignoreList.contains(packet.coordSet) || !coordSet.isIInventory(world) || !StoredQueues.getInstance().isAtXYZ(coordSet))
            return;

        if (packetList.contains(packet))
            packetList.remove(packet);
        packetList.add(packet);
    }

    public void removePacketAtXYZ(CoordSet coordSet) {
        boolean flag = packetList.remove(coordSet);
        if (flag)
            ignoreList.add(new PacketTimeout(coordSet));
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        World world = Minecraft.getMinecraft().theWorld;
        ArrayList<PacketTimeout> utilList = new ArrayList<PacketTimeout>();
        utilList.addAll(ignoreList);

        for (PacketTimeout packet : utilList)
            if (packet.tickTimeout())
                ignoreList.remove(packet);

        if (packetList.isEmpty())
            return;

        ArrayList<InventoryPacket> packetUtilList = new ArrayList<InventoryPacket>();
        packetUtilList.addAll(packetList);

        for (InventoryPacket packet : packetUtilList)
            if ((!PlayerHelper.isWearingItem(ModItems.glasses) && packet.tick()) || packet.canRemove() || packet.coordSet.isAirBlock(world))
                packetList.remove(packet);

        packetUtilList.clear();
        packetUtilList.addAll(packetList);

        Collections.sort(packetUtilList, packetListComparator);

        for (InventoryPacket p : packetUtilList)
            renderInventoryScreen(p, event.partialTicks);
    }

    private void renderInventoryScreen(InventoryPacket p, double partialTicks) {
        EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;
        World world = player.worldObj;

        boolean underBlock = false;
        CoordSet coordSet = p.coordSet;
        CoordSet tempSet = coordSet.copy().addY(1);

        double x = coordSet.getX();
        double y = coordSet.getY();
        double z = coordSet.getZ();

        underBlock = (!tempSet.isAirBlock(world) && !HUDBlackLists.ignoreListContains(tempSet.getBlock(world)));

        glPushMatrix();
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        HUDCOLOUR.doGL();

        if (underBlock)
            glClear(GL_DEPTH_BUFFER_BIT);

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
            yd += 0.25F;
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

        if (underBlock)
            if (DebugHelper.canShowDebugHUD())
                RenderUtils.renderColouredBox(partialTicks, p, underBlock);
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

    public static final Comparator<ItemStack> itemStackComparator = new Comparator<ItemStack>() {
        @Override
        public int compare(ItemStack stack1, ItemStack stack2) {
            return stack2.stackSize - stack1.stackSize;
        }
    };

    public static final Comparator<InventoryPacket> packetListComparator = new Comparator<InventoryPacket>() {
        @Override
        public int compare(InventoryPacket packet1, InventoryPacket packet2) {
            return (int) Math.floor(packet2.distanceToPlayer() - packet1.distanceToPlayer());
        }
    };

    private static class PacketTimeout {
        public CoordSet coordSet;
        public int timeout;

        public PacketTimeout(CoordSet coordSet) {
            this.coordSet = coordSet;
            timeout = 30;
        }

        public boolean isAtXYZ(InventoryPacket packet) {
            return coordSet.equals(packet.coordSet);
        }

        public boolean tickTimeout() {
            return --timeout <= 0;
        }
    }
}
