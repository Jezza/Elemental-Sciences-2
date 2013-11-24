package me.jezzadabomb.es2.common.hud;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.items.ItemGlasses;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class HUDUtilities {
    
    public static final HUDUtilities instance = new HUDUtilities();
    
    public ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>(0);
    
    public boolean isEntityWearingGlasses(EntityPlayer player) {
        ItemStack helmet = (player).inventory.armorItemInSlot(3);
        return helmet != null && helmet.getItem() instanceof ItemGlasses;
    }
    
    public boolean isPlayerWearingGlasses(EntityPlayer player) {
        //Entity e = FMLClientHandler.instance().getClient().renderViewEntity;
        return isEntityWearingGlasses(player);
    }
}
