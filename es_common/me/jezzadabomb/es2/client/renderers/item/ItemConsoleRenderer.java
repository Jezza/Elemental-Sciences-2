package me.jezzadabomb.es2.client.renderers.item;

import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.client.models.ModelPlate;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public class ItemConsoleRenderer implements IItemRenderer {

    ModelPlate modelPlate;
    int renderType = 0;

    public ItemConsoleRenderer() {
        modelPlate = new ModelPlate();
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
                renderConsole(-0.5F, 0.0F, -0.5F, 1.0F);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                renderType = 1;
                renderConsole(0.9F, 0.5F, 0.8F, 0.7F);
                renderType = 0;
                break;
            }
            case INVENTORY: {
                renderConsole(-0.2F, -0.4F, -0.2F, 0.8F);
                break;
            }
            case EQUIPPED: {
                renderConsole(0.0F, 0.4F, 0.1F, 1.0F);
                break;
            }
            default:
                return;
        }
    }

    private void renderConsole(float x, float y, float z, float scale) {
        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef((float) x, (float) y, (float) z);
        glScalef(scale, scale, scale);
        if (renderType == 1)
            glRotatef(180F, 0.0F, 1.0F, 0.0F);
        glPushMatrix();
        glTranslatef(0.5F, 1.5F - 0.625F, 0.5F);
        glRotated(180.0D, 1.0D, 0.0D, 0.0D);
        for (int i = 0; i <= 1; i++) {
            RenderUtils.bindTexture(TextureMaps.CONSOLE_BASE);
            modelPlate.render();
            glTranslatef(0.0F, -0.06F, 0.0F);
            glScalef(0.75F, 1.0F, 0.75F);
        }
        glPopMatrix();

        glPushMatrix();
        glTranslatef(0.5F, 0.5F, 0.5F);
        glRotatef(90F, 0.0F, 1.0F, 0.0F);

        glTranslatef(0.0F, (float) ((0.4 * (Math.sin((24 * Math.PI * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL)))) / 8), -0.6F);
        glRotatef(90F / 2F, 1.0F, 0.0F, 0.0F);
        RenderUtils.bindTexture(TextureMaps.CONSOLE_SCREEN);
        modelPlate.render();
        glPopMatrix();

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

}
