package me.jezzadabomb.es2.client.renderers.entity;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.ModelConstructorDrone;
import me.jezzadabomb.es2.common.entities.EntityDrone;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class EntityDroneRenderer extends Render {

    ModelConstructorDrone modelConstructorDrone;

    public EntityDroneRenderer() {
        modelConstructorDrone = new ModelConstructorDrone();
        shadowOpaque = 0.0F;
    }

    public void renderDrone(EntityDrone drone, double x, double y, double z, float yaw, float partialTickTime) {
        glPushMatrix();

        glTranslated(x, y, z);
        glScalef(0.1F, 0.1F, 0.1F);
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
