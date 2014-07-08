package me.jezzadabomb.es2.client.models;

import me.jezzadabomb.es2.client.lib.Models;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDroneBay extends ModelCustomAbstract {

    public ModelDroneBay() {
        super(Models.DRONE_BAY);
    }

    public void renderDoor(int door) {
        renderPart("Door" + door);
    }

    public void renderAllDoors() {
        for (int i = 0; i < 32; i++)
            renderDoor(i);
    }
}
