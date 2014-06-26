package me.jezzadabomb.es2.common.containers;

import me.jezzadabomb.es2.common.tileentity.TileConsole;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerConsole extends ContainerES {

    public ContainerConsole(InventoryPlayer inventory, TileConsole console) {
        //TODO Fix GUI
        super(console.getCoordSet());
    }

}
