package me.jezzadabomb.es2.common.core.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemInformation {

    private ArrayList<String> infoList = new ArrayList<String>();
    private ArrayList<String> shiftList = new ArrayList<String>();

    public void addToList(List list) {
        if (!shiftList.isEmpty() && UtilMethods.hasPressedShift())
            list.addAll(shiftList);
        else
            list.addAll(infoList);
    }

    public void defaultInfoList() {
        infoList.add("Press" + EnumChatFormatting.DARK_RED + " Shift" + EnumChatFormatting.GRAY + " for more info.");
    }

    public void addShiftList(String string) {
        shiftList.add(string);
    }

    public void addInfoList(String string) {
        infoList.add(string);
    }
}
