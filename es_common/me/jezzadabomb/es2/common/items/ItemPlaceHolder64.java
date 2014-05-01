package me.jezzadabomb.es2.common.items;

import java.util.List;

import me.jezzadabomb.es2.common.ModItems;
import me.jezzadabomb.es2.common.core.utils.Identifier;
import me.jezzadabomb.es2.common.core.utils.UtilMethods;
import me.jezzadabomb.es2.common.core.utils.helpers.PlayerHelper;
import me.jezzadabomb.es2.common.entities.EntityConstructorDrone;
import me.jezzadabomb.es2.common.entities.EntityDrone;
import me.jezzadabomb.es2.common.items.framework.ItemMetaES;
import me.jezzadabomb.es2.common.tileentity.TileDroneBay;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemPlaceHolder64 extends ItemMetaES {
    public static final String[] names = new String[] { "glassesLens", "ironStrip", "ironBar", "spectrumSensor", "selectiveEMPTrigger", "empTrigger", "strengthenedIron", "strengthenedPlate", "ironPlate", "strengthenedIronBar", "atomicFrame"};

    public ItemPlaceHolder64(String name) {
        super(name);
        setMaxStackSize(64);
    }

    protected void addInformation(EntityPlayer player, ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 0:
                addToBothLists("A carefully crafted, highly fragile glass lens.");
                break;
            case 1:
                addToBothLists("A tough piece of metal, could be used for a frame.");
                break;
            case 2:
                addToBothLists("A solid iron rod.");
                break;
            case 3:
                addToBothLists("A very fragile crystal.");
                break;
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if (ModItems.isPlaceHolderStack("selectiveEMPTrigger", stack, true) && Identifier.isDroneBay(world, x, y, z)) {
                TileDroneBay droneBay = (TileDroneBay) world.getTileEntity(x, y, z);
                droneBay.recallDrones(-1);
            }
        }

        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (ModItems.isPlaceHolderStack("empTrigger", stack, true)) {
            float range = 5.0F; // Around the player.
            List<Object> droneList = world.getEntitiesWithinAABB(EntityConstructorDrone.class, AxisAlignedBB.getAABBPool().getAABB(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
            for (Object object : droneList) {
                if (object instanceof EntityConstructorDrone) {
                    EntityConstructorDrone drone = (EntityConstructorDrone) object;
                    drone.setDead();
                    ItemStack droneStack = ModItems.getPlaceHolderStack("constructorDrone");
                    if (!player.capabilities.isCreativeMode && !player.inventory.addItemStackToInventory(droneStack))
                        world.spawnEntityInWorld(new EntityItem(world, drone.posX, drone.posY, drone.posZ, droneStack));
                }
            }
            if (!world.isRemote)
                UtilMethods.addChatMessage(player, "Removed " + droneList.size() + " drones.");

            boolean flag = !droneList.isEmpty();

            if (flag) {
                player.swingItem();
                if (!player.capabilities.isCreativeMode)
                    PlayerHelper.decrCurrentItem(player);
            }
            return stack;
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public String[] getNames() {
        return names;
    }
}
