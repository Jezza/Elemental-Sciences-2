package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;
import me.jezzadabomb.es2.client.models.ModelSolarLens;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TileSolarLens;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileSolarLensRenderer extends TileEntitySpecialRenderer {

    ModelSolarLens modelSolarLens;

    public TileSolarLensRenderer() {
        modelSolarLens = new ModelSolarLens();
    }

    public void renderSolarLensAt(TileSolarLens solarLens, double x, double y, double z, float tick) {

        float rotationAngle = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
        float hoverHeight = (float) ((0.4 * (Math.sin((24 * Math.PI * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL)))) / 8);

        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef((float) x + 0.5f, (float) y + 0.8F, (float) z + 0.5f);
        glScalef(0.25F, 0.25F, 0.25F);

        glPushMatrix();
        // glTranslated(0.0F, hoverHeight, 0.0F);
        glRotatef(-rotationAngle, 0.0F, 1.0F, 0.0F);
        translateBindRender("lens");
        glPopMatrix();

        glPushMatrix();
        glTranslated(0.0F, -hoverHeight - 1.2F, 0.0F);
        glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
        translateBindRender("loop");
        glPopMatrix();

        glPushMatrix();
        glTranslated(0.0F, -(hoverHeight * 1.5) - 1.8F, 0.0F);
        glRotatef(rotationAngle * 1.5F, 0.0F, 1.0F, 0.0F);
        glScalef(0.9F, 0.9F, 0.9F);
        translateBindRender("loop");
        glPopMatrix();

        glPushMatrix();
        glTranslated(0.0F, (-hoverHeight * 2) - 2.4F, 0.0F);
        glRotatef(rotationAngle * 2, 0.0F, 1.0F, 0.0F);
        glScalef(0.8F, 0.8F, 0.8F);
        translateBindRender("loop");
        glPopMatrix();

        // glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
        // RenderUtils.drawLineFrom(x + 0.5F, y, z + 0.5F, x + 0.5F, y - 5F, z + 0.5F);

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    private void translateBindRender(String part) {

        glPushMatrix();
        switch (part) {
            case "lens":
                RenderUtils.bindTexture(TextureMaps.SOLAR_LENS);
                modelSolarLens.renderPart(part);
                break;
            case "loop":
                RenderUtils.bindTexture(TextureMaps.SOLAR_LOOP);
                modelSolarLens.renderPart(part);
                break;
        }
        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TileSolarLens)
            renderSolarLensAt((TileSolarLens) tileEntity, x, y, z, tick);
    }

}
