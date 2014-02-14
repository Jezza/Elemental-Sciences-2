package me.jezzadabomb.es2.common.items;

import me.jezzadabomb.es2.common.items.framework.ArmourSlotIndex;
import me.jezzadabomb.es2.common.items.framework.ItemArmourES;

public class ItemHoverBoots extends ItemArmourES {
    public ItemHoverBoots(ArmorMaterial armorMaterial, int renderIndex, ArmourSlotIndex armourIndex, String name, String textureLocation) {
        super(armorMaterial, renderIndex, armourIndex, name, textureLocation);
    }
    
    @Override
    protected void addInformation() { 
        addToBothLists("Very much a WIP");
    }
}
