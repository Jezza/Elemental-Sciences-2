package me.jezzadabomb.es2.common.blocks.construction;

import me.jezzadabomb.es2.common.blocks.framework.BlockES;
import me.jezzadabomb.es2.common.tileentity.TileAtomicShredder;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockAtomicShredder extends BlockES {

    public BlockAtomicShredder(Material material, String name) {
        super(material, name);
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TileAtomicShredder();
    }

}
