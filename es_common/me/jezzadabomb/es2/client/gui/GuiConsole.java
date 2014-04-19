package me.jezzadabomb.es2.client.gui;

import me.jezzadabomb.es2.client.utils.Colour;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.containers.ContainerConsole;
import me.jezzadabomb.es2.common.lib.GuiTextureMaps;
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

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        addButton(x + 20, y + 10, 20, 20, "<");
        addButton(x + xSize - 40, y + 10, 20, 20, ">");
        addButton(x + xSize / 2 - 20, y + ySize - 30, 40, 20, "Done");
        addButton(x + xSize / 2 - 20, y + ySize - 30, 40, 20, "Search");
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
        int x = (xSize - fontRendererObj.getStringWidth("asd")) / 2;
        int y = ySize / 2;

        fontRendererObj.drawString("asd", x, y, new Colour(1.0F, 0.0F, 0.0F, 1.0F).getInt());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        RenderUtils.bindTexture(GuiTextureMaps.GUI_CONSOLE_TEXTURE);

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
