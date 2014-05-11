package me.jezzadabomb.es2.common.blocks;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import me.jezzadabomb.es2.common.blocks.framework.BlockMetaHolder;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.Identifier;
import me.jezzadabomb.es2.common.items.ItemBlockHolder;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystal;
import me.jezzadabomb.es2.common.tileentity.TilePylonDummyCrystal;

public class BlockPylonCrystal extends BlockMetaHolder {

    private static final String[] names = new String[] { "pylonCrystal0", "pylonCrystal1", "pylonCrystal2", "pylonCrystalDummy0", "pylonCrystalDummy1", "pylonCrystalDummy2" };

    public BlockPylonCrystal(Material material, String name) {
        super(material, name);
    }

    @Override
    public String[] getNames() {
        return names;
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TilePylonCrystal)
            ((TilePylonCrystal) tileEntity).notifyBlockBroken();
        return super.removedByPlayer(world, player, x, y, z);
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
        switch (metadata) {
            case 0:
            case 1:
            case 2:
                return new TilePylonCrystal(metadata);
            case 3:
            case 4:
            case 5:
                return new TilePylonDummyCrystal(metadata - 3);
        }
        return null;
    }

}
