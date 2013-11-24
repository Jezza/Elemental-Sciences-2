package me.jezzadabomb.es2.client.renderers;

import me.jezzadabomb.es2.client.models.ModelAtomicCatalyst;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class ItemAtomicCatalystRenderer implements IItemRenderer{

    private ModelAtomicCatalyst modelAtomicCatalyst;
    
    public ItemAtomicCatalystRenderer()
    {
        modelAtomicCatalyst = new ModelAtomicCatalyst();
    }
    
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch(type)
        {
            case ENTITY:{
                renderAtomicCatalyst(0F, 0.5F, 0F, 1.9F, false, false, false);
                return;
            }
             
            case EQUIPPED:{
                renderAtomicCatalyst(0.5F, 0.5F, 0F, 2F, false, false, true);
                return;
            }
                 
            case EQUIPPED_FIRST_PERSON: {
                renderAtomicCatalyst(0F, 1.0F, 0.4F, 1.9F, true, false, false);
                break;
            }
            
            case INVENTORY:{
                renderAtomicCatalyst(0F, 0.1F, 0.0F, 2.5F, false, true, false);
                return;
            }
             
            default:return;
        }
    }
    //Bohr Model, because bite me.
    private void renderAtomicCatalyst(float x, float y, float z, float scale, boolean fp, boolean inv, boolean eq){
        
        TextureManager texture = Minecraft.getMinecraft().renderEngine;
        
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        
        //Translate and Scale
        GL11.glTranslatef(x, y, z);
        GL11.glScalef(scale, scale, scale);
        
        //Translate, render and texture the electrons.
        translateBindRender(texture, "Main", x, y, z, 0.0D);
        
        //First Person translations because it's weird for some reason..
        if(fp)GL11.glTranslatef(0F, -0.20F, -0.17F);
        //Inventory translations, because it's also a bit weird. 
        if(inv)GL11.glTranslatef(-0.01F, 0.12F, 0.0F);
        //Equipped translations, because it's even more retarded..
        if(eq)GL11.glTranslatef(-0.22F, 0F, 0F);
        
        //Math for the rotation periods.
        double local = (24 * Math.PI * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
        
        //Translate, texture, and render the electrons. Theoretically I can easily add electrons.
        translateBindRender(texture, "Electron1", x, y, z, local);
        translateBindRender(texture, "Electron2", x, y, z, local);     
        translateBindRender(texture, "Electron3", x, y, z, local);
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        
    }
    
    public void translateBindRender(TextureManager texture, String part, float x, float y, float z, double angle){
        float sinDis = ((float) ((0.4 * (Math.sin(angle))) / 8))*10;
        float cosDis = ((float) ((0.4 * (Math.cos(angle))) / 8))*10;
        
        switch(part){
            case "Main":
                GL11.glPushMatrix();
                texture.bindTexture(TextureMaps.MODEL_ATOMIC_CATALYST_MAIN);
                modelAtomicCatalyst.renderPart("Main");
                GL11.glPopMatrix();
                break;
            case "Electron1":
                GL11.glPushMatrix();
                GL11.glScalef(0.4F, 0.4F, 0.4F);
                GL11.glTranslatef(x - sinDis, y - 0.5F, z - cosDis);
                texture.bindTexture(TextureMaps.MODEL_ATOMIC_CATALYST_ELECTRON_1);
                modelAtomicCatalyst.renderPart(part);                
                GL11.glPopMatrix();
                break;
            case "Electron2":
                GL11.glPushMatrix();
                GL11.glScalef(0.4F, 0.4F, 0.4F);
                GL11.glTranslatef(x - sinDis, y - cosDis - 0.5F, z);
                texture.bindTexture(TextureMaps.MODEL_ATOMIC_CATALYST_ELECTRON_2);
                modelAtomicCatalyst.renderPart(part);
                GL11.glPopMatrix();
                break;
            case "Electron3":
                GL11.glPushMatrix();
                GL11.glScalef(0.4F, 0.4F, 0.4F);
                GL11.glTranslatef(x - sinDis, y - sinDis - 0.5F, z - cosDis);
                texture.bindTexture(TextureMaps.MODEL_ATOMIC_CATALYST_ELECTRON_3);
                modelAtomicCatalyst.renderPart(part);
                GL11.glPopMatrix();
                break;
            default:
                break;
        }
        
        
        
        
    }

}
