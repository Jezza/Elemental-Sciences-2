package me.jezzadabomb.es2.common.containers;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.network.packet.client.GuiConsolePacket;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class ContainerConsole extends ContainerES {

    public String loc;

    public ContainerConsole(InventoryPlayer inventory, TileConsole console) {
        super(console);

        loc = UtilMethods.getLocFromXYZ(console.getCoordSet());
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            PacketDispatcher.sendToServer(new GuiConsolePacket(this));
        } else {
            if (loc != null) {
                int[] coords = UtilMethods.getArrayFromString(loc);
                if (coords == null)
                    return;
                int x = coords[0];
                int y = coords[1];
                int z = coords[2];

                World world = player.worldObj;
                if (UtilMethods.isConsole(world, x, y, z)) {
                    TileConsole tile = (TileConsole) world.getTileEntity(x, y, z);
                    tile.closeGui(world, player);
                }
                PacketDispatcher.sendToAllAround(new GuiConsolePacket(this), new TargetPoint(world.provider.dimensionId, x + 0.5F, y + 0.5F, z + 0.5F, 80));
            }
        }
        super.onContainerClosed(player);
    }

}
