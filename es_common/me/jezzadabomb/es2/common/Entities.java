package me.jezzadabomb.es2.common;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.lib.Strings;
import cpw.mods.fml.common.registry.EntityRegistry;

public class Entities {

    private static int index = 0;

    public static void init() {
        EntityRegistry.registerModEntity(EntityConstructorDrone.class, Strings.CONSTRUCTOR_DRONE, index++, ElementalSciences2.instance, 80, 2, true);
    }
}
