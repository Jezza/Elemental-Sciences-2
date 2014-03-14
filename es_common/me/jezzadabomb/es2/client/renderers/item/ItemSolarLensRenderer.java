package me.jezzadabomb.es2.client.renderers.item;

import static org.lwjgl.opengl.GL11.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import me.jezzadabomb.es2.client.models.ModelSolarLens;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TileSolarLens;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public class ItemSolarLensRenderer implements IItemRenderer {

    ModelSolarLens modelSolarLens;

    public ItemSolarLensRenderer() {
        modelSolarLens = new ModelSolarLens();
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
            case ENTITY:
                renderSolarLens(0.0F, 1.4F, 0.0F, 0.45F);
                break;
            case EQUIPPED:
                renderSolarLens(0.5F, 1.3F, 0.4F, 0.45F);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderSolarLens(0.0F, 1.3F, 0.5F, 0.4F);
                break;
            case INVENTORY:
                renderSolarLens(0.0F, 0.50F, 0.0F, 0.43F);
                break;
            default:
                break;
        }
    }

    public void renderSolarLens(float x, float y, float z, float scale) {
        float rotationAngle = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
        float hoverHeight = (float) ((0.4 * (Math.sin((24 * Math.PI * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL)))) / 8);

        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef(x, y, z);

        glRotatef(0.0F, 0.0F, 1.0F, 0.0F);

        glScalef(scale, scale, scale);

        glPushMatrix();
        glTranslated(0.0F, hoverHeight, 0.0F);
        glRotatef(-rotationAngle, 0.0F, 1.0F, 0.0F);
        translateBindRender("lens");
        glPopMatrix();

        glPushMatrix();
        glTranslated(0.0F, -hoverHeight - 1.2F, 0.0F);
        glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
        translateBindRender("loop");
        glPopMatrix();

        glPushMatrix();
        glTranslated(0.0F, -hoverHeight - 1.7F, 0.0F);
        glRotatef(rotationAngle * 2, 0.0F, 1.0F, 0.0F);
        glScalef(0.9F, 0.9F, 0.9F);
        translateBindRender("loop");
        glPopMatrix();

        glPushMatrix();
        glTranslated(0.0F, -hoverHeight - 2.2F, 0.0F);
        glRotatef(rotationAngle * 4, 0.0F, 1.0F, 0.0F);
        glScalef(0.8F, 0.8F, 0.8F);
        translateBindRender("loop");
        glPopMatrix();

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    private void translateBindRender(String part) {

        glPushMatrix();
        switch (part) {
            case "lens":
                glDisable(GL_CULL_FACE);
                RenderUtils.bindTexture(TextureMaps.SOLAR_LENS);
                modelSolarLens.renderPart(part);
                glEnable(GL_CULL_FACE);
                break;
            case "loop":
                RenderUtils.bindTexture(TextureMaps.SOLAR_LOOP);
                modelSolarLens.renderPart(part);
                break;
            default:
                glPopMatrix();
                return;
        }
        glPopMatrix();
    }

}
