package me.jezzadabomb.es2.common.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.network.packet.client.ConsoleInfoPacket;
import me.jezzadabomb.es2.common.network.packet.server.InventoryPacket;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import me.jezzadabomb.es2.common.tileentity.TileSolarLens;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDebugTool extends ItemES {

    public int debugMode;
    public boolean canFlood;
    private CoordSet hitBlock;
    private TileConsole tileConsole;

    public ItemDebugTool(String name) {
        super(name);
        setMaxStackSize(1);
        canFlood = false;
        debugMode = 0;
    }

    private static ArrayList<String> debugStringList = new ArrayList<String>() {
        {
            add("Normal Debug"); // 0
            add("Minor Packet Monitoring"); // 1
            add("Extreme Packet Monitoring"); // 2
            add("Y-Axis Toggle"); // 3
            add("Constructor - Set Pos"); // 4
            add("Constructor - Get Pos"); // 5
            add("Constructor - Get Nearby Count"); // 6
            add("Constructor - 3x3x3"); // 7
            add("Drone Bay - Door control"); // 8
            add("Drone Bay - Spawn Drone"); // 9
            add("Console - Set master"); // 10
            add("Console - Info"); // 11
            add("Console - Send To"); // 12
            add("Quantum - Give Life Coin"); // 13
        }
    };

    public boolean getDebugMode(String mode) {
        return debugMode == debugStringList.indexOf(mode);
    }

    public int getDebugPos(String mode) {
        return debugStringList.indexOf(mode);
    }

    private String getDebugString() {
        return debugStringList.get(debugMode) + " Mode";
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (world.isRemote && Reference.isDebugMode) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (++debugMode == debugStringList.size())
                    debugMode = 0;
                Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
                player.addChatMessage(new ChatComponentText(getDebugString()));
            } else {
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
                    case 11:
                        if (tileConsole != null) {
                            PacketDispatcher.sendToServer(new ConsoleInfoPacket(tileConsole));
                        } else {
                            player.addChatMessage(new ChatComponentText("Master not set."));
                        }
                        break;
                    case 13:
                        player.inventory.addItemStackToInventory(ModItems.getPlaceHolderStack("lifeCoin"));
                        break;
                }
            }
        }
        return itemStack;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {

        if (getDebugMode("Console - Send To")) {
            if (tileConsole != null) {
                if (UtilMethods.isConstructor(world, x, y, z))
                    tileConsole.testDatShit((TileAtomicConstructor) world.getTileEntity(x, y, z));
            } else {
                addChatMessage(player, "Need to set master");
            }
        }

        if (isDroneBay(world, x, y, z)) {
            if (getDebugMode("Drone Bay - Door control")) {
                TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x, y, z);
                droneBay.toggleDoor();
            }

            if (getDebugMode("Drone Bay - Spawn Drone")) {
                TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x, y, z);
                droneBay.addDroneToSpawnList(1, null);
            }
        }

        if (getDebugMode("Console - Locate Master")) {
            if (isConstructor(world, x, y, z)) {
                TileAtomicConstructor tAC = (TileAtomicConstructor) world.getTileEntity(x, y, z);
                if (tAC.hasConsole()) {
                    TileConsole tC = tAC.getConsole();
                    addChatMessage(player, "Found: " + new CoordSet(tC.xCoord, tC.yCoord, tC.zCoord));
                } else {
                    addChatMessage(player, "No console found.");
                }
            }
        }

        if (world.isRemote) {
            if (getDebugMode("Console - Set master") && isConsole(world, x, y, z)) {
                tileConsole = (TileConsole) world.getTileEntity(x, y, z);
                addChatMessage(player, "Set Console" + new CoordSet(x, y, z));
            }
            if (isConstructor(world, x, y, z)) {
                if (getDebugMode("Constructor - Get Nearby Count")) {
                    ArrayList<TileAtomicConstructor> tempList = new ArrayList<TileAtomicConstructor>();
                    for (int i = -1; i < 2; i++)
                        for (int j = -1; j < 2; j++)
                            for (int k = -1; k < 2; k++) {
                                if (!(i == 0 && j == 0 && k == 0) && isConstructor(world, x + i, y + j, z + k))
                                    tempList.add((TileAtomicConstructor) world.getTileEntity(x + i, y + j, z + k));
                            }
                    addChatMessage(player, "" + tempList.size());
                }

                if (getDebugMode("Constructor - Set pos")) {
                    CoordSet tempBlock = new CoordSet(x, y, z);
                    if (hitBlock != null && hitBlock.equals(tempBlock)) {
                        addChatMessage(player, "Already set to this block.");
                        return true;
                    }
                    hitBlock = tempBlock;
                    addChatMessage(player, "Set New Constructor @ " + hitBlock);
                }

                if (getDebugMode("Constructor - Get pos")) {
                    if (hitBlock == null)
                        return true;
                    if (hitBlock.equals(new CoordSet(x, y, z))) {
                        addChatMessage(player, "Same block");
                        return true;
                    }
                    if (!(MathHelper.withinRange(x, hitBlock.getX(), 1) && MathHelper.withinRange(y, hitBlock.getY(), 1) && MathHelper.withinRange(z, hitBlock.getZ(), 1))) {
                        addChatMessage(player, "Not within range of block");
                        return true;
                    }

                    int index = 0;
                    for (int i = -1; i < 2; i++)
                        for (int j = -1; j < 2; j++)
                            for (int k = -1; k < 2; k++) {
                                if (i == 0 && j == 0 && k == 0)
                                    continue;
                                if (hitBlock.getX() + i == x && hitBlock.getY() + j == y && hitBlock.getZ() + k == z) {
                                    addChatMessage(player, "Pos in array: " + index);
                                    return true;
                                }
                                index++;
                            }

                    addChatMessage(player, "Error");
                }
            }

            if (getDebugMode("Minor Packet Monitoring")) {
                InventoryPacket packet = ClientProxy.getHUDRenderer().getPacket(x, y, z);
                if (packet != null) {
                    addChatMessage(player, "");
                    addChatMessage(player, "Contents: ");
                    String tempString = packet.getItemStacksInfo();
                    if (tempString == null)
                        return false;
                    int lastIndex = 0;
                    for (int i = 0; i < tempString.length(); i++) {
                        if (tempString.charAt(i) == ',') {
                            addChatMessage(player, tempString.substring(lastIndex, i));
                            lastIndex = i + 1;
                        }
                    }
                }
                return packet != null;
            }
        }
        return false;
    }

    private void addChatMessage(EntityPlayer player, String string) {
        UtilMethods.addChatMessage(player, string);
    }

    private boolean isSolarLens(World world, int x, int y, int z) {
        return world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileSolarLens;
    }

    private boolean isConsole(World world, int x, int y, int z) {
        return world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileConsole;
    }

    private boolean isConstructor(World world, int x, int y, int z) {
        return world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileAtomicConstructor;
    }

    private boolean isDroneBay(World world, int x, int y, int z) {
        return world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileDroneBay;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void addInformation(EntityPlayer player, ItemStack stack) {
        addToBothLists(getDebugString());
        addToBothLists("Debug Mode: " + debugMode);
    }

}
