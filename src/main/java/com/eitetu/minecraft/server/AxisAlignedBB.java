package com.eitetu.minecraft.server;

import com.eitetu.minecraft.server.server.MovingObjectPosition;
import com.eitetu.minecraft.server.server.Vec3D;

public class AxisAlignedBB {
	public double d1;
	public double d2;
	public double d3;
	public double d4;
	public double d5;
	public double d6;

	public static AxisAlignedBB newInstance(double d1, double d2, double d3, double d4, double d5, double d6) {
		return new AxisAlignedBB(d1, d2, d3, d4, d5, d6);
	}

	protected AxisAlignedBB(double d1, double d2, double d3, double d4, double d5, double d6) {
		this.d1 = d1;
		this.d2 = d2;
		this.d3 = d3;
		this.d4 = d4;
		this.d5 = d5;
		this.d6 = d6;
	}

	public AxisAlignedBB b(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
		this.d1 = paramDouble1;
		this.d2 = paramDouble2;
		this.d3 = paramDouble3;
		this.d4 = paramDouble4;
		this.d5 = paramDouble5;
		this.d6 = paramDouble6;
		return this;
	}

	public AxisAlignedBB a(double paramDouble1, double paramDouble2, double paramDouble3) {
		double d1 = this.d1;
		double d2 = this.d2;
		double d3 = this.d3;
		double d4 = this.d4;
		double d5 = this.d5;
		double d6 = this.d6;

		if (paramDouble1 < 0.0D)
			d1 += paramDouble1;
		if (paramDouble1 > 0.0D)
			d4 += paramDouble1;

		if (paramDouble2 < 0.0D)
			d2 += paramDouble2;
		if (paramDouble2 > 0.0D)
			d5 += paramDouble2;

		if (paramDouble3 < 0.0D)
			d3 += paramDouble3;
		if (paramDouble3 > 0.0D)
			d6 += paramDouble3;

		return newInstance(d1, d2, d3, d4, d5, d6);
	}

	public AxisAlignedBB grow(double paramDouble1, double paramDouble2, double paramDouble3) {
		double d1 = this.d1 - paramDouble1;
		double d2 = this.d2 - paramDouble2;
		double d3 = this.d3 - paramDouble3;
		double d4 = this.d4 + paramDouble1;
		double d5 = this.d5 + paramDouble2;
		double d6 = this.d6 + paramDouble3;

		return newInstance(d1, d2, d3, d4, d5, d6);
	}

	public AxisAlignedBB a(AxisAlignedBB paramAxisAlignedBB) {
		double d1 = Math.min(this.d1, paramAxisAlignedBB.d1);
		double d2 = Math.min(this.d2, paramAxisAlignedBB.d2);
		double d3 = Math.min(this.d3, paramAxisAlignedBB.d3);
		double d4 = Math.max(this.d4, paramAxisAlignedBB.d4);
		double d5 = Math.max(this.d5, paramAxisAlignedBB.d5);
		double d6 = Math.max(this.d6, paramAxisAlignedBB.d6);

		return newInstance(d1, d2, d3, d4, d5, d6);
	}

	public AxisAlignedBB c(double paramDouble1, double paramDouble2,
			double paramDouble3) {
		return newInstance(this.d1 + paramDouble1, this.d2 + paramDouble2, this.d3
				+ paramDouble3, this.d4 + paramDouble1, this.d5 + paramDouble2,
				this.d6 + paramDouble3);
	}

	public double a(AxisAlignedBB paramAxisAlignedBB, double paramDouble) {
		if ((paramAxisAlignedBB.d5 <= this.d2)
				|| (paramAxisAlignedBB.d2 >= this.d5))
			return paramDouble;
		if ((paramAxisAlignedBB.d6 <= this.d3)
				|| (paramAxisAlignedBB.d3 >= this.d6))
			return paramDouble;
		double d1;
		if ((paramDouble > 0.0D) && (paramAxisAlignedBB.d4 <= this.d1)) {
			d1 = this.d1 - paramAxisAlignedBB.d4;
			if (d1 < paramDouble)
				paramDouble = d1;
		}
		if ((paramDouble < 0.0D) && (paramAxisAlignedBB.d1 >= this.d4)) {
			d1 = this.d4 - paramAxisAlignedBB.d1;
			if (d1 > paramDouble)
				paramDouble = d1;
		}

		return paramDouble;
	}

	public double b(AxisAlignedBB paramAxisAlignedBB, double paramDouble) {
		if ((paramAxisAlignedBB.d4 <= this.d1)
				|| (paramAxisAlignedBB.d1 >= this.d4))
			return paramDouble;
		if ((paramAxisAlignedBB.d6 <= this.d3)
				|| (paramAxisAlignedBB.d3 >= this.d6))
			return paramDouble;
		double d1;
		if ((paramDouble > 0.0D) && (paramAxisAlignedBB.d5 <= this.d2)) {
			d1 = this.d2 - paramAxisAlignedBB.d5;
			if (d1 < paramDouble)
				paramDouble = d1;
		}
		if ((paramDouble < 0.0D) && (paramAxisAlignedBB.d2 >= this.d5)) {
			d1 = this.d5 - paramAxisAlignedBB.d2;
			if (d1 > paramDouble)
				paramDouble = d1;
		}

		return paramDouble;
	}

	public double c(AxisAlignedBB paramAxisAlignedBB, double paramDouble) {
		if ((paramAxisAlignedBB.d4 <= this.d1)
				|| (paramAxisAlignedBB.d1 >= this.d4))
			return paramDouble;
		if ((paramAxisAlignedBB.d5 <= this.d2)
				|| (paramAxisAlignedBB.d2 >= this.d5))
			return paramDouble;
		double d1;
		if ((paramDouble > 0.0D) && (paramAxisAlignedBB.d6 <= this.d3)) {
			d1 = this.d3 - paramAxisAlignedBB.d6;
			if (d1 < paramDouble)
				paramDouble = d1;
		}
		if ((paramDouble < 0.0D) && (paramAxisAlignedBB.d3 >= this.d6)) {
			d1 = this.d6 - paramAxisAlignedBB.d3;
			if (d1 > paramDouble)
				paramDouble = d1;
		}

		return paramDouble;
	}

	public boolean b(AxisAlignedBB paramAxisAlignedBB) {
		if ((paramAxisAlignedBB.d4 <= this.d1)
				|| (paramAxisAlignedBB.d1 >= this.d4))
			return false;
		if ((paramAxisAlignedBB.d5 <= this.d2)
				|| (paramAxisAlignedBB.d2 >= this.d5))
			return false;
		return ((paramAxisAlignedBB.d6 > this.d3) && (paramAxisAlignedBB.d3 < this.d6));
	}

	public AxisAlignedBB d(double paramDouble1, double paramDouble2,
			double paramDouble3) {
		this.d1 += paramDouble1;
		this.d2 += paramDouble2;
		this.d3 += paramDouble3;
		this.d4 += paramDouble1;
		this.d5 += paramDouble2;
		this.d6 += paramDouble3;
		return this;
	}

	public boolean a(Vec3D paramVec3D) {
		if ((paramVec3D.a <= this.d1) || (paramVec3D.a >= this.d4))
			return false;
		if ((paramVec3D.b <= this.d2) || (paramVec3D.b >= this.d5))
			return false;
		return ((paramVec3D.c > this.d3) && (paramVec3D.c < this.d6));
	}

	public double a() {
		double d1 = this.d4 - this.d1;
		double d2 = this.d5 - this.d2;
		double d3 = this.d6 - this.d3;
		return ((d1 + d2 + d3) / 3.0D);
	}

	public AxisAlignedBB shrink(double double1, double double2, double double3) {
		double d1 = this.d1 + double1;
		double d2 = this.d2 + double2;
		double d3 = this.d3 + double3;
		double d4 = this.d4 - double1;
		double d5 = this.d5 - double2;
		double d6 = this.d6 - double3;

		return newInstance(d1, d2, d3, d4, d5, d6);
	}

	public AxisAlignedBB clone() {
		return newInstance(this.d1, this.d2, this.d3, this.d4, this.d5, this.d6);
	}

	public MovingObjectPosition a(Vec3D vec3D1, Vec3D vec3D2) {
		Vec3D localVec3D1 = vec3D1.b(vec3D2, this.d1);
		Vec3D localVec3D2 = vec3D1.b(vec3D2, this.d4);

		Vec3D localVec3D3 = vec3D1.c(vec3D2, this.d2);
		Vec3D localVec3D4 = vec3D1.c(vec3D2, this.d5);

		Vec3D localVec3D5 = vec3D1.d(vec3D2, this.d3);
		Vec3D localVec3D6 = vec3D1.d(vec3D2, this.d6);

		if (!(b(localVec3D1)))
			localVec3D1 = null;
		if (!(b(localVec3D2)))
			localVec3D2 = null;
		if (!(c(localVec3D3)))
			localVec3D3 = null;
		if (!(c(localVec3D4)))
			localVec3D4 = null;
		if (!(d(localVec3D5)))
			localVec3D5 = null;
		if (!(d(localVec3D6)))
			localVec3D6 = null;

		Vec3D localVec3D7 = null;

		if ((localVec3D1 != null)
				&& (((localVec3D7 == null) || (vec3D1
						.distanceSquared(localVec3D1) < vec3D1
						.distanceSquared(localVec3D7)))))
			localVec3D7 = localVec3D1;
		if ((localVec3D2 != null)
				&& (((localVec3D7 == null) || (vec3D1
						.distanceSquared(localVec3D2) < vec3D1
						.distanceSquared(localVec3D7)))))
			localVec3D7 = localVec3D2;
		if ((localVec3D3 != null)
				&& (((localVec3D7 == null) || (vec3D1
						.distanceSquared(localVec3D3) < vec3D1
						.distanceSquared(localVec3D7)))))
			localVec3D7 = localVec3D3;
		if ((localVec3D4 != null)
				&& (((localVec3D7 == null) || (vec3D1
						.distanceSquared(localVec3D4) < vec3D1
						.distanceSquared(localVec3D7)))))
			localVec3D7 = localVec3D4;
		if ((localVec3D5 != null)
				&& (((localVec3D7 == null) || (vec3D1
						.distanceSquared(localVec3D5) < vec3D1
						.distanceSquared(localVec3D7)))))
			localVec3D7 = localVec3D5;
		if ((localVec3D6 != null)
				&& (((localVec3D7 == null) || (vec3D1
						.distanceSquared(localVec3D6) < vec3D1
						.distanceSquared(localVec3D7)))))
			localVec3D7 = localVec3D6;

		if (localVec3D7 == null)
			return null;

		int i = -1;

		if (localVec3D7 == localVec3D1)
			i = 4;
		if (localVec3D7 == localVec3D2)
			i = 5;
		if (localVec3D7 == localVec3D3)
			i = 0;
		if (localVec3D7 == localVec3D4)
			i = 1;
		if (localVec3D7 == localVec3D5)
			i = 2;
		if (localVec3D7 == localVec3D6)
			i = 3;

		return new MovingObjectPosition(0, 0, 0, i, localVec3D7);
	}

	private boolean b(Vec3D paramVec3D) {
		if (paramVec3D == null)
			return false;
		return ((paramVec3D.b >= this.d2) && (paramVec3D.b <= this.d5)
				&& (paramVec3D.c >= this.d3) && (paramVec3D.c <= this.d6));
	}

	private boolean c(Vec3D paramVec3D) {
		if (paramVec3D == null)
			return false;
		return ((paramVec3D.a >= this.d1) && (paramVec3D.a <= this.d4)
				&& (paramVec3D.c >= this.d3) && (paramVec3D.c <= this.d6));
	}

	private boolean d(Vec3D paramVec3D) {
		if (paramVec3D == null)
			return false;
		return ((paramVec3D.a >= this.d1) && (paramVec3D.a <= this.d4)
				&& (paramVec3D.b >= this.d2) && (paramVec3D.b <= this.d5));
	}

	public void d(AxisAlignedBB paramAxisAlignedBB) {
		this.d1 = paramAxisAlignedBB.d1;
		this.d2 = paramAxisAlignedBB.d2;
		this.d3 = paramAxisAlignedBB.d3;
		this.d4 = paramAxisAlignedBB.d4;
		this.d5 = paramAxisAlignedBB.d5;
		this.d6 = paramAxisAlignedBB.d6;
	}

	public String toString() {
		return "box[" + this.d1 + ", " + this.d2 + ", " + this.d3 + " -> "
				+ this.d4 + ", " + this.d5 + ", " + this.d6 + "]";
	}
}
