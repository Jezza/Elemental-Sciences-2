package me.jezzadabomb.es2.common.lib.handlers;

import java.io.File;
import java.util.logging.Level;

import me.jezzadabomb.es2.common.lib.BlockIds;
import me.jezzadabomb.es2.common.lib.ItemIds;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.lib.Strings;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;

public class ConfigHandler {
    private static Configuration config;

    public static void init(String file) {
        config = new Configuration(new File(file + "ElementalSciences.cfg"));

        try {
            config.load();
            BlockIds.BLOCK_TEST = config.getBlock(Strings.BLOCK_TEST, BlockIds.BLOCK_TEST_DEFAULT).getInt();

            ItemIds.ATOMIC_CATALYST = config.getItem(Strings.ATOMIC_CATALYST, ItemIds.ATOMIC_CATALYST_DEFAULT).getInt();
            ItemIds.GLASSES = config.getItem(Strings.GLASSES, ItemIds.GLASSES_DEFAULT).getInt();

        } catch (Exception e) {
            FMLLog.log(Level.SEVERE, e, Reference.MOD_NAME + " has had a problem loading its block configuration");
        } finally {
            config.save();
        }
    }
}
