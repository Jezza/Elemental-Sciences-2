package me.jezzadabomb.es2.common.blocks.power;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import me.jezzadabomb.es2.common.blocks.framework.BlockES;
import me.jezzadabomb.es2.common.blocks.framework.BlockType;
import me.jezzadabomb.es2.common.tileentity.TileObelisk;

public class BlockObelisk extends BlockES {

    public BlockObelisk(Material material, String name) {
        super(material, name);
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.MODEL;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TileObelisk();
    }

}
