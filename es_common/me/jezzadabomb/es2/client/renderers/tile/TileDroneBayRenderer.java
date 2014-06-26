package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import me.jezzadabomb.es2.client.models.ModelDroneBay;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class TileDroneBayRenderer extends TileEntitySpecialRenderer {

    ModelDroneBay modelDroneBay;

    public TileDroneBayRenderer() {
        modelDroneBay = new ModelDroneBay();
    }

    public void renderDroneBayAt(TileDroneBay droneBay, double x, double y, double z, double f) {
        glPushMatrix();
        glDisable(GL_ALPHA_TEST);
        glDisable(GL_CULL_FACE);

        glTranslated(x + 0.5F, y + 0.0255F, z + 0.5F);

        if (RenderUtils.isPlayerRendering("ASB2")) {
            float healthNum = Minecraft.getMinecraft().thePlayer.getHealth() / 5F;

            glRotatef(-(float) (720.0 * Math.floor(healthNum) * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL), 0.0F, 1.0F, 0.0F);
        }

        if (droneBay.isOverChestRenderType()) {
            glTranslatef(0.0F, -0.125F, 0.0F);
            glScalef(0.9F, 0.9F, 0.9F);
        }

        glScaled(0.5F, 0.5F, 0.5F);

        RenderUtils.bindTexture(TextureMaps.DRONE_BAY_FRAME);

        modelDroneBay.renderPart("BaseFrame");

        RenderUtils.bindTexture(TextureMaps.DRONE_BAY_DOOR);

        float progress = droneBay.getDoorProgress();

        for (int i = 0; i < 32; i++)
            translateScaleRender(progress, i);

        glEnable(GL_CULL_FACE);
        glEnable(GL_ALPHA_TEST);
        glPopMatrix();
    }

    public void translateScaleRender(float percentage, int door) {
        door = MathHelper.clamp_int(door, 0, 31);
        percentage = MathHelper.clamp_float(percentage, 0.0F, 1.0F);
        glPushMatrix();

        float x = getXTarget(door);
        float z = getZTarget(door);
        float scale = (1.0F - percentage) / 1.55F;

        glTranslatef(x * scale, 0.0F, z * scale);

        glScalef(percentage, 1.0F, percentage);

        if (percentage > 0.0F)
            modelDroneBay.renderDoor(door);

        glPopMatrix();
    }

    private float getXTarget(int door) {
        return (float) -Math.sin(getAnimationEquation(door)) * 1.3F;
    }

    private float getZTarget(int door) {
        return (float) -Math.cos(getAnimationEquation(door)) * 1.3F;
    }

    private double getAnimationEquation(int door) {
        float magicNum = 1.1F;
        if (Reference.DRONE_BAY_DOOR_TYPE == 1)
            magicNum = 0.1F;
        else if (Reference.DRONE_BAY_DOOR_TYPE == 2)
            magicNum = 2.0F;

        if (RenderUtils.isPlayerRendering("ZimmyG"))
            magicNum = 2.0F;
        return Math.PI * door / 16 + magicNum;
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        if (tileEntity instanceof TileDroneBay)
            renderDroneBayAt((TileDroneBay) tileEntity, x, y, z, f);
    }

}
