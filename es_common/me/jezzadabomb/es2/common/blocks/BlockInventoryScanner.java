package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.client.ClientProxy;
import me.jezzadabomb.es2.client.sound.Sounds;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileInventoryScanner;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockInventoryScanner extends BlockES {

    public BlockInventoryScanner(Material par2Material, String name) {
        super(par2Material, name);
        setHardness(2.5F);
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
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        
        ClientProxy.getHUDRenderer().removePacketAtXYZ(x, y - 1, z);

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    protected boolean canSendUpdatePacket() {
        return true;
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
