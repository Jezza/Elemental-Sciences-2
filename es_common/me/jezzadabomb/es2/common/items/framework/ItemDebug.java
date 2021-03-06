package me.jezzadabomb.es2.common.items.framework;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.utils.ItemInformation;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;

public abstract class ItemDebug extends ItemES {

    public int debugMode;

    public ItemDebug(String name) {
        super(name);
        setMaxStackSize(1);
        debugMode = 0;
    }

    public boolean isDebugMode(String mode) {
        return debugMode == getDebugPos(mode);
    }

    public boolean debugModeStartsWith(String text) {
        return getDebugList().get(debugMode).startsWith(text);
    }

    public int getDebugPos(String mode) {
        return getDebugList().indexOf(mode);
    }

    private String getDebugString() {
        return getCurrentDebugString() + " Mode";
    }

    public String getCurrentDebugString() {
        return getDebugList().get(debugMode);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote && Reference.isDebugMode) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (++debugMode == getDebugList().size())
                    debugMode = 0;
                Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
                UtilMethods.addChatMessage(player, getDebugString());
            } else {
                onDebugModeSwitch(itemStack, world, player);
            }
        }
        return itemStack;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {
        if (world.isRemote)
            return onItemDebugUse(itemStack, player, world, x, y, z, sideHit, hitVecX, hitVecY, hitVecZ, Side.CLIENT);
        return onItemDebugUse(itemStack, player, world, x, y, z, sideHit, hitVecX, hitVecY, hitVecZ, Side.SERVER);
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {
        if (world.isRemote)
            return onItemDebugUseFirst(itemStack, player, world, x, y, z, sideHit, hitVecX, hitVecY, hitVecZ, Side.CLIENT);
        return onItemDebugUseFirst(itemStack, player, world, x, y, z, sideHit, hitVecX, hitVecY, hitVecZ, Side.SERVER);
    }

    @Override
    protected void addInformation(ItemStack stack, EntityPlayer player, ItemInformation information) {
        information.addInfoList("Debug Mode: " + debugMode);
        information.addInfoList(getDebugString());
    }

    public abstract ArrayList<String> getDebugList();

    public abstract boolean onItemDebugUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ, Side side);

    public abstract boolean onItemDebugUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ, Side side);

    public abstract void onDebugModeSwitch(ItemStack itemStack, World world, EntityPlayer player);
}
