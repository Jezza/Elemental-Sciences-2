package me.jezzadabomb.es2.common.items.framework;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.common.blocks.framework.BlockESMeta;
import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBlockES extends ItemBlock {

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
        if (!(block instanceof BlockESMeta))
            super.getUnlocalizedName(itemStack);
        String[] names = ((BlockESMeta) block).getNames().toArray(new String[0]);
        return "item." + names[MathHelper.clipInt(itemStack.getItemDamage(), names.length - 1)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ItemInformation information = new ItemInformation();
        addInformation(stack, player, information);
        information.addToList(list);
    }

    protected void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
    };

}
