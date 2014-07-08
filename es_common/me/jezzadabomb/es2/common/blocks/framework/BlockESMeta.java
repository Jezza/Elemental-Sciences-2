package me.jezzadabomb.es2.common.blocks.framework;

import java.util.List;

import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import me.jezzadabomb.es2.common.items.framework.ItemBlockES;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockESMeta extends BlockES {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockESMeta(Material material, String name) {
        super(material, name);
    }

    @Override
    public BlockESMeta register(String name) {
        GameRegistry.registerBlock(this, getItemBlockClass(), name);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        List<String> names = getNames();
        for (int i = 0; i < names.size(); i++)
            list.add(new ItemStack(item, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        List<String> names = getNames();
        icons = new IIcon[names.size()];

        // TODO Remove all this shit.
        StringBuilder registryBuilder = new StringBuilder(Reference.MOD_IDENTIFIER);
        String subDirectory = getTextureSubDirectory();
        if (subDirectory != null)
            registryBuilder.append(subDirectory + "/");
        String registryString = registryBuilder.toString();

        for (int i = 0; i < icons.length; i++)
            icons[i] = iconRegister.registerIcon(registryString + names.get(MathHelper.clipInt(i, names.size())));
    }

    @Override
    public int damageDropped(int damage) {
        return damage;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return icons[meta % icons.length];
    }

    protected Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockES.class;
    }

    public String getTextureSubDirectory() {
        return null;
    }

    public abstract List<String> getNames();
}
