package me.jezzadabomb.es2.common.blocks;

import java.util.List;

import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDroneBay extends BlockES {

    public BlockDroneBay(Material material, String name) {
        super(material, name);
        setHardness(3.0F);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof TileDroneBay) {
            TileDroneBay droneBay = (TileDroneBay) tileEntity;
            if (droneBay.isOverChestRenderType()) {
                float offset = 1F / 16F;
                setBlockBounds(offset, -0.1F, offset, 1.0F - offset, 0.0F, 1F - offset);
            } else {
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);
            }
        }
        super.setBlockBoundsBasedOnState(blockAccess, x, y, z);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
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
