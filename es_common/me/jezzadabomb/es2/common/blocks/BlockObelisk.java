package me.jezzadabomb.es2.common.blocks;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.blocks.framework.BlockESMeta;
import me.jezzadabomb.es2.common.tileentity.TileObelisk;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystal;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockObelisk extends BlockESMeta {

    public static final String[] names = new String[] { "obelisk0", "obelisk1", "obelisk2" };

    public BlockObelisk(Material material, String name) {
        super(material, name);
        setHardness(4F);
    }

    public boolean renderWithModel() {
        return true;
    }

    @Override
    public void onBlockRemoval(World world, int x, int y, int z) {
        super.onBlockRemoval(world, x, y, z);
        for (int i = 1; i <= 3; i++) {
            TileEntity tileEntity = world.getTileEntity(x, y + i, z);
            if (tileEntity instanceof TilePylonCrystal)
                ((TilePylonCrystal) tileEntity).onBlockRemoval(world, x, y, z);
        }
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TileObelisk();
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

    @Override
    public String[] getNames() {
        return names;
    }
}
