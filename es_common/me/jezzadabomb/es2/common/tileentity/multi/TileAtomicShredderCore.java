package me.jezzadabomb.es2.common.tileentity.multi;

import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.shredder.AtomicShredderItemHandler;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileAtomicShredderCore extends TileES {

    AtomicShredderItemHandler itemHandler;

    boolean informed = false;

    public TileAtomicShredderCore() {
        // TODO NBT STUFF
        // TODO DEBUG SHIT
        // TODO UPDATE PACKET
        itemHandler = new AtomicShredderItemHandler();
    }

    @Override
    public void updateEntity() {
        if (worldObj == null)
            return;

        itemHandler.processingTick();
    }

    public void inform(int x, int y, int z) {
        if (informed)
            return;
        informed = true;
        revert();
        int meta = worldObj.getBlockMetadata(x, y, z);
        UtilMethods.breakBlock(worldObj, x, y, z, meta == 0 ? ModBlocks.atomicShredderDummyCore : ModBlocks.atomicShredderDummy, meta, true);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        itemHandler.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        itemHandler.readFromNBT(tag);
    }

    public boolean absorbItemStack(ItemStack itemStack) {
        return itemHandler.addItemStack(itemStack);
    }

    public boolean processBlockActivation(EntityPlayer player) {
        ESLogger.info("ASDASDASDASDAS");
        // TODO ACTUAL FUNCTION
        // TODO PARTICLES MUTHA FUCKER
        ItemStack itemStack = player.getCurrentEquippedItem();

        boolean hasHoveringItem = itemHandler.hasHoverItem();

        if (itemStack == null) {
            if (!hasHoveringItem)
                return false;
            itemHandler.dropSlot(player, 0);
            return true;
        } else {
            if (absorbItemStack(itemStack)) {
                player.setCurrentItemOrArmor(0, null);
                return true;
            }
        }
        return true;
    }

    public AtomicShredderItemHandler getItemHandler() {
        return itemHandler;
    }

    public void revert() {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            worldObj.setBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, ModBlocks.atomicShredderDummy, 1, 3);
            worldObj.func_147480_a(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, false);
            worldObj.setBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, ModBlocks.atomicShredderDummy, 1, 3);
        }

        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                for (int k = -1; k <= 1; k++) {
                    if (i == 0 && j == 0 && k == 0 || (worldObj.getBlock(xCoord + i, yCoord + j, zCoord + k) == ModBlocks.atomicShredderDummy && worldObj.getBlockMetadata(xCoord + i, yCoord + j, zCoord + k) == 1))
                        continue;
                    worldObj.setBlock(xCoord + i, yCoord + j, zCoord + k, ModBlocks.atomicShredderDummyCore, 0, 3);
                    worldObj.func_147480_a(xCoord + i, yCoord + j, zCoord + k, false);
                    worldObj.setBlock(xCoord + i, yCoord + j, zCoord + k, ModBlocks.atomicShredderDummyCore, 0, 3);
                }
        worldObj.setBlock(xCoord, yCoord, zCoord, ModBlocks.atomicShredderDummyCore, 1, 3);
    }

    public void convert() {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            worldObj.func_147480_a(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, false);
            worldObj.setBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, ModBlocks.atomicShredderDummy, 2, 3);
            TileEntity tile = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            if (tile instanceof TileAtomicShredderDummy)
                ((TileAtomicShredderDummy) tile).setMaster(getCoordSet());
        }

        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                for (int k = -1; k <= 1; k++) {
                    if ((i == 0 && j == 0 && k == 0) || (worldObj.getBlock(xCoord + i, yCoord + j, zCoord + k) == ModBlocks.atomicShredderDummy && worldObj.getBlockMetadata(xCoord + i, yCoord + j, zCoord + k) == 2))
                        continue;
                    worldObj.func_147480_a(xCoord + i, yCoord + j, zCoord + k, false);
                    worldObj.setBlock(xCoord + i, yCoord + j, zCoord + k, ModBlocks.atomicShredderDummy, 0, 3);
                    TileEntity tile = worldObj.getTileEntity(xCoord + i, yCoord + j, zCoord + k);
                    if (tile instanceof TileAtomicShredderDummy)
                        ((TileAtomicShredderDummy) tile).setMaster(getCoordSet());
                }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getAABBPool().getAABB(xCoord - 1.0F, yCoord - 1.0F, zCoord - 1.0F, xCoord + 2.0F, yCoord + 2.0F, zCoord + 2.0F);
    }
}
