package me.jezzadabomb.es2.common.core.handlers;

import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof TileES) {
            TileES tileES = (TileES) tileEntity;

            return tileES.getGui(ID, Side.SERVER, player);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof TileES) {
            TileES tileES = (TileES) tileEntity;

            return tileES.getGui(ID, Side.CLIENT, player);
        }

        return null;
    }
}
