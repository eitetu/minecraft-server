package com.eitetu.minecraft.server.server;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class EntityHuman extends EntityLiving implements ICommandListener {
	public PlayerInventory inventory = new PlayerInventory(this);
	private InventoryEnderChest enderChest = new InventoryEnderChest();
	public Container defaultContainer;
	public Container activeContainer;
	protected FoodMetaData foodData = new FoodMetaData();
	protected int bq;
	public float br;
	public float bs;
	public int bt;
	public double bu;
	public double bv;
	public double bw;
	public double bx;
	public double by;
	public double bz;
	protected boolean sleeping;
	public ChunkCoordinates bB;
	private int sleepTicks;
	public float bC;
	public float bD;
	private ChunkCoordinates c;
	private boolean d;
	private ChunkCoordinates e;
	public PlayerAbilities abilities = new PlayerAbilities();
	public int expLevel;
	public int expTotal;
	public float exp;
	private ItemStack f;
	private int g;
	protected float bI = 0.1F;
	protected float bJ = 0.02F;
	private int h;
	private final GameProfile i;
	public EntityFishingHook hookedFish;

	public EntityHuman(World paramWorld, GameProfile paramGameProfile) {
		super(paramWorld);
		this.uniqueID = a(paramGameProfile);

		this.i = paramGameProfile;

		this.defaultContainer = new ContainerPlayer(this.inventory,
				!(paramWorld.isStatic), this);
		this.activeContainer = this.defaultContainer;

		this.height = 1.62F;
		ChunkCoordinates localChunkCoordinates = paramWorld.getSpawn();
		setPositionRotation(localChunkCoordinates.x + 0.5D,
				localChunkCoordinates.y + 1, localChunkCoordinates.z + 0.5D,
				0.0F, 0.0F);

		this.aZ = 180.0F;
		this.maxFireTicks = 20;
	}

	protected void aC() {
		super.aC();

		bb().b(GenericAttributes.e).setValue(1.0D);
	}

	protected void c() {
		super.c();

		this.datawatcher.a(16, Byte.valueOf(0));
		this.datawatcher.a(17, Float.valueOf(0.0F));
		this.datawatcher.a(18, Integer.valueOf(0));
	}

	public boolean bx() {
		return (this.f != null);
	}

	public void bz() {
		if (this.f != null) {
			this.f.b(this.world, this, this.g);
		}
		bA();
	}

	public void bA() {
		this.f = null;
		this.g = 0;
		if (!(this.world.isStatic))
			e(false);
	}

	public boolean isBlocking() {
		return ((bx()) && (this.f.getItem().d(this.f) == EnumAnimation.BLOCK));
	}

	public void h() {
		if (this.f != null) {
			ItemStack localItemStack = this.inventory.getItemInHand();
			if (localItemStack == this.f) {
				if ((this.g <= 25) && (this.g % 4 == 0)) {
					c(localItemStack, 5);
				}
				if ((--this.g == 0) && (!(this.world.isStatic)))
					p();
			} else {
				bA();
			}
		}

		if (this.bt > 0)
			this.bt -= 1;
		if (isSleeping()) {
			this.sleepTicks += 1;
			if (this.sleepTicks > 100) {
				this.sleepTicks = 100;
			}

			if (!(this.world.isStatic)) {
				if (!(j()))
					a(true, true, false);
				else if (this.world.w())
					a(false, true, true);
			}
		} else if (this.sleepTicks > 0) {
			this.sleepTicks += 1;
			if (this.sleepTicks >= 110) {
				this.sleepTicks = 0;
			}
		}

		super.h();

		if ((!(this.world.isStatic)) && (this.activeContainer != null)
				&& (!(this.activeContainer.a(this)))) {
			closeInventory();
			this.activeContainer = this.defaultContainer;
		}

		if ((isBurning()) && (this.abilities.isInvulnerable)) {
			extinguish();
		}

		this.bu = this.bx;
		this.bv = this.by;
		this.bw = this.bz;

		double d1 = this.locX - this.bx;
		double d2 = this.locY - this.by;
		double d3 = this.locZ - this.bz;

		double d4 = 10.0D;
		if (d1 > d4)
			this.bu = (this.bx = this.locX);
		if (d3 > d4)
			this.bw = (this.bz = this.locZ);
		if (d2 > d4)
			this.bv = (this.by = this.locY);
		if (d1 < -d4)
			this.bu = (this.bx = this.locX);
		if (d3 < -d4)
			this.bw = (this.bz = this.locZ);
		if (d2 < -d4)
			this.bv = (this.by = this.locY);

		this.bx += d1 * 0.25D;
		this.bz += d3 * 0.25D;
		this.by += d2 * 0.25D;

		if (this.vehicle == null) {
			this.e = null;
		}

		if (!(this.world.isStatic)) {
			this.foodData.a(this);
			a(StatisticList.g, 1);
		}
	}

	public int C() {
		return ((this.abilities.isInvulnerable) ? 0 : 80);
	}

	protected String G() {
		return "game.player.swim";
	}

	protected String N() {
		return "game.player.swim.splash";
	}

	public int ah() {
		return 10;
	}

	public void makeSound(String paramString, float paramFloat1,
			float paramFloat2) {
		this.world.a(this, paramString, paramFloat1, paramFloat2);
	}

	protected void c(ItemStack paramItemStack, int paramInt) {
		if (paramItemStack.o() == EnumAnimation.DRINK) {
			makeSound("random.drink", 0.5F,
					this.world.random.nextFloat() * 0.1F + 0.9F);
		}
		if (paramItemStack.o() == EnumAnimation.EAT) {
			for (int j = 0; j < paramInt; ++j) {
				Vec3D localVec3D1 = Vec3D.a(
						(this.random.nextFloat() - 0.5D) * 0.1D,
						Math.random() * 0.1D + 0.1D, 0.0D);
				localVec3D1.a(-this.pitch * 3.141593F / 180.0F);
				localVec3D1.b(-this.yaw * 3.141593F / 180.0F);

				Vec3D localVec3D2 = Vec3D.a(
						(this.random.nextFloat() - 0.5D) * 0.3D,
						-this.random.nextFloat() * 0.6D - 0.3D, 0.6D);
				localVec3D2.a(-this.pitch * 3.141593F / 180.0F);
				localVec3D2.b(-this.yaw * 3.141593F / 180.0F);
				localVec3D2 = localVec3D2.add(this.locX, this.locY
						+ getHeadHeight(), this.locZ);
				String str = "iconcrack_" + Item.b(paramItemStack.getItem());
				if (paramItemStack.usesData()) {
					str = str + "_" + paramItemStack.getData();
				}
				this.world.addParticle(str, localVec3D2.a, localVec3D2.b,
						localVec3D2.c, localVec3D1.a, localVec3D1.b + 0.05D,
						localVec3D1.c);
			}
			makeSound(
					"random.eat",
					0.5F + 0.5F * this.random.nextInt(2),
					(this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
		}
	}

	protected void p() {
		if (this.f != null) {
			c(this.f, 16);

			int j = this.f.count;
			ItemStack localItemStack = this.f.b(this.world, this);
			if ((localItemStack != this.f)
					|| ((localItemStack != null) && (localItemStack.count != j))) {
				this.inventory.items[this.inventory.itemInHandIndex] = localItemStack;
				if (localItemStack.count == 0) {
					this.inventory.items[this.inventory.itemInHandIndex] = null;
				}
			}
			bA();
		}
	}

	protected boolean bg() {
		return ((getHealth() <= 0.0F) || (isSleeping()));
	}

	protected void closeInventory() {
		this.activeContainer = this.defaultContainer;
	}

	public void mount(Entity paramEntity) {
		if ((this.vehicle != null) && (paramEntity == null)) {
			if (!(this.world.isStatic))
				m(this.vehicle);

			if (this.vehicle != null) {
				this.vehicle.passenger = null;
			}
			this.vehicle = null;

			return;
		}
		super.mount(paramEntity);
	}

	public void aa() {
		if ((!(this.world.isStatic)) && (isSneaking())) {
			mount(null);
			setSneaking(false);
			return;
		}

		double d1 = this.locX;
		double d2 = this.locY;
		double d3 = this.locZ;
		float f1 = this.yaw;
		float f2 = this.pitch;

		super.aa();
		this.br = this.bs;
		this.bs = 0.0F;

		l(this.locX - d1, this.locY - d2, this.locZ - d3);

		if (this.vehicle instanceof EntityPig) {
			this.pitch = f2;
			this.yaw = f1;
			this.aM = ((EntityPig) this.vehicle).aM;
		}
	}

	protected void bp() {
		super.bp();
		ba();
	}

	public void e() {
		if (this.bq > 0)
			this.bq -= 1;

		if ((this.world.difficulty == EnumDifficulty.PEACEFUL)
				&& (getHealth() < getMaxHealth())
				&& (this.world.getGameRules().getBoolean("naturalRegeneration"))
				&& (this.ticksLived % 20 * 12 == 0))
			heal(1.0F);

		this.inventory.k();
		this.br = this.bs;

		super.e();

		AttributeInstance localAttributeInstance = getAttributeInstance(GenericAttributes.d);
		if (!(this.world.isStatic))
			localAttributeInstance.setValue(this.abilities.b());
		this.aQ = this.bJ;
		if (isSprinting()) {
			EntityHuman tmp143_142 = this;
			tmp143_142.aQ = (float) (tmp143_142.aQ + this.bJ * 0.3D);
		}

		i((float) localAttributeInstance.getValue());

		float f1 = MathHelper.sqrt(this.motX * this.motX + this.motZ
				* this.motZ);
		float f2 = (float) Math.atan(-this.motY * 0.2000000029802322D) * 15.0F;
		if (f1 > 0.1F)
			f1 = 0.1F;
		if ((!(this.onGround)) || (getHealth() <= 0.0F))
			f1 = 0.0F;
		if ((this.onGround) || (getHealth() <= 0.0F))
			f2 = 0.0F;
		this.bs += (f1 - this.bs) * 0.4F;
		this.aJ += (f2 - this.aJ) * 0.8F;

		if (getHealth() > 0.0F) {
			AxisAlignedBB localAxisAlignedBB = null;
			if ((this.vehicle != null) && (!(this.vehicle.dead))) {
				localAxisAlignedBB = this.boundingBox.a(
						this.vehicle.boundingBox).grow(1.0D, 0.0D, 1.0D);
			} else
				localAxisAlignedBB = this.boundingBox.grow(1.0D, 0.5D, 1.0D);

			List localList = this.world.getEntities(this, localAxisAlignedBB);
			if (localList != null)
				for (int j = 0; j < localList.size(); ++j) {
					Entity localEntity = (Entity) localList.get(j);
					if (!(localEntity.dead))
						d(localEntity);
				}
		}
	}

	private void d(Entity paramEntity) {
		paramEntity.b_(this);
	}

	public int getScore() {
		return this.datawatcher.getInt(18);
	}

	public void setScore(int paramInt) {
		this.datawatcher.watch(18, Integer.valueOf(paramInt));
	}

	public void addScore(int paramInt) {
		int j = getScore();
		this.datawatcher.watch(18, Integer.valueOf(j + paramInt));
	}

	public void die(DamageSource paramDamageSource) {
		super.die(paramDamageSource);
		a(0.2F, 0.2F);
		setPosition(this.locX, this.locY, this.locZ);
		this.motY = 0.1000000014901161D;

		if (getName().equals("Notch")) {
			a(new ItemStack(Items.APPLE, 1), true, false);
		}
		if (!(this.world.getGameRules().getBoolean("keepInventory"))) {
			this.inventory.m();
		}

		if (paramDamageSource != null) {
			this.motX = (-MathHelper
					.cos((this.az + this.yaw) * 3.141593F / 180.0F) * 0.1F);
			this.motZ = (-MathHelper
					.sin((this.az + this.yaw) * 3.141593F / 180.0F) * 0.1F);
		} else {
			this.motX = (this.motZ = 0.0D);
		}
		this.height = 0.1F;

		a(StatisticList.v, 1);
	}

	protected String aS() {
		return "game.player.hurt";
	}

	protected String aT() {
		return "game.player.die";
	}

	public void b(Entity paramEntity, int paramInt) {
		addScore(paramInt);
		Collection localCollection = getScoreboard().getObjectivesForCriteria(
				IScoreboardCriteria.e);

		if (paramEntity instanceof EntityHuman) {
			a(StatisticList.y, 1);
			localCollection.addAll(getScoreboard().getObjectivesForCriteria(
					IScoreboardCriteria.d));
		} else {
			a(StatisticList.w, 1);
		}

		for (ScoreboardObjective localScoreboardObjective : localCollection) {
			ScoreboardScore localScoreboardScore = getScoreboard()
					.getPlayerScoreForObjective(getName(),
							localScoreboardObjective);
			localScoreboardScore.incrementScore();
		}
	}

	public EntityItem a(boolean paramBoolean) {
		return a(
				this.inventory.splitStack(
						this.inventory.itemInHandIndex,
						((paramBoolean) && (this.inventory.getItemInHand() != null)) ? this.inventory
								.getItemInHand().count : 1), false, true);
	}

	public EntityItem drop(ItemStack paramItemStack, boolean paramBoolean) {
		return a(paramItemStack, false, false);
	}

	public EntityItem a(ItemStack paramItemStack, boolean paramBoolean1,
			boolean paramBoolean2) {
		if (paramItemStack == null)
			return null;
		if (paramItemStack.count == 0)
			return null;

		EntityItem localEntityItem = new EntityItem(this.world, this.locX,
				this.locY - 0.300000011920929D + getHeadHeight(), this.locZ,
				paramItemStack);
		localEntityItem.pickupDelay = 40;

		if (paramBoolean2) {
			localEntityItem.b(getName());
		}

		float f1 = 0.1F;
		float f2;
		if (paramBoolean1) {
			f2 = this.random.nextFloat() * 0.5F;
			float f3 = this.random.nextFloat() * 3.141593F * 2.0F;
			localEntityItem.motX = (-MathHelper.sin(f3) * f2);
			localEntityItem.motZ = (MathHelper.cos(f3) * f2);
			localEntityItem.motY = 0.2000000029802322D;
		} else {
			f1 = 0.3F;
			localEntityItem.motX = (-MathHelper
					.sin(this.yaw / 180.0F * 3.141593F)
					* MathHelper.cos(this.pitch / 180.0F * 3.141593F) * f1);
			localEntityItem.motZ = (MathHelper
					.cos(this.yaw / 180.0F * 3.141593F)
					* MathHelper.cos(this.pitch / 180.0F * 3.141593F) * f1);
			localEntityItem.motY = (-MathHelper
					.sin(this.pitch / 180.0F * 3.141593F) * f1 + 0.1F);
			f1 = 0.02F;

			f2 = this.random.nextFloat() * 3.141593F * 2.0F;
			f1 *= this.random.nextFloat();
			localEntityItem.motX += Math.cos(f2) * f1;
			localEntityItem.motY += (this.random.nextFloat() - this.random
					.nextFloat()) * 0.1F;
			localEntityItem.motZ += Math.sin(f2) * f1;
		}

		a(localEntityItem);
		a(StatisticList.s, 1);

		return localEntityItem;
	}

	protected void a(EntityItem paramEntityItem) {
		this.world.addEntity(paramEntityItem);
	}

	public float a(Block paramBlock, boolean paramBoolean) {
		float f1 = this.inventory.a(paramBlock);
		if (f1 > 1.0F) {
			int j = EnchantmentManager.getDigSpeedEnchantmentLevel(this);
			ItemStack localItemStack = this.inventory.getItemInHand();

			if ((j > 0) && (localItemStack != null)) {
				float f2 = j * j + 1;

				if ((localItemStack.b(paramBlock)) || (f1 > 1.0F))
					f1 += f2;
				else {
					f1 += f2 * 0.08F;
				}
			}
		}

		if (hasEffect(MobEffectList.FASTER_DIG)) {
			f1 *= (1.0F + (getEffect(MobEffectList.FASTER_DIG).getAmplifier() + 1) * 0.2F);
		}
		if (hasEffect(MobEffectList.SLOWER_DIG)) {
			f1 *= (1.0F - ((getEffect(MobEffectList.SLOWER_DIG).getAmplifier() + 1) * 0.2F));
		}

		if ((a(Material.WATER))
				&& (!(EnchantmentManager.hasWaterWorkerEnchantment(this))))
			f1 /= 5.0F;
		if (!(this.onGround))
			f1 /= 5.0F;

		return f1;
	}

	public boolean a(Block paramBlock) {
		return this.inventory.b(paramBlock);
	}

	public void a(NBTTagCompound paramNBTTagCompound) {
		super.a(paramNBTTagCompound);
		this.uniqueID = a(this.i);
		NBTTagList localNBTTagList1 = paramNBTTagCompound.getList("Inventory",
				10);
		this.inventory.b(localNBTTagList1);
		this.inventory.itemInHandIndex = paramNBTTagCompound
				.getInt("SelectedItemSlot");
		this.sleeping = paramNBTTagCompound.getBoolean("Sleeping");
		this.sleepTicks = paramNBTTagCompound.getShort("SleepTimer");

		this.exp = paramNBTTagCompound.getFloat("XpP");
		this.expLevel = paramNBTTagCompound.getInt("XpLevel");
		this.expTotal = paramNBTTagCompound.getInt("XpTotal");
		setScore(paramNBTTagCompound.getInt("Score"));

		if (this.sleeping) {
			this.bB = new ChunkCoordinates(MathHelper.floor(this.locX),
					MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
			a(true, true, false);
		}

		if ((paramNBTTagCompound.hasKeyOfType("SpawnX", 99))
				&& (paramNBTTagCompound.hasKeyOfType("SpawnY", 99))
				&& (paramNBTTagCompound.hasKeyOfType("SpawnZ", 99))) {
			this.c = new ChunkCoordinates(paramNBTTagCompound.getInt("SpawnX"),
					paramNBTTagCompound.getInt("SpawnY"),
					paramNBTTagCompound.getInt("SpawnZ"));
			this.d = paramNBTTagCompound.getBoolean("SpawnForced");
		}

		this.foodData.a(paramNBTTagCompound);
		this.abilities.b(paramNBTTagCompound);

		if (paramNBTTagCompound.hasKeyOfType("EnderItems", 9)) {
			NBTTagList localNBTTagList2 = paramNBTTagCompound.getList(
					"EnderItems", 10);
			this.enderChest.a(localNBTTagList2);
		}
	}

	public void b(NBTTagCompound paramNBTTagCompound) {
		super.b(paramNBTTagCompound);
		paramNBTTagCompound
				.set("Inventory", this.inventory.a(new NBTTagList()));
		paramNBTTagCompound.setInt("SelectedItemSlot",
				this.inventory.itemInHandIndex);
		paramNBTTagCompound.setBoolean("Sleeping", this.sleeping);
		paramNBTTagCompound.setShort("SleepTimer", (short) this.sleepTicks);
		paramNBTTagCompound.setFloat("XpP", this.exp);
		paramNBTTagCompound.setInt("XpLevel", this.expLevel);
		paramNBTTagCompound.setInt("XpTotal", this.expTotal);
		paramNBTTagCompound.setInt("Score", getScore());

		if (this.c != null) {
			paramNBTTagCompound.setInt("SpawnX", this.c.x);
			paramNBTTagCompound.setInt("SpawnY", this.c.y);
			paramNBTTagCompound.setInt("SpawnZ", this.c.z);
			paramNBTTagCompound.setBoolean("SpawnForced", this.d);
		}

		this.foodData.b(paramNBTTagCompound);
		this.abilities.a(paramNBTTagCompound);
		paramNBTTagCompound.set("EnderItems", this.enderChest.h());
	}

	public void openContainer(IInventory paramIInventory) {
	}

	public void openHopper(TileEntityHopper paramTileEntityHopper) {
	}

	public void openMinecartHopper(
			EntityMinecartHopper paramEntityMinecartHopper) {
	}

	public void openHorseInventory(EntityHorse paramEntityHorse,
			IInventory paramIInventory) {
	}

	public void startEnchanting(int paramInt1, int paramInt2, int paramInt3,
			String paramString) {
	}

	public void openAnvil(int paramInt1, int paramInt2, int paramInt3) {
	}

	public void startCrafting(int paramInt1, int paramInt2, int paramInt3) {
	}

	public float getHeadHeight() {
		return 0.12F;
	}

	protected void e_() {
		this.height = 1.62F;
	}

	public boolean damageEntity(DamageSource paramDamageSource, float paramFloat) {
		if (isInvulnerable())
			return false;
		if ((this.abilities.isInvulnerable)
				&& (!(paramDamageSource.ignoresInvulnerability())))
			return false;

		this.aU = 0;
		if (getHealth() <= 0.0F)
			return false;

		if ((isSleeping()) && (!(this.world.isStatic))) {
			a(true, true, false);
		}

		if (paramDamageSource.r()) {
			if (this.world.difficulty == EnumDifficulty.PEACEFUL)
				paramFloat = 0.0F;
			if (this.world.difficulty == EnumDifficulty.EASY)
				paramFloat = paramFloat / 2.0F + 1.0F;
			if (this.world.difficulty == EnumDifficulty.HARD)
				paramFloat = paramFloat * 3.0F / 2.0F;
		}

		if (paramFloat == 0.0F)
			return false;

		Entity localEntity = paramDamageSource.getEntity();
		if ((localEntity instanceof EntityArrow)
				&& (((EntityArrow) localEntity).shooter != null)) {
			localEntity = ((EntityArrow) localEntity).shooter;
		}

		a(StatisticList.u, Math.round(paramFloat * 10.0F));

		return super.damageEntity(paramDamageSource, paramFloat);
	}

	public boolean a(EntityHuman paramEntityHuman) {
		ScoreboardTeamBase localScoreboardTeamBase1 = getScoreboardTeam();
		ScoreboardTeamBase localScoreboardTeamBase2 = paramEntityHuman
				.getScoreboardTeam();

		if (localScoreboardTeamBase1 == null) {
			return true;
		}
		if (!(localScoreboardTeamBase1.isAlly(localScoreboardTeamBase2))) {
			return true;
		}
		return localScoreboardTeamBase1.allowFriendlyFire();
	}

	protected void h(float paramFloat) {
		this.inventory.a(paramFloat);
	}

	public int aU() {
		return this.inventory.l();
	}

	public float bD() {
		int j = 0;
		for (ItemStack localItemStack : this.inventory.armor) {
			if (localItemStack != null) {
				++j;
			}
		}
		return (j / this.inventory.armor.length);
	}

	protected void d(DamageSource paramDamageSource, float paramFloat) {
		if (isInvulnerable())
			return;
		if ((!(paramDamageSource.ignoresArmor())) && (isBlocking())
				&& (paramFloat > 0.0F)) {
			paramFloat = (1.0F + paramFloat) * 0.5F;
		}
		paramFloat = b(paramDamageSource, paramFloat);
		paramFloat = c(paramDamageSource, paramFloat);

		float f1 = paramFloat;
		paramFloat = Math.max(paramFloat - br(), 0.0F);
		m(br() - (f1 - paramFloat));
		if (paramFloat == 0.0F)
			return;

		a(paramDamageSource.f());
		float f2 = getHealth();
		setHealth(getHealth() - paramFloat);
		aV().a(paramDamageSource, f2, paramFloat);
	}

	public void openFurnace(TileEntityFurnace paramTileEntityFurnace) {
	}

	public void openDispenser(TileEntityDispenser paramTileEntityDispenser) {
	}

	public void a(TileEntity paramTileEntity) {
	}

	public void a(CommandBlockListenerAbstract paramCommandBlockListenerAbstract) {
	}

	public void openBrewingStand(
			TileEntityBrewingStand paramTileEntityBrewingStand) {
	}

	public void openBeacon(TileEntityBeacon paramTileEntityBeacon) {
	}

	public void openTrade(IMerchant paramIMerchant, String paramString) {
	}

	public void b(ItemStack paramItemStack) {
	}

	public boolean q(Entity paramEntity) {
		Object localObject1 = bE();
		Object localObject2 = (localObject1 != null) ? ((ItemStack) localObject1)
				.cloneItemStack() : null;
		if (paramEntity.c(this)) {
			if ((localObject1 != null) && (localObject1 == bE())) {
				if ((((ItemStack) localObject1).count <= 0)
						&& (!(this.abilities.canInstantlyBuild)))
					bF();
				else if ((((ItemStack) localObject1).count < localObject2.count)
						&& (this.abilities.canInstantlyBuild)) {
					((ItemStack) localObject1).count = localObject2.count;
				}
			}
			return true;
		}

		if ((localObject1 != null) && (paramEntity instanceof EntityLiving)) {
			if (this.abilities.canInstantlyBuild)
				localObject1 = localObject2;
			if (((ItemStack) localObject1).a(this, (EntityLiving) paramEntity)) {
				if ((((ItemStack) localObject1).count <= 0)
						&& (!(this.abilities.canInstantlyBuild))) {
					bF();
				}
				return true;
			}
		}
		return false;
	}

	public ItemStack bE() {
		return this.inventory.getItemInHand();
	}

	public void bF() {
		this.inventory.setItem(this.inventory.itemInHandIndex, null);
	}

	public double ac() {
		return (this.height - 0.5F);
	}

	public void attack(Entity paramEntity) {
		if (!(paramEntity.au())) {
			return;
		}
		if (paramEntity.j(this)) {
			return;
		}

		float f1 = (float) getAttributeInstance(GenericAttributes.e).getValue();

		int j = 0;
		float f2 = 0.0F;
		if (paramEntity instanceof EntityLiving) {
			f2 = EnchantmentManager.a(this, (EntityLiving) paramEntity);
			j += EnchantmentManager.getKnockbackEnchantmentLevel(this,
					(EntityLiving) paramEntity);
		}
		if (isSprinting()) {
			++j;
		}

		if ((f1 <= 0.0F) && (f2 <= 0.0F))
			return;
		int k = ((this.fallDistance > 0.0F) && (!(this.onGround)) && (!(h_()))
				&& (!(L())) && (!(hasEffect(MobEffectList.BLINDNESS)))
				&& (this.vehicle == null) && (paramEntity instanceof EntityLiving)) ? 1
				: 0;
		if ((k != 0) && (f1 > 0.0F)) {
			f1 *= 1.5F;
		}
		f1 += f2;

		int l = 0;
		int i1 = EnchantmentManager.getFireAspectEnchantmentLevel(this);
		if ((paramEntity instanceof EntityLiving) && (i1 > 0)
				&& (!(paramEntity.isBurning()))) {
			l = 1;
			paramEntity.setOnFire(1);
		}

		boolean bool = paramEntity.damageEntity(
				DamageSource.playerAttack(this), f1);
		if (bool) {
			if (j > 0) {
				paramEntity.g(-MathHelper.sin(this.yaw * 3.141593F / 180.0F)
						* j * 0.5F, 0.1D,
						MathHelper.cos(this.yaw * 3.141593F / 180.0F) * j
								* 0.5F);
				this.motX *= 0.6D;
				this.motZ *= 0.6D;
				setSprinting(false);
			}

			if (k != 0) {
				b(paramEntity);
			}
			if (f2 > 0.0F) {
				c(paramEntity);
			}

			if (f1 >= 18.0F) {
				a(AchievementList.F);
			}
			l(paramEntity);

			if (paramEntity instanceof EntityLiving)
				EnchantmentManager.a((EntityLiving) paramEntity, this);
			EnchantmentManager.b(this, paramEntity);

			ItemStack localItemStack = bE();
			Object localObject = paramEntity;
			if (paramEntity instanceof EntityComplexPart) {
				IComplex localIComplex = ((EntityComplexPart) paramEntity).owner;
				if ((localIComplex != null)
						&& (localIComplex instanceof EntityLiving)) {
					localObject = (EntityLiving) localIComplex;
				}
			}
			if ((localItemStack != null)
					&& (localObject instanceof EntityLiving)) {
				localItemStack.a((EntityLiving) localObject, this);
				if (localItemStack.count <= 0) {
					bF();
				}
			}
			if (paramEntity instanceof EntityLiving) {
				a(StatisticList.t, Math.round(f1 * 10.0F));

				if (i1 > 0) {
					paramEntity.setOnFire(i1 * 4);
				}
			}

			a(0.3F);
		} else if (l != 0) {
			paramEntity.extinguish();
		}
	}

	public void b(Entity paramEntity) {
	}

	public void c(Entity paramEntity) {
	}

	public void die() {
		super.die();
		this.defaultContainer.b(this);
		if (this.activeContainer != null)
			this.activeContainer.b(this);
	}

	public boolean inBlock() {
		return ((!(this.sleeping)) && (super.inBlock()));
	}

	public GameProfile getProfile() {
		return this.i;
	}

	public EnumBedResult a(int paramInt1, int paramInt2, int paramInt3) {
		if (!(this.world.isStatic)) {
			if ((isSleeping()) || (!(isAlive()))) {
				return EnumBedResult.OTHER_PROBLEM;
			}

			if (!(this.world.worldProvider.d())) {
				return EnumBedResult.NOT_POSSIBLE_HERE;
			}
			if (this.world.w()) {
				return EnumBedResult.NOT_POSSIBLE_NOW;
			}
			if ((Math.abs(this.locX - paramInt1) > 3.0D)
					|| (Math.abs(this.locY - paramInt2) > 2.0D)
					|| (Math.abs(this.locZ - paramInt3) > 3.0D)) {
				return EnumBedResult.TOO_FAR_AWAY;
			}

			double d1 = 8.0D;
			double d2 = 5.0D;
			List localList = this.world.a(
					EntityMonster.class,
					AxisAlignedBB.a(paramInt1 - d1, paramInt2 - d2, paramInt3
							- d1, paramInt1 + d1, paramInt2 + d2, paramInt3
							+ d1));
			if (!(localList.isEmpty())) {
				return EnumBedResult.NOT_SAFE;
			}
		}

		if (al()) {
			mount(null);
		}

		a(0.2F, 0.2F);
		this.height = 0.2F;
		if (this.world.isLoaded(paramInt1, paramInt2, paramInt3)) {
			int j = this.world.getData(paramInt1, paramInt2, paramInt3);
			int k = BlockBed.l(j);
			float f1 = 0.5F;
			float f2 = 0.5F;

			switch (k) {
			case 0:
				f2 = 0.9F;
				break;
			case 2:
				f2 = 0.1F;
				break;
			case 1:
				f1 = 0.1F;
				break;
			case 3:
				f1 = 0.9F;
			}

			w(k);
			setPosition(paramInt1 + f1, paramInt2 + 0.9375F, paramInt3 + f2);
		} else {
			setPosition(paramInt1 + 0.5F, paramInt2 + 0.9375F, paramInt3 + 0.5F);
		}
		this.sleeping = true;
		this.sleepTicks = 0;
		this.bB = new ChunkCoordinates(paramInt1, paramInt2, paramInt3);
		this.motX = (this.motZ = this.motY = 0.0D);

		if (!(this.world.isStatic)) {
			this.world.everyoneSleeping();
		}

		return EnumBedResult.OK;
	}

	private void w(int paramInt) {
		this.bC = 0.0F;
		this.bD = 0.0F;

		switch (paramInt) {
		case 0:
			this.bD = -1.8F;
			break;
		case 2:
			this.bD = 1.8F;
			break;
		case 1:
			this.bC = 1.8F;
			break;
		case 3:
			this.bC = -1.8F;
		}
	}

	public void a(boolean paramBoolean1, boolean paramBoolean2,
			boolean paramBoolean3) {
		a(0.6F, 1.8F);
		e_();

		ChunkCoordinates localChunkCoordinates1 = this.bB;
		ChunkCoordinates localChunkCoordinates2 = this.bB;
		if ((localChunkCoordinates1 != null)
				&& (this.world.getType(localChunkCoordinates1.x,
						localChunkCoordinates1.y, localChunkCoordinates1.z) == Blocks.BED)) {
			BlockBed.a(this.world, localChunkCoordinates1.x,
					localChunkCoordinates1.y, localChunkCoordinates1.z, false);

			localChunkCoordinates2 = BlockBed.a(this.world,
					localChunkCoordinates1.x, localChunkCoordinates1.y,
					localChunkCoordinates1.z, 0);
			if (localChunkCoordinates2 == null) {
				localChunkCoordinates2 = new ChunkCoordinates(
						localChunkCoordinates1.x, localChunkCoordinates1.y + 1,
						localChunkCoordinates1.z);
			}
			setPosition(localChunkCoordinates2.x + 0.5F,
					localChunkCoordinates2.y + this.height + 0.1F,
					localChunkCoordinates2.z + 0.5F);
		}

		this.sleeping = false;
		if ((!(this.world.isStatic)) && (paramBoolean2)) {
			this.world.everyoneSleeping();
		}
		if (paramBoolean1)
			this.sleepTicks = 0;
		else {
			this.sleepTicks = 100;
		}
		if (paramBoolean3)
			setRespawnPosition(this.bB, false);
	}

	private boolean j() {
		return (this.world.getType(this.bB.x, this.bB.y, this.bB.z) == Blocks.BED);
	}

	public static ChunkCoordinates getBed(World paramWorld,
			ChunkCoordinates paramChunkCoordinates, boolean paramBoolean) {
		IChunkProvider localIChunkProvider = paramWorld.L();
		localIChunkProvider.getChunkAt(paramChunkCoordinates.x - 3 >> 4,
				paramChunkCoordinates.z - 3 >> 4);
		localIChunkProvider.getChunkAt(paramChunkCoordinates.x + 3 >> 4,
				paramChunkCoordinates.z - 3 >> 4);
		localIChunkProvider.getChunkAt(paramChunkCoordinates.x - 3 >> 4,
				paramChunkCoordinates.z + 3 >> 4);
		localIChunkProvider.getChunkAt(paramChunkCoordinates.x + 3 >> 4,
				paramChunkCoordinates.z + 3 >> 4);

		if (paramWorld.getType(paramChunkCoordinates.x,
				paramChunkCoordinates.y, paramChunkCoordinates.z) != Blocks.BED) {
			localObject = paramWorld.getType(paramChunkCoordinates.x,
					paramChunkCoordinates.y, paramChunkCoordinates.z)
					.getMaterial();
			Material localMaterial = paramWorld.getType(
					paramChunkCoordinates.x, paramChunkCoordinates.y + 1,
					paramChunkCoordinates.z).getMaterial();
			int j = ((!(((Material) localObject).isBuildable())) && (!(((Material) localObject)
					.isLiquid()))) ? 1 : 0;
			int k = ((!(localMaterial.isBuildable())) && (!(localMaterial
					.isLiquid()))) ? 1 : 0;

			if ((paramBoolean) && (j != 0) && (k != 0)) {
				return paramChunkCoordinates;
			}

			return null;
		}

		Object localObject = BlockBed.a(paramWorld, paramChunkCoordinates.x,
				paramChunkCoordinates.y, paramChunkCoordinates.z, 0);
		return ((ChunkCoordinates) localObject);
	}

	public boolean isSleeping() {
		return this.sleeping;
	}

	public boolean isDeeplySleeping() {
		return ((this.sleeping) && (this.sleepTicks >= 100));
	}

	protected void b(int paramInt, boolean paramBoolean) {
		int j = this.datawatcher.getByte(16);
		if (paramBoolean)
			this.datawatcher
					.watch(16, Byte.valueOf((byte) (j | 1 << paramInt)));
		else
			this.datawatcher.watch(16,
					Byte.valueOf((byte) (j & (1 << paramInt ^ 0xFFFFFFFF))));
	}

	public void b(IChatBaseComponent paramIChatBaseComponent) {
	}

	public ChunkCoordinates getBed() {
		return this.c;
	}

	public boolean isRespawnForced() {
		return this.d;
	}

	public void setRespawnPosition(ChunkCoordinates paramChunkCoordinates,
			boolean paramBoolean) {
		if (paramChunkCoordinates != null) {
			this.c = new ChunkCoordinates(paramChunkCoordinates);
			this.d = paramBoolean;
		} else {
			this.c = null;
			this.d = false;
		}
	}

	public void a(Statistic paramStatistic) {
		a(paramStatistic, 1);
	}

	public void a(Statistic paramStatistic, int paramInt) {
	}

	public void bi() {
		super.bi();

		a(StatisticList.r, 1);
		if (isSprinting())
			a(0.8F);
		else
			a(0.2F);
	}

	public void e(float paramFloat1, float paramFloat2) {
		double d1 = this.locX;
		double d2 = this.locY;
		double d3 = this.locZ;

		if ((this.abilities.isFlying) && (this.vehicle == null)) {
			double d4 = this.motY;
			float f1 = this.aQ;
			this.aQ = this.abilities.a();
			super.e(paramFloat1, paramFloat2);
			this.motY = (d4 * 0.6D);
			this.aQ = f1;
		} else {
			super.e(paramFloat1, paramFloat2);
		}

		checkMovement(this.locX - d1, this.locY - d2, this.locZ - d3);
	}

	public float bk() {
		return (float) getAttributeInstance(GenericAttributes.d).getValue();
	}

	public void checkMovement(double paramDouble1, double paramDouble2,
			double paramDouble3) {
		if (this.vehicle != null)
			return;
		int j;
		if (a(Material.WATER)) {
			j = Math.round(MathHelper
					.sqrt(paramDouble1 * paramDouble1 + paramDouble2
							* paramDouble2 + paramDouble3 * paramDouble3) * 100.0F);
			if (j > 0) {
				a(StatisticList.m, j);
				a(0.015F * j * 0.01F);
			}
		} else if (L()) {
			j = Math.round(MathHelper.sqrt(paramDouble1 * paramDouble1
					+ paramDouble3 * paramDouble3) * 100.0F);
			if (j > 0) {
				a(StatisticList.i, j);
				a(0.015F * j * 0.01F);
			}
		} else if (h_()) {
			if (paramDouble2 > 0.0D)
				a(StatisticList.k, (int) Math.round(paramDouble2 * 100.0D));
		} else if (this.onGround) {
			j = Math.round(MathHelper.sqrt(paramDouble1 * paramDouble1
					+ paramDouble3 * paramDouble3) * 100.0F);
			if (j > 0) {
				a(StatisticList.h, j);
				if (isSprinting())
					a(0.09999999F * j * 0.01F);
				else
					a(0.01F * j * 0.01F);
			}
		} else {
			j = Math.round(MathHelper.sqrt(paramDouble1 * paramDouble1
					+ paramDouble3 * paramDouble3) * 100.0F);
			if (j > 25)
				a(StatisticList.l, j);
		}
	}

	private void l(double paramDouble1, double paramDouble2, double paramDouble3) {
		if (this.vehicle != null) {
			int j = Math
					.round(MathHelper.sqrt(paramDouble1 * paramDouble1
							+ paramDouble2 * paramDouble2 + paramDouble3
							* paramDouble3) * 100.0F);
			if (j > 0)
				if (this.vehicle instanceof EntityMinecartAbstract) {
					a(StatisticList.n, j);

					if (this.e == null)
						this.e = new ChunkCoordinates(
								MathHelper.floor(this.locX),
								MathHelper.floor(this.locY),
								MathHelper.floor(this.locZ));
					else if (this.e.e(MathHelper.floor(this.locX),
							MathHelper.floor(this.locY),
							MathHelper.floor(this.locZ)) >= 1000000.0D) {
						a(AchievementList.q, 1);
					}
				} else if (this.vehicle instanceof EntityBoat) {
					a(StatisticList.o, j);
				} else if (this.vehicle instanceof EntityPig) {
					a(StatisticList.p, j);
				} else if (this.vehicle instanceof EntityHorse) {
					a(StatisticList.q, j);
				}
		}
	}

	protected void b(float paramFloat) {
		if (this.abilities.canFly)
			return;

		if (paramFloat >= 2.0F) {
			a(StatisticList.j, (int) Math.round(paramFloat * 100.0D));
		}
		super.b(paramFloat);
	}

	protected String o(int paramInt) {
		if (paramInt > 4) {
			return "game.player.hurt.fall.big";
		}
		return "game.player.hurt.fall.small";
	}

	public void a(EntityLiving paramEntityLiving) {
		if (paramEntityLiving instanceof IMonster) {
			a(AchievementList.s);
		}

		int j = EntityTypes.a(paramEntityLiving);
		MonsterEggInfo localMonsterEggInfo = (MonsterEggInfo) EntityTypes.eggInfo
				.get(Integer.valueOf(j));
		if (localMonsterEggInfo != null)
			a(localMonsterEggInfo.killEntityStatistic, 1);
	}

	public void ar() {
		if (this.abilities.isFlying)
			return;
		super.ar();
	}

	public ItemStack r(int paramInt) {
		return this.inventory.d(paramInt);
	}

	public void giveExp(int paramInt) {
		addScore(paramInt);
		int j = 2147483647 - this.expTotal;
		if (paramInt > j) {
			paramInt = j;
		}

		this.exp += paramInt / getExpToLevel();
		this.expTotal += paramInt;
		while (this.exp >= 1.0F) {
			this.exp = ((this.exp - 1.0F) * getExpToLevel());
			levelDown(1);
			this.exp /= getExpToLevel();
		}
	}

	public void levelDown(int paramInt) {
		this.expLevel += paramInt;
		if (this.expLevel < 0) {
			this.expLevel = 0;
			this.exp = 0.0F;
			this.expTotal = 0;
		}

		if ((paramInt > 0) && (this.expLevel % 5 == 0)
				&& (this.h < this.ticksLived - 100.0F)) {
			float f1 = (this.expLevel > 30) ? 1.0F : this.expLevel / 30.0F;
			this.world.makeSound(this, "random.levelup", f1 * 0.75F, 1.0F);
			this.h = this.ticksLived;
		}
	}

	public int getExpToLevel() {
		if (this.expLevel >= 30) {
			return (62 + (this.expLevel - 30) * 7);
		}
		if (this.expLevel >= 15) {
			return (17 + (this.expLevel - 15) * 3);
		}
		return 17;
	}

	public void a(float paramFloat) {
		if (this.abilities.isInvulnerable)
			return;

		if (!(this.world.isStatic))
			this.foodData.a(paramFloat);
	}

	public FoodMetaData getFoodData() {
		return this.foodData;
	}

	public boolean g(boolean paramBoolean) {
		return ((((paramBoolean) || (this.foodData.c()))) && (!(this.abilities.isInvulnerable)));
	}

	public boolean bQ() {
		return ((getHealth() > 0.0F) && (getHealth() < getMaxHealth()));
	}

	public void a(ItemStack paramItemStack, int paramInt) {
		if (paramItemStack == this.f)
			return;
		this.f = paramItemStack;
		this.g = paramInt;
		if (!(this.world.isStatic))
			e(true);
	}

	public boolean d(int paramInt1, int paramInt2, int paramInt3) {
		if (this.abilities.mayBuild) {
			return true;
		}
		Block localBlock = this.world.getType(paramInt1, paramInt2, paramInt3);
		if (localBlock.getMaterial() != Material.AIR) {
			if (localBlock.getMaterial().q())
				return true;
			if (bE() != null) {
				ItemStack localItemStack = bE();

				if ((localItemStack.b(localBlock))
						|| (localItemStack.a(localBlock) > 1.0F)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean a(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4, ItemStack paramItemStack) {
		if (this.abilities.mayBuild) {
			return true;
		}
		if (paramItemStack != null) {
			return paramItemStack.z();
		}
		return false;
	}

	protected int getExpValue(EntityHuman paramEntityHuman) {
		if (this.world.getGameRules().getBoolean("keepInventory"))
			return 0;

		int j = this.expLevel * 7;
		if (j > 100) {
			return 100;
		}
		return j;
	}

	protected boolean alwaysGivesExp() {
		return true;
	}

	public void copyTo(EntityHuman paramEntityHuman, boolean paramBoolean) {
		if (paramBoolean) {
			this.inventory.b(paramEntityHuman.inventory);

			setHealth(paramEntityHuman.getHealth());
			this.foodData = paramEntityHuman.foodData;

			this.expLevel = paramEntityHuman.expLevel;
			this.expTotal = paramEntityHuman.expTotal;
			this.exp = paramEntityHuman.exp;

			setScore(paramEntityHuman.getScore());
			this.aq = paramEntityHuman.aq;
		} else if (this.world.getGameRules().getBoolean("keepInventory")) {
			this.inventory.b(paramEntityHuman.inventory);

			this.expLevel = paramEntityHuman.expLevel;
			this.expTotal = paramEntityHuman.expTotal;
			this.exp = paramEntityHuman.exp;
			setScore(paramEntityHuman.getScore());
		}
		this.enderChest = paramEntityHuman.enderChest;
	}

	protected boolean g_() {
		return (!(this.abilities.isFlying));
	}

	public void updateAbilities() {
	}

	public void a(EnumGamemode paramEnumGamemode) {
	}

	public String getName() {
		return this.i.getName();
	}

	public World getWorld() {
		return this.world;
	}

	public InventoryEnderChest getEnderChest() {
		return this.enderChest;
	}

	public ItemStack getEquipment(int paramInt) {
		if (paramInt == 0)
			return this.inventory.getItemInHand();
		return this.inventory.armor[(paramInt - 1)];
	}

	public ItemStack bd() {
		return this.inventory.getItemInHand();
	}

	public void setEquipment(int paramInt, ItemStack paramItemStack) {
		this.inventory.armor[paramInt] = paramItemStack;
	}

	public ItemStack[] getEquipment() {
		return this.inventory.armor;
	}

	public boolean aB() {
		return (!(this.abilities.isFlying));
	}

	public Scoreboard getScoreboard() {
		return this.world.getScoreboard();
	}

	public ScoreboardTeamBase getScoreboardTeam() {
		return getScoreboard().getPlayerTeam(getName());
	}

	public IChatBaseComponent getScoreboardDisplayName() {
		ChatComponentText localChatComponentText = new ChatComponentText(
				ScoreboardTeam.getPlayerDisplayName(getScoreboardTeam(),
						getName()));
		localChatComponentText.getChatModifier().setChatClickable(
				new ChatClickable(EnumClickAction.SUGGEST_COMMAND, "/msg "
						+ getName() + " "));
		return localChatComponentText;
	}

	public void m(float paramFloat) {
		if (paramFloat < 0.0F)
			paramFloat = 0.0F;
		getDataWatcher().watch(17, Float.valueOf(paramFloat));
	}

	public float br() {
		return getDataWatcher().getFloat(17);
	}

	public static UUID a(GameProfile paramGameProfile) {
		UUID localUUID = paramGameProfile.getId();
		if (localUUID == null) {
			localUUID = UUID.nameUUIDFromBytes("OfflinePlayer:"
					+ paramGameProfile.getName().getBytes(Charsets.UTF_8));
		}
		return localUUID;
	}
}