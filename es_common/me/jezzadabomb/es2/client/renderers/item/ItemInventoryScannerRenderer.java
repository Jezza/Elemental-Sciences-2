package me.jezzadabomb.es2.client.renderers.item;

import me.jezzadabomb.es2.client.models.ModelInventoryScanner;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import static org.lwjgl.opengl.GL11.*;

public class ItemInventoryScannerRenderer implements IItemRenderer {

    private ModelInventoryScanner modelInventoryScanner;
    private int renderType = -1;

    public ItemInventoryScannerRenderer() {
        modelInventoryScanner = new ModelInventoryScanner();
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
        switch (type) {
        case ENTITY: {
            renderType = 1;
            renderInventoryScanner(0F, 1F, 0F, 0.6F);
            break;
        }
        case EQUIPPED_FIRST_PERSON: {
            renderType = 2;
            renderInventoryScanner(0F, 1.0F, 0.4F, 0.6F);
            break;
        }
        case INVENTORY: {
            renderType = 3;
            renderInventoryScanner(0F, 0.1F, 0.0F, 0.7F);
            break;
        }
        case EQUIPPED: {
            renderType = 4;
            renderInventoryScanner(0.1F, 0.5F, 0.4F, 0.8F);
            break;
        }
        default:
            return;
        }
        renderType = -1;
    }

    private void renderInventoryScanner(float x, float y, float z, float scale) {
        if (renderType == -1)
            return;

        TextureManager texture = Minecraft.getMinecraft().renderEngine;

        glPushMatrix();
        glDisable(GL_LIGHTING);

        // Translate and scale whole model.
        glTranslatef(x, y, z);
        glScalef(scale, scale, scale);

        // Translate individual parts
        translateBindRender("Base", x, y, z);

        glPushMatrix();
        glTranslated(0.0D, 0.04D, 0.0D);
        glScaled(0.5D, 0.9D, 0.5D);
        translateBindRender("Base", x, y, z);
        glPopMatrix();

        glPushMatrix();
        if (renderType == 1)
            glTranslated(-0.4D, 0.0D, 0.0D);
        if (renderType == 3)
            glTranslated(-0.3D, 0.8D, 0.0D);
        if (renderType == 4)
            glTranslated(0.0D, 0.4D, 0.0D);

        translateBindRender("Spinner", x, y, z);
        glPopMatrix();

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    private void translateBindRender(String part, float x, float y, float z) {
        switch (part) {
        case "Base":
            glPushMatrix();
            RenderUtils.bindTexture(TextureMaps.INVENTORY_SCANNER_BASE_UPPER);
            modelInventoryScanner.renderPart(part);
            glPopMatrix();
            break;
        case "Spinner":
            glPushMatrix();
            glRotated(-90, 0.0D, 1.0D, 0.0D);
            glTranslatef(x, y - 1.2F, z - 0.4F);
            glScaled(0.9D, 0.5D, 0.3D);
            RenderUtils.bindTexture(TextureMaps.INVENTORY_SCANNER_SPINNER);
            modelInventoryScanner.renderPart(part);
            glPopMatrix();
            break;
        default:
            break;
        }

    }

}
