package me.jezzadabomb.es2.client.gui;

import me.jezzadabomb.es2.common.containers.ContainerAtomicShredder;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderCore;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiAtomicShredder extends GuiContainerES {
    
    public GuiAtomicShredder(InventoryPlayer inventory, TileAtomicShredderCore tileAtomicShredder) {
        super(new ContainerAtomicShredder(inventory, tileAtomicShredder));
        
        
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {

    }

}
