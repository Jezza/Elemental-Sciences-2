package me.jezzadabomb.es2.common.core.utils;

import me.jezzadabomb.es2.common.core.ESLogger;

public class MathHelper {

	public static double pythagoras(double a, double b) {
		return Math.sqrt(a * a + b * b);
	}

	public static double pythagoras(float a, float b) {
		return Math.sqrt(a * a + b * b);
	}
	
	public static int pythagoras(int a, int b) {
        return net.minecraft.util.MathHelper.truncateDoubleToInt((Math.sqrt(a * a + b * b)));
    }

	public static boolean withinRange(float value, float target, float tolerance) {
		return value >= (target - tolerance) && value <= (target + tolerance);
	}
	
	public static boolean withinRange(int value, int target, int tolerance){
	    return value >= (target - tolerance) && value <= (target + tolerance);
	}

	public static double clipDouble(double value, double min, double max) {
		if (value > max)
			value = max;
		if (value < min)
			value = min;
		return value;
	}

	public static float clipFloat(float value, float min, float max) {
		if (value > max)
			value = max;
		if (value < min)
			value = min;
		return value;
	}

	public static int clipInt(int value, int min, int max) {
		if (value > max)
			value = max;
		if (value < min)
			value = min;
		return value;
	}

	public static int clipInt(int value, int max) {
		if (value > max)
			value = max;
		if (value < 0)
			value = 0;
		return value;
	}

	public static double interpolate(double a, double b, double d) {
		return a + (b - a) * d;
	}

	public static float interpolate(float a, float b, float d) {
		return a + (b - a) * d;
	}

	public static void getAngleFromCoords(float droneX, float droneY, float droneZ, float droneTargetX, float droneTargetY, float droneTargetZ) {
		ESLogger.severe(Math.atan2(droneX, droneY));
	}
}
