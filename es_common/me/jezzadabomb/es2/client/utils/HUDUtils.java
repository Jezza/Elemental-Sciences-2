package me.jezzadabomb.es2.client.utils;

import static org.lwjgl.opengl.GL11.glTranslated;

public class HUDUtils {

    public static void translateWithRowAndColumn(int columnNum, int rowNum, boolean itemBlock) {
        if (itemBlock) {
            switch (columnNum) {
                case 0:
                    glTranslated(2.0D, 0.0D, 0.0D);
                    break;
                case 1:
                    break;
                case 2:
                    glTranslated(-2.0D, 0.0D, 0.0D);
                    break;
                default:
                    return;
            }

            switch (rowNum) {
                case 0:
                    glTranslated(0.0D, 3.0D, 0.0D);
                    break;
                case 1:
                    glTranslated(0.0D, 2.0D, 0.0D);
                    break;
                case 2:
                    break;
                default:
                    return;
            }
        } else {
            switch (columnNum) {
                case 0:
                    glTranslated(1.0D, 0.0D, 0.0D);
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    return;
            }

            switch (rowNum) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    return;
            }
        }
    }

}
