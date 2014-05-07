package me.jezzadabomb.es2.common.blocks;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import me.jezzadabomb.es2.common.blocks.framework.BlockMetaHolder;
import me.jezzadabomb.es2.common.core.utils.Identifier;
import me.jezzadabomb.es2.common.items.ItemBlockHolder;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystal;

public class BlockPylonCrystal extends BlockMetaHolder {

    private static final String[] names = new String[] { "pylonCrystal0", "pylonCrystal1", "pylonCrystal2" };

    public BlockPylonCrystal(Material material, String name) {
        super(material, name);
    }

    @Override
    public String[] getNames() {
        return names;
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TilePylonCrystal)
            ((TilePylonCrystal) tileEntity).notifyBlockBroken();
        super.onBlockPreDestroy(world, x, y, z, meta);
    }

    @Override
    protected Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockHolder.class;
    }

    @Override
    public boolean renderWithModel() {
        return false;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return new TilePylonCrystal(metadata);
    }

}
