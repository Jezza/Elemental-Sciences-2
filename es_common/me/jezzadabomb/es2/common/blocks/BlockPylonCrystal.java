package me.jezzadabomb.es2.common.blocks;

import java.util.ArrayList;
import java.util.List;

import me.jezzadabomb.es2.common.blocks.framework.BlockESMeta;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystal;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystalDummy;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPylonCrystal extends BlockESMeta {

    private static final String[] names = new String[] { "pylonCrystal0", "pylonCrystal1", "pylonCrystal2", "pylonCrystalDummy0", "pylonCrystalDummy1", "pylonCrystalDummy2" };

    public BlockPylonCrystal(Material material, String name) {
        super(material, name);
        setHardness(2F);
        setBlockBounds(0.25F, 0.1F, 0.25F, 0.75F, 0.9F, 0.75F);
    }

    @Override
    public String[] getNames() {
        return names;
    }

    @Override
    public boolean renderWithModel() {
        return true;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        if (metadata <= 2)
            metadata += 3;

        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1, metadata));
        return ret;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        String[] names = getNames();
        for (int i = 3; i < names.length; i++)
            list.add(new ItemStack(item, 1, i));
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
                return new TilePylonCrystalDummy(metadata - 3);
        }
        return null;
    }

}
