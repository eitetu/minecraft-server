package com.eitetu.minecraft.server.server;

import java.util.Random;

public class MathHelper {
	private static float[] a = new float[65536];
	private static final int[] b;

	public static final float sin(float paramFloat) {
		return a[((int) (paramFloat * 10430.378F) & 0xFFFF)];
	}

	public static final float cos(float paramFloat) {
		return a[((int) (paramFloat * 10430.378F + 16384.0F) & 0xFFFF)];
	}

	public static final float c(float paramFloat) {
		return (float) Math.sqrt(paramFloat);
	}

	public static final float sqrt(double paramDouble) {
		return (float) Math.sqrt(paramDouble);
	}

	public static int d(float paramFloat) {
		int i = (int) paramFloat;
		return ((paramFloat < i) ? i - 1 : i);
	}

	public static int floor(double paramDouble) {
		int i = (int) paramDouble;
		return ((paramDouble < i) ? i - 1 : i);
	}

	public static long d(double paramDouble) {
    long l = (long)paramDouble;
    return ((paramDouble < l) ? l - 1L : l);
  }

	public static float abs(float paramFloat) {
		return ((paramFloat >= 0.0F) ? paramFloat : -paramFloat);
	}

	public static int a(int paramInt) {
		return ((paramInt >= 0) ? paramInt : -paramInt);
	}

	public static int f(float paramFloat) {
		int i = (int) paramFloat;
		return ((paramFloat > i) ? i + 1 : i);
	}

	public static int f(double paramDouble) {
		int i = (int) paramDouble;
		return ((paramDouble > i) ? i + 1 : i);
	}

	public static int a(int paramInt1, int paramInt2, int paramInt3) {
		if (paramInt1 < paramInt2) {
			return paramInt2;
		}
		if (paramInt1 > paramInt3) {
			return paramInt3;
		}
		return paramInt1;
	}

	public static float a(float paramFloat1, float paramFloat2,
			float paramFloat3) {
		if (paramFloat1 < paramFloat2) {
			return paramFloat2;
		}
		if (paramFloat1 > paramFloat3) {
			return paramFloat3;
		}
		return paramFloat1;
	}

	public static double a(double paramDouble1, double paramDouble2,
			double paramDouble3) {
		if (paramDouble1 < paramDouble2) {
			return paramDouble2;
		}
		if (paramDouble1 > paramDouble3) {
			return paramDouble3;
		}
		return paramDouble1;
	}

	public static double b(double paramDouble1, double paramDouble2,
			double paramDouble3) {
		if (paramDouble3 < 0.0D) {
			return paramDouble1;
		}
		if (paramDouble3 > 1.0D) {
			return paramDouble2;
		}
		return (paramDouble1 + (paramDouble2 - paramDouble1) * paramDouble3);
	}

	public static double a(double paramDouble1, double paramDouble2) {
		if (paramDouble1 < 0.0D)
			paramDouble1 = -paramDouble1;
		if (paramDouble2 < 0.0D)
			paramDouble2 = -paramDouble2;
		return ((paramDouble1 > paramDouble2) ? paramDouble1 : paramDouble2);
	}

	public static int nextInt(Random paramRandom, int paramInt1, int paramInt2) {
		if (paramInt1 >= paramInt2) {
			return paramInt1;
		}
		return (paramRandom.nextInt(paramInt2 - paramInt1 + 1) + paramInt1);
	}

	public static float a(Random paramRandom, float paramFloat1,
			float paramFloat2) {
		if (paramFloat1 >= paramFloat2)
			return paramFloat1;
		return (paramRandom.nextFloat() * (paramFloat2 - paramFloat1) + paramFloat1);
	}

	public static double a(Random paramRandom, double paramDouble1,
			double paramDouble2) {
		if (paramDouble1 >= paramDouble2)
			return paramDouble1;
		return (paramRandom.nextDouble() * (paramDouble2 - paramDouble1) + paramDouble1);
	}

	public static double a(long[] paramArrayOfLong) {
		long l1 = 0L;

		for (long l2 : paramArrayOfLong) {
			l1 += l2;
		}

		return (l1 / paramArrayOfLong.length);
	}

	public static float g(float paramFloat) {
		paramFloat %= 360.0F;
		if (paramFloat >= 180.0F)
			paramFloat -= 360.0F;
		if (paramFloat < -180.0F)
			paramFloat += 360.0F;
		return paramFloat;
	}

	public static double g(double paramDouble) {
		paramDouble %= 360.0D;
		if (paramDouble >= 180.0D)
			paramDouble -= 360.0D;
		if (paramDouble < -180.0D)
			paramDouble += 360.0D;
		return paramDouble;
	}

	public static int a(String paramString, int paramInt) {
		int i = paramInt;
		try {
			i = Integer.parseInt(paramString);
		} catch (Throwable localThrowable) {
		}
		return i;
	}

	public static int a(String paramString, int paramInt1, int paramInt2) {
		int i = paramInt1;
		try {
			i = Integer.parseInt(paramString);
		} catch (Throwable localThrowable) {
		}
		if (i < paramInt2)
			i = paramInt2;
		return i;
	}

	public static double a(String paramString, double paramDouble) {
		double d = paramDouble;
		try {
			d = Double.parseDouble(paramString);
		} catch (Throwable localThrowable) {
		}
		return d;
	}

	public static double a(String paramString, double paramDouble1,
			double paramDouble2) {
		double d = paramDouble1;
		try {
			d = Double.parseDouble(paramString);
		} catch (Throwable localThrowable) {
		}
		if (d < paramDouble2)
			d = paramDouble2;
		return d;
	}

	static {
		for (int i = 0; i < 65536; ++i) {
			a[i] = (float) Math.sin(i * 3.141592653589793D * 2.0D / 65536.0D);
		}

		b = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4,
				8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };
	}
}
