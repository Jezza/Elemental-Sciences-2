package me.jezzadabomb.es2.common.blocks;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.blocks.framework.BlockESMeta;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockResourceBlock extends BlockESMeta {

    private static final ArrayList<String> names = new ArrayList<String>() {
        {
            add("strengthenedIronBlock");
            add("crystalBlock");
        }
    };

    public BlockResourceBlock(Material material, String name) {
        super(material, name);
        setHardness(3F);
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
        return true;
    }

    @Override
    public ArrayList<String> getNames() {
        return names;
    }

}
