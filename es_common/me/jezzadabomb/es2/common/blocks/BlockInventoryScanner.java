package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.client.sound.Sounds;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilHelpers;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockInventoryScanner extends BlockES {

    public BlockInventoryScanner(int par1, Material par2Material, String name) {
        super(par1, par2Material, name);
        setHardness(2.5F);
        setBlockBounds(0F, 0F, 0F, 1F, 0.1F, 1F);
    }

    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        ESLogger.debug("Broken Block");
        if (world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileInventoryScanner) {
            if (world.isRemote) {
                if (!UtilHelpers.isWearingItem(ModItems.glasses)) {
                    ClientProxy.hudRenderer.addToRemoveList(x, y - 1, z);
                }
            }
        }
        return super.removeBlockByPlayer(world, player, x, y, z);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase livingBase, ItemStack itemStack) {
        Sounds.SCANNING_WAVE.playAtPlayer(livingBase);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        y -= 1;
        return world.blockHasTileEntity(x, y, z) ? (world.getBlockTileEntity(x, y, z) instanceof IInventory) : false;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileInventoryScanner();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
