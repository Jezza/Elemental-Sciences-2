package me.jezzadabomb.es2.common.core.utils;

public class MathHelper {

	public static double pythagoras(double a, double b) {
		return Math.sqrt(a * a + b * b);
	}

	public static double pythagoras(float a, float b) {
		return Math.sqrt(a * a + b * b);
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
	
	public static int clipInt(int value, int min, int max){
		if (value > max)
			value = max;
		if (value < min)
			value = min;
		return value;		
	}
	
	public static int clipInt(int value, int max){
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
}
