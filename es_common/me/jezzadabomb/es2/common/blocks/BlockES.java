package me.jezzadabomb.es2.common.blocks;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.packets.NeighbourChangedPacket;
import me.jezzadabomb.es2.common.tileentity.TileES;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockES extends Block {

    public BlockES(int id, Material material, String name) {
        super(id, material);
        setUnlocalizedName(name);
        setCreativeTab(ElementalSciences2.creativeTab);
        register(name);
    }

    public void register(String name) {
        GameRegistry.registerBlock(this, name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + getUnlocalizedName().replace("tile.", ""));
    }

    @Override
    public boolean renderAsNormalBlock() {
        return !renderWithModel();
    }

    @Override
    public boolean isOpaqueCube() {
        return !renderWithModel();
    }

    @Override
    public int getRenderType() {
        return renderWithModel() ? -1 : super.getRenderType();
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return getTileEntity();
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return getTileEntity() == null ? false : true;
    }

    protected boolean canSendUpdatePacket() {
        return false;
    }

    /**
     * A client side update method. Calls it inside TileES
     */
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int ID) {
        if (!canSendUpdatePacket()) {
            super.onNeighborBlockChange(world, x, y, z, ID);
            return;
        }
        if (getTileEntity() == null) {
            super.onNeighborBlockChange(world, x, y, z, ID);
        } else if (getTileEntity() instanceof TileES) {
            PacketDispatcher.sendPacketToAllAround(x, y, z, 64, world.provider.dimensionId, new NeighbourChangedPacket(new CoordSet(x, y, z)).makePacket());
        }
        super.onNeighborBlockChange(world, x, y, z, ID);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int ID, int param) {
        super.onBlockEventReceived(world, x, y, z, ID, param);
        TileEntity tileentity = world.getBlockTileEntity(x, y, z);
        return tileentity != null ? tileentity.receiveClientEvent(ID, param) : false;
    }

    public abstract boolean renderWithModel();

    public abstract TileEntity getTileEntity();
}
