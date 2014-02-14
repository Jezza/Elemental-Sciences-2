package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockConsole extends BlockES {

    public BlockConsole(Material material, String name) {
        super(material, name);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 7F / 8F, 1.0F);
        setBlockUnbreakable();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        if (world.getTileEntity(x, y, z) instanceof TileConsole) {
            int direction = 0;
            int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

            if (facing == 0) {
                direction = ForgeDirection.NORTH.ordinal();
            } else if (facing == 1) {
                direction = ForgeDirection.EAST.ordinal();
            } else if (facing == 2) {
                direction = ForgeDirection.SOUTH.ordinal();
            } else if (facing == 3) {
                direction = ForgeDirection.WEST.ordinal();
            }
            TileConsole tC = ((TileConsole) world.getTileEntity(x, y, z));
            tC.setOrientation(direction);
            tC.updateRenderCables();
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVectorX, float hitVectorY, float hitVectorZ) {
        return false;
    }

    private TileConsole getNearbyMaster(World world, int x, int y, int z) {
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++)
                for (int k = -1; k < 2; k++) {
                    if (i == 0 && j == 0 && k == 0 || !(world.getTileEntity(x + i, y + j, z + k) != null))
                        continue;
                    if (world.getTileEntity(x + i, y + j, z + k) instanceof TileAtomicConstructor) {
                        TileAtomicConstructor atomic = ((TileAtomicConstructor) world.getTileEntity(x + i, y + j, z + k));
                        if (atomic.hasConsole())
                            return atomic.getConsole();
                    }
                }
        return null;
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
        return new TileConsole();
    }

}
