package me.jezzadabomb.es2.client.tickers;

import java.util.ArrayList;
import java.util.EnumSet;

import me.jezzadabomb.es2.common.hud.StoredQueues;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HUDHandler implements ITickHandler {

    EntityClientPlayerMP player;
    Minecraft mc;
    public static ArrayList<InventoryPacket> packetList;
    public ArrayList<InventoryPacket> removeList;

    public HUDHandler() {
        mc = FMLClientHandler.instance().getClient();
        player = mc.thePlayer;
        packetList = new ArrayList<InventoryPacket>(0);
        removeList = new ArrayList<InventoryPacket>(0);
    }

    public static void addHUDHandler(InventoryPacket inventoryPacket) {
        packetList.add(inventoryPacket);
    }

    public void printPacketList() {
        for (InventoryPacket p : packetList) {
            System.out.println("PacketList: " + p);
            System.out.println("X: " + p.x);
            System.out.println("Y: " + p.y);
            System.out.println("Z: " + p.z);
            for (ItemStack i : p.itemStacks) {
                System.out.println(i);
            }
            System.out.println();
        }
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
//        printPacketList();
        if (packetList.isEmpty())
            return;
        for (InventoryPacket p : packetList) {
            if (!StoredQueues.instance().getStrXYZ(p.inventoryTitle, p.x, p.y, p.z)) {
                removeList.add(p);
            }
        }
        packetList.removeAll(removeList);
        if (type.contains(TickType.RENDER)) {
            if(!StoredQueues.instance().getPlayer().isEmpty()){
                renderWorld();
            }
        }
    }
    
    public void renderWorld(){
        for(InventoryPacket p : packetList){
            drawGuiAtXYZ(p);
        }
    }
    
    public void drawGuiAtXYZ(InventoryPacket p){
        FontRenderer fontrenderer = mc.fontRenderer;
        RenderManager renderManager = RenderManager.instance;
        String par2Str = p.inventoryTitle;
        float x = p.x;
        float y = p.y;
        float z = p.z;
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y + 1F, (float)z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-f1, -f1, f1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.instance;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        int j = fontrenderer.getStringWidth(par2Str) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.addVertex((double)(-j - 1), (double)(-1), 0.0D);
        tessellator.addVertex((double)(-j - 1), (double)(8), 0.0D);
        tessellator.addVertex((double)(j + 1), (double)(8), 0.0D);
        tessellator.addVertex((double)(j + 1), (double)(-1), 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, 0, 553648127);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, 0, -1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
    
    public void drawGuiBackGround(Minecraft mc, int guiLeft, int guiTop, int right, int bottom, float zLevel){
        
    }
    
    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.RENDER);
    }

    @Override
    public String getLabel() {
        return "ES2-ClientRenderer";
    }
}
