package me.jezzadabomb.es2.client.renderers.item;

import static org.lwjgl.opengl.GL11.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import me.jezzadabomb.es2.client.models.drones.ModelConstructorDrone;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public class ItemPlaceHolderRenderer implements IItemRenderer {

    ModelConstructorDrone modelDrone;

    public ItemPlaceHolderRenderer() {
        modelDrone = new ModelConstructorDrone();
    }

    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        return ModItems.isPlaceHolderStack(stack, "constructorDrone");
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper) {
        return ModItems.isPlaceHolderStack(stack, "constructorDrone");
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
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
    }

    public void renderItemDrone(float x, float y, float z, float scale, ItemRenderType type) {
        glPushMatrix();

        glTranslatef(x, y, z);

        glScalef(scale, scale, scale);

        RenderUtils.bindTexture(TextureMaps.CONSTRUCTOR_DRONE);
        modelDrone.render();

        glPopMatrix();
    }
}
