package me.jezzadabomb.es2.client.renderers.entity;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.drones.ModelDrone;
import me.jezzadabomb.es2.common.entities.EntityDrone;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityDroneRenderer extends Render {

    ModelDrone modelConstructorDrone;
    boolean shouldHover;

    public EntityDroneRenderer(ModelDrone modelDrone, boolean shouldHover) {
        modelConstructorDrone = modelDrone;
        this.shouldHover = shouldHover;
        shadowSize = 0.1F;
    }

    public void renderDrone(EntityDrone drone, double x, double y, double z, float yaw, float partialTickTime) {
        glPushMatrix();

        glTranslated(x, y + 0.15D, z);

        if (shouldHover)
            glTranslatef(0.0F, (float) ((0.4 * (Math.sin((6 * Math.PI * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) + drone.randomHoverSeed))) / 8), 0.0F);

        glRotatef(-(float) (720.0 * 1 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL), 0.0F, 1.0F, 0.0F);

        float scale = 0.15F;

        glScalef(scale, scale, scale);

        bindEntityTexture(drone);

        modelConstructorDrone.render();

        glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TextureMaps.CONSTRUCTOR_DRONE;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime) {
        if (entity instanceof EntityDrone)
            renderDrone((EntityDrone) entity, x, y, z, yaw, partialTickTime);
    }
}
