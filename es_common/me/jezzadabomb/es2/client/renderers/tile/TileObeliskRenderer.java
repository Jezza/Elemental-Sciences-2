package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.ModelObelisk;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.tileentity.TileObelisk;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TileObeliskRenderer extends TileEntitySpecialRenderer {

    private ModelObelisk modelObelisk;

    public TileObeliskRenderer() {
        modelObelisk = new ModelObelisk();
    }

    public void renderObeliskAt(TileObelisk obelisk, double x, double y, double z, float tick) {

        glPushMatrix();

        glTranslated(x + 0.5F, y + 0.55F, z + 0.5F);

        glScaled(0.5F, 0.5F, 0.5F);

        glRotated(RenderUtils.rotationEquation(1, 0), 0.0F, 1.0F, 0.0F);

        glColor4f(0.5F, 0.5F, 0.5F, 1.0F);

        glBindTexture(GL_TEXTURE_2D, 0);
        modelObelisk.renderAll();

        glPopMatrix();

    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TileObelisk)
            renderObeliskAt((TileObelisk) tileEntity, x, y, z, tick);
    }

}
