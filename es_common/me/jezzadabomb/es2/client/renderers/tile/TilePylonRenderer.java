package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.ModelPylonCrystal;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystal;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TilePylonRenderer extends TileEntitySpecialRenderer {

    ModelPylonCrystal modelPylonCrystal;
    boolean dummy = false;

    public TilePylonRenderer(boolean dummy) {
        this.dummy = dummy;
        modelPylonCrystal = new ModelPylonCrystal();
    }

    public void renderPylonCrystalAt(TileEntity pylon, double x, double y, double z, float tick) {
        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glTranslated(x + 0.5F, y + 0.5F, z + 0.5F);

        glRotated(RenderUtils.rotationEquation(-1, 0), 0.0F, 1.0F, 0.0F);
        glRotated(180, 1.0F, 0.0F, 0.0F);

        float scale = 0.30F;
        glScalef(scale, scale, scale);
        glScalef(0.65F, 1.0F, 0.65F);

        glColor4f(0.357F, 0.91F, 0.961F, 0.6F);

        glBindTexture(GL_TEXTURE_2D, 0);
        modelPylonCrystal.renderAll();
        scale = 1.025F;
        for (int i = 1; i <= 5; i++) {
            glScalef(scale, scale, scale);
            glColor4f(0.357F, 0.91F, 0.961F, 0.6F / i);
            modelPylonCrystal.renderAll();
        }

        glDisable(GL_DEPTH_TEST);
        glColor4f(1.0F, 1.0F, 1.0F, 0.1F);
        glScalef(scale, scale, scale);
        modelPylonCrystal.renderAll();
        glEnable(GL_DEPTH_TEST);

        glEnable(GL_BLEND);
        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TilePylonCrystal)
            renderPylonCrystalAt(tileEntity, x, y, z, tick);
    }

}
