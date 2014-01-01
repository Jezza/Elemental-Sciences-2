package me.jezzadabomb.es2.client.renderers;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class QuantumBombRenderer {

	private String player = null;

	@ForgeSubscribe
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		String tempString = ElementalSciences2.proxy.quantumBomb.getPlayer();
		if (tempString == null || tempString.equals("null"))
			return;
		if (!tempString.equals(player)) {
			player = tempString;
		}

		// TODO write rendering code
	}
}
