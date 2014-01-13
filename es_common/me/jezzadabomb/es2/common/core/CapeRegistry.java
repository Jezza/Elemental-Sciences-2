package me.jezzadabomb.es2.common.core;

import cofh.api.core.RegistryAccess;

public class CapeRegistry {

	private static final String[] players = new String[] { "Jezzadabomb338", "ZimmyG" };

	private static final String[] urls = new String[] { "https://github.com/Jezzadabomb338/Elemental-Sciences-2/blob/master/resources/assets/elementalsciences2/textures/misc/JezzaCape.png" };

	public static void init() {
		registerCape(players[0], urls[0]);
		registerCape(players[1], urls[0]);
	}

	private static void registerCape(String player, String url) {
		if (!RegistryAccess.capeRegistry.register(player, url))
			ESLogger.info("Failed registering: " + player);
	}
}
