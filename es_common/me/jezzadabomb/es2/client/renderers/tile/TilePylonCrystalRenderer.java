package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.ModelPylonCrystal;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystal;
import me.jezzadabomb.es2.common.tileentity.TilePylonDummyCrystal;
import me.jezzadabomb.es2.common.tileentity.framework.TilePylon;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TilePylonCrystalRenderer extends TileEntitySpecialRenderer {

    ModelPylonCrystal modelPylonCrystal;
    boolean dummy = false;

    public TilePylonCrystalRenderer(boolean dummy) {
        this.dummy = dummy;
        modelPylonCrystal = new ModelPylonCrystal();
    }

    public void renderPylonCrystalAt(TileEntity pylon, double x, double y, double z, float tick) {
        glPushMatrix();
        glDisable(GL_CULL_FACE);

        glTranslated(x + 0.5F, y + 0.5F, z + 0.5F);

        float scale = 0.4F;
        glScalef(scale, scale, scale);

        glPushMatrix();
        
        glTranslated(0.0F, RenderUtils.hoverEquation(0.4F, -2F, 8F), 0.0F);

        RenderUtils.bindTexture(TextureMaps.PYLON_CRYSTAL);
        modelPylonCrystal.renderCrystal();

        glPopMatrix();
        if (!dummy)
            glRotated(RenderUtils.rotationEquation(4, 0), 0.0F, 1.0F, 0.0F);
        glTranslated(0.0F, RenderUtils.hoverEquation(0.4F, 2F, 8F), 0.0F);

        RenderUtils.bindTexture(TextureMaps.ATOMIC_CATALYST_MAIN);
        modelPylonCrystal.renderRings();

        glEnable(GL_CULL_FACE);
        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TilePylonCrystal || tileEntity instanceof TilePylonDummyCrystal)
            renderPylonCrystalAt(tileEntity, x, y, z, tick);
    }

}
