package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;
import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.client.models.ModelInventoryScanner;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileInventoryScannerRenderer extends TileEntitySpecialRenderer {

    private ModelInventoryScanner modelInventoryScanner = new ModelInventoryScanner();
    private TileInventoryScanner inventoryScanner;
    private int fader = 0;
    private boolean broken = false;
    private boolean ticked = false;

    public void renderInventoryScanner(TileInventoryScanner inventoryScanner, double x, double y, double z, float tick) {
        this.inventoryScanner = inventoryScanner;

        glPushMatrix();
        glDisable(GL_LIGHTING);

        // Translations
        glTranslatef((float) x + 0.5f, (float) (y + 0.009F), (float) z + 0.5f);
        // Rotations
        glRotated(22.5D, 0.0D, 1.0D, 0.0D);
        // Scaling
        glScalef(0.5F, 0.5F, 0.5F);

        if (UtilMethods.isRenderType(inventoryScanner, Block.chest.getRenderType())) {
            glTranslated(0.0D, -0.25D, 0.0D);
            glScalef(0.85F, 0.85F, 0.85F);
        }

        // Just the base parts
        translateBindRender("Base");

        glPushMatrix();
        glTranslated(0.0D, 0.04D, 0.0D);
        glScaled(0.5D, 0.9D, 0.5D);
        translateBindRender("Base");
        glPopMatrix();

        translateBindRender("Spinner");

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    private void translateBindRender(String part) {
        glPushMatrix();
        switch (part) {
        case "Base":
            RenderUtils.bindTexture(TextureMaps.INVENTORY_SCANNER_BASE_UPPER);
            modelInventoryScanner.renderPart(part);
            break;
        case "Spinner":
            glTranslated(0.0D, -0.35D, 0.0D);
            glRotated(inventoryScanner.rotYaw - 22.5D, 0.0D, 1.0D, 0.0D);
            glScaled(0.9D, 0.5D, 0.3D);
            glDisable(GL_CULL_FACE);
            RenderUtils.bindTexture(TextureMaps.INVENTORY_SCANNER_SPINNER);
            modelInventoryScanner.renderPart(part);
            glEnable(GL_CULL_FACE);
            break;
        default:
            glPopMatrix();
            return;
        }
        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TileInventoryScanner) {
            renderInventoryScanner((TileInventoryScanner) tileEntity, x, y, z, tick);
        }
    }
}
