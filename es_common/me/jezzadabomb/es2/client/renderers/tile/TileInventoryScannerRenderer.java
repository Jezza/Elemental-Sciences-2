package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;
import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.client.models.ModelInventoryScanner;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.core.ESLogger;
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

	private int fader = 0;
	private boolean broken = false;
	private boolean ticked = false;

	public void renderInventoryScanner(TileInventoryScanner inventoryScanner, double x, double y, double z, float tick) {
		if (inventoryScanner.restart) {
			fader = 0;
			inventoryScanner.restart = false;
			ESLogger.debug("Reset");
		}
		glPushMatrix();
		glDisable(GL_LIGHTING);
		// Translations and Scales
		glTranslatef((float) x + 0.5f, (float) (y + 0.009F), (float) z + 0.5f);
		glRotated(22.5D, 0.0D, 1.0D, 0.0D);
		glScalef(0.5F, 0.5F, 0.5F);

		if (isRenderType(inventoryScanner, Block.chest.getRenderType())) {
			glTranslated(0.0D, -0.222D, 0.0D);
			glScalef(0.85F, 0.85F, 0.85F);
		}

		// Just the base parts
		translateBindRender("Base");

		glPushMatrix();
		glTranslated(0.0D, 0.04D, 0.0D);
		glScaled(0.5D, 0.9D, 0.5D);
		translateBindRender("Base");
		glPopMatrix();

		// if (fader < 100) {
		// if (!ticked) {
		// fader++;
		// }
		// // glColor4d(1.0D, 1.0D, 1.0D, fader * 0.01D);
		// glScaled(fader * 0.01, fader * 0.01, fader * 0.01);
		// }

		// if (inventoryScanner.hasInventory) {
		glPushMatrix();
		glTranslated(0.0D, -0.35D, 0.0D);
		glRotated(inventoryScanner.rotYaw - 22.5D, 0.0D, 1.0D, 0.0D);
		glScaled(0.9D, 0.5D, 0.3D);
		translateBindRender("Spinner");
		glPopMatrix();
		// ticked = !ticked;
		// }else{
		// fader = 0;
		// }

		glEnable(GL_LIGHTING);
		glPopMatrix();
	}

	private boolean isRenderType(TileEntity tileEntity, int type) {
		return tileEntity.worldObj.blockGetRenderType(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord) == type;
	}

	private void translateBindRender(String part) {
		switch (part) {
		case "Base":
			RenderUtils.bindTexture(TextureMaps.INVENTORY_SCANNER_BASE_UPPER);
			modelInventoryScanner.renderPart(part);
			break;
		case "Spinner":
			glDisable(GL_CULL_FACE);
			RenderUtils.bindTexture(TextureMaps.INVENTORY_SCANNER_SPINNER);
			modelInventoryScanner.renderPart(part);
			glEnable(GL_CULL_FACE);
			break;
		default:
			return;
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
		if (tileEntity instanceof TileInventoryScanner) {
			renderInventoryScanner((TileInventoryScanner) tileEntity, x, y, z, tick);
		}
	}
}
