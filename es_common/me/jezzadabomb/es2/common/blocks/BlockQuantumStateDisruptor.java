package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.tileentity.TileQuantumStateDisruptor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockQuantumStateDisruptor extends BlockES {

    public BlockQuantumStateDisruptor(Material material, String name) {
        super(material, name);
        setCreativeTab(null);
        setBlockUnbreakable();
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1F / 16F, 1.0F);
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        super.breakBlock(world, x, y, z, block, meta);
    }
    
    @Override
    public boolean renderWithModel() {
        return true;
    }

    @Override
    public TileEntity getTileEntity() {
        return new TileQuantumStateDisruptor();
    }

}
