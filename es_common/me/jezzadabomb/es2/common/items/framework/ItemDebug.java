package me.jezzadabomb.es2.common.items.framework;

import java.util.ArrayList;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.interfaces.IMasterable;
import me.jezzadabomb.es2.common.core.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.core.utils.Identifier;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderCore;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
        else
            return onItemDebugUse(itemStack, player, world, x, y, z, sideHit, hitVecX, hitVecY, hitVecZ, Side.SERVER);
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {
        if (world.isRemote)
            return onItemDebugUseFirst(itemStack, player, world, x, y, z, sideHit, hitVecX, hitVecY, hitVecZ, Side.CLIENT);
        else
            return onItemDebugUseFirst(itemStack, player, world, x, y, z, sideHit, hitVecX, hitVecY, hitVecZ, Side.SERVER);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void addInformation(EntityPlayer player, ItemStack stack) {
        addToBothLists("Debug Mode: " + debugMode);
        addToBothLists(getDebugString());
    }

    public abstract ArrayList<String> getDebugList();

    public abstract boolean onItemDebugUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ, Side side);

    public abstract boolean onItemDebugUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ, Side side);

    public abstract void onDebugModeSwitch(ItemStack itemStack, World world, EntityPlayer player);
}
