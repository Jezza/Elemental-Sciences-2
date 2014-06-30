package me.jezzadabomb.es2.client.renderers;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import me.jezzadabomb.es2.api.HUDBlackLists;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.helpers.DebugHelper;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import me.jezzadabomb.es2.common.core.utils.helpers.PlayerHelper;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
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

    public void addPacket(InventoryPacket packet) {
        World world = Minecraft.getMinecraft().theWorld;
        CoordSet coordSet = packet.coordSet;

        if (ignoreList.contains(coordSet) || !coordSet.isIInventory(world) || coordSet.isAirBlock(world))
            return;

        if (packetList.contains(packet))
            packetList.remove(packet);
        packetList.add(packet);
    }

    public void removePacket(CoordSet coordSet) {
        if (packetList.contains(coordSet))
            if (packetList.remove(coordSet))
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

        for (InventoryPacket packet : packetUtilList) {
            CoordSet coordSet = packet.coordSet;
            if ((!PlayerHelper.isWearingItem(ModItems.glasses) && packet.tick()) || packet.canRemove() || !coordSet.isIInventory(world) || coordSet.isAirBlock(world))
                packetList.remove(packet);
        }

        if (packetList.isEmpty())
            return;

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
        glColor4f(1.0F, 1.0F, 1.0F, 0.6F);

        if (underBlock)
            glClear(GL_DEPTH_BUFFER_BIT);

        double[] num = RenderUtils.worldCoordShifted(player, partialTicks, x, y, z, true);

        if (num == null) {
            glEnable(GL_CULL_FACE);
            glDisable(GL_BLEND);
            glPopMatrix();

            // if (DebugHelper.canShowDebugHUD())
            // RenderUtils.renderColouredBox(partialTicks, p.coordSet, new Colour());
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

        // if (underBlock && DebugHelper.canShowDebugHUD())
        // RenderUtils.renderColouredBox(partialTicks, p.coordSet, new Colour(0.0F, 1.0F, 0.0F, 0.5F));
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

    public static final Comparator<InventoryPacket> packetListComparator = new Comparator<InventoryPacket>() {
        @Override
        public int compare(InventoryPacket packet1, InventoryPacket packet2) {
            return (int) Math.floor(packet2.distanceToPlayer() - packet1.distanceToPlayer());
        }
    };

    public static final Comparator<ItemStack> itemStackComparator = new Comparator<ItemStack>() {
        @Override
        public int compare(ItemStack stack1, ItemStack stack2) {
            return stack2.stackSize - stack1.stackSize;
        }
    };

    private static class PacketTimeout {
        private CoordSet coordSet;
        private int timeout;

        public PacketTimeout(CoordSet coordSet) {
            this.coordSet = coordSet;
            timeout = 30;
        }

        public boolean tickTimeout() {
            return --timeout <= 0;
        }
    }
}
