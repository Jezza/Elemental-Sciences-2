package me.jezzadabomb.es2.client.renderers.item;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.ModelAtomicConstructor;
import me.jezzadabomb.es2.client.models.ModelIronBar;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemPlaceHolder64Renderer implements IItemRenderer {

    ModelIronBar modelIronBar;

    public ItemPlaceHolder64Renderer() {
        modelIronBar = new ModelIronBar();
    }

    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        return ModItems.isPlaceHolderStack("strengthenedIronBar", stack, true);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper) {
        return ModItems.isPlaceHolderStack("strengthenedIronBar", stack, true);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        switch (type) {
            case ENTITY:
                renderStrengthenedIronBar(0.0F, 1.5F, 0.0F, 1.0F, type);
                break;
            case EQUIPPED:
                renderStrengthenedIronBar(-0.4F, 3F, -0.3F, 2F, type);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderStrengthenedIronBar(0.5F, 2.0F, 0.5F, 1.5F, type);
                break;
            case INVENTORY:
                renderStrengthenedIronBar(0.0F, 1.5F, 0.1F, 1.5F, type);
                break;
            default:
                break;
        }
    }

    public void renderStrengthenedIronBar(float x, float y, float z, float scale, ItemRenderType type) {

        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef(x, y, z);

        glRotated(180.0D, 1.0D, 0.0D, 0.0D);
        if (type == type.EQUIPPED)
            glRotated(-40.0D, 1.0D, 0.0D, 1.0D);

        glScalef(scale, scale, scale);

        RenderUtils.bindTexture(TextureMaps.ATOMIC_CONSTRUCTOR);

        modelIronBar.render();

        glEnable(GL_LIGHTING);
        glPopMatrix();

    }
}
