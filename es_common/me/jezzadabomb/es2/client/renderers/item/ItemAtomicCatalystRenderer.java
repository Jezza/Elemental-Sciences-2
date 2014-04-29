package me.jezzadabomb.es2.client.renderers.item;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.ModelCube;
import me.jezzadabomb.es2.client.utils.AtomicCatalystRenderState;
import me.jezzadabomb.es2.client.utils.Colour;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.AtomicCatalystAttribute;
import me.jezzadabomb.es2.common.items.ItemAtomicCatalyst;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemAtomicCatalystRenderer implements IItemRenderer {

    private ModelCube cube;

    public ItemAtomicCatalystRenderer() {
        cube = new ModelCube();
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
                renderAtomicCatalyst(item, 0.0F, 0.0F, 0.0F, 0.4F);
                break;
            case EQUIPPED_FIRST_PERSON:
                renderAtomicCatalyst(item, 0.0F, 0.9F, 0.4F, 0.4F);
                break;
            case INVENTORY:
                renderAtomicCatalyst(item, 0.0F, 0.0F, 0.0F, 0.4F);
                break;
            case EQUIPPED:
                renderAtomicCatalyst(item, 0.5F, 0.8F, 0.5F, 0.4F);
                break;
            default:
                break;
        }
    }

    private void renderAtomicCatalyst(ItemStack itemStack, float x, float y, float z, float scale) {
        glPushMatrix();
        glDisable(GL_LIGHTING);

        glTranslatef(x, y, z);
        glScalef(scale, scale, scale);

        if (!itemStack.hasTagCompound())
            ItemAtomicCatalyst.setTag(itemStack);
        AtomicCatalystAttribute atomic = AtomicCatalystAttribute.readFromNBT(itemStack.getTagCompound());

        int fortune = atomic.getFortune();
        int speed = atomic.getSpeed() + 1;
        int strength = atomic.getStrength();

        glPushMatrix();
        if (fortune > 0)
            glRotated(RenderUtils.rotationEquation(fortune, 0), 1.0F, 1.0F, 1.0F);

        RenderUtils.bindTexture(TextureMaps.ATOMIC_CATALYST_MAIN);
        cube.renderAll();
        glPopMatrix();

        double angle = (speed * 6 * Math.PI * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

        double cosDis = 3 * Math.cos(angle);
        double sinDis = 3 * Math.sin(angle);

        glScalef(0.4F, 0.4F, 0.4F);
        for (int i = 0; i < strength; i++)
            renderElectron(i, cosDis, sinDis);

        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    private void renderElectron(int electron, double cosDis, double sinDis) {
        glPushMatrix();
        getRelativeDirection(electron).translate(cosDis, sinDis);

        RenderUtils.bindTexture(TextureMaps.ATOMIC_CATALYST_ELECTRON);
        cube.renderAll();

        glPopMatrix();
    }

    private AtomicCatalystRenderState getRelativeDirection(int casingNum) {
        switch (casingNum) {
            case 0:
                return new AtomicCatalystRenderState(1, 0, -1);
            case 1:
                return new AtomicCatalystRenderState(-1, 1, 0);
            case 2:
                return new AtomicCatalystRenderState(0, -1, 1);
            case 3:
                return new AtomicCatalystRenderState(1, -1, -1);
            case 4:
                return new AtomicCatalystRenderState(-1, -2, 1);
            case 5:
                return new AtomicCatalystRenderState(2, 1, -1);
            case 6:
                return new AtomicCatalystRenderState(-1, 1, -1);
            case 7:
                return new AtomicCatalystRenderState(-2, 1, -1);
        }
        return new AtomicCatalystRenderState(0, 0, 0);
    }
}
