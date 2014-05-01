package me.jezzadabomb.es2.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import me.jezzadabomb.es2.common.blocks.framework.BlockES;
import me.jezzadabomb.es2.common.tileentity.TileCrystalObelisk;

public class BlockCrystalObelisk extends BlockES {

    public BlockCrystalObelisk(Material material, String name) {
        super(material, name);
    }

    @Override
    public boolean renderWithModel() {
        return true;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TileCrystalObelisk();
    }

}
