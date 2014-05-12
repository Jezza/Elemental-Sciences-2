package me.jezzadabomb.es2.common.blocks;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import me.jezzadabomb.es2.common.ModBlocks;
import me.jezzadabomb.es2.common.blocks.framework.BlockMetaHolder;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.core.utils.Identifier;
import me.jezzadabomb.es2.common.items.ItemBlockHolder;
import me.jezzadabomb.es2.common.lib.Reference;
import me.jezzadabomb.es2.common.tileentity.TilePylonCrystal;
import me.jezzadabomb.es2.common.tileentity.TilePylonDummyCrystal;

public class BlockPylonCrystal extends BlockMetaHolder {

    private static final String[] names = new String[] { "pylonCrystal0", "pylonCrystal1", "pylonCrystal2", "pylonCrystalDummy0", "pylonCrystalDummy1", "pylonCrystalDummy2" };

    public BlockPylonCrystal(Material material, String name) {
        super(material, name);
        setHardness(2F);
    }

    @Override
    public String[] getNames() {
        return names;
    }

    @Override
    protected Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockHolder.class;
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
        if (!Reference.isDebugMode) {
            String[] names = getNames();
            for (int i = 3; i < names.length; i++)
                list.add(new ItemStack(item, 1, i));
            return;
        }
        super.getSubBlocks(item, tab, list);
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
