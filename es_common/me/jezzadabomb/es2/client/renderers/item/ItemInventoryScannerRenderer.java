package me.jezzadabomb.es2.client.renderers.item;

import me.jezzadabomb.es2.client.models.ModelInventoryScanner;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;

import static org.lwjgl.opengl.GL11.*;

public class ItemInventoryScannerRenderer implements IItemRenderer {
    private ModelInventoryScanner modelInventoryScanner;

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
            renderInventoryScanner(0.0F, 1.2F, 0.0F, 1.0F);
            break;
        }
        case EQUIPPED_FIRST_PERSON: {
            renderInventoryScanner(0.4F, 2.0F, 0.5F, 1.0F);
            break;
        }
        case INVENTORY: {
            renderInventoryScanner(0.0F, 1.0F, 0.0F, 1.0F);
            break;
        }
        case EQUIPPED: {
            renderInventoryScanner(0.4F, 2.0F, 0.4F, 1.0F);
            break;
        }
        default:
            return;
        }
    }

    private void renderInventoryScanner(float x, float y, float z, float scale) {
        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef(x, y, z);

        glRotatef(180F, 1.0F, 0.0F, 0.0F);

        glScalef(scale, scale, scale);

        RenderUtils.bindTexture(TextureMaps.INVENTORY_SCANNER);

        modelInventoryScanner.render();

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

}
