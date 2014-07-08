package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;

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

        glTranslated(x, y, z);
        glTranslatef(0.5F, 0.035F, 0.5F);

        float scale = 0.50025F;
        glScalef(scale, scale, scale);
        RenderUtils.bindTexture(TextureMaps.ATOMIC_CONSTRUCTOR);
        modelAtomicConstructor.render(tAC.getRenderMatrix());

        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TileAtomicConstructor)
            renderAtomicConstructorAt((TileAtomicConstructor) tileEntity, x, y, z, tick);
    }
}
