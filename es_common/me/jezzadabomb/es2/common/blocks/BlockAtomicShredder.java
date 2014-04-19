package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.blocks.framework.BlockES;
import me.jezzadabomb.es2.common.core.utils.Identifier;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderCore;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockAtomicShredder extends BlockES {

    public BlockAtomicShredder(Material material, String name) {
        super(material, name);
        // TODO Remove this
        // setCreativeTab(null);
    }

    @Override
    public boolean renderWithModel() {
        return true;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TileAtomicShredderCore();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (Identifier.isAtomicShredderCore(world, x, y, z)) {
            ItemStack itemStack = player.getCurrentEquippedItem();
            TileAtomicShredderCore core = (TileAtomicShredderCore) world.getTileEntity(x, y, z);
            return core.processBlockActivation(player);
        }
        return false;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(ModBlocks.atomicShredderDummyCore, 1, 1);
    }

}
