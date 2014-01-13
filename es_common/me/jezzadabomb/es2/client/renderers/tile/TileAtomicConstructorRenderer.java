package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;
import me.jezzadabomb.es2.client.models.ModelAtomicCatalyst;
import me.jezzadabomb.es2.client.models.ModelAtomicConstructor;
import me.jezzadabomb.es2.client.models.ModelConstructorDrone;
import me.jezzadabomb.es2.client.models.ModelPixel;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.client.drone.*;
import me.jezzadabomb.es2.client.drone.DroneState;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileAtomicConstructorRenderer extends TileEntitySpecialRenderer {

    private final RenderItem customItemRenderer;

    ModelAtomicConstructor modelAtomicConstructor;
    ModelConstructorDrone modelConstructorDrone;
    ModelAtomicCatalyst modelAtomicCatalyst;
    ModelPixel modelPixel;

    public TileAtomicConstructorRenderer() {
        modelAtomicConstructor = new ModelAtomicConstructor();
        modelConstructorDrone = new ModelConstructorDrone();
        modelAtomicCatalyst = new ModelAtomicCatalyst();
        modelPixel = new ModelPixel();
        customItemRenderer = new RenderItem();
        customItemRenderer.setRenderManager(RenderManager.instance);
    }

    public void renderAtomicConstructorAt(TileAtomicConstructor tAC, double x, double y, double z, float tick) {
        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef((float) x, (float) y, (float) z);

        glPushMatrix();
        glTranslatef(0.5F, 1.5F, 0.5F);
        glRotated(180.0D, 1.0D, 0.0D, 0.0D);
        RenderUtils.bindTexture(TextureMaps.ATOMIC_CONSTRUCTOR);
        modelAtomicConstructor.render(tAC.getRenderMatrix());
        glPopMatrix();

        for (DroneState drone : tAC.getDroneList()) {
            drone.render(modelConstructorDrone);
        }

        // TODO Remove temp code.
        if (tAC.hasWork()) {
            glPushMatrix();
            glTranslatef(0.5F, 0.5F, 0.5F);
            glScalef(0.005F, 0.005F, 0.005F);
            RenderUtils.drawItemStack((int) Math.round(x), (int) Math.round(y), tAC.getWork(), customItemRenderer, 0, 0, 0);
            glPopMatrix();
        }
        
        // Add Proper rendering code for working stack
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glTranslatef(0.33F, 0.3F, 0.28F);
        glScaled(0.05D, 0.05D, 0.05D);
        RenderUtils.bindTexture(TextureMaps.BLANK_PIXEL);
        boolean skip = false;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                for (int k = 1; k < 9; k++) {
                    // TODO Fix the block rendering
                    if (i * k * j >= 1) {
                        skip = true;
                        break;
                    }
                    glPushMatrix();
                    glTranslatef(i, k, j);
                    modelPixel.renderAll();
                    glPopMatrix();
                }
            }
        }

        glEnable(GL_LIGHTING);
        glDisable(GL_BLEND);
        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TileAtomicConstructor)
            renderAtomicConstructorAt((TileAtomicConstructor) tileEntity, x, y, z, tick);
    }
}
