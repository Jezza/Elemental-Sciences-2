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
                renderItem(-0.2F, -0.1F, 0.2F, 0.25F);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderItem(0.2F, 0.2F, 0.8F, 0.43F);
                break;
            case INVENTORY:
                renderItem(-1.1F, -1F, 0.0F, 0.6F);
                break;
            case EQUIPPED:
                renderItem(0F, 0.0F, 0.6F, 0.4F);
                break;
            default:
                break;
        }
    }

    @Override
    public void renderModel() {
        mainModel.renderPart("SupportPosXPosZ");
    }

}
