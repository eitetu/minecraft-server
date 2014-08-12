package com.eitetu.minecraft.server.server;

import net.minecraft.server.EnumMovingObjectType;

public class MovingObjectPosition {
	public EnumMovingObjectType type;
	public int b;
	public int c;
	public int d;
	public int face;
	public Vec3D pos;
	public Entity entity;

	public MovingObjectPosition(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4, Vec3D paramVec3D) {
		this(paramInt1, paramInt2, paramInt3, paramInt4, paramVec3D, true);
	}

	public MovingObjectPosition(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4, Vec3D paramVec3D, boolean paramBoolean) {
		this.type = ((paramBoolean) ? EnumMovingObjectType.BLOCK
				: EnumMovingObjectType.MISS);
		this.b = paramInt1;
		this.c = paramInt2;
		this.d = paramInt3;
		this.face = paramInt4;
		this.pos = Vec3D.a(paramVec3D.a, paramVec3D.b, paramVec3D.c);
	}

	public MovingObjectPosition(Entity paramEntity) {
		this(paramEntity, Vec3D.a(paramEntity.locX, paramEntity.locY,
				paramEntity.locZ));
	}

	public MovingObjectPosition(Entity paramEntity, Vec3D paramVec3D) {
		this.type = EnumMovingObjectType.ENTITY;
		this.entity = paramEntity;
		this.pos = paramVec3D;
	}

	public String toString() {
		return "HitResult{type=" + this.type + ", x=" + this.b + ", y="
				+ this.c + ", z=" + this.d + ", f=" + this.face + ", pos="
				+ this.pos + ", entity=" + this.entity + '}';
	}
}
