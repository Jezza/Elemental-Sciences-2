package me.jezzadabomb.es2.common.core.constructor;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class ConstructorRecipeHandler {

    private static ArrayList<ConstructorRecipe> recipeList = new ArrayList<ConstructorRecipe>();

    public static ConstructorRecipe addRecipe(ItemStack itemStack, int atomicMatter) {
        return addRecipe(new ConstructorRecipe(itemStack, atomicMatter));
    }

    public static ConstructorRecipe addRecipe(ConstructorRecipe constructorRecipe) {
        recipeList.add(constructorRecipe);
        return constructorRecipe;
    }

    public static ArrayList<ConstructorRecipe> getRecipeList() {
        return recipeList;
    }

}
