package me.jezzadabomb.es2.common.items;

import org.lwjgl.input.Keyboard;

import me.jezzadabomb.es2.common.items.framework.ArmourSlotIndex;
import me.jezzadabomb.es2.common.items.framework.ItemArmourES;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHoverBoots extends ItemArmourES {
    public ItemHoverBoots(int id, EnumArmorMaterial armorMaterial, int renderIndex, ArmourSlotIndex armourIndex, String name, String textureLocation) {
        super(id, armorMaterial, renderIndex, armourIndex, name, textureLocation);
    }
}
