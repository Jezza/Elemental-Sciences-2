package me.jezzadabomb.es2.common.blocks;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.blocks.framework.BlockESMeta;
import me.jezzadabomb.es2.common.blocks.framework.BlockType;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystal;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockPylon extends BlockESMeta {

    private static final ArrayList<String> names = new ArrayList<String>() {
        {
            add("pylonCrystal0");
            add("pylonCrystal1");
            add("pylonCrystal2");
        }
    };

    public BlockPylon(Material material, String name) {
        super(material, name);
        setHardness(2F);
        setBlockBounds(0.25F, 0.1F, 0.25F, 0.75F, 0.9F, 0.75F);
    }

    @Override
    public ArrayList<String> getNames() {
        return names;
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.MODEL;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TilePylonCrystal(metadata + 1);
    }

}
