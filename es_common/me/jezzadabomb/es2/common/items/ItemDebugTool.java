package me.jezzadabomb.es2.common.items;

import java.util.ArrayList;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.core.utils.MathHelper;
import me.jezzadabomb.es2.common.items.framework.ItemES;
import me.jezzadabomb.es2.common.lib.Reference;
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
    private ArrayList<TileAtomicConstructor> solarList;

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
            add("Console - Locate Master"); // 13
            add("Constructor - 3x3"); // 14
            add("Drone Bay - Door control"); // 15
            add("Drone Bay - Spawn Drone");
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
                Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
                player.addChatMessage(new ChatComponentText(getDebugString()));
            } else {
                switch (debugMode) {
                    case 2:
                        canFlood = !canFlood;
                        player.addChatMessage(new ChatComponentText((canFlood ? "Enabled" : "Disabled") + " Debug Flooding."));
                        break;
                    case 3:
                        boolean temp = Reference.HUD_VERTICAL_ROTATION;
                        Reference.HUD_VERTICAL_ROTATION = !temp;
                        player.addChatMessage(new ChatComponentText("HUD Rotation: " + !temp));
                        break;
                    case 5:
                        String playerString = ElementalSciences2.proxy.quantumBomb.getPlayer();
                        if (playerString == null) {
                            player.addChatMessage(new ChatComponentText("Empty"));
                        } else {
                            player.addChatMessage(new ChatComponentText(playerString));
                        }
                        break;
                    case 15:
                        int range = 3;
                        // PacketDispatcher.sendPacketToServer(new SetBlockChunkPacket(new CoordSet((int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ)), ModBlocks.atomicConstructor.blockID, range).makePacket());
                        break;
                }
            }
        }
        return itemStack;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float hitVecX, float hitVecY, float hitVecZ) {

        if (getDebugMode("Drone Bay - Door control") && isDroneBay(world, x, y, z)) {
            TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x, y, z);
            droneBay.toggleDoor();
        }

        if (getDebugMode("Drone Bay - Spawn Drone") && isDroneBay(world, x, y, z)) {
            TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x, y, z);
            droneBay.spawnDrone();
        }

        if (getDebugMode("Console - Locate Master")) {
            if (isConstructor(world, x, y, z)) {
                TileAtomicConstructor tAC = (TileAtomicConstructor) world.getTileEntity(x, y, z);
                if (tAC.hasConsole()) {
                    TileConsole tC = tAC.getConsole();
                    player.addChatMessage(new ChatComponentText("Found: " + new CoordSet(tC.xCoord, tC.yCoord, tC.zCoord)));
                } else {
                    player.addChatMessage(new ChatComponentText("No console found."));
                }
            }
        }

        if (getDebugMode("Constructor - Drone count")) {
            int droneSize = 0;
            if (isConsole(world, x, y, z)) {
                TileConsole tAC = (TileConsole) world.getTileEntity(x, y, z);
                droneSize = tAC.getDroneSize();
            } else if (isConstructor(world, x, y, z)) {
                TileAtomicConstructor tAC = (TileAtomicConstructor) world.getTileEntity(x, y, z);
                if (tAC.hasConsole())
                    droneSize = tAC.getConsole().getDroneSize();
            }
            if (world.isRemote) {
                player.addChatMessage(new ChatComponentText("ClientSide: " + (droneSize == 0 ? "Empty" : droneSize)));
            } else {
                //TODO Probably need to fix this up.
                player.addChatMessage(new ChatComponentText("ServerSide: " + (droneSize == 0 ? "Empty" : droneSize)));
            }
            return true;
        }

        if (world.isRemote) {
            if (getDebugMode("Constructor - Get Nearby Count") && isConstructor(world, x, y, z)) {
                ArrayList<TileAtomicConstructor> tempList = new ArrayList<TileAtomicConstructor>();
                for (int i = -1; i < 2; i++)
                    for (int j = -1; j < 2; j++)
                        for (int k = -1; k < 2; k++) {
                            if (!(i == 0 && j == 0 && k == 0) && isConstructor(world, x + i, y + j, z + k))
                                tempList.add((TileAtomicConstructor) world.getTileEntity(x + i, y + j, z + k));
                        }
                player.addChatMessage(new ChatComponentText("" + tempList.size()));
            }

            if (getDebugMode("Solar Lens - Set List") && isSolarLens(world, x, y, z)) {
                TileSolarLens tSL = (TileSolarLens) world.getTileEntity(x, y, z);
                solarList = tSL.getConstructorList();
                player.addChatMessage(new ChatComponentText("Set list from Solar lens @ " + new CoordSet(tSL.xCoord, tSL.yCoord, tSL.zCoord)));
            }

            if (getDebugMode("Solar Lens - Get List") && isConstructor(world, x, y, z)) {
                if (solarList.isEmpty()) {
                    player.addChatMessage(new ChatComponentText("List is empty"));
                    return true;
                }
                TileAtomicConstructor tAC = (TileAtomicConstructor) world.getTileEntity(x, y, z);
                player.addChatMessage(new ChatComponentText("" + solarList.contains(tAC)));
            }

            if (getDebugMode("Constructor - Set pos") && isConstructor(world, x, y, z)) {
                CoordSet tempBlock = new CoordSet(x, y, z);
                if (hitBlock != null && hitBlock.equals(tempBlock)) {
                    player.addChatMessage(new ChatComponentText("Already set to this block."));
                    return true;
                }
                hitBlock = tempBlock;
                player.addChatMessage(new ChatComponentText("Set New Constructor @ " + hitBlock));
            }

            if (getDebugMode("Constructor - Get pos") && isConstructor(world, x, y, z)) {
                if (hitBlock == null)
                    return true;
                if (hitBlock.equals(new CoordSet(x, y, z))) {
                    player.addChatMessage(new ChatComponentText("Same block"));
                    return true;
                }
                if (!(MathHelper.withinRange(x, hitBlock.getX(), 1) && MathHelper.withinRange(y, hitBlock.getY(), 1) && MathHelper.withinRange(z, hitBlock.getZ(), 1))) {
                    player.addChatMessage(new ChatComponentText(("Not within range of block")));
                    return true;
                }
                int index = 0;
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        for (int k = -1; k < 2; k++) {
                            if (i == 0 && j == 0 && k == 0)
                                continue;
                            if (hitBlock.getX() + i == x && hitBlock.getY() + j == y && hitBlock.getZ() + k == z) {
                                player.addChatMessage(new ChatComponentText("Pos in array: " + index));
                                return true;
                            }
                            index++;
                        }
                    }
                }
                player.addChatMessage(new ChatComponentText("Error"));
            }

            if (getDebugMode("Minor Packet Monitoring")) {
                InventoryPacket packet = ClientProxy.getHUDRenderer().getPacket(x, y, z);
                if (packet != null) {
                    player.addChatMessage(new ChatComponentText(""));
                    player.addChatMessage(new ChatComponentText("Contents: "));
                    String tempString = packet.getItemStacksInfo();
                    if (tempString == null)
                        return false;
                    int lastIndex = 0;
                    for (int i = 0; i < tempString.length(); i++) {
                        if (tempString.charAt(i) == ',') {
                            player.addChatMessage(new ChatComponentText(tempString.substring(lastIndex, i)));
                            lastIndex = i + 1;
                        }
                    }
                }
                return packet != null;
            }
        }
        return false;
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
        defaultInfoList();
        shiftList.add("Debug tool for dev. You shouldn't even");
        shiftList.add("see this if this is a release.");
        shiftList.add("If it is, contact me.");
    }

}
