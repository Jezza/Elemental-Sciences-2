package me.jezzadabomb.es2.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import me.jezzadabomb.es2.common.blocks.framework.BlockES;

public class BlockPlastic extends BlockES {

    public BlockPlastic(Material material, String name) {
        super(material, name);
        setHardness(3F);
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
