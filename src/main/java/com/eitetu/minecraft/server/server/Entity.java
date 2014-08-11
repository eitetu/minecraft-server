package com.eitetu.minecraft.server.server;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.eitetu.minecraft.server.AxisAlignedBB;

public abstract class Entity {
	private static int entityCount;
	private int id = entityCount++;

	public double j = 1.0D;
	public boolean k;
	public Entity passenger;
	public Entity vehicle;
	public boolean n;
	public World world;
	public double lastX;
	public double lastY;
	public double lastZ;
	public double locX;
	public double locY;
	public double locZ;
	public double motX;
	public double motY;
	public double motZ;
	public float yaw;
	public float pitch;
	public float lastYaw;
	public float lastPitch;
	public final AxisAlignedBB boundingBox = AxisAlignedBB.a(0.0D, 0.0D, 0.0D,
			0.0D, 0.0D, 0.0D);
	public boolean onGround;
	public boolean positionChanged;
	public boolean F;
	public boolean G;
	public boolean velocityChanged;
	protected boolean I;
	public boolean J = true;
	public boolean dead;
	public float height;
	public float width = 0.6F;
	public float length = 1.8F;
	public float O;
	public float P;
	public float Q;
	public float fallDistance;
	private int d = 1;
	public double S;
	public double T;
	public double U;
	public float V;
	public float W;
	public boolean X;
	public float Y;
	protected Random random = new Random();
	public int ticksLived;
	public int maxFireTicks = 1;
	private int fireTicks;
	protected boolean inWater;
	public int noDamageTicks;
	private boolean justCreated = true;
	protected boolean fireProof;
	protected DataWatcher datawatcher;
	private double g;
	private double h;
	public boolean ag;
	public int ah;
	public int ai;
	public int aj;
	public boolean ak;
	public boolean al;
	public int portalCooldown;
	protected boolean an;
	protected int ao;
	public int dimension;
	protected int aq;
	private boolean invulnerable;
	protected UUID uniqueID = UUID.randomUUID();

	public EnumEntitySize as = EnumEntitySize.SIZE_2;

	public int getId() {
		return this.id;
	}

	public void d(int paramInt) {
		this.id = paramInt;
	}

	public Entity(World paramWorld) {
		this.world = paramWorld;
		setPosition(0.0D, 0.0D, 0.0D);

		if (paramWorld != null) {
			this.dimension = paramWorld.worldProvider.dimension;
		}

		this.datawatcher = new DataWatcher(this);
		this.datawatcher.a(0, Byte.valueOf(0));
		this.datawatcher.a(1, Short.valueOf(300));
		c();
	}

	protected abstract void c();

	public DataWatcher getDataWatcher() {
		return this.datawatcher;
	}

	public boolean equals(Object paramObject) {
		if (paramObject instanceof Entity) {
			return (((Entity) paramObject).id == this.id);
		}
		return false;
	}

	public int hashCode() {
		return this.id;
	}

	public void die() {
		this.dead = true;
	}

	protected void a(float paramFloat1, float paramFloat2) {
		if ((paramFloat1 != this.width) || (paramFloat2 != this.length)) {
			f = this.width;

			this.width = paramFloat1;
			this.length = paramFloat2;
			this.boundingBox.d = (this.boundingBox.a + this.width);
			this.boundingBox.f = (this.boundingBox.c + this.width);
			this.boundingBox.e = (this.boundingBox.b + this.length);

			if ((this.width > f) && (!(this.justCreated))
					&& (!(this.world.isStatic))) {
				move(f - this.width, 0.0D, f - this.width);
			}
		}
		float f = paramFloat1 % 2.0F;
		if (f < 0.375D)
			this.as = EnumEntitySize.SIZE_1;
		else if (f < 0.75D)
			this.as = EnumEntitySize.SIZE_2;
		else if (f < 1.0D)
			this.as = EnumEntitySize.SIZE_3;
		else if (f < 1.375D)
			this.as = EnumEntitySize.SIZE_4;
		else if (f < 1.75D)
			this.as = EnumEntitySize.SIZE_5;
		else
			this.as = EnumEntitySize.SIZE_6;
	}

	protected void b(float paramFloat1, float paramFloat2) {
		this.yaw = (paramFloat1 % 360.0F);
		this.pitch = (paramFloat2 % 360.0F);
	}

	public void setPosition(double paramDouble1, double paramDouble2,
			double paramDouble3) {
		this.locX = paramDouble1;
		this.locY = paramDouble2;
		this.locZ = paramDouble3;
		float f1 = this.width / 2.0F;
		float f2 = this.length;
		this.boundingBox.b(paramDouble1 - f1, paramDouble2 - this.height
				+ this.V, paramDouble3 - f1, paramDouble1 + f1, paramDouble2
				- this.height + this.V + f2, paramDouble3 + f1);
	}

	public void h() {
		B();
	}

	public void B() {
		this.world.methodProfiler.a("entityBaseTick");

		if ((this.vehicle != null) && (this.vehicle.dead)) {
			this.vehicle = null;
		}

		this.O = this.P;
		this.lastX = this.locX;
		this.lastY = this.locY;
		this.lastZ = this.locZ;
		this.lastPitch = this.pitch;
		this.lastYaw = this.yaw;
		int l;
		int i1;
		if ((!(this.world.isStatic)) && (this.world instanceof WorldServer)) {
			this.world.methodProfiler.a("portal");
			MinecraftServer localMinecraftServer = ((WorldServer) this.world)
					.getMinecraftServer();
			l = C();

			if (this.an) {
				if (localMinecraftServer.getAllowNether()) {
					if (this.vehicle == null) {
						if (this.ao++ >= l) {
							this.ao = l;
							this.portalCooldown = ah();

							if (this.world.worldProvider.dimension == -1)
								i1 = 0;
							else {
								i1 = -1;
							}

							b(i1);
						}
					}
					this.an = false;
				}
			} else {
				if (this.ao > 0)
					this.ao -= 4;
				if (this.ao < 0)
					this.ao = 0;
			}
			if (this.portalCooldown > 0)
				this.portalCooldown -= 1;
			this.world.methodProfiler.b();
		}

		if ((isSprinting()) && (!(L()))) {
			int i = MathHelper.floor(this.locX);
			l = MathHelper.floor(this.locY - 0.2000000029802322D - this.height);
			i1 = MathHelper.floor(this.locZ);
			Block localBlock = this.world.getType(i, l, i1);

			if (localBlock.getMaterial() != Material.AIR) {
				this.world.addParticle("blockcrack_" + Block.b(localBlock)
						+ "_" + this.world.getData(i, l, i1), this.locX
						+ (this.random.nextFloat() - 0.5D) * this.width,
						this.boundingBox.b + 0.1D,
						this.locZ + (this.random.nextFloat() - 0.5D)
								* this.width, -this.motX * 4.0D, 1.5D,
						-this.motZ * 4.0D);
			}

		}

		M();

		if (this.world.isStatic) {
			this.fireTicks = 0;
		} else if (this.fireTicks > 0) {
			if (this.fireProof) {
				this.fireTicks -= 4;
				if (this.fireTicks < 0)
					this.fireTicks = 0;
			} else {
				if (this.fireTicks % 20 == 0) {
					damageEntity(DamageSource.BURN, 1.0F);
				}
				this.fireTicks -= 1;
			}

		}

		if (O()) {
			D();
			this.fallDistance *= 0.5F;
		}

		if (this.locY < -64.0D) {
			F();
		}

		if (!(this.world.isStatic)) {
			a(0, this.fireTicks > 0);
		}

		this.justCreated = false;

		this.world.methodProfiler.b();
	}

	public int C() {
		return 0;
	}

	protected void D() {
		if (this.fireProof)
			return;
		damageEntity(DamageSource.LAVA, 4.0F);
		setOnFire(15);
	}

	public void setOnFire(int paramInt) {
		int i = paramInt * 20;
		i = EnchantmentProtection.a(this, i);
		if (this.fireTicks < i)
			this.fireTicks = i;
	}

	public void extinguish() {
		this.fireTicks = 0;
	}

	protected void F() {
		die();
	}

	public boolean c(double paramDouble1, double paramDouble2,
			double paramDouble3) {
		AxisAlignedBB localAxisAlignedBB = this.boundingBox.c(paramDouble1,
				paramDouble2, paramDouble3);
		List localList = this.world.getCubes(this, localAxisAlignedBB);
		if (!(localList.isEmpty()))
			return false;
		return (!(this.world.containsLiquid(localAxisAlignedBB)));
	}

	public void move(double paramDouble1, double paramDouble2,
			double paramDouble3) {
		if (this.X) {
			this.boundingBox.d(paramDouble1, paramDouble2, paramDouble3);
			this.locX = ((this.boundingBox.a + this.boundingBox.d) / 2.0D);
			this.locY = (this.boundingBox.b + this.height - this.V);
			this.locZ = ((this.boundingBox.c + this.boundingBox.f) / 2.0D);
			return;
		}

		this.world.methodProfiler.a("move");

		this.V *= 0.4F;

		double d1 = this.locX;
		double d2 = this.locY;
		double d3 = this.locZ;

		if (this.I) {
			this.I = false;

			paramDouble1 *= 0.25D;
			paramDouble2 *= 0.0500000007450581D;
			paramDouble3 *= 0.25D;
			this.motX = 0.0D;
			this.motY = 0.0D;
			this.motZ = 0.0D;
		}

		double d4 = paramDouble1;
		double d5 = paramDouble2;
		double d6 = paramDouble3;

		AxisAlignedBB localAxisAlignedBB1 = this.boundingBox.clone();

		int i = ((this.onGround) && (isSneaking()) && (this instanceof EntityHuman)) ? 1
				: 0;

		if (i != 0) {
			double d7 = 0.05D;
			while ((paramDouble1 != 0.0D)
					&& (this.world.getCubes(this,
							this.boundingBox.c(paramDouble1, -1.0D, 0.0D))
							.isEmpty())) {
				if ((paramDouble1 < d7) && (paramDouble1 >= -d7))
					paramDouble1 = 0.0D;
				else if (paramDouble1 > 0.0D)
					paramDouble1 -= d7;
				else
					paramDouble1 += d7;
				d4 = paramDouble1;
			}
			while ((paramDouble3 != 0.0D)
					&& (this.world.getCubes(this,
							this.boundingBox.c(0.0D, -1.0D, paramDouble3))
							.isEmpty())) {
				if ((paramDouble3 < d7) && (paramDouble3 >= -d7))
					paramDouble3 = 0.0D;
				else if (paramDouble3 > 0.0D)
					paramDouble3 -= d7;
				else
					paramDouble3 += d7;
				d6 = paramDouble3;
			}
			while ((paramDouble1 != 0.0D)
					&& (paramDouble3 != 0.0D)
					&& (this.world.getCubes(this, this.boundingBox.c(
							paramDouble1, -1.0D, paramDouble3)).isEmpty())) {
				if ((paramDouble1 < d7) && (paramDouble1 >= -d7))
					paramDouble1 = 0.0D;
				else if (paramDouble1 > 0.0D)
					paramDouble1 -= d7;
				else
					paramDouble1 += d7;
				if ((paramDouble3 < d7) && (paramDouble3 >= -d7))
					paramDouble3 = 0.0D;
				else if (paramDouble3 > 0.0D)
					paramDouble3 -= d7;
				else
					paramDouble3 += d7;
				d4 = paramDouble1;
				d6 = paramDouble3;
			}
		}

		List localList = this.world.getCubes(this,
				this.boundingBox.a(paramDouble1, paramDouble2, paramDouble3));

		for (int l = 0; l < localList.size(); ++l)
			paramDouble2 = ((AxisAlignedBB) localList.get(l)).b(
					this.boundingBox, paramDouble2);
		this.boundingBox.d(0.0D, paramDouble2, 0.0D);

		if ((!(this.J)) && (d5 != paramDouble2)) {
			paramDouble1 = paramDouble2 = paramDouble3 = 0.0D;
		}

		l = ((this.onGround) || ((d5 != paramDouble2) && (d5 < 0.0D))) ? 1 : 0;

		for (int i1 = 0; i1 < localList.size(); ++i1) {
			paramDouble1 = ((AxisAlignedBB) localList.get(i1)).a(
					this.boundingBox, paramDouble1);
		}
		this.boundingBox.d(paramDouble1, 0.0D, 0.0D);

		if ((!(this.J)) && (d4 != paramDouble1)) {
			paramDouble1 = paramDouble2 = paramDouble3 = 0.0D;
		}

		for (i1 = 0; i1 < localList.size(); ++i1)
			paramDouble3 = ((AxisAlignedBB) localList.get(i1)).c(
					this.boundingBox, paramDouble3);
		this.boundingBox.d(0.0D, 0.0D, paramDouble3);

		if ((!(this.J)) && (d6 != paramDouble3))
			paramDouble1 = paramDouble2 = paramDouble3 = 0.0D;
		int i3;
		if ((this.W > 0.0F) && (l != 0) && (((i != 0) || (this.V < 0.05F)))
				&& (((d4 != paramDouble1) || (d6 != paramDouble3)))) {
			d8 = paramDouble1;
			d9 = paramDouble2;
			d10 = paramDouble3;

			paramDouble1 = d4;
			paramDouble2 = this.W;
			paramDouble3 = d6;

			AxisAlignedBB localAxisAlignedBB2 = this.boundingBox.clone();
			this.boundingBox.d(localAxisAlignedBB1);
			localList = this.world.getCubes(this, this.boundingBox.a(
					paramDouble1, paramDouble2, paramDouble3));

			for (i3 = 0; i3 < localList.size(); ++i3)
				paramDouble2 = ((AxisAlignedBB) localList.get(i3)).b(
						this.boundingBox, paramDouble2);
			this.boundingBox.d(0.0D, paramDouble2, 0.0D);

			if ((!(this.J)) && (d5 != paramDouble2)) {
				paramDouble1 = paramDouble2 = paramDouble3 = 0.0D;
			}

			for (i3 = 0; i3 < localList.size(); ++i3)
				paramDouble1 = ((AxisAlignedBB) localList.get(i3)).a(
						this.boundingBox, paramDouble1);
			this.boundingBox.d(paramDouble1, 0.0D, 0.0D);

			if ((!(this.J)) && (d4 != paramDouble1)) {
				paramDouble1 = paramDouble2 = paramDouble3 = 0.0D;
			}

			for (i3 = 0; i3 < localList.size(); ++i3)
				paramDouble3 = ((AxisAlignedBB) localList.get(i3)).c(
						this.boundingBox, paramDouble3);
			this.boundingBox.d(0.0D, 0.0D, paramDouble3);

			if ((!(this.J)) && (d6 != paramDouble3)) {
				paramDouble1 = paramDouble2 = paramDouble3 = 0.0D;
			}

			if ((!(this.J)) && (d5 != paramDouble2)) {
				paramDouble1 = paramDouble2 = paramDouble3 = 0.0D;
			} else {
				paramDouble2 = -this.W;

				for (i3 = 0; i3 < localList.size(); ++i3)
					paramDouble2 = ((AxisAlignedBB) localList.get(i3)).b(
							this.boundingBox, paramDouble2);
				this.boundingBox.d(0.0D, paramDouble2, 0.0D);
			}

			if (d8 * d8 + d10 * d10 >= paramDouble1 * paramDouble1
					+ paramDouble3 * paramDouble3) {
				paramDouble1 = d8;
				paramDouble2 = d9;
				paramDouble3 = d10;
				this.boundingBox.d(localAxisAlignedBB2);
			}
		}
		this.world.methodProfiler.b();
		this.world.methodProfiler.a("rest");

		this.locX = ((this.boundingBox.a + this.boundingBox.d) / 2.0D);
		this.locY = (this.boundingBox.b + this.height - this.V);
		this.locZ = ((this.boundingBox.c + this.boundingBox.f) / 2.0D);

		this.positionChanged = ((d4 != paramDouble1) || (d6 != paramDouble3));
		this.F = (d5 != paramDouble2);
		this.onGround = ((d5 != paramDouble2) && (d5 < 0.0D));
		this.G = ((this.positionChanged) || (this.F));
		a(paramDouble2, this.onGround);

		if (d4 != paramDouble1)
			this.motX = 0.0D;
		if (d5 != paramDouble2)
			this.motY = 0.0D;
		if (d6 != paramDouble3)
			this.motZ = 0.0D;

		double d8 = this.locX - d1;
		double d9 = this.locY - d2;
		double d10 = this.locZ - d3;

		if ((g_()) && (i == 0) && (this.vehicle == null)) {
			int i2 = MathHelper.floor(this.locX);
			i3 = MathHelper
					.floor(this.locY - 0.2000000029802322D - this.height);
			int i4 = MathHelper.floor(this.locZ);

			Block localBlock = this.world.getType(i2, i3, i4);
			int i5 = this.world.getType(i2, i3 - 1, i4).b();
			if ((i5 == 11) || (i5 == 32) || (i5 == 21)) {
				localBlock = this.world.getType(i2, i3 - 1, i4);
			}
			if (localBlock != Blocks.LADDER)
				d9 = 0.0D;
			Entity tmp1670_1669 = this;
			tmp1670_1669.P = (float) (tmp1670_1669.P + MathHelper.sqrt(d8 * d8
					+ tmp1670_1669 * tmp1670_1669) * 0.6D);
			Entity tmp1700_1699 = this;
			tmp1700_1699.Q = (float) (tmp1700_1699.Q + MathHelper.sqrt(d8 * d8
					+ d9 * d9 + tmp1670_1669 * tmp1670_1669) * 0.6D);

			if ((this.Q > this.d) && (localBlock.getMaterial() != Material.AIR)) {
				this.d = ((int) this.Q + 1);
				if (L()) {
					float f = MathHelper.sqrt(this.motX * this.motX
							* 0.2000000029802322D + this.motY * this.motY
							+ this.motZ * this.motZ * 0.2000000029802322D) * 0.35F;
					if (f > 1.0F)
						f = 1.0F;
					makeSound(G(), f,
							1.0F + (this.random.nextFloat() - this.random
									.nextFloat()) * 0.4F);
				}
				a(i2, i3, i4, localBlock);
				localBlock.b(this.world, i2, i3, i4, this);
			}
		}
		try {
			H();
		} catch (Throwable localThrowable) {
			CrashReport localCrashReport = CrashReport.a(localThrowable,
					"Checking entity block collision");
			CrashReportSystemDetails localCrashReportSystemDetails = localCrashReport
					.a("Entity being checked for collision");

			a(localCrashReportSystemDetails);

			throw new ReportedException(localCrashReport);
		}

		boolean bool = K();
		if (this.world.e(this.boundingBox.shrink(0.001D, 0.001D, 0.001D))) {
			burn(1);
			if (!(bool)) {
				this.fireTicks += 1;
				if (this.fireTicks == 0)
					setOnFire(8);
			}
		} else if (this.fireTicks <= 0) {
			this.fireTicks = (-this.maxFireTicks);
		}

		if ((bool) && (this.fireTicks > 0)) {
			makeSound(
					"random.fizz",
					0.7F,
					1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
			this.fireTicks = (-this.maxFireTicks);
		}

		this.world.methodProfiler.b();
	}

	protected String G() {
		return "game.neutral.swim";
	}

	protected void H() {
		int i = MathHelper.floor(this.boundingBox.a + 0.001D);
		int l = MathHelper.floor(this.boundingBox.b + 0.001D);
		int i1 = MathHelper.floor(this.boundingBox.c + 0.001D);
		int i2 = MathHelper.floor(this.boundingBox.d - 0.001D);
		int i3 = MathHelper.floor(this.boundingBox.e - 0.001D);
		int i4 = MathHelper.floor(this.boundingBox.f - 0.001D);

		if (this.world.b(i, l, i1, i2, i3, i4))
			for (int i5 = i; i5 <= i2; ++i5)
				for (int i6 = l; i6 <= i3; ++i6)
					for (int i7 = i1; i7 <= i4; ++i7) {
						Block localBlock = this.world.getType(i5, i6, i7);
						try {
							localBlock.a(this.world, i5, i6, i7, this);
						} catch (Throwable localThrowable) {
							CrashReport localCrashReport = CrashReport.a(
									localThrowable,
									"Colliding entity with block");
							CrashReportSystemDetails localCrashReportSystemDetails = localCrashReport
									.a("Block being collided with");

							CrashReportSystemDetails.a(
									localCrashReportSystemDetails, i5, i6, i7,
									localBlock, this.world.getData(i5, i6, i7));

							throw new ReportedException(localCrashReport);
						}
					}
	}

	protected void a(int paramInt1, int paramInt2, int paramInt3,
			Block paramBlock) {
		StepSound localStepSound = paramBlock.stepSound;
		if (this.world.getType(paramInt1, paramInt2 + 1, paramInt3) == Blocks.SNOW) {
			localStepSound = Blocks.SNOW.stepSound;
			makeSound(localStepSound.getStepSound(),
					localStepSound.getVolume1() * 0.15F,
					localStepSound.getVolume2());
		} else if (!(paramBlock.getMaterial().isLiquid())) {
			makeSound(localStepSound.getStepSound(),
					localStepSound.getVolume1() * 0.15F,
					localStepSound.getVolume2());
		}
	}

	public void makeSound(String paramString, float paramFloat1,
			float paramFloat2) {
		this.world.makeSound(this, paramString, paramFloat1, paramFloat2);
	}

	protected boolean g_() {
		return true;
	}

	protected void a(double paramDouble, boolean paramBoolean) {
		if (paramBoolean) {
			if (this.fallDistance > 0.0F) {
				b(this.fallDistance);
				this.fallDistance = 0.0F;
			}
		} else {
			if (paramDouble >= 0.0D)
				return;
			Entity tmp36_35 = this;
			tmp36_35.fallDistance = (float) (tmp36_35.fallDistance - paramDouble);
		}
	}

	public AxisAlignedBB I() {
		return null;
	}

	protected void burn(int paramInt) {
		if (!(this.fireProof))
			damageEntity(DamageSource.FIRE, paramInt);
	}

	public final boolean isFireproof() {
		return this.fireProof;
	}

	protected void b(float paramFloat) {
		if (this.passenger == null)
			return;
		this.passenger.b(paramFloat);
	}

	public boolean K() {
		return ((this.inWater)
				|| (this.world.isRainingAt(MathHelper.floor(this.locX),
						MathHelper.floor(this.locY),
						MathHelper.floor(this.locZ))) || (this.world
					.isRainingAt(MathHelper.floor(this.locX),
							MathHelper.floor(this.locY + this.length),
							MathHelper.floor(this.locZ))));
	}

	public boolean L() {
		return this.inWater;
	}

	public boolean M() {
		if (this.world.a(
				this.boundingBox.grow(0.0D, -0.4000000059604645D, 0.0D).shrink(
						0.001D, 0.001D, 0.001D), Material.WATER, this)) {
			if ((!(this.inWater)) && (!(this.justCreated))) {
				float f1 = MathHelper.sqrt(this.motX * this.motX
						* 0.2000000029802322D + this.motY * this.motY
						+ this.motZ * this.motZ * 0.2000000029802322D) * 0.2F;
				if (f1 > 1.0F)
					f1 = 1.0F;
				makeSound(N(), f1,
						1.0F + (this.random.nextFloat() - this.random
								.nextFloat()) * 0.4F);
				float f2 = MathHelper.floor(this.boundingBox.b);
				float f3;
				float f4;
				for (int i = 0; i < 1.0F + this.width * 20.0F; ++i) {
					f3 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
					f4 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
					this.world.addParticle("bubble", this.locX + f3, f2 + 1.0F,
							this.locZ + f4, this.motX,
							this.motY - (this.random.nextFloat() * 0.2F),
							this.motZ);
				}
				for (i = 0; i < 1.0F + this.width * 20.0F; ++i) {
					f3 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
					f4 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
					this.world.addParticle("splash", this.locX + f3, f2 + 1.0F,
							this.locZ + f4, this.motX, this.motY, this.motZ);
				}
			}
			this.fallDistance = 0.0F;
			this.inWater = true;
			this.fireTicks = 0;
		} else {
			this.inWater = false;
		}
		return this.inWater;
	}

	protected String N() {
		return "game.neutral.swim.splash";
	}

	public boolean a(Material paramMaterial) {
		double d1 = this.locY + getHeadHeight();
		int i = MathHelper.floor(this.locX);
		int l = MathHelper.d(MathHelper.floor(d1));
		int i1 = MathHelper.floor(this.locZ);
		Block localBlock = this.world.getType(i, l, i1);
		if (localBlock.getMaterial() == paramMaterial) {
			float f1 = BlockFluids.b(this.world.getData(i, l, i1)) - 0.1111111F;
			float f2 = l + 1 - f1;
			return (d1 < f2);
		}
		return false;
	}

	public float getHeadHeight() {
		return 0.0F;
	}

	public boolean O() {
		return this.world.a(this.boundingBox.grow(-0.1000000014901161D,
				-0.4000000059604645D, -0.1000000014901161D), Material.LAVA);
	}

	public void a(float paramFloat1, float paramFloat2, float paramFloat3) {
		float f1 = paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2;
		if (f1 < 1.0E-004F)
			return;

		f1 = MathHelper.c(f1);
		if (f1 < 1.0F)
			f1 = 1.0F;
		f1 = paramFloat3 / f1;
		paramFloat1 *= f1;
		paramFloat2 *= f1;

		float f2 = MathHelper.sin(this.yaw * 3.141593F / 180.0F);
		float f3 = MathHelper.cos(this.yaw * 3.141593F / 180.0F);

		this.motX += paramFloat1 * f3 - (paramFloat2 * f2);
		this.motZ += paramFloat2 * f3 + paramFloat1 * f2;
	}

	public float d(float paramFloat) {
		int i = MathHelper.floor(this.locX);
		int l = MathHelper.floor(this.locZ);
		if (this.world.isLoaded(i, 0, l)) {
			double d1 = (this.boundingBox.e - this.boundingBox.b) * 0.66D;
			int i1 = MathHelper.floor(this.locY - this.height + d1);
			return this.world.n(i, i1, l);
		}
		return 0.0F;
	}

	public void spawnIn(World paramWorld) {
		this.world = paramWorld;
	}

	public void setLocation(double paramDouble1, double paramDouble2,
			double paramDouble3, float paramFloat1, float paramFloat2) {
		this.lastX = (this.locX = paramDouble1);
		this.lastY = (this.locY = paramDouble2);
		this.lastZ = (this.locZ = paramDouble3);
		this.lastYaw = (this.yaw = paramFloat1);
		this.lastPitch = (this.pitch = paramFloat2);
		this.V = 0.0F;

		double d1 = this.lastYaw - paramFloat1;
		if (d1 < -180.0D)
			this.lastYaw += 360.0F;
		if (d1 >= 180.0D)
			this.lastYaw -= 360.0F;
		setPosition(this.locX, this.locY, this.locZ);
		b(paramFloat1, paramFloat2);
	}

	public void setPositionRotation(double paramDouble1, double paramDouble2,
			double paramDouble3, float paramFloat1, float paramFloat2) {
		this.S = (this.lastX = this.locX = paramDouble1);
		this.T = (this.lastY = this.locY = paramDouble2 + this.height);
		this.U = (this.lastZ = this.locZ = paramDouble3);
		this.yaw = paramFloat1;
		this.pitch = paramFloat2;
		setPosition(this.locX, this.locY, this.locZ);
	}

	public float e(Entity paramEntity) {
		float f1 = (float) (this.locX - paramEntity.locX);
		float f2 = (float) (this.locY - paramEntity.locY);
		float f3 = (float) (this.locZ - paramEntity.locZ);
		return MathHelper.c(f1 * f1 + f2 * f2 + f3 * f3);
	}

	public double e(double paramDouble1, double paramDouble2,
			double paramDouble3) {
		double d1 = this.locX - paramDouble1;
		double d2 = this.locY - paramDouble2;
		double d3 = this.locZ - paramDouble3;
		return (d1 * d1 + d2 * d2 + d3 * d3);
	}

	public double f(double paramDouble1, double paramDouble2,
			double paramDouble3) {
		double d1 = this.locX - paramDouble1;
		double d2 = this.locY - paramDouble2;
		double d3 = this.locZ - paramDouble3;
		return MathHelper.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
	}

	public double f(Entity paramEntity) {
		double d1 = this.locX - paramEntity.locX;
		double d2 = this.locY - paramEntity.locY;
		double d3 = this.locZ - paramEntity.locZ;
		return (d1 * d1 + d2 * d2 + d3 * d3);
	}

	public void b_(EntityHuman paramEntityHuman) {
	}

	public void collide(Entity paramEntity) {
		if ((paramEntity.passenger == this) || (paramEntity.vehicle == this))
			return;

		double d1 = paramEntity.locX - this.locX;
		double d2 = paramEntity.locZ - this.locZ;

		double d3 = MathHelper.a(d1, d2);

		if (d3 >= 0.009999999776482582D) {
			d3 = MathHelper.sqrt(d3);
			d1 /= d3;
			d2 /= d3;

			double d4 = 1.0D / d3;
			if (d4 > 1.0D)
				d4 = 1.0D;
			d1 *= d4;
			d2 *= d4;

			d1 *= 0.0500000007450581D;
			d2 *= 0.0500000007450581D;

			d1 *= (1.0F - this.Y);
			d2 *= (1.0F - this.Y);

			g(-d1, 0.0D, -d2);
			paramEntity.g(d1, 0.0D, d2);
		}
	}

	public void g(double paramDouble1, double paramDouble2, double paramDouble3) {
		this.motX += paramDouble1;
		this.motY += paramDouble2;
		this.motZ += paramDouble3;
		this.al = true;
	}

	protected void P() {
		this.velocityChanged = true;
	}

	public boolean damageEntity(DamageSource paramDamageSource, float paramFloat) {
		if (isInvulnerable())
			return false;
		P();
		return false;
	}

	public boolean Q() {
		return false;
	}

	public boolean R() {
		return false;
	}

	public void b(Entity paramEntity, int paramInt) {
	}

	public boolean c(NBTTagCompound paramNBTTagCompound) {
		String str = V();
		if ((this.dead) || (str == null)) {
			return false;
		}
		paramNBTTagCompound.setString("id", str);
		e(paramNBTTagCompound);
		return true;
	}

	public boolean d(NBTTagCompound paramNBTTagCompound) {
		String str = V();
		if ((this.dead) || (str == null) || (this.passenger != null)) {
			return false;
		}
		paramNBTTagCompound.setString("id", str);
		e(paramNBTTagCompound);
		return true;
	}

	public void e(NBTTagCompound paramNBTTagCompound) {
		try {
			paramNBTTagCompound.set("Pos", a(new double[] { this.locX,
					this.locY + this.V, this.locZ }));
			paramNBTTagCompound.set("Motion", a(new double[] { this.motX,
					this.motY, this.motZ }));
			paramNBTTagCompound.set("Rotation", a(new float[] { this.yaw,
					this.pitch }));

			paramNBTTagCompound.setFloat("FallDistance", this.fallDistance);
			paramNBTTagCompound.setShort("Fire", (short) this.fireTicks);
			paramNBTTagCompound.setShort("Air", (short) getAirTicks());
			paramNBTTagCompound.setBoolean("OnGround", this.onGround);
			paramNBTTagCompound.setInt("Dimension", this.dimension);
			paramNBTTagCompound.setBoolean("Invulnerable", this.invulnerable);
			paramNBTTagCompound.setInt("PortalCooldown", this.portalCooldown);

			paramNBTTagCompound.setLong("UUIDMost", getUniqueID()
					.getMostSignificantBits());
			paramNBTTagCompound.setLong("UUIDLeast", getUniqueID()
					.getLeastSignificantBits());

			b(paramNBTTagCompound);

			if (this.vehicle != null) {
				NBTTagCompound localNBTTagCompound = new NBTTagCompound();
				if (this.vehicle.c(localNBTTagCompound))
					paramNBTTagCompound.set("Riding", localNBTTagCompound);
			}
		} catch (Throwable localThrowable) {
			CrashReport localCrashReport = CrashReport.a(localThrowable,
					"Saving entity NBT");
			CrashReportSystemDetails localCrashReportSystemDetails = localCrashReport
					.a("Entity being saved");
			a(localCrashReportSystemDetails);
			throw new ReportedException(localCrashReport);
		}
	}

	public void f(NBTTagCompound paramNBTTagCompound) {
		Object localObject1;
		Object localObject2;
		try {
			NBTTagList localNBTTagList = paramNBTTagCompound.getList("Pos", 6);
			localObject1 = paramNBTTagCompound.getList("Motion", 6);
			localObject2 = paramNBTTagCompound.getList("Rotation", 5);

			this.motX = ((NBTTagList) localObject1).d(0);
			this.motY = ((NBTTagList) localObject1).d(1);
			this.motZ = ((NBTTagList) localObject1).d(2);

			if (Math.abs(this.motX) > 10.0D) {
				this.motX = 0.0D;
			}
			if (Math.abs(this.motY) > 10.0D) {
				this.motY = 0.0D;
			}
			if (Math.abs(this.motZ) > 10.0D) {
				this.motZ = 0.0D;
			}

			this.lastX = (this.S = this.locX = localNBTTagList.d(0));
			this.lastY = (this.T = this.locY = localNBTTagList.d(1));
			this.lastZ = (this.U = this.locZ = localNBTTagList.d(2));

			this.lastYaw = (this.yaw = ((NBTTagList) localObject2).e(0));
			this.lastPitch = (this.pitch = ((NBTTagList) localObject2).e(1));

			this.fallDistance = paramNBTTagCompound.getFloat("FallDistance");
			this.fireTicks = paramNBTTagCompound.getShort("Fire");
			setAirTicks(paramNBTTagCompound.getShort("Air"));
			this.onGround = paramNBTTagCompound.getBoolean("OnGround");
			this.dimension = paramNBTTagCompound.getInt("Dimension");
			this.invulnerable = paramNBTTagCompound.getBoolean("Invulnerable");
			this.portalCooldown = paramNBTTagCompound.getInt("PortalCooldown");

			if ((paramNBTTagCompound.hasKeyOfType("UUIDMost", 4))
					&& (paramNBTTagCompound.hasKeyOfType("UUIDLeast", 4))) {
				this.uniqueID = new UUID(
						paramNBTTagCompound.getLong("UUIDMost"),
						paramNBTTagCompound.getLong("UUIDLeast"));
			}

			setPosition(this.locX, this.locY, this.locZ);
			b(this.yaw, this.pitch);

			a(paramNBTTagCompound);

			if (U())
				setPosition(this.locX, this.locY, this.locZ);
		} catch (Throwable localThrowable) {
			localObject1 = CrashReport.a(localThrowable, "Loading entity NBT");
			localObject2 = ((CrashReport) localObject1)
					.a("Entity being loaded");
			a((CrashReportSystemDetails) localObject2);
			throw new ReportedException((CrashReport) localObject1);
		}
	}

	protected boolean U() {
		return true;
	}

	protected final String V() {
		return EntityTypes.b(this);
	}

	protected abstract void a(NBTTagCompound paramNBTTagCompound);

	protected abstract void b(NBTTagCompound paramNBTTagCompound);

	public void W() {
	}

	protected NBTTagList a(double[] paramArrayOfDouble) {
		NBTTagList localNBTTagList = new NBTTagList();
		for (double d1 : paramArrayOfDouble)
			localNBTTagList.add(new NBTTagDouble(d1));
		return localNBTTagList;
	}

	protected NBTTagList a(float[] paramArrayOfFloat) {
		NBTTagList localNBTTagList = new NBTTagList();
		for (float f : paramArrayOfFloat)
			localNBTTagList.add(new NBTTagFloat(f));
		return localNBTTagList;
	}

	public EntityItem a(Item paramItem, int paramInt) {
		return a(paramItem, paramInt, 0.0F);
	}

	public EntityItem a(Item paramItem, int paramInt, float paramFloat) {
		return a(new ItemStack(paramItem, paramInt, 0), paramFloat);
	}

	public EntityItem a(ItemStack paramItemStack, float paramFloat) {
		if ((paramItemStack.count == 0) || (paramItemStack.getItem() == null)) {
			return null;
		}

		EntityItem localEntityItem = new EntityItem(this.world, this.locX,
				this.locY + paramFloat, this.locZ, paramItemStack);
		localEntityItem.pickupDelay = 10;
		this.world.addEntity(localEntityItem);
		return localEntityItem;
	}

	public boolean isAlive() {
		return (!(this.dead));
	}

	public boolean inBlock() {
		for (int i = 0; i < 8; ++i) {
			float f1 = ((i >> 0) % 2 - 0.5F) * this.width * 0.8F;
			float f2 = ((i >> 1) % 2 - 0.5F) * 0.1F;
			float f3 = ((i >> 2) % 2 - 0.5F) * this.width * 0.8F;
			int l = MathHelper.floor(this.locX + f1);
			int i1 = MathHelper.floor(this.locY + getHeadHeight() + f2);
			int i2 = MathHelper.floor(this.locZ + f3);
			if (this.world.getType(l, i1, i2).r()) {
				return true;
			}
		}
		return false;
	}

	public boolean c(EntityHuman paramEntityHuman) {
		return false;
	}

	public AxisAlignedBB h(Entity paramEntity) {
		return null;
	}

	public void aa() {
		if (this.vehicle.dead) {
			this.vehicle = null;
			return;
		}
		this.motX = 0.0D;
		this.motY = 0.0D;
		this.motZ = 0.0D;
		h();
		if (this.vehicle == null)
			return;

		this.vehicle.ab();

		this.h += this.vehicle.yaw - this.vehicle.lastYaw;
		this.g += this.vehicle.pitch - this.vehicle.lastPitch;

		while (this.h >= 180.0D)
			this.h -= 360.0D;
		while (this.h < -180.0D) {
			this.h += 360.0D;
		}
		while (this.g >= 180.0D)
			this.g -= 360.0D;
		while (this.g < -180.0D) {
			this.g += 360.0D;
		}
		double d1 = this.h * 0.5D;
		double d2 = this.g * 0.5D;

		float f = 10.0F;
		if (d1 > f)
			d1 = f;
		if (d1 < -f)
			d1 = -f;
		if (d2 > f)
			d2 = f;
		if (d2 < -f)
			d2 = -f;

		this.h -= d1;
		this.g -= d2;
	}

	public void ab() {
		if (this.passenger == null) {
			return;
		}
		this.passenger.setPosition(this.locX,
				this.locY + ad() + this.passenger.ac(), this.locZ);
	}

	public double ac() {
		return this.height;
	}

	public double ad() {
		return (this.length * 0.75D);
	}

	public void mount(Entity paramEntity) {
		this.g = 0.0D;
		this.h = 0.0D;

		if (paramEntity == null) {
			if (this.vehicle != null) {
				setPositionRotation(this.vehicle.locX,
						this.vehicle.boundingBox.b + this.vehicle.length,
						this.vehicle.locZ, this.yaw, this.pitch);
				this.vehicle.passenger = null;
			}
			this.vehicle = null;
			return;
		}
		if (this.vehicle != null) {
			this.vehicle.passenger = null;
		}

		if (paramEntity != null) {
			Entity localEntity = paramEntity.vehicle;
			while (localEntity != null) {
				if (localEntity == this) {
					return;
				}
				localEntity = localEntity.vehicle;
			}
		}
		this.vehicle = paramEntity;
		paramEntity.passenger = this;
	}

	public float ae() {
		return 0.1F;
	}

	public Vec3D af() {
		return null;
	}

	public void ag() {
		if (this.portalCooldown > 0) {
			this.portalCooldown = ah();
			return;
		}

		double d1 = this.lastX - this.locX;
		double d2 = this.lastZ - this.locZ;

		if ((!(this.world.isStatic)) && (!(this.an))) {
			this.aq = Direction.a(d1, d2);
		}

		this.an = true;
	}

	public int ah() {
		return 300;
	}

	public ItemStack[] getEquipment() {
		return null;
	}

	public void setEquipment(int paramInt, ItemStack paramItemStack) {
	}

	public boolean isBurning() {
		int i = ((this.world != null) && (this.world.isStatic)) ? 1 : 0;

		return ((!(this.fireProof)) && (((this.fireTicks > 0) || ((i != 0) && (g(0))))));
	}

	public boolean al() {
		return (this.vehicle != null);
	}

	public boolean isSneaking() {
		return g(1);
	}

	public void setSneaking(boolean paramBoolean) {
		a(1, paramBoolean);
	}

	public boolean isSprinting() {
		return g(3);
	}

	public void setSprinting(boolean paramBoolean) {
		a(3, paramBoolean);
	}

	public boolean isInvisible() {
		return g(5);
	}

	public void setInvisible(boolean paramBoolean) {
		a(5, paramBoolean);
	}

	public void e(boolean paramBoolean) {
		a(4, paramBoolean);
	}

	protected boolean g(int paramInt) {
		return ((this.datawatcher.getByte(0) & 1 << paramInt) != 0);
	}

	protected void a(int paramInt, boolean paramBoolean) {
		int i = this.datawatcher.getByte(0);
		if (paramBoolean)
			this.datawatcher.watch(0, Byte.valueOf((byte) (i | 1 << paramInt)));
		else
			this.datawatcher.watch(0,
					Byte.valueOf((byte) (i & (1 << paramInt ^ 0xFFFFFFFF))));
	}

	public int getAirTicks() {
		return this.datawatcher.getShort(1);
	}

	public void setAirTicks(int paramInt) {
		this.datawatcher.watch(1, Short.valueOf((short) paramInt));
	}

	public void a(EntityLightning paramEntityLightning) {
		burn(5);
		this.fireTicks += 1;
		if (this.fireTicks != 0)
			return;
		setOnFire(8);
	}

	public void a(EntityLiving paramEntityLiving) {
	}

	protected boolean j(double paramDouble1, double paramDouble2,
			double paramDouble3) {
		int i = MathHelper.floor(paramDouble1);
		int l = MathHelper.floor(paramDouble2);
		int i1 = MathHelper.floor(paramDouble3);

		double d1 = paramDouble1 - i;
		double d2 = paramDouble2 - l;
		double d3 = paramDouble3 - i1;

		List localList = this.world.a(this.boundingBox);

		if ((!(localList.isEmpty())) || (this.world.q(i, l, i1))) {
			int i2 = (!(this.world.q(i - 1, l, i1))) ? 1 : 0;
			int i3 = (!(this.world.q(i + 1, l, i1))) ? 1 : 0;
			int i4 = (!(this.world.q(i, l - 1, i1))) ? 1 : 0;
			int i5 = (!(this.world.q(i, l + 1, i1))) ? 1 : 0;
			int i6 = (!(this.world.q(i, l, i1 - 1))) ? 1 : 0;
			int i7 = (!(this.world.q(i, l, i1 + 1))) ? 1 : 0;

			int i8 = 3;
			double d4 = 9999.0D;
			if ((i2 != 0) && (d1 < d4)) {
				d4 = d1;
				i8 = 0;
			}
			if ((i3 != 0) && (1.0D - d1 < d4)) {
				d4 = 1.0D - d1;
				i8 = 1;
			}
			if ((i5 != 0) && (1.0D - d2 < d4)) {
				d4 = 1.0D - d2;
				i8 = 3;
			}
			if ((i6 != 0) && (d3 < d4)) {
				d4 = d3;
				i8 = 4;
			}
			if ((i7 != 0) && (1.0D - d3 < d4)) {
				d4 = 1.0D - d3;
				i8 = 5;
			}

			float f = this.random.nextFloat() * 0.2F + 0.1F;
			if (i8 == 0)
				this.motX = (-f);
			if (i8 == 1)
				this.motX = f;

			if (i8 == 2) {
				this.motY = (-f);
			}
			if (i8 == 3)
				this.motY = f;

			if (i8 == 4)
				this.motZ = (-f);
			if (i8 == 5)
				this.motZ = f;
			return true;
		}

		return false;
	}

	public void ar() {
		this.I = true;
		this.fallDistance = 0.0F;
	}

	public String getName() {
		String str = EntityTypes.b(this);
		if (str == null)
			str = "generic";

		return LocaleI18n.get("entity." + str + ".name");
	}

	public Entity[] as() {
		return null;
	}

	public boolean i(Entity paramEntity) {
		return (this == paramEntity);
	}

	public float getHeadRotation() {
		return 0.0F;
	}

	public boolean au() {
		return true;
	}

	public boolean j(Entity paramEntity) {
		return false;
	}

	public String toString() {
		return String.format(
				"%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]",
				new Object[] {
						super.getClass().getSimpleName(),
						getName(),
						Integer.valueOf(this.id),
						(this.world == null) ? "~NULL~" : this.world
								.getWorldData().getName(),
						Double.valueOf(this.locX), Double.valueOf(this.locY),
						Double.valueOf(this.locZ) });
	}

	public boolean isInvulnerable() {
		return this.invulnerable;
	}

	public void k(Entity paramEntity) {
		setPositionRotation(paramEntity.locX, paramEntity.locY,
				paramEntity.locZ, paramEntity.yaw, paramEntity.pitch);
	}

	public void a(Entity paramEntity, boolean paramBoolean) {
		NBTTagCompound localNBTTagCompound = new NBTTagCompound();
		paramEntity.e(localNBTTagCompound);
		f(localNBTTagCompound);
		this.portalCooldown = paramEntity.portalCooldown;
		this.aq = paramEntity.aq;
	}

	public void b(int paramInt) {
		if ((this.world.isStatic) || (this.dead))
			return;
		this.world.methodProfiler.a("changeDimension");

		MinecraftServer localMinecraftServer = MinecraftServer.getServer();
		int i = this.dimension;
		WorldServer localWorldServer1 = localMinecraftServer.getWorldServer(i);
		WorldServer localWorldServer2 = localMinecraftServer
				.getWorldServer(paramInt);
		this.dimension = paramInt;

		if ((i == 1) && (paramInt == 1)) {
			localWorldServer2 = localMinecraftServer.getWorldServer(0);
			this.dimension = 0;
		}

		this.world.kill(this);
		this.dead = false;
		this.world.methodProfiler.a("reposition");
		localMinecraftServer.getPlayerList().a(this, i, localWorldServer1,
				localWorldServer2);
		this.world.methodProfiler.c("reloading");
		Entity localEntity = EntityTypes.createEntityByName(
				EntityTypes.b(this), localWorldServer2);

		if (localEntity != null) {
			localEntity.a(this, true);

			if ((i == 1) && (paramInt == 1)) {
				ChunkCoordinates localChunkCoordinates = localWorldServer2
						.getSpawn();
				localChunkCoordinates.y = this.world.i(localChunkCoordinates.x,
						localChunkCoordinates.z);
				localEntity.setPositionRotation(localChunkCoordinates.x,
						localChunkCoordinates.y, localChunkCoordinates.z,
						localEntity.yaw, localEntity.pitch);
			}

			localWorldServer2.addEntity(localEntity);
		}

		this.dead = true;
		this.world.methodProfiler.b();

		localWorldServer1.i();
		localWorldServer2.i();
		this.world.methodProfiler.b();
	}

	public float a(Explosion paramExplosion, World paramWorld, int paramInt1,
			int paramInt2, int paramInt3, Block paramBlock) {
		return paramBlock.a(this);
	}

	public boolean a(Explosion paramExplosion, World paramWorld, int paramInt1,
			int paramInt2, int paramInt3, Block paramBlock, float paramFloat) {
		return true;
	}

	public int aw() {
		return 3;
	}

	public int ax() {
		return this.aq;
	}

	public boolean ay() {
		return false;
	}

	public void a(CrashReportSystemDetails paramCrashReportSystemDetails) {
		paramCrashReportSystemDetails.a("Entity Type",
				new CrashReportEntityType(this));

		paramCrashReportSystemDetails.a("Entity ID", Integer.valueOf(this.id));
		paramCrashReportSystemDetails.a("Entity Name",
				new CrashReportEntityName(this));

		paramCrashReportSystemDetails.a(
				"Entity's Exact location",
				String.format(
						"%.2f, %.2f, %.2f",
						new Object[] { Double.valueOf(this.locX),
								Double.valueOf(this.locY),
								Double.valueOf(this.locZ) }));
		paramCrashReportSystemDetails.a("Entity's Block location",
				CrashReportSystemDetails.a(MathHelper.floor(this.locX),
						MathHelper.floor(this.locY),
						MathHelper.floor(this.locZ)));
		paramCrashReportSystemDetails.a(
				"Entity's Momentum",
				String.format(
						"%.2f, %.2f, %.2f",
						new Object[] { Double.valueOf(this.motX),
								Double.valueOf(this.motY),
								Double.valueOf(this.motZ) }));
	}

	public UUID getUniqueID() {
		return this.uniqueID;
	}

	public boolean aB() {
		return true;
	}

	public IChatBaseComponent getScoreboardDisplayName() {
		return new ChatComponentText(getName());
	}

	public void i(int paramInt) {
	}
}
