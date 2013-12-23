package me.jezzadabomb.es2.common.core.handlers;

import static org.lwjgl.opengl.GL11.*;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.utils.UtilHelpers;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HoverBootsHandler {

	private boolean hovering = false;
	private boolean hitGround = false;
	private boolean justStartedHovering = true;
	private int WAIT_TIME = 40;
	private int timeHovering = WAIT_TIME;

	public HoverBootsHandler() {
	}

	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if (hovering) {
			// Bind texture
			RenderUtils.bindTexture(TextureMaps.HOVER_TEXTURE);

			// Flips it upright
			glRotated(90, 1.0D, 0.0D, 0.0D);
			// Push it under the player
			glTranslated(-1.3D, -1.3D, 0.0D);
			// Scale it, so it isn't massive.
			glScaled(0.01D, 0.01D, 0.01D);

			glColor4f(1.0F, 1.0F, 0.6F, 0.5F);
			
			glEnable(GL_BLEND);
			glDisable(GL_LIGHTING);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			// Get the Util class to render it for us.
			RenderUtils.drawTexturedQuad(0, 0, 0, 0, 256, 256, 161);
			
			glEnable(GL_LIGHTING);
			glDisable(GL_BLEND);
		}
	}

	@ForgeSubscribe
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (event.entity == null || !(event.entity instanceof EntityPlayer)) {
			return;
		}
		if (UtilHelpers.isWearingItem(ModItems.hoverBoots)) {
			EntityPlayer player = (EntityPlayer) event.entity;
			if (!hitGround || player.onGround) {
				hitGround = player.onGround;
				return;
			}

			if (hovering) {
				timeHovering--;
				if (timeHovering < 1) {
					timeHovering = WAIT_TIME;
					hovering = hitGround = false;
					justStartedHovering = true;
					return;
				}
			}
			hovering = checkForHovering(event, player);
			if (hovering)
				startHovering(event, player);
		}else{
			hovering = false;
		}
	}

	private boolean checkForHovering(LivingUpdateEvent event, EntityPlayer player) {
		return player.fallDistance > 0 || player.isAirBorne || hovering;
	}

	private void startHovering(LivingUpdateEvent event, EntityPlayer player) {
		if(justStartedHovering){
			justStartedHovering = false;
			player.moveEntity(0, 0.1F, 0);
		}
		if (player.motionY < 0)
			player.motionY = 0;
		player.moveEntity(player.motionX, 0, player.motionZ);
		double slipperness = 0.99999D;
		player.motionX *= slipperness;
		player.motionZ *= slipperness;
	}
}
