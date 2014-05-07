package me.jezzadabomb.es2.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import me.jezzadabomb.es2.common.blocks.framework.BlockES;

public class BlockStrengthenedIron extends BlockES {

    public BlockStrengthenedIron(Material material, String name) {
        super(material, name);
        setHardness(3F);
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
        return true;
    }

    @Override
    public boolean renderWithModel() {
        return false;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return null;
    }

}
