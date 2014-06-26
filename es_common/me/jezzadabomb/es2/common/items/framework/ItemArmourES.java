package me.jezzadabomb.es2.common.items.framework;

import java.util.List;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemArmourES extends ItemArmor {

    String textureLocation;
    int slot;

    public ItemArmourES(ArmorMaterial armorMaterial, int renderIndex, ArmourSlotIndex armourIndex, String name, String textureLocation) {
        super(armorMaterial, renderIndex, armourIndex.ordinal());
        slot = armourIndex.ordinal();
        this.textureLocation = textureLocation;
        setCreativeTab(ElementalSciences2.creativeTab);
        setMaxDamage(0);
        setName(name);
        register(name);
    }

    public void register(String name) {
        GameRegistry.registerItem(this, name);
    }

    public void setName(String name) {
        setUnlocalizedName(name);
        setTextureName(name);
    }

    @Override
    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
        return armorType == slot;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return false;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return textureLocation;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(Reference.MOD_IDENTIFIER + getIconString());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ItemInformation information = new ItemInformation();
        addInformation(information);
        information.addToList(list);
    }

    public void addInformation(ItemInformation information) {
    }

    public enum ArmourSlotIndex {
        HEAD, CHESTPLATE, LEGGINGS, BOOTS;
    }
}
