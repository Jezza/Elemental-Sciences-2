package me.jezzadabomb.es2.common.items;

import java.util.ArrayList;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.client.drone.DroneState;
import me.jezzadabomb.es2.client.utils.CoordSet;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.api.HUDBlackLists;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.packets.InventoryPacket;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import me.jezzadabomb.es2.common.tileentity.TileSolarLens;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.input.Keyboard;

import cofh.api.energy.IEnergyHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDebugTool extends ItemES {

    public int debugMode;
    public boolean canFlood;
    private CoordSet hitBlock;
    private ArrayList<TileAtomicConstructor> solarList;

    public ItemDebugTool(int id, String name) {
        super(id, name);
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
            add("Get Bomb Holder"); // 4
            add("Constructor - Set work"); // 5
            add("Constructor - Energy add"); // 6
            add("Constructor - Energy count"); // 7
            add("Constructor - Drone count"); // 8
            add("Constructor - Set Pos"); // 9
            add("Constructor - Get Pos"); // 10
            add("Constructor - Get Nearby Count"); // 13
            add("Solar Lens - Set List"); // 11
            add("Solar Lens - Get List"); // 12
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
        if (world.isRemote && Reference.CAN_DEBUG) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if (++debugMode == debugStringList.size())
                    debugMode = 0;
                player.addChatMessage(getDebugString());
            } else {
                switch (debugMode) {
                case 0:
                    break;
                case 1:
                    canFlood = !canFlood;
                    player.addChatMessage((canFlood ? "Enabled" : "Disabled") + " Debug Flooding.");
                    break;
                case 2:
                    boolean temp = Reference.HUD_VERTICAL_ROTATION;
                    Reference.HUD_VERTICAL_ROTATION = !temp;
                    player.addChatMessage("HUD Rotation: " + !temp);
                    break;
                case 3:
                    String playerString = ElementalSciences2.proxy.quantumBomb.getPlayer();
                    if (playerString == null) {
                        player.addChatMessage("Empty");
                    } else {
                        player.addChatMessage(playerString);
                    }
                    break;
                case 4:
                    break;
                case 5:
                    break;
                }
            }
        }
        return itemStack;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {
        if (getDebugMode("Constructor - Set work") && world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor) {
            TileAtomicConstructor tAC = (TileAtomicConstructor) world.getBlockTileEntity(x, y, z);
            tAC.setHasWork(new ItemStack(ModItems.glasses));
            return true;
        }

        if (getDebugMode("Constructor - Energy count") && world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof IEnergyHandler) {
            IEnergyHandler tEH = (IEnergyHandler) world.getBlockTileEntity(x, y, z);
            if (world.isRemote) {
                player.addChatMessage("Client Side: " + tEH.getEnergyStored(null));
            } else {
                player.sendChatToPlayer(new ChatMessageComponent().addText("Server Side: " + tEH.getEnergyStored(null)));
            }
        }

        if (getDebugMode("Constructor - Energy add") && world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof IEnergyHandler) {
            IEnergyHandler tEH = (IEnergyHandler) world.getBlockTileEntity(x, y, z);
            int prevEnergy = tEH.getEnergyStored(null);
            tEH.receiveEnergy(null, 2000, false);
            if (world.isRemote) {
                player.addChatMessage("Client Side: " + prevEnergy + " ==> " + tEH.getEnergyStored(null));
            } else {
                player.sendChatToPlayer(new ChatMessageComponent().addText("Server Side: " + prevEnergy + " ==> " + tEH.getEnergyStored(null)));
            }
        }

        if (world.isRemote) {
            if (getDebugMode("Constructor - Get Nearby Count") && world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor) {
                ArrayList<TileAtomicConstructor> tempList = new ArrayList<TileAtomicConstructor>();
                if (isMatch(world, x + 1, y, z)) {
                    tempList.add((TileAtomicConstructor) world.getBlockTileEntity(x + 1, y, z));
                }
                if (isMatch(world, x - 1, y, z)) {
                    tempList.add((TileAtomicConstructor) world.getBlockTileEntity(x - 1, y, z));
                }
                if (isMatch(world, x, y + 1, z)) {
                    tempList.add((TileAtomicConstructor) world.getBlockTileEntity(x, y + 1, z));
                }
                if (isMatch(world, x, y - 1, z)) {
                    tempList.add((TileAtomicConstructor) world.getBlockTileEntity(x, y - 1, z));
                }
                if (isMatch(world, x, y, z + 1)) {
                    tempList.add((TileAtomicConstructor) world.getBlockTileEntity(x, y, z + 1));
                }
                if (isMatch(world, x, y, z - 1)) {
                    tempList.add((TileAtomicConstructor) world.getBlockTileEntity(x, y, z - 1));
                }
                player.addChatMessage("" + tempList.size());
            }

            if (getDebugMode("Solar Lens - Set List") && world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileSolarLens) {
                TileSolarLens tSL = (TileSolarLens) world.getBlockTileEntity(x, y, z);
                solarList = tSL.getConstructorList();
                player.addChatMessage("Set list from Solar lens @ " + new CoordSet(tSL.xCoord, tSL.yCoord, tSL.zCoord));
            }

            if (getDebugMode("Solar Lens - Get List") && world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor) {
                if (solarList.isEmpty()) {
                    player.addChatMessage("List is empty");
                    return true;
                }
                TileAtomicConstructor tAC = (TileAtomicConstructor) world.getBlockTileEntity(x, y, z);
                player.addChatMessage("" + solarList.contains(tAC));
            }

            if (getDebugMode("Constructor - Set pos") && world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor) {
                CoordSet tempBlock = new CoordSet(x, y, z);
                if (hitBlock != null && hitBlock.equals(tempBlock)) {
                    player.addChatMessage("Already set to this block.");
                    return true;
                }
                hitBlock = tempBlock;
                player.addChatMessage("Set New Constructor @ " + hitBlock);
            }

            if (getDebugMode("Constructor - Get pos") && world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor) {
                if (hitBlock == null)
                    return true;
                if (hitBlock.equals(new CoordSet(x, y, z))) {
                    player.addChatMessage("Same block");
                    return true;
                }
                if (!(MathHelper.withinRange(x, hitBlock.getX(), 1) && MathHelper.withinRange(y, hitBlock.getY(), 1) && MathHelper.withinRange(z, hitBlock.getZ(), 1))) {
                    player.addChatMessage("Not within range of block");
                    return true;
                }
                int index = 0;
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        for (int k = -1; k < 2; k++) {
                            if (i == 0 && j == 0 && k == 0)
                                continue;
                            if (hitBlock.getX() + i == x && hitBlock.getY() + j == y && hitBlock.getZ() + k == z) {
                                player.addChatMessage("Pos in array: " + index);
                                return true;
                            }
                            index++;
                        }
                    }
                }
                player.addChatMessage("Error");
            }

            if (getDebugMode("Constructor - Drone count") && world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor) {
                TileAtomicConstructor tAC = (TileAtomicConstructor) world.getBlockTileEntity(x, y, z);
                if (!tAC.isDroneListEmpty()) {
                    ArrayList<DroneState> droneList = tAC.getDroneList();
                    if (droneList.size() < 5) {
                        for (DroneState drone : droneList) {
                            player.addChatMessage(drone.toString());
                        }
                    } else {
                        player.addChatMessage("" + droneList.size());
                    }
                } else {
                    player.addChatMessage("No Drones found.");
                }
                return true;
            }
            if (getDebugMode("Minor Packet Monitoring")) {
                InventoryPacket packet = ElementalSciences2.proxy.hudRenderer.getPacket(x, y, z);
                if (packet != null) {
                    player.addChatMessage("");
                    player.addChatMessage("Inventory Name: " + packet.inventoryTitle);
                    player.addChatMessage("Contents: ");
                    String tempString = packet.getItemStacksInfo();
                    if (tempString == null)
                        return false;
                    int lastIndex = 0;
                    for (int i = 0; i < tempString.length(); i++) {
                        if (tempString.charAt(i) == ',') {
                            player.addChatMessage(tempString.substring(lastIndex, i));
                            lastIndex = i + 1;
                        }
                    }
                }
                return packet != null;
            }
        }
        return false;
    }

    private boolean isMatch(World world, int x, int y, int z) {
        return world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileAtomicConstructor;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void addInformation(EntityPlayer player, ItemStack stack) {
        defaultInfoList();
        shiftList.add("Debug tool for dev. You shouldn't even");
        shiftList.add("see this if this is a release.");
        shiftList.add("If it is, contact me.");
    }

}
