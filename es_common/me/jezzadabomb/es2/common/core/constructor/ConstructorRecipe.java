package me.jezzadabomb.es2.common.core.constructor;

import net.minecraft.item.ItemStack;

public class ConstructorRecipe {

    ItemStack result;
    float atomicMatter;

    public ConstructorRecipe(ItemStack result, float atomicMatter) {
        this.result = result;
        this.atomicMatter = atomicMatter;
    }

    public ItemStack getResult() {
        return result;
    }

    public float getAtomicMatter() {
        return atomicMatter;
    }
}
