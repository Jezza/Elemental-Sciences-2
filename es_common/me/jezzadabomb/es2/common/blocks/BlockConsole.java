package me.jezzadabomb.es2.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezzadabomb.es2.ElementalSciences2;
import me.jezzadabomb.es2.common.core.ESLogger;
import me.jezzadabomb.es2.common.tileentity.TileConsole;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockConsole extends BlockES {

    public BlockConsole(Material material, String name) {
        super(material, name);
        setHardness(5F);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 6F / 8F, 1.0F);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        if (world.getTileEntity(x, y, z) instanceof TileConsole) {
            int direction = 0;
            int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

            switch (facing) {
                case 0:
                    direction = ForgeDirection.NORTH.ordinal();
                    break;
                case 1:
                    direction = ForgeDirection.EAST.ordinal();
                    break;
                case 2:
                    direction = ForgeDirection.SOUTH.ordinal();
                    break;
                case 3:
                    direction = ForgeDirection.WEST.ordinal();
                    break;
            }

            TileConsole tC = ((TileConsole) world.getTileEntity(x, y, z));
            tC.setOrientation(direction);
            tC.updateRenderCables();
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVectorX, float hitVectorY, float hitVectorZ) {
        TileConsole tileConsole = (TileConsole) world.getTileEntity(x, y, z);

        if (player.isSneaking()) {
            if (!world.isRemote) {
                String[] consoleInfo = tileConsole.toString().split(System.lineSeparator());

                for (String str : consoleInfo)
                    player.addChatComponentMessage(new ChatComponentText(str));
            }
            return true;
        }

        tileConsole.openGui(world, player);

        player.openGui(ElementalSciences2.instance, 0, world, x, y, z);

        return true;
    }

    @Override
    protected boolean canSendUpdatePacket() {
        return true;
    }

    @Override
    public boolean renderWithModel() {
        return true;
    }

    @Override
    public TileEntity getTileEntity() {
        return new TileConsole();
    }
}
