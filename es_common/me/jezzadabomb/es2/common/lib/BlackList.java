package me.jezzadabomb.es2.common.lib;

import java.util.ArrayList;

import me.jezzadabomb.es2.common.core.ESLogger;
import net.minecraft.block.Block;

public class BlackList {
    public static ArrayList<Integer> blackList = new ArrayList<Integer>(0);
    public static String blackListDefault = "7:0,8:0,9:0,10:0,11:0";

    private static String splitChar = ",";
    private static String sepChar = ":";

    private static int getTotalEntries(String values) {
        int num = 0;
        for (int i = 0; i < values.length(); i++)
            if (values.charAt(i) == ',')
                num++;
        return num;
    }

    public static boolean OnBlackList(Block block, int meta) {
        int id = Block.getIdFromBlock(block);
        for (int i = 0; i < blackList.size(); i += 2)
            if (blackList.get(i) == id && blackList.get(i + 1) == meta)
                return true;
        return false;
    }

    public static void putValues(String values) {
        if (values.lastIndexOf(splitChar) != values.length())
            values += splitChar;

        int tempStringLength = 0;
        for (int i = 0; i < getTotalEntries(values); i++) {
            String temp = values.substring(tempStringLength, values.indexOf(splitChar, tempStringLength));
            if (temp.matches("\\d+:\\d+")) {
                blackList.add(Integer.parseInt(temp.substring(0, temp.indexOf(sepChar))));
                blackList.add(Integer.parseInt(temp.substring(temp.indexOf(sepChar) + 1, temp.length())));
                tempStringLength += (temp.length() + 1);
            } else {
                ESLogger.severe("Couldn't not parse: " + temp);
                ESLogger.severe("Defaulting to premade list.");
                putValues(blackListDefault);
            }
        }
    }
}
