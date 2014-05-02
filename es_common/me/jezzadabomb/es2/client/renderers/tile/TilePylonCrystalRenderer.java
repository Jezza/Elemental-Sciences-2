package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.ModelPylonCrystal;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystal;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TilePylonCrystalRenderer extends TileEntitySpecialRenderer {

    ModelPylonCrystal modelPylonCrystal;

    public TilePylonCrystalRenderer() {
        modelPylonCrystal = new ModelPylonCrystal();
    }

    public void renderPylonCrystalAt(TilePylonCrystal pylon, double x, double y, double z, float tick) {
        glPushMatrix();

        glTranslated(x + 0.5F, y + 0.383F, z + 0.5F);
        glScalef(0.5F, 0.5F, 0.5F);

        RenderUtils.bindTexture(TextureMaps.ATOMIC_CATALYST_ELECTRON);
        modelPylonCrystal.render();

        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TilePylonCrystal)
            renderPylonCrystalAt((TilePylonCrystal) tileEntity, x, y, z, tick);
    }

}
