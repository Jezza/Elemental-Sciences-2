package me.jezzadabomb.es2.common.blocks;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.blocks.framework.BlockMetaHolder;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.items.ItemBlockHolder;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderCore;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderDummy;

public class BlockAtomicShredderDummy extends BlockMetaHolder {

    public static final String[] names = new String[] { "shredderDummy", "shredderGlass", "shredderDummyGlass" };

    public BlockAtomicShredderDummy(Material material, String name) {
        super(material, name);
        setHardness(5.0F);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        if (meta == 1)
            return super.getDrops(world, x, y, z, meta, fortune);
        ArrayList<ItemStack> dropList = new ArrayList<ItemStack>();
        switch (meta) {
            case 0:
                dropList.add(new ItemStack(ModBlocks.atomicShredderDummyCore, 0));
                return dropList;
            case 2:
                dropList.add(new ItemStack(this, 1));
                return dropList;
        }
        return dropList;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        switch (meta) {
            case 0:
                return new ItemStack(ModBlocks.atomicShredderDummyCore, 1, 0);
            case 2:
                return new ItemStack(this, 1, 1);
        }

        return super.getPickBlock(target, world, x, y, z);
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int oldMeta) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 0 || meta == 2) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileAtomicShredderDummy)
                ((TileAtomicShredderDummy) tile).informCore();
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 1)
            return super.onBlockActivated(world, x, y, z, player, side, hitVecX, hitVecY, hitVecZ);
        if (meta == 0 || meta == 2) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileAtomicShredderDummy)
                return ((TileAtomicShredderDummy) tile).processBlockActivation(player);
        }
        return true;
    }

    @Override
    public String[] getNames() {
        return names;
    }

    @Override
    protected Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockHolder.class;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getBlockMetadata(x, y, z) == 0)
            return true;
        return !(world.getBlock(x, y, z) == this && world.getBlockMetadata(x, y, z) == 1);
    }

    @Override
    public boolean renderWithModel() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        // String[] names = getNames();
        // for (int i = 1; i < names.length; i++)
        // list.add(new ItemStack(item, 1, i));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        switch (metadata) {
            case 0:
                return new TileAtomicShredderDummy();
            case 2:
                return new TileAtomicShredderDummy();
        }
        return null;
    }

}
