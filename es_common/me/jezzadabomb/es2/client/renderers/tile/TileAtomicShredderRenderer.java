package me.jezzadabomb.es2.client.renderers.tile;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.ModelAtomicShredder;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import me.jezzadabomb.es2.common.shredder.AtomicShredderItemHandler;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileAtomicShredderRenderer extends TileEntitySpecialRenderer {

    ModelAtomicShredder modelAtomicShredder;

    public TileAtomicShredderRenderer() {
        modelAtomicShredder = new ModelAtomicShredder();
    }

    public void renderAtomicShredderAt(TileAtomicShredderCore atomicShredder, double x, double y, double z, float tick) {
        glPushMatrix();

        glTranslated(x + 0.5F, y + 0.5F, z + 0.5F);

        float scale = 1.001F;

        glScalef(scale, scale, scale);

        glRotatef(180F, 0.0F, 0.0F, 0.0F);
        // glRotatef(90F, 0.0F, 1.0F, 0.0F);

        glDisable(GL_CULL_FACE);
        renderShredderFrame(atomicShredder, x, y, z, tick);
        renderHoverPlate(atomicShredder, x, y, z, tick);
        renderShredderPlate(atomicShredder, x, y, z, tick);
        renderShredderRing(atomicShredder, x, y, z, tick);
        renderItemCasing(atomicShredder, x, y, z, tick);
        renderInventory(atomicShredder, x, y, z, tick);
        glEnable(GL_CULL_FACE);

        glPopMatrix();
    }

    public void renderShredderFrame(TileAtomicShredderCore atomicShredder, double x, double y, double z, float tick) {
        glPushMatrix();

        RenderUtils.bindTexture(TextureMaps.ATOMIC_SHREDDER);
        modelAtomicShredder.renderPart("Frame");

        glPopMatrix();
    }

    public void renderHoverPlate(TileAtomicShredderCore atomicShredder, double x, double y, double z, float tick) {
        glPushMatrix();

        RenderUtils.bindTexture(TextureMaps.ATOMIC_SHREDDER_HOVER_PLATE);
        modelAtomicShredder.renderPart("BasePlate");

        RenderUtils.bindTexture(TextureMaps.ATOMIC_SHREDDER_HOVER_RING);
        modelAtomicShredder.renderPart("BaseRing");

        glPopMatrix();
    }

    public void renderShredderPlate(TileAtomicShredderCore atomicShredder, double x, double y, double z, float tick) {
        for (int i = 1; i <= 2; i++) {
            glPushMatrix();

            RenderUtils.bindTexture(TextureMaps.ATOMIC_SHREDDER_PLATES);
            modelAtomicShredder.renderPart("Shredder" + i);

            glPopMatrix();
        }
    }

    public void renderShredderRing(TileAtomicShredderCore atomicShredder, double x, double y, double z, float tick) {
        for (int i = 1; i <= 2; i++) {
            glPushMatrix();

            AtomicShredderItemHandler itemHandler = atomicShredder.getItemHandler();

            float rotationalIncrement = itemHandler.getRing(i).getAmount();

            double rotation = 0.0F;

            glRotated(rotation, 0.0F, 0.0F, 1.0F);

            RenderUtils.bindTexture(TextureMaps.ATOMIC_SHREDDER_RINGS);
            modelAtomicShredder.renderPart("ShredderRing" + i);

            glPopMatrix();
        }
    }

    // DONE
    public void renderItemCasing(TileAtomicShredderCore atomicShredder, double x, double y, double z, float tick) {
        AtomicShredderItemHandler itemHandler = atomicShredder.getItemHandler();

        if (itemHandler.isCaseOpen())
            return;

        glPushMatrix();
        RenderUtils.bindTexture(TextureMaps.ATOMIC_SHREDDER_CASING);
        float distance = itemHandler.getCasingDistance();

        for (int i = 1; i <= 8; i++) {
            glPushMatrix();
            CoordSet relativeSet = getRelativeDirection(i);
            glTranslatef(relativeSet.getX() * -distance, relativeSet.getY() * -distance, relativeSet.getZ() * -distance);
            modelAtomicShredder.renderCasing(i);
            glPopMatrix();
        }

        glPopMatrix();
    }

    private CoordSet getRelativeDirection(int casingNum) {
        switch (casingNum) {
            case 1:
                return new CoordSet(1, -1, 1);
            case 2:
                return new CoordSet(-1, 1, -1);
            case 3:
                return new CoordSet(-1, 1, 1);
            case 4:
                return new CoordSet(1, 1, 1);
            case 5:
                return new CoordSet(1, 1, -1);
            case 6:
                return new CoordSet(-1, -1, -1);
            case 7:
                return new CoordSet(1, -1, -1);
            case 8:
                return new CoordSet(-1, -1, 1);
        }
        return new CoordSet(0, 0, 0);
    }

    public void renderInventory(TileAtomicShredderCore atomicShredder, double x, double y, double z, float tick) {
        glPushMatrix();

        World world = atomicShredder.getWorldObj();

        AtomicShredderItemHandler itemHandler = atomicShredder.getItemHandler();

        if (itemHandler.hasCasedItem())
            renderItemStack(tick, world, itemHandler.getCasedItem(), 0.0F, true, true);

        if (itemHandler.hasHoverItem()) {
            glTranslatef(0.0F, 0.8F - itemHandler.getHoverHeight(), 0.0F);
            renderItemStack(tick, world, itemHandler.getHoverItem(), 50.0F, true, false);
        }

        glPopMatrix();
    }

    private void renderItemStack(float tick, World world, ItemStack itemStack, float externalRotation, boolean rotate, boolean hover) {
        glPushMatrix();

        glRotatef(180F, 0.0F, 0.0F, 0.0F);
        EntityItem entityItem = new EntityItem(world, 0.0D, 0.0D, 0.0D, itemStack);
        Item item = itemStack.getItem();
        entityItem.getEntityItem().stackSize = 1;
        entityItem.hoverStart = 0.0F;

        boolean flag = item instanceof ItemBlock;

        if (!flag) {
            float scale = 0.8F;
            glScalef(scale, scale, scale);
        }

        if (rotate) {
            float rotationAngle = (float) RenderUtils.rotationEquation(2, externalRotation);
            glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
        }

        if (hover) {
            float hoverHeight = (float) RenderUtils.hoverEquation(0.4F, 2F, 8F);
            glTranslatef(0.0F, hoverHeight, 0.0F);
        }

        double translationY = flag ? -0.125D : -0.25D;

        RenderManager.instance.renderEntityWithPosYaw(entityItem, 0.0D, translationY, 0.0D, 0.0F, 1.0F);

        if (item == Items.compass) {
            TextureAtlasSprite textureAtlasSprite = ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationItemsTexture)).getAtlasSprite(Items.compass.getIconIndex(entityItem.getEntityItem()).getIconName());

            if (textureAtlasSprite.getFrameCount() > 0)
                textureAtlasSprite.updateAnimation();
        }

        glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        if (tileEntity instanceof TileAtomicShredderCore)
            renderAtomicShredderAt((TileAtomicShredderCore) tileEntity, x, y, z, tick);
    }

}
