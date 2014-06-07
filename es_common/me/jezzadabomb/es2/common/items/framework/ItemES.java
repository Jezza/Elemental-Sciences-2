package me.jezzadabomb.es2.common.items.framework;

import java.util.ArrayList;
import java.util.List;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemES extends Item {

    public ItemES(String name) {
        setUnlocalizedName(name);
        setTextureName(name);
        setCreativeTab(ElementalSciences2.creativeTab);
        register(name);
    }

    public void register(String name) {
        GameRegistry.registerItem(this, name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(Reference.MOD_IDENTIFIER + getIconString());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ItemInformation information = new ItemInformation();
        addInformation(stack, player, information);
        information.addToList(list);
    }

    protected abstract void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information);
}
