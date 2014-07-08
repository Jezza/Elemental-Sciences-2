package me.jezzadabomb.es2.client.renderers.item;

import me.jezzadabomb.es2.client.models.ModelIronBar;
import me.jezzadabomb.es2.client.renderers.ItemRendererAbstract;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.item.ItemStack;

public class ItemStrengthenedIronBarRenderer extends ItemRendererAbstract {

    public ItemStrengthenedIronBarRenderer() {
        super(new ModelIronBar(), TextureMaps.ATOMIC_CONSTRUCTOR);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY:
                renderItem(0.0F, 1.5F, 0.0F, 1.0F);
                break;
            case EQUIPPED:
                renderItem(-0.4F, 3F, -0.3F, 2F);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderItem(0.5F, 2.0F, 0.5F, 1.5F);
                break;
            case INVENTORY:
                renderItem(0.0F, 1.5F, 0.1F, 1.5F);
                break;
            default:
                break;
        }
    }

}
