package me.jezzadabomb.es2.common.core.handlers;

import static org.lwjgl.opengl.GL11.*;
import me.jezzadabomb.es2.client.utils.RenderUtils;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilHelpers;
import me.jezzadabomb.es2.common.items.ItemHoverBoots;
import me.jezzadabomb.es2.common.lib.TextureMaps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HoverBootsHandler {

	private boolean hovering = false;
	private boolean hitGround = false;
	private boolean justStartedHovering = true;
	private float WAIT_TIME = 60;
	private float timeHovering = WAIT_TIME;

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

			glColor4f(1.0F, 1.0F, 0.6F, (timeHovering / (WAIT_TIME * 2)) + 0.1F);

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
			if (player.onGround) {
				justStartedHovering = true;
				hovering = hitGround = false;
				return;
			}

			if (timeHovering < WAIT_TIME / 2 && player.isCollided) {
				resetValues(player);
				return;
			}

			checkForHovering(event, player);

			if (hovering) {
				if (--timeHovering < 1) {
					resetValues(player);
					return;
				}
			}

			if (hovering) {
				startHovering(event, player);
			}
		} else {
			hovering = false;
		}
	}

	private void resetValues(EntityPlayer player) {
		timeHovering = WAIT_TIME;
		justStartedHovering = hitGround = true;
		hovering = false;
	}

	private void checkForHovering(LivingUpdateEvent event, EntityPlayer player) {
		hovering = (player.fallDistance > 0 || hovering) && !hitGround;
	}

	private void startHovering(LivingUpdateEvent event, EntityPlayer player) {
//		if (!player.worldObj.isRemote) {
			if (justStartedHovering) {
				justStartedHovering = false;
				player.moveEntity(0, 0.08F, 0);
			}
			if (player.motionY < 0)
				player.motionY = 0;
			player.moveEntity(player.motionX, 0, player.motionZ);
			double slipperness = 0.95;
			player.motionX *= slipperness;
			player.motionZ *= slipperness;
//		}
	}
}
