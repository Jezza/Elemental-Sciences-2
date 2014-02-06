package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.ModelDroneBay;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TileDroneBayRenderer extends TileEntitySpecialRenderer {

    ModelDroneBay modelDroneBay;

    public TileDroneBayRenderer() {
        modelDroneBay = new ModelDroneBay();
    }

    public void renderDroneBayAt(TileDroneBay droneBay, double x, double y, double z, double f) {
        glPushMatrix();
        
        glTranslated(x + 0.5F, y + 0.0255F, z + 0.5F);
        
        glScaled(0.5F, 0.5F, 0.5F);
        
        RenderUtils.bindTexture(TextureMaps.DRONE_BAY);
        
        glDisable(GL_CULL_FACE);
        modelDroneBay.render();
        glEnable(GL_CULL_FACE);
        
        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        if (tileEntity instanceof TileDroneBay)
            renderDroneBayAt((TileDroneBay) tileEntity, x, y, z, f);
    }

}
