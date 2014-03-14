package me.jezzadabomb.es2.common.gui;

import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.containers.ContainerConsole;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.network.packet.client.GuiConsolePacket;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.GuiTextureMap;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiConsole extends GuiContainerES {

    public GuiConsole(InventoryPlayer inventory, TileConsole tileConsole) {
        super(new ContainerConsole(inventory, tileConsole));

        xSize = 215;
        ySize = 136;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        RenderUtils.bindTexture(GuiTextureMap.GUI_CONSOLE_TEXTURE);

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
