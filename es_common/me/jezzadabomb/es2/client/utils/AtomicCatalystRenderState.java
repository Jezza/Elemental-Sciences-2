package me.jezzadabomb.es2.client.utils;

import static org.lwjgl.opengl.GL11.glTranslated;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AtomicCatalystRenderState {

    int x, y, z;

    public AtomicCatalystRenderState(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void translate(double cosDis, double sinDis) {
        double xTrans = 0;
        double yTrans = 0;
        double zTrans = 0;

        if (x != 0) {
            switch (x) {
                case -2:
                    xTrans = -cosDis;
                    break;
                case -1:
                    xTrans = -sinDis;
                    break;
                case 1:
                    xTrans = cosDis;
                    break;
                case 2:
                    xTrans = sinDis;
                    break;
            }
        }

        if (y != 0) {
            switch (y) {
                case -2:
                    yTrans = -cosDis;
                    break;
                case -1:
                    yTrans = -sinDis;
                    break;
                case 1:
                    yTrans = cosDis;
                    break;
                case 2:
                    yTrans = sinDis;
                    break;
            }
        }

        if (z != 0) {
            switch (z) {
                case -2:
                    zTrans = -cosDis;
                    break;
                case -1:
                    zTrans = -sinDis;
                    break;
                case 1:
                    zTrans = cosDis;
                    break;
                case 2:
                    zTrans = sinDis;
                    break;
            }
        }

        glTranslated(xTrans, yTrans, zTrans);
    }
}
