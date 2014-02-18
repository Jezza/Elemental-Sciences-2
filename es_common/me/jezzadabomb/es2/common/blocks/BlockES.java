package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.utils.CoordSet;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.network.packet.server.NeighbourChangedPacket;
import me.jezzadabomb.es2.common.tileentity.TileES;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockES extends Block {

    public BlockES(Material material, String name) {
        super(material);
        setBlockName(name);
        setCreativeTab(ElementalSciences2.creativeTab);
        register(name);
    }

    public void register(String name) {
        GameRegistry.registerBlock(this, name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
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
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!canSendUpdatePacket()) {
            super.onNeighborBlockChange(world, x, y, z, block);
            return;
        }
        if (getTileEntity() == null) {
            super.onNeighborBlockChange(world, x, y, z, block);
        } else if (getTileEntity() instanceof TileES) {
            PacketDispatcher.sendToAllAround(new NeighbourChangedPacket(new CoordSet(x, y, z)), new TargetPoint(world.provider.dimensionId, x, y, z, 64));
        }
        super.onNeighborBlockChange(world, x, y, z, block);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int ID, int param) {
        super.onBlockEventReceived(world, x, y, z, ID, param);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        return tileentity != null ? tileentity.receiveClientEvent(ID, param) : false;
    }

    public abstract boolean renderWithModel();

    public abstract TileEntity getTileEntity();
}
