package me.jezzadabomb.es2.client.models.drones;

import me.jezzadabomb.es2.client.lib.Models;
import me.jezzadabomb.es2.client.models.ModelCustomAbstract;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelConstructorDrone extends ModelCustomAbstract {

    public ModelConstructorDrone() {
        super(Models.CONSTRUCTOR_DRONE);
    }
}
