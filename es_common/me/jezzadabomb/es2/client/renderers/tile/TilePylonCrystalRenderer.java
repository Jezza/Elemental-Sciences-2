package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslated;
import me.jezzadabomb.es2.client.models.ModelPylonCrystal;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystal;
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
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glTranslated(x + 0.5F, y + 0.5F, z + 0.5F);

        // glRotated(180, 0.0, 0.0, 1.0);

        float scale = 0.35F;
        glScalef(scale, scale, scale);
        glScalef(0.65F, 1.0F, 0.65F);

        glColor4f(0.357F, 0.91F, 0.961F, 0.6F);

        glBindTexture(GL_TEXTURE_2D, 0);
        modelPylonCrystal.renderAll();

        glEnable(GL_BLEND);
        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TilePylonCrystal)
            renderPylonCrystalAt(tileEntity, x, y, z, tick);
    }

}
