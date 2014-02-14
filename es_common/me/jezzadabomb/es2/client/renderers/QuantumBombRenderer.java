package me.jezzadabomb.es2.client.renderers;

import me.jezzadabomb.es2.ElementalSciences2;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class QuantumBombRenderer {

	private String player = null;

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
	    player = ElementalSciences2.proxy.quantumBomb.getPlayer();
		if (player == null || player.equals("null"))
			return;

		// TODO write rendering code
	}
}
