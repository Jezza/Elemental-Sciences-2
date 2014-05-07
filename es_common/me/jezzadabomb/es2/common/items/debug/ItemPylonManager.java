package me.jezzadabomb.es2.common.items.debug;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.IPylonRegistry;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.items.framework.ItemDebug;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;

public class ItemPylonManager extends ItemDebug {

    private static ArrayList<String> debugList = new ArrayList<String>() {
        {
            add("Field Check");
        }
    };

    public ItemPylonManager(String name) {
        super(name);
    }

    @Override
    public ArrayList<String> getDebugList() {
        return debugList;
    }

    @Override
    public boolean onItemDebugUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ, Side side) {
        if (isDebugMode("Field Check")) {
            int tier = IPylonRegistry.isPowered(world, x, y, z);
            if (tier >= 0)
                UtilMethods.addChatMessage(player, "Pylon found: " + tier);
            else
                UtilMethods.addChatMessage(player, "No pylon found");
        }
        return false;
    }

    @Override
    public boolean onItemDebugUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ, Side side) {
        return false;
    }

    @Override
    public void onDebugModeSwitch(ItemStack itemStack, World world, EntityPlayer player) {
    }
}
