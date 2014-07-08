package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.common.blocks.framework.BlockES;
import me.jezzadabomb.es2.common.blocks.framework.BlockType;
import me.jezzadabomb.es2.common.tileentity.TileQuantumStateDisruptor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

public class BlockQuantumStateDisruptor extends BlockES {

    public BlockQuantumStateDisruptor(Material material, String name) {
        super(material, name);
        setCreativeTab(null);
        setBlockUnbreakable();
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1F / 16F, 1.0F);
    }

    @Override
    public BlockType getBlockType() {
        return BlockType.MODEL;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TileQuantumStateDisruptor();
    }
}
