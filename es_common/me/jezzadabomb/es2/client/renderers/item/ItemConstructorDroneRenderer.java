package me.jezzadabomb.es2.client.renderers.item;

import me.jezzadabomb.es2.client.models.drones.ModelConstructorDrone;
import me.jezzadabomb.es2.client.renderers.ItemRendererAbstract;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemConstructorDroneRenderer extends ItemRendererAbstract {

    public ItemConstructorDroneRenderer() {
        super(new ModelConstructorDrone(), TextureMaps.CONSTRUCTOR_DRONE);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY:
                renderItem(0.0F, 0.2F, 0.0F, 0.3F);
                break;
            case EQUIPPED:
                renderItem(0.5F, 0.5F, 0.5F, 0.4F);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderItem(0.3F, 0.8F, 0.4F, 0.4F);
                break;
            case INVENTORY:
                renderItem(0.0F, 0.0F, 0.0F, 0.5F);
                break;
            default:
                break;
        }
    }

}
