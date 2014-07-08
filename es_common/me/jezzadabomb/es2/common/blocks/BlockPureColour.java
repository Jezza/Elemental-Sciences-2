package me.jezzadabomb.es2.common.blocks;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.blocks.framework.BlockESMeta;
import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.items.framework.ItemBlockES;
import me.jezzadabomb.es2.common.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockPureColour extends BlockESMeta {

    private static ArrayList<String> names = new ArrayList<String>() {
        {
            String temp = Strings.PURE_COLOUR + "_";
            add(temp + "black");
            add(temp + "red");
            add(temp + "green");
            add(temp + "brown");
            add(temp + "blue");
            add(temp + "purple");
            add(temp + "cyan");
            add(temp + "silver");
            add(temp + "gray");
            add(temp + "pink");
            add(temp + "lime");
            add(temp + "lightBlue");
            add(temp + "magenta");
            add(temp + "orange");
            add(temp + "white");
        }
    };

    public BlockPureColour(Material material, String name) {
        super(material, name);
    }

    @Override
    public ArrayList<String> getNames() {
        return names;
    }

    @Override
    protected Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockPureColour.class;
    }

    @Override
    public String getTextureSubDirectory() {
        return "colour";
    }

    public static class ItemBlockPureColour extends ItemBlockES {

        public ItemBlockPureColour(Block block) {
            super(block);
        }

        @Override
        protected void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
            information.addToBothLists("Nano-Constructed blocks.");
            information.defaultInfoList();
            information.addShiftList("Made out of the best materials");
            information.addShiftList("for the best colour for you.");
        }
    }
}
