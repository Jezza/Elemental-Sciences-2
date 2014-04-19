package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TileQuantumStateDisruptor;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;

@SideOnly(Side.CLIENT)
public class TileQuantumStateDisruptorRenderer extends TileEntitySpecialRenderer {

    int texture;

    public TileQuantumStateDisruptorRenderer() {
        texture = new Random().nextInt(TextureMaps.QUANTUM_TEXTURES.length);
    }

    private void renderQuantumStateDisruptor(TileQuantumStateDisruptor tileEntity, double x, double y, double z, float tick) {
        glPushMatrix();
        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_CULL_FACE);

        glTranslated(x - 0.75F, y - 1.60F, z + 1.75F);


        float translate = 1.28F;
        
        glTranslatef(translate, 0.0F, -translate);
        glRotatef(-(float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL), 0.0F, 1.0F, 0.0F);
        glTranslatef(-translate, 0.0F, translate);

        glRotatef(-90F, 1.0F, 0.0F, 0.0F);
        
        glColor4f(1.0F, 1.0F, 1.0F, 0.5F);

        float scale = 0.01F;
        glScalef(scale, scale, scale);

        RenderUtils.bindTexture(TextureMaps.QUANTUM_TEXTURES[texture]);

        RenderUtils.drawTexturedQuad(0, 0, 0, 0, 256, 256, 161);

        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TileQuantumStateDisruptor)
            renderQuantumStateDisruptor(((TileQuantumStateDisruptor) tileEntity), x, y, z, tick);
    }
}
