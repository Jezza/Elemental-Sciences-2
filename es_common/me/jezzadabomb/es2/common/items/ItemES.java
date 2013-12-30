package me.jezzadabomb.es2.common.items;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemES extends Item {

    ArrayList<String> infoList = new ArrayList<String>();
    ArrayList<String> shiftList = new ArrayList<String>();

    public ItemES(int id, String name) {
        super(id);
        setUnlocalizedName(name);
        setCreativeTab(ElementalSciences2.creativeTab);
        register(name);
    }

    public void register(String name) {
        GameRegistry.registerItem(this, name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister) {
        this.itemIcon = par1IconRegister.registerIcon(Reference.MOD_ID + ":" + this.getUnlocalizedName().replace("item.", ""));
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        shiftList.clear();
        infoList.clear();
        addInformation();
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.addAll(shiftList);
        } else {
            list.addAll(infoList);
        }
    }

    protected void defaultInfoList(){
        infoList.add(EnumChatFormatting.WHITE + "Press" + EnumChatFormatting.DARK_RED + " Shift" + EnumChatFormatting.WHITE + " for more info.");
    }
    
    protected void addInformation() {}
}
