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
import me.jezzadabomb.es2.client.models.ModelPlate;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemConsoleRenderer implements IItemRenderer {

    ModelPlate modelPlate;

    public ItemConsoleRenderer() {
        modelPlate = new ModelPlate();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY: {
                renderConsole(0.0F, 1.2F, 0.0F, 1.0F);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                renderConsole(0.4F, 1.5F, 0.5F, 1.0F);
                break;
            }
            case INVENTORY: {
                renderConsole(0.0F, 1.0F, 0.0F, 1.0F);
                break;
            }
            case EQUIPPED: {
                renderConsole(0.4F, 1.5F, 0.4F, 1.0F);
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

        glPushMatrix();
        glTranslatef(0.5F, 1.5F - 0.625F, 0.5F);
        glRotated(180.0D, 1.0D, 0.0D, 0.0D);
        for (int i = 0; i < 2; i++) {
            RenderUtils.bindTexture(TextureMaps.CONSOLE_BASE);
            modelPlate.render();
            glTranslatef(0.0F, -0.06F, 0.0F);
            glScalef(0.75F, 1.0F, 0.75F);
        }
        glPopMatrix();

        int tempNum = 1;

        glPushMatrix();
        glTranslatef(0.5F, 0.5F, 0.5F);
        glRotatef(90F * tempNum, 0.0F, 1.0F, 0.0F);

        glTranslatef(0.0F, 0.0F, -0.6F);
        glRotatef(90F / 2F, 1.0F, 0.0F, 0.0F);

        RenderUtils.bindTexture(TextureMaps.CONSOLE_SCREEN);
        modelPlate.render();

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

}
