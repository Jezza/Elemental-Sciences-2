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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileInventoryScannerRenderer extends TileEntitySpecialRenderer {

    private ModelInventoryScanner modelInventoryScanner = new ModelInventoryScanner();
    private int fader = 0;
    private boolean broken = false;
    private boolean ticked = false;
    private boolean onChest;

    public void renderInventoryScanner(TileInventoryScanner inventoryScanner, double x, double y, double z, float tick) {
        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef((float) x, (float) y, (float) z);

        glTranslatef(0.5F, 1.5F, 0.5F);
        glRotated(180.0D, 1.0D, 0.0D, 0.0D);
        RenderUtils.bindTexture(TextureMaps.INVENTORY_SCANNER);
        modelInventoryScanner.render();

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TileInventoryScanner) {
            renderInventoryScanner((TileInventoryScanner) tileEntity, x, y, z, tick);
        }
    }
}
