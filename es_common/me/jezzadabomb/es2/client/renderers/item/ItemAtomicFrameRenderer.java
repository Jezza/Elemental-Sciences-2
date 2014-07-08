package me.jezzadabomb.es2.client.renderers.item;

import me.jezzadabomb.es2.client.models.ModelAtomicConstructor;
import me.jezzadabomb.es2.client.renderers.ItemRendererAbstract;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.item.ItemStack;

public class ItemAtomicFrameRenderer extends ItemRendererAbstract {

    public ItemAtomicFrameRenderer() {
        super(new ModelAtomicConstructor(), TextureMaps.ATOMIC_CONSTRUCTOR);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY:
                renderItem(0.0F, 0.55F, 0.0F, 0.5F);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderItem(0F, 0.75F, 0F, 0.5F);
                break;
            case INVENTORY:
                renderItem(0.0F, 0.68F, 0.0F, 0.5F);
                break;
            case EQUIPPED:
                renderItem(0F, 0.7F, 0F, 0.5F);
                break;
            default:
                return;
        }
    }

    @Override
    public void renderModel() {
        mainModel.renderAllExcept("BasePlate");
    }

}
