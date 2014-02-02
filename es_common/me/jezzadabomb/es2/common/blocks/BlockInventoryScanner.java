package me.jezzadabomb.es2.common.blocks;

import cofh.api.block.IDismantleable;
import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.client.sound.Sounds;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockInventoryScanner extends BlockES {

    public BlockInventoryScanner(int par1, Material par2Material, String name) {
        super(par1, par2Material, name);
        setBlockUnbreakable();
        setBlockBounds(0F, 0F, 0F, 1F, 1F / 16F, 1F);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase livingBase, ItemStack itemStack) {
        Sounds.SCANNING_WAVE.playAtPlayer(livingBase);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return UtilMethods.isIInventory(world, x, y - 1, z);
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
