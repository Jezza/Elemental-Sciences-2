package me.jezzadabomb.es2.common.items.framework;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.core.utils.MovingObjectPositionUtil;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public abstract class ItemES extends Item {

    private String donator = null;

    private boolean notifyRightClick = false;
    private boolean notifyLeftClick = false;

    public ItemES(String name) {
        setName(name);
        setCreativeTab(ElementalSciences2.creativeTab);
        register(name);
    }

    public ItemES register(String name) {
        GameRegistry.registerItem(this, name);
        return this;
    }

    public ItemES setName(String name) {
        setUnlocalizedName(name);
        setTextureName(name);
        return this;
    }

    public ItemES setClickableRight() {
        notifyRightClick = true;
        return this;
    }

    public ItemES setClickableLeft() {
        notifyLeftClick = true;
        return this;
    }

    public ItemES setClickable() {
        notifyRightClick = true;
        notifyLeftClick = true;
        return this;
    }

    public ItemES setDonatorItem(String donator) {
        this.donator = donator;
        return this;
    }

    public ItemES setShapelessRecipe(Object... items) {
        return setShapelessRecipe(1, items);
    }

    public ItemES setShapelessRecipe(int resultSize, Object... items) {
        return setShapelessRecipe(resultSize, 0, items);
    }

    public ItemES setShapelessRecipe(int resultSize, int meta, Object... items) {
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(this, resultSize, meta), items);
        return this;
    }

    @SideOnly(Side.CLIENT)
    public boolean isDonator() {
        return donator != null && RenderUtils.isPlayerRendering(donator);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (notifyRightClick)
            return onItemRightClick(itemStack, world, player, MovingObjectPositionUtil.getCurrentMovingObjectPosition(player));
        return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entity, ItemStack itemStack) {
        if (notifyLeftClick)
            return onItemLeftClick(itemStack, entity.worldObj, entity, MovingObjectPositionUtil.getCurrentMovingObjectPosition(entity));
        return super.onEntitySwing(entity, itemStack);
    }

    /**
     * Called whenever the item is right clicked.
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop) {
        return itemStack;
    }

    /**
     * Return true to stop all further processing.
     */
    public boolean onItemLeftClick(ItemStack itemStack, World world, EntityLivingBase player, MovingObjectPosition mop) {
        return false;
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
        if (isDonator())
            information.addShiftList("This item was made thanks to you.");
        information.addToList(list);
    }

    protected void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
    }
}
