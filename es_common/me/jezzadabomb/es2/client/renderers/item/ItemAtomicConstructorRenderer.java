package me.jezzadabomb.es2.client.renderers.item;

import static org.lwjgl.opengl.GL11.*;
import me.jezzadabomb.es2.client.models.ModelAtomicConstructor;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemAtomicConstructorRenderer implements IItemRenderer {

    ModelAtomicConstructor modelAtomicConstructor;

    public ItemAtomicConstructorRenderer() {
        modelAtomicConstructor = new ModelAtomicConstructor();
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
                renderAtomicConstructor(0.0F, 1.2F, 0.0F, 1.0F);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                renderAtomicConstructor(0.4F, 1.5F, 0.5F, 1.0F);
                break;
            }
            case INVENTORY: {
                renderAtomicConstructor(0.0F, 1.0F, 0.0F, 1.0F);
                break;
            }
            case EQUIPPED: {
                renderAtomicConstructor(0.4F, 1.5F, 0.4F, 1.0F);
                break;
            }
            default:
                return;
        }
    }

    private void renderAtomicConstructor(float x, float y, float z, float scale) {
        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef(x, y, z);

        glRotatef(180F, 1.0F, 0.0F, 0.0F);

        glScalef(scale, scale, scale);

        RenderUtils.bindTexture(TextureMaps.ATOMIC_CONSTRUCTOR);

        modelAtomicConstructor.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

}
