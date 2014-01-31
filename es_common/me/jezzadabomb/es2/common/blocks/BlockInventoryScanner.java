package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.client.sound.Sounds;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockInventoryScanner extends BlockES {

    public BlockInventoryScanner(int par1, Material par2Material, String name) {
        super(par1, par2Material, name);
        setHardness(2.5F);
        setBlockBounds(0F, 0F, 0F, 1F, 1F / 16F, 1F);
    }

    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        if (world.blockHasTileEntity(x, y, z) && world.getBlockTileEntity(x, y, z) instanceof TileInventoryScanner && world.isRemote && !UtilMethods.isWearingItem(ModItems.glasses)) {
            ClientProxy.getHUDRenderer().addToRemoveList(x, y - 1, z);
        }
        return super.removeBlockByPlayer(world, player, x, y, z);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase livingBase, ItemStack itemStack) {
        Sounds.SCANNING_WAVE.playAtPlayer(livingBase);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.blockHasTileEntity(x, y - 1, z) ? (world.getBlockTileEntity(x, y - 1, z) instanceof IInventory) : false;
    }

    @Override
    public boolean renderWithModel() {
        return true;
    }

    @Override
    public TileEntity getTileEntity() {
        return new TileInventoryScanner();
    }
}
