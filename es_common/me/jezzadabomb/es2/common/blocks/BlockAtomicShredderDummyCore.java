package me.jezzadabomb.es2.common.blocks;

import me.jezzadabomb.es2.common.blocks.framework.BlockMetaHolder;
import me.jezzadabomb.es2.common.items.ItemBlockHolder;
import me.jezzadabomb.es2.common.tileentity.TilePylonUser;
import me.jezzadabomb.es2.common.tileentity.multi.TileAtomicShredderDummyCore;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

public class BlockAtomicShredderDummyCore extends BlockMetaHolder {

    public static final String[] names = new String[] { "shredderFrameBlock", "shredderCore", "pylonDummyBlock" };

    public BlockAtomicShredderDummyCore(Material material, String name) {
        super(material, name);
        setHardness(5.0F);
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
        return false;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        switch (metadata) {
            case 1:
                return new TileAtomicShredderDummyCore();
            case 2:
                return new TilePylonUser();
        }
        return null;
    }

}
