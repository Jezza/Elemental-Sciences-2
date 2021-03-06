package me.jezzadabomb.es2.common.items.debug;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.interfaces.IMasterable;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.core.utils.helpers.MathHelper;
import me.jezzadabomb.es2.common.items.framework.ItemDebug;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;

public class ItemDebugTool extends ItemDebug {

    public boolean canFlood;
    private CoordSet hitBlock;

    private static ArrayList<String> debugList = new ArrayList<String>() {
        {
            add("Normal Debug"); // 0
            add("Y-Axis Toggle"); // 3
            add("Constructor - Set Pos"); // 4
            add("Constructor - Get Pos"); // 5
            add("Constructor - Get Nearby Count"); // 6
            add("Constructor - 3x3x3"); // 7
            add("Drone Bay - Door control"); // 8
            add("Drone Bay - Spawn Drone"); // 9
            add("Console - Set master"); // 10
            add("Console - Send To"); // 11
            add("Quantum - Give Life Coin"); // 12
            add("IMasterable - Locate Master"); // 13
        }
    };

    public ItemDebugTool(String name) {
        super(name);
    }

    @Override
    public ArrayList<String> getDebugList() {
        return debugList;
    }

    @Override
    public boolean onItemDebugUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ, Side side) {
        CoordSet coordSet = new CoordSet(x, y, z);

        if (isDebugMode("IMasterable - Locate Master")) {
            if (coordSet.isMasterable(world)) {
                IMasterable masterable = (IMasterable) coordSet.getTileEntity(world);
                if (masterable.hasMaster()) {
                    UtilMethods.addChatMessage(player, "Found: " + masterable.getMaster().toString());
                } else {
                    UtilMethods.addChatMessage(player, "No Master Found.");
                }
            }
        }

        if (side.isServer()) {
            if (coordSet.isDroneBay(world)) {
                if (isDebugMode("Drone Bay - Door control")) {
                    TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x, y, z);
                    droneBay.toggleDoor();
                }

                if (isDebugMode("Drone Bay - Spawn Drone")) {
                    TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x, y, z);
                    droneBay.addDroneToSpawnList(1, null);
                }
            }
        }

        if (side.isClient()) {
            if (coordSet.isConstructor(world)) {
                if (isDebugMode("Constructor - Get Nearby Count")) {
                    ArrayList<TileAtomicConstructor> tempList = new ArrayList<TileAtomicConstructor>();
                    for (int i = -1; i <= 1; i++)
                        for (int j = -1; j <= 1; j++)
                            for (int k = -1; k <= 1; k++) {
                                if (!(i == 0 && j == 0 && k == 0) && new CoordSet(x + i, y + j, z + k).isConstructor(world))
                                    tempList.add((TileAtomicConstructor) world.getTileEntity(x + i, y + j, z + k));
                            }
                    UtilMethods.addChatMessage(player, "" + tempList.size());
                }

                if (isDebugMode("Constructor - Set pos")) {
                    CoordSet tempBlock = new CoordSet(x, y, z);
                    if (hitBlock != null && hitBlock.equals(tempBlock)) {
                        UtilMethods.addChatMessage(player, "Already set to this block.");
                        return true;
                    }
                    hitBlock = tempBlock;
                    UtilMethods.addChatMessage(player, "Set New Constructor @ " + hitBlock);
                }

                if (isDebugMode("Constructor - Get pos")) {
                    if (hitBlock == null)
                        return true;
                    if (hitBlock.equals(new CoordSet(x, y, z))) {
                        UtilMethods.addChatMessage(player, "Same block");
                        return true;
                    }
                    if (!(MathHelper.withinRange(x, hitBlock.getX(), 1) && MathHelper.withinRange(y, hitBlock.getY(), 1) && MathHelper.withinRange(z, hitBlock.getZ(), 1))) {
                        UtilMethods.addChatMessage(player, "Not within range of block");
                        return true;
                    }

                    int index = 0;
                    for (int i = -1; i <= 1; i++)
                        for (int j = -1; j <= 1; j++)
                            for (int k = -1; k <= 1; k++) {
                                if (i == 0 && j == 0 && k == 0)
                                    continue;
                                if (hitBlock.getX() + i == x && hitBlock.getY() + j == y && hitBlock.getZ() + k == z) {
                                    UtilMethods.addChatMessage(player, "Pos in array: " + index);
                                    return true;
                                }
                                index++;
                            }

                    UtilMethods.addChatMessage(player, "Error");
                }
            }
        }
        return false;
    }

    @Override
    public void onDebugModeSwitch(ItemStack itemStack, World world, EntityPlayer player) {
        switch (debugMode) {
            case 3:
                canFlood = !canFlood;
                player.addChatMessage(new ChatComponentText((canFlood ? "Enabled" : "Disabled") + " Debug Flooding."));
                break;
            case 4:
                boolean temp = Reference.HUD_VERTICAL_ROTATION;
                Reference.HUD_VERTICAL_ROTATION = !temp;
                player.addChatMessage(new ChatComponentText("HUD Rotation: " + !temp));
                break;
            case 12:
                player.inventory.addItemStackToInventory(new ItemStack(ModItems.lifeCoin));
                break;
        }
    }

    @Override
    public boolean onItemDebugUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ, Side side) {
        return false;
    }

}
