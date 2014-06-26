package me.jezzadabomb.es2.common.blocks.framework;

import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.core.interfaces.IBlockInteract;
import me.jezzadabomb.es2.common.core.interfaces.IBlockNotifier;
import me.jezzadabomb.es2.common.core.network.PacketDispatcher;
import me.jezzadabomb.es2.common.core.network.packet.server.NeighbourChangedPacket;
import me.jezzadabomb.es2.common.core.utils.coordset.CoordSet;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.tileentity.framework.TileES;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockES extends Block {

    public BlockES(Material material, String name) {
        super(material);
        setCreativeTab(ElementalSciences2.creativeTab);
        setName(name);
        register(name);
    }

    public BlockES register(String name) {
        GameRegistry.registerBlock(this, name);
        return this;
    }

    public BlockES setName(String name) {
        setBlockName(name);
        setBlockTextureName(name);
        return this;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockInteract)
            return ((IBlockInteract) tileEntity).onActivated(world, x, y, z, player, side, hitVecX, hitVecY, hitVecZ);
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockNotifier)
            ((IBlockNotifier) tileEntity).onBlockAdded(world, x, y, z);
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        onBlockRemoval(world, x, y, z);
        super.onBlockExploded(world, x, y, z, explosion);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        onBlockRemoval(world, x, y, z);
        return super.removedByPlayer(world, player, x, y, z);
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof IBlockNotifier)
            ((IBlockNotifier) tileEntity).onBlockRemoval(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(Reference.MOD_IDENTIFIER + getTextureName());
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return getTileEntity(metadata);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return getTileEntity(metadata) == null ? false : true;
    }

    protected boolean canSendUpdatePacket() {
        return false;
    }

    public TileEntity getTileEntity(int metadata) {
        return null;
    };

    // ----------------------------- Update methods -------------------------------

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (canSendUpdatePacket() && world.getTileEntity(x, y, z) instanceof TileES)
            PacketDispatcher.sendToAllAround(new NeighbourChangedPacket(new CoordSet(x, y, z)), new TargetPoint(world.provider.dimensionId, x, y, z, 64));

        super.onNeighborBlockChange(world, x, y, z, block);
    }

    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int ID, int param) {
        super.onBlockEventReceived(world, x, y, z, ID, param);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        return tileentity != null ? tileentity.receiveClientEvent(ID, param) : false;
    }

    // ----------------------------- BlockType start -------------------------------

    @Override
    public boolean renderAsNormalBlock() {
        BlockType blockType = getBlockType();
        if (blockType.hasOverride("renderAsNormalBlock"))
            return blockType.renderAsNormalBlock();
        return super.renderAsNormalBlock();
    }

    @Override
    public boolean isOpaqueCube() {
        BlockType blockType = getBlockType();
        if (blockType.hasOverride("isOpaqueCube"))
            return blockType.isOpaqueCube();
        return super.isOpaqueCube();
    }

    @Override
    public int getRenderType() {
        BlockType blockType = getBlockType();
        if (blockType.hasOverride("getRenderType"))
            return blockType.getRenderType();
        return super.getRenderType();
    }

    public BlockType getBlockType() {
        return BlockType.NORMAL;
    }
}
