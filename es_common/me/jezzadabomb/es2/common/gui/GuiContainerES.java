package me.jezzadabomb.es2.common.gui;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.utils.RenderUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class GuiContainerES extends GuiContainer {

    public GuiContainerES(Container par1Container) {
        super(par1Container);
    }

    protected void drawTextureAt(ResourceLocation texture, int x, int y) {
        RenderUtils.bindTexture(texture);

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    protected void drawCentredTexture(ResourceLocation texture) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTextureAt(texture, x, y);
    }
}
