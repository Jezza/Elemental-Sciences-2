package me.jezzadabomb.es2.client.renderers.item;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.ModelDroneBay;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemDroneBayRenderer implements IItemRenderer {

    ModelDroneBay droneBay;

    public ItemDroneBayRenderer() {
        droneBay = new ModelDroneBay();
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
            case ENTITY:
                renderDroneBay(0.0F, 0.0F, 0.0F, 0.8F);
                break;
            case EQUIPPED:
                renderDroneBay(0.35F, 0.5F, 0.35F, 0.8F);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderDroneBay(0.5F, 0.7F, 0.5F, 0.8F);
                break;
            case INVENTORY:
                renderDroneBay(0.0F, 0.0F, 0.0F, 0.75F);
                break;
            default:
                break;

        }
    }

    public void renderDroneBay(float x, float y, float z, float scale) {
        glPushMatrix();

        glDisable(GL_CULL_FACE);

        glTranslatef(x, y, z);
        glScalef(scale, scale, scale);

        RenderUtils.bindTexture(TextureMaps.DRONE_BAY_DOOR);

        droneBay.renderAllDoors();

        RenderUtils.bindTexture(TextureMaps.DRONE_BAY_FRAME);

        droneBay.renderPart("BaseFrame");

        glEnable(GL_CULL_FACE);
        glPopMatrix();
    }

}
