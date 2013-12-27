package me.jezzadabomb.es2.common.api;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RenderBlackList {

    public static ArrayList<Item> renderBlackList = new ArrayList<Item>();

    public static boolean addItemToRenderBlackList(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        Item tempItem = itemStack.getItem();
        if (!renderBlackList.contains(tempItem)) {
            renderBlackList.add(tempItem);
        }
        return renderBlackList.contains(tempItem);
    }

    public static boolean addItemToRenderBlackList(Item item) {
        if(item == null)
            return false;
        
        if (!renderBlackList.contains(item)) {
            renderBlackList.add(item);
        }
        return renderBlackList.contains(item);
    }
    
    public static boolean addBlockToRenderBlackList(Block block){
        return addItemToRenderBlackList(new ItemStack(block));
    }
    
    public static boolean addBlockToRenderBlackList(Block block, int damage){
        return addItemToRenderBlackList(new ItemStack(block, 0, damage));
    }

    public static ArrayList<Item> getRenderBlackList() {
        return renderBlackList;
    }

    public static void refreshTestList() {
        renderBlackList.clear();
    }

}
