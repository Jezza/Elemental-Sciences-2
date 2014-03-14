package me.jezzadabomb.es2.client.renderers.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.client.models.ModelAtomicCatalyst;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class ItemAtomicCatalystRenderer implements IItemRenderer {

    private ModelAtomicCatalyst modelAtomicCatalyst;

    public ItemAtomicCatalystRenderer() {
        modelAtomicCatalyst = new ModelAtomicCatalyst();
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
                renderAtomicCatalyst(0.0F, 0.1F, 0.0F, 0.4F, false, false, false);
                return;
            }

            case EQUIPPED_FIRST_PERSON: {
                renderAtomicCatalyst(0.0F, 1.0F, 0.4F, 0.5F, true, false, false);
                break;
            }

            case INVENTORY: {
                renderAtomicCatalyst(0.0F, 0.1F, 0.0F, 0.4F, false, true, false);
                return;
            }

            case EQUIPPED: {
                renderAtomicCatalyst(0.5F, 0.5F, 0.4F, 0.5F, false, false, true);
                return;
            }

            default:
                return;
        }
    }

    // Bohr Model, because bite me.
    private void renderAtomicCatalyst(float x, float y, float z, float scale, boolean fp, boolean inv, boolean eq) {
        TextureManager texture = Minecraft.getMinecraft().renderEngine;

        glPushMatrix();
        glDisable(GL_LIGHTING);

        // Translate and Scale
        glTranslatef(x, y, z);
        glScalef(scale, scale, scale);

        // Translate, render and texture the electrons.
        translateBindRender("Main", x, y, z, 0.0D);

        // First Person translations because it's weird for some reason..
        if (fp)
            glTranslatef(0F, -0.20F, -0.17F);
        // Inventory translations, because it's also a bit weird.
        if (inv)
            glTranslatef(-0.01F, 0.12F, 0.0F);
        // Equipped translations, because it's even more retarded..
        if (eq)
            glTranslatef(-0.22F, 0F, 0F);

        // Math for the rotation periods.
        double local = (24 * Math.PI * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

        // Translate, texture, and render the electrons. Theoretically I can
        // easily add electrons.
        translateBindRender("Electron1", x, y, z, local);
        translateBindRender("Electron2", x, y, z, local);
        translateBindRender("Electron3", x, y, z, local);

        glEnable(GL_LIGHTING);
        glPopMatrix();

    }

    private void translateBindRender(String part, float x, float y, float z, double angle) {
        float sinDis = ((float) ((0.4 * (Math.sin(angle))) / 8)) * 60;
        float cosDis = ((float) ((0.4 * (Math.cos(angle))) / 8)) * 60;

        switch (part) {
            case "Main":
                glPushMatrix();
                glRotatef((float) angle, 0.0F, 1.0F, 0.0F);
                RenderUtils.bindTexture(TextureMaps.MODEL_ATOMIC_CATALYST_MAIN);
                modelAtomicCatalyst.renderPart("Main");
                glPopMatrix();
                break;
            case "Electron1":
                glPushMatrix();
                glScalef(0.4F, 0.4F, 0.4F);
                glTranslatef(x - cosDis, y - 0.5F, z - sinDis);
                RenderUtils.bindTexture(TextureMaps.MODEL_ATOMIC_CATALYST_ELECTRON_1);
                modelAtomicCatalyst.renderPart(part);
                glPopMatrix();
                break;
            case "Electron2":
                glPushMatrix();
                glScalef(0.4F, 0.4F, 0.4F);
                glTranslatef(x - sinDis, y + cosDis - 0.5F, z);
                RenderUtils.bindTexture(TextureMaps.MODEL_ATOMIC_CATALYST_ELECTRON_2);
                modelAtomicCatalyst.renderPart(part);
                glPopMatrix();
                break;
            case "Electron3":
                glPushMatrix();
                glScalef(0.4F, 0.4F, 0.4F);
                glTranslatef(x, y - sinDis - 0.5F, z + cosDis);
                RenderUtils.bindTexture(TextureMaps.MODEL_ATOMIC_CATALYST_ELECTRON_3);
                modelAtomicCatalyst.renderPart(part);
                glPopMatrix();
                break;
            default:
                break;
        }
    }

}
