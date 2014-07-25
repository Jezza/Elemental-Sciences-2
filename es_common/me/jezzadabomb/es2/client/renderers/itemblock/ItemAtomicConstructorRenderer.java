package me.jezzadabomb.es2.client.renderers.itemblock;

import me.jezzadabomb.es2.client.models.ModelAtomicConstructor;
import me.jezzadabomb.es2.client.renderers.ItemRendererAbstract;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.item.ItemStack;

public class ItemAtomicConstructorRenderer extends ItemRendererAbstract {

    public ItemAtomicConstructorRenderer() {
        super(new ModelAtomicConstructor(), TextureMaps.ATOMIC_CONSTRUCTOR);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY:
                renderItem(0.0F, -0.1F, 0.0F, 0.5F);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderItem(0.2F, 0.25F, 0.5F, 0.43F);
                break;
            case INVENTORY:
                renderItem(0.0F, -0.5F, 0.0F, 0.5F);
                break;
            case EQUIPPED:
                renderItem(0.35F, 0.0F, 0.35F, 0.4F);
                break;
            default:
                break;
        }
    }
}
