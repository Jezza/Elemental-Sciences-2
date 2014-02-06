package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockDroneBay extends BlockES {

    public BlockDroneBay(int id, Material material, String name) {
        super(id, material, name);
    }

    @Override
    public boolean renderWithModel() {
        return true;
    }

    @Override
    public TileEntity getTileEntity() {
        return new TileDroneBay();
    }

}
