package me.jezzadabomb.es2.client.gui;

import me.jezzadabomb.es2.client.lib.GuiSheet;
import me.jezzadabomb.es2.client.utils.Colour;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.containers.ContainerConsole;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiConsole extends GuiContainerES {

    public GuiConsole(InventoryPlayer inventory, TileConsole tileConsole) {
        super(new ContainerConsole(inventory, tileConsole));

        xSize = 215;
        ySize = 136;
    }

    @Override
    public void initGui() {
        super.initGui();

        addButton(guiLeft + 20, guiTop + 10, 20, 20, "<");
        addButton(guiLeft + xSize - 40, guiTop + 10, 20, 20, ">");
        addButton(guiLeft + xSize / 2 - 20, guiTop + ySize - 30, 40, 20, "Done");
        addButton(guiLeft + xSize / 2 - 20, guiTop + ySize - 30, 40, 20, "Search");
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawCentredText(0, 0, "asd");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
        RenderUtils.bindTexture(GuiSheet.GUI_CONSOLE_TEXTURE);

        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
