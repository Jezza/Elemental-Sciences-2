package me.jezzadabomb.es2.common;

import net.minecraft.entity.Entity;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.entities.EntityCombatDrone;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.lib.Strings;
import cpw.mods.fml.common.registry.EntityRegistry;

public class Entities {

    private static int index = 0;

    public static void init() {
        registerEntity(EntityConstructorDrone.class, Strings.CONSTRUCTOR_DRONE, 3);
        registerEntity(EntityCombatDrone.class, Strings.COMBAT_DRONE, 2);
    }

    private static void registerEntity(Class<? extends Entity> clazz, String entityName, int updateTime) {
        EntityRegistry.registerModEntity(clazz, entityName, index++, ElementalSciences2.instance, 80, updateTime, true);
    }

}
