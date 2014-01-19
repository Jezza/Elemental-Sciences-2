package me.jezzadabomb.es2.common.blocks;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import me.jezzadabomb.es2.client.drone.DroneState;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.tileentity.TileAtomicConstructor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockAtomicConstructor extends BlockES {

    public BlockAtomicConstructor(int id, Material material, String name) {
        super(id, material, name);
    }

    @Override
    protected boolean canSendUpdatePacket() {
        return true;
    }

    @Override
    public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB aabb, List list, Entity ent) {
        TileEntity te = world.getBlockTileEntity(i, j, k);
        if (te instanceof TileAtomicConstructor) {
            TileAtomicConstructor tAC = (TileAtomicConstructor) te;
            boolean[] renderMatrix = tAC.constructRenderMatrix();
            if (renderMatrix == null)
                return;
            float thickness = 1F / 16F;

            if (renderMatrix[0]) {
                this.setBlockBounds(thickness, 0.0F, thickness, 1.0F - thickness, thickness, 1.0F - thickness);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            if (renderMatrix[5]) {
                this.setBlockBounds(0.0F, 1.0F - thickness, 0.0F, 1.0F, 1.0F, thickness);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            if (renderMatrix[6]) {
                this.setBlockBounds(0.0F, 1.0F - thickness, 0.0F, thickness, 1.0F, 1.0F);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            if (renderMatrix[7]) {
                this.setBlockBounds(1.0F - thickness, 1.0F - thickness, 0.0F, 1.0F, 1.0F, 1.0F);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            if (renderMatrix[8]) {
                this.setBlockBounds(0.0F, 1.0F - thickness, 1.0F - thickness, 1.0F, 1.0F, 1.0F);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            if (renderMatrix[9]) {
                this.setBlockBounds(1.0F - thickness, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            if (renderMatrix[10]) {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, thickness, 1.0F, thickness);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            if (renderMatrix[11]) {
                this.setBlockBounds(0.0F, 0.0F, 1.0F - thickness, thickness, 1.0F, 1.0F);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            if (renderMatrix[12]) {
                this.setBlockBounds(1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, thickness);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            if (renderMatrix[17]) {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, thickness, thickness);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            if (renderMatrix[18]) {
                this.setBlockBounds(1.0F - thickness, 0.0F, 0.0F, 1.0F, thickness, 1.0F);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            if (renderMatrix[19]) {
                this.setBlockBounds(0.0F, 0.0F, 1.0F - thickness, 1.0F, thickness, 1.0F);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            if (renderMatrix[20]) {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, thickness, thickness, 1.0F);
                super.addCollisionBoxesToList(world, i, j, k, aabb, list, ent);
            }

            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean renderWithModel() {
        return true;
    }

    @Override
    public TileEntity getTileEntity() {
        return new TileAtomicConstructor();
    }

}
