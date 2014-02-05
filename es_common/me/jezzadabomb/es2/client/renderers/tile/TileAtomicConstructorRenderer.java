package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTranslatef;
import me.jezzadabomb.es2.client.models.ModelAtomicConstructor;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileAtomicConstructorRenderer extends TileEntitySpecialRenderer {
    ModelAtomicConstructor modelAtomicConstructor;

    public TileAtomicConstructorRenderer() {
        modelAtomicConstructor = new ModelAtomicConstructor();
    }

    public void renderAtomicConstructorAt(TileAtomicConstructor tAC, double x, double y, double z, float tick) {
        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef((float) x, (float) y, (float) z);

        glTranslatef(0.5F, 1.5F, 0.5F);
        glRotated(180.0D, 1.0D, 0.0D, 0.0D);
        RenderUtils.bindTexture(TextureMaps.ATOMIC_CONSTRUCTOR);
        modelAtomicConstructor.render(tAC.getRenderMatrix());

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TileAtomicConstructor)
            renderAtomicConstructorAt((TileAtomicConstructor) tileEntity, x, y, z, tick);
    }
}
