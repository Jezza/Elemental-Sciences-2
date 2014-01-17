package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;
import me.jezzadabomb.es2.client.models.ModelInventoryScanner;
import me.jezzadabomb.es2.client.models.ModelPixel;
import me.jezzadabomb.es2.client.models.ModelPlate;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TileConsoleRenderer extends TileEntitySpecialRenderer {

    private ModelPlate modelPlate;
    private ModelPixel modelPixel;

    public TileConsoleRenderer() {
        modelPlate = new ModelPlate();
        modelPixel = new ModelPixel();
    }

    public void renderConsole(TileConsole tileConsole, double x, double y, double z, float tick) {
        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef((float) x, (float) y, (float) z);

        glPushMatrix();
        glTranslatef(0.5F, 1.5F - 0.625F, 0.5F);
        glRotated(180.0D, 1.0D, 0.0D, 0.0D);
        for (int i = 0; i < 2; i++) {
            RenderUtils.bindTexture(TextureMaps.CONSOLE_BASE);
            modelPlate.render();
            glTranslatef(0.0F, -0.06F, 0.0F);
            glScalef(0.75F, 1.0F, 0.75F);
        }
        glPopMatrix();

        int tempNum = 0;
        switch (tileConsole.getOrientation()) {
            case 2:
                tempNum = 2;
                break;
            case 3:
                tempNum = 0;
                break;
            case 4:
                tempNum = 3;
                break;
            case 5:
                tempNum = 1;
                break;
        }

        glPushMatrix();
        glTranslatef(0.5F, 0.5F, 0.5F);
        glRotatef(90F * tempNum, 0.0F, 1.0F, 0.0F);

        glTranslatef(0.0F, 0.0F, -0.6F);
        glRotatef(90F / 2F, 1.0F, 0.0F, 0.0F);

        
        
        RenderUtils.bindTexture(TextureMaps.CONSOLE_SCREEN);
        modelPlate.render();
        glPopMatrix();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                renderPixelAt(i, 1, 7 + j);
                renderPixelAt(14 + i, 1, 7 + j);
                renderPixelAt(7 + j, 1, i);
                renderPixelAt(7 + j, 1, 14 + i);
            }
        }

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    private void renderPixelAt(int x, int y, int z) {
        glPushMatrix();
        glTranslatef(0.029F, 0.029F, 0.029F);
        glTranslatef(x / 16F, y / 16F, z / 16F);
        glScalef(0.06F, 0.06F, 0.06F);

        RenderUtils.bindTexture(TextureMaps.BLANK_PIXEL);
        modelPixel.renderAll();
        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TileConsole) {
            renderConsole((TileConsole) tileEntity, x, y, z, tick);
        }
    }
}