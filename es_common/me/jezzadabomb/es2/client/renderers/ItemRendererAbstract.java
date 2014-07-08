package me.jezzadabomb.es2.client.renderers;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import me.jezzadabomb.es2.client.models.ModelCustomAbstract;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public abstract class ItemRendererAbstract implements IItemRenderer {

    public final ModelCustomAbstract mainModel;
    public final ResourceLocation textureMap;

    public ItemRendererAbstract(ModelCustomAbstract model, ResourceLocation textureMap) {
        this.mainModel = model;
        this.textureMap = textureMap;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    public void renderItem(float x, float y, float z, float scale) {
        glPushMatrix();

        glTranslatef(x, y, z);

        glScalef(scale, scale, scale);

        bindTexture();
        renderModel();

        glPopMatrix();
    }

    public void bindTexture() {
        RenderUtils.bindTexture(textureMap);
    }

    public void renderModel() {
        mainModel.renderAll();
    }

}
