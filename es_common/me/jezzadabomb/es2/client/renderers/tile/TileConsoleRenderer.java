package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.BitSet;

import me.jezzadabomb.es2.client.models.ModelCube;
import me.jezzadabomb.es2.client.models.ModelPlate;
import me.jezzadabomb.es2.client.models.drones.ModelConstructorDrone;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileConsoleRenderer extends TileEntitySpecialRenderer {

    private ModelPlate modelPlate;
    private ModelCube modelPixel;
    private ModelConstructorDrone modelConstructorDrone;

    public TileConsoleRenderer() {
        modelPlate = new ModelPlate();
        modelPixel = new ModelCube();
        modelConstructorDrone = new ModelConstructorDrone();
    }

    public void renderConsole(TileConsole tileConsole, double x, double y, double z, float tick) {
        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef((float) x, (float) y, (float) z);

        glPushMatrix();
        glTranslatef(0.5F, 1.5F - 0.625F, 0.5F);
        glRotated(180.0D, 1.0D, 0.0D, 0.0D);
        for (int i = 0; i <= 1; i++) {
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

        RenderUtils.bindTexture(TextureMaps.CONSOLE_SCREEN);
        if (tileConsole.getWorldObj().isAirBlock(tileConsole.xCoord, tileConsole.yCoord + 1, tileConsole.zCoord)) {
            glTranslatef(0.5F, 0.5F, 0.5F);
            glRotatef(90F * tempNum, 0.0F, 1.0F, 0.0F);

            glTranslatef(0.0F, (float) ((0.4 * (Math.sin((24 * Math.PI * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) + tileConsole.randomHoverSeed))) / 8), -0.6F);
            glRotatef(90F / 2F, 1.0F, 0.0F, 0.0F);
        } else {
            glTranslatef(0.5F, 0.125F, 0.5F);
            glRotatef(90F * tempNum, 0.0F, 1.0F, 0.0F);
        }

        modelPlate.render();
        glPopMatrix();

        BitSet check = tileConsole.getRenderCables();

        for (int i = 0; i <= 1; i++)
            for (int j = 0; j <= 1; j++) {
                if (check.get(0))
                    renderPixelAt(i, 1, 7 + j);
                if (check.get(1))
                    renderPixelAt(14 + i, 1, 7 + j);
                if (check.get(2))
                    renderPixelAt(7 + j, 1, i);
                if (check.get(3))
                    renderPixelAt(7 + j, 1, 14 + i);
            }

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    private void renderPixelAt(int x, int y, int z) {
        glPushMatrix();
        glTranslatef(0.029F, 0.029F, 0.029F);
        glTranslatef(x / 16F, y / 16F, z / 16F);
        glScalef(0.065F, 0.065F, 0.065F);

        RenderUtils.bindTexture(TextureMaps.CABLE_PIXEL);
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