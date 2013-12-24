package me.jezzadabomb.es2.client.renderers.item;

import me.jezzadabomb.es2.client.models.ModelInventoryScanner;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import static org.lwjgl.opengl.GL11.*;

public class ItemInventoryScannerRenderer implements IItemRenderer {

	private ModelInventoryScanner modelInventoryScanner;

	public ItemInventoryScannerRenderer() {
		modelInventoryScanner = new ModelInventoryScanner();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
		case ENTITY: {
			renderInventoryScanner(0F, 0.5F, 0.6F, 0.6F, false, false, false, true);
			return;
		}

		case EQUIPPED_FIRST_PERSON: {
			renderInventoryScanner(0F, 1.0F, 0.4F, 0.6F, true, false, false, false);
			break;
		}

		case INVENTORY: {
			renderInventoryScanner(0F, 0.1F, 0.0F, 0.7F, false, true, false, false);
			return;
		}

		case EQUIPPED: {
			renderInventoryScanner(-0.1F, 0.5F, 0.4F, 1.0F, false, false, true, false);
			return;
		}

		default:
			return;
		}
	}

	private void renderInventoryScanner(float x, float y, float z, float scale, boolean fp, boolean inv, boolean eq, boolean entity) {

		TextureManager texture = Minecraft.getMinecraft().renderEngine;

		glPushMatrix();
		glDisable(GL_LIGHTING);

		// Translate and Scale
		glTranslatef(x, y, z);
		glScalef(scale, scale, scale);

		// Translate, render and texture the electrons.
		if (entity)
			glTranslated(0.0D, 0.0D, -1.0D);
		translateBindRender("Base", x, y, z);

		glPushMatrix();
		glTranslated(0.0D, 0.04D, 0.0D);
		glScaled(0.5D, 0.9D, 0.5D);
		translateBindRender("Base", x, y, z);
		glPopMatrix();

		// Secondary translations because it's weird for some reason..
		if (fp)
			glTranslatef(0F, -0.20F, -0.06F);
		if (inv)
			glTranslatef(-0.2F, 0.12F, 0.0F);
		if (eq)
			glTranslatef(-0.1F, 0.0F, 0.1F);

		glRotated(270, 0.0D, 1.0D, 0.0D);
		glTranslated(0.0D, -0.6D, -0.2D);
		glScaled(0.9D, 0.5D, 0.3D);
		translateBindRender("Spinner", x, y, z);

		glEnable(GL_LIGHTING);
		glPopMatrix();
	}

	private void translateBindRender(String part, float x, float y, float z) {
		switch (part) {
		case "Base":
			glPushMatrix();
			RenderUtils.bindTexture(TextureMaps.INVENTORY_SCANNER_BASE_UPPER);
			modelInventoryScanner.renderPart(part);
			glPopMatrix();
			break;
		case "Spinner":
			glPushMatrix();
			glTranslatef(x, y, z);
			RenderUtils.bindTexture(TextureMaps.INVENTORY_SCANNER_SPINNER);
			modelInventoryScanner.renderPart(part);
			glPopMatrix();
			break;
		default:
			break;
		}

	}

}
