package me.jezzadabomb.es2.client.renderers.entity;

import static org.lwjgl.opengl.GL11.*;

import me.jezzadabomb.es2.client.models.ModelConstructorDrone;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class EntityDroneRenderer extends Render {

    ModelConstructorDrone modelConstructorDrone;

    public EntityDroneRenderer() {
        modelConstructorDrone = new ModelConstructorDrone();
        shadowSize = 0.1F;
    }

    public void renderDrone(EntityConstructorDrone drone, double x, double y, double z, float yaw, float partialTickTime) {
        glPushMatrix();

        glTranslated(x, y, z);

        // TODO Stop it doing it in unison.
        glTranslatef(0.0F, (float) ((0.4 * (Math.sin((6 * Math.PI * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL)))) / 8), 0.0F);
        
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
        if (entity instanceof EntityConstructorDrone)
            renderDrone((EntityConstructorDrone) entity, x, y, z, yaw, partialTickTime);
    }
}
