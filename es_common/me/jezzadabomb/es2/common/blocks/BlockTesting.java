package me.jezzadabomb.es2.common.blocks;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import me.jezzadabomb.es2.common.blocks.framework.BlockESMeta;

public class BlockTesting extends BlockESMeta {

    public static final ArrayList<String> names = new ArrayList<String>() {
        {
            add("steamPunk");
            add("steamPunk2");
            add("steamPunk3");
            add("steamPunk4");
            add("steamPunk5");
            add("steamPunk6");
            add("shredderCore");
            add("atomicShredder2");
            add("metal");
            add("fireRock");
            add("plasticBlock");
            add("console");
            add("crystalBlock2");
            add("sapphire");
            add("lapisBrick");
        }
    };

    public BlockTesting(Material material, String name) {
        super(material, name);
    }

    @Override
    public ArrayList<String> getNames() {
        return names;
    }

}
