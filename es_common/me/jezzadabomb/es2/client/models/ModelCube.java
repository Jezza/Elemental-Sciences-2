package me.jezzadabomb.es2.client.models;

import me.jezzadabomb.es2.client.lib.Models;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCube extends ModelCustomAbstract {

    public ModelCube() {
        super(Models.CUBE);
    }

}
