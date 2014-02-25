package me.jezzadabomb.es2.client.renderers.item;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.ModelAtomicConstructor;
import me.jezzadabomb.es2.client.models.ModelConstructorDrone;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.items.ItemPlaceHolder;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemPlaceHolderRenderer implements IItemRenderer {

    ModelConstructorDrone modelDrone;
    ModelAtomicConstructor modelAtomic;

    public ItemPlaceHolderRenderer() {
        modelDrone = new ModelConstructorDrone();
        modelAtomic = new ModelAtomicConstructor();
    }

    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        return ModItems.isPlaceHolderStack("constructorDrone", stack, true) || ModItems.isPlaceHolderStack("atomicFrame", stack, true);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper) {
        return ModItems.isPlaceHolderStack("constructorDrone", stack, true) || ModItems.isPlaceHolderStack("atomicFrame", stack, true);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        if (ModItems.isPlaceHolderStack("constructorDrone", stack, true)) {
            switch (type) {
                case ENTITY:
                    renderItemDrone(0.0F, 0.2F, 0.0F, 0.3F, type);
                    break;
                case EQUIPPED:
                    renderItemDrone(0.5F, 0.5F, 0.5F, 0.4F, type);
                    break;
                case EQUIPPED_FIRST_PERSON:
                    renderItemDrone(0.3F, 0.8F, 0.4F, 0.4F, type);
                    break;
                case INVENTORY:
                    renderItemDrone(0.0F, 0.0F, 0.0F, 0.5F, type);
                    break;
                default:
                    break;
            }
        } else if (ModItems.isPlaceHolderStack("atomicFrame", stack, true)) {
            switch (type) {
                case ENTITY: {
                    renderAtomicFrame(0.0F, 0.55F, 0.0F, 0.5F, type);
                    break;
                }
                case EQUIPPED_FIRST_PERSON: {
                    renderAtomicFrame(0.4F, 1.5F, 0.5F, 1.0F, type);
                    break;
                }
                case INVENTORY: {
                    renderAtomicFrame(0.0F, 1.0F, 0.0F, 1.0F, type);
                    break;
                }
                case EQUIPPED: {
                    renderAtomicFrame(0.4F, 1.5F, 0.4F, 1.0F, type);
                    break;
                }
                default:
                    return;
            }
        }
    }

    public void renderItemDrone(float x, float y, float z, float scale, ItemRenderType type) {
        glPushMatrix();

        glTranslatef(x, y, z);

        glScalef(scale, scale, scale);

        RenderUtils.bindTexture(TextureMaps.CONSTRUCTOR_DRONE);
        modelDrone.render();

        glPopMatrix();
    }

    public void renderAtomicFrame(float x, float y, float z, float scale, ItemRenderType type) {
        glPushMatrix();
        glDisable(GL_LIGHTING);
        
        glTranslatef(x, y, z);

        glRotated(180.0D, 1.0D, 0.0D, 0.0D);

        glScalef(scale, scale, scale);

        RenderUtils.bindTexture(TextureMaps.ATOMIC_CONSTRUCTOR);
        modelAtomic.renderFrame();

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

}
