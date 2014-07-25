package me.jezzadabomb.es2.common.items.framework;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.common.blocks.framework.BlockESMeta;
import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemBlockES extends ItemBlock {

    public ItemBlockES(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public Block getBlock(){
        return field_150939_a;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return getBlock().getIcon(2, damage);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        if (!(getBlock() instanceof BlockESMeta))
            return super.getUnlocalizedName(itemStack);
        String[] names = ((BlockESMeta) getBlock()).getNames().toArray(new String[0]);
        return "item." + names[itemStack.getItemDamage() & names.length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        ItemInformation information = new ItemInformation();
        addInformation(stack, player, information);
        information.addToList(list);
    }

    protected void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
    }
}
