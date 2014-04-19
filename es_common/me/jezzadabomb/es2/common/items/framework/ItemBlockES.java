package me.jezzadabomb.es2.common.items.framework;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.common.blocks.framework.BlockMetaHolder;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public abstract class ItemBlockES extends ItemBlock {

    private Block block;

    public ItemBlockES(Block block) {
        super(block);
        this.block = block;
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return block.getIcon(2, damage);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        if (!(block instanceof BlockMetaHolder))
            super.getUnlocalizedName(itemStack);
        String[] names = ((BlockMetaHolder) block).getNames();
        return "item." + names[MathHelper.clipInt(itemStack.getItemDamage(), names.length - 1)];
    }

}
