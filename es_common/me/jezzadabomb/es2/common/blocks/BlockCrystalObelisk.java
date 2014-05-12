package me.jezzadabomb.es2.common.blocks;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.blocks.framework.BlockES;
import me.jezzadabomb.es2.common.blocks.framework.BlockMetaHolder;
import me.jezzadabomb.es2.common.items.ItemBlockHolder;
import me.jezzadabomb.es2.common.tileentity.TileCrystalObelisk;

public class BlockCrystalObelisk extends BlockES {

    public BlockCrystalObelisk(Material material, String name) {
        super(material, name);
        setHardness(4F);
    }

    @Override
    public boolean renderWithModel() {
        return true;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TileCrystalObelisk();
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<ItemStack>() {
            {
                add(new ItemStack(ModBlocks.strengthenedIronBlock, 1));
            }
        };
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(ModBlocks.strengthenedIronBlock);
    }
}
