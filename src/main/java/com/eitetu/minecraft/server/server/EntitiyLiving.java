package com.eitetu.minecraft.server.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class EntitiyLiving extends Entity {

	  private static final UUID b = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
	  private static final AttributeModifier c = new AttributeModifier(b, "Sprinting speed boost", 0.300000011920929D, 2).a(false);
	  private AttributeMapBase d;
	  private final CombatTracker combatTracker = new CombatTracker(???);
	  private final HashMap effects = new HashMap();
	  private final ItemStack[] g = new ItemStack[5];
	  public boolean at;
	  public int au;
	  public int av;
	  public float aw;
	  public int hurtTicks;
	  public int ay;
	  public float az;
	  public int deathTicks;
	  public int attackTicks;
	  public float aC;
	  public float aD;
	  public float aE;
	  public float aF;
	  public float aG;
	  public int maxNoDamageTicks = 20;
	  public float aI;
	  public float aJ;
	  public float aK;
	  public float aL;
	  public float aM;
	  public float aN;
	  public float aO;
	  public float aP;
	  public float aQ = 0.02F;
	  protected EntityHuman killer;
	  protected int lastDamageByPlayerTime;
	  protected boolean aT;
	  protected int aU;
	  protected float aV;
	  protected float aW;
	  protected float aX;
	  protected float aY;
	  protected float aZ;
	  protected int ba;
	  protected float lastDamage;
	  protected boolean bc;
	  public float bd;
	  public float be;
	  protected float bf;
	  protected int bg;
	  protected double bh;
	  protected double bi;
	  protected double bj;
	  protected double bk;
	  protected double bl;
	  private boolean updateEffects = true;
	  private EntityLiving lastDamager;
	  private int bm;
	  private EntityLiving bn;
	  private int bo;
	  private float bp;
	  private int bq;
	  private float br;

	  public EntityLiving(World paramWorld)
	  {
	    super(paramWorld);

	    aC();
	    setHealth(getMaxHealth());

	    this.k = true;
	    this.aL = ((float)(Math.random() + 1.0D) * 0.01F);
	    setPosition(this.locX, this.locY, this.locZ);
	    this.aK = ((float)Math.random() * 12398.0F);
	    this.yaw = (float)(Math.random() * 3.141592741012573D * 2.0D);
	    this.aO = this.yaw;
	    this.W = 0.5F;
	  }

	  protected void c()
	  {
	    this.datawatcher.a(7, Integer.valueOf(0));
	    this.datawatcher.a(8, Byte.valueOf(0));
	    this.datawatcher.a(9, Byte.valueOf(0));
	    this.datawatcher.a(6, Float.valueOf(1.0F));
	  }

	  protected void aC() {
	    bb().b(GenericAttributes.a);
	    bb().b(GenericAttributes.c);
	    bb().b(GenericAttributes.d);

	    if (!(bj()))
	      getAttributeInstance(GenericAttributes.d).setValue(0.1000000014901161D);
	  }

	  protected void a(double paramDouble, boolean paramBoolean)
	  {
	    if (!(L()))
	    {
	      M();
	    }

	    if ((paramBoolean) && (this.fallDistance > 0.0F)) {
	      int i = MathHelper.floor(this.locX);
	      int j = MathHelper.floor(this.locY - 0.2000000029802322D - this.height);
	      int k = MathHelper.floor(this.locZ);
	      Block localBlock = this.world.getType(i, j, k);
	      if (localBlock.getMaterial() == Material.AIR) {
	        int l = this.world.getType(i, j - 1, k).b();
	        if ((l == 11) || (l == 32) || (l == 21))
	          localBlock = this.world.getType(i, j - 1, k);
	      }
	      else if ((!(this.world.isStatic)) && (this.fallDistance > 3.0F)) {
	        this.world.triggerEffect(2006, i, j, k, MathHelper.f(this.fallDistance - 3.0F));
	      }

	      localBlock.a(this.world, i, j, k, this, this.fallDistance);
	    }

	    super.a(paramDouble, paramBoolean);
	  }

	  public boolean aD() {
	    return false;
	  }

	  public void B()
	  {
	    this.aC = this.aD;
	    super.B();

	    this.world.methodProfiler.a("livingEntityBaseTick");

	    if ((isAlive()) && (inBlock())) {
	      damageEntity(DamageSource.STUCK, 1.0F);
	    }

	    if ((isFireproof()) || (this.world.isStatic)) extinguish();
	    int i = ((this instanceof EntityHuman) && (((EntityHuman)this).abilities.isInvulnerable)) ? 1 : 0;

	    if ((isAlive()) && (a(Material.WATER))) {
	      if ((!(aD())) && (!(hasEffect(MobEffectList.WATER_BREATHING.id))) && (i == 0)) {
	        setAirTicks(j(getAirTicks()));
	        if (getAirTicks() == -20) {
	          setAirTicks(0);
	          for (int j = 0; j < 8; ++j) {
	            float f1 = this.random.nextFloat() - this.random.nextFloat();
	            float f2 = this.random.nextFloat() - this.random.nextFloat();
	            float f3 = this.random.nextFloat() - this.random.nextFloat();
	            this.world.addParticle("bubble", this.locX + f1, this.locY + f2, this.locZ + f3, this.motX, this.motY, this.motZ);
	          }
	          damageEntity(DamageSource.DROWN, 2.0F);
	        }
	      }

	      if ((!(this.world.isStatic)) && (al()) && (this.vehicle instanceof EntityLiving))
	        mount(null);
	    }
	    else {
	      setAirTicks(300);
	    }

	    if ((isAlive()) && (K())) extinguish();

	    this.aI = this.aJ;

	    if (this.attackTicks > 0) this.attackTicks -= 1;
	    if (this.hurtTicks > 0) this.hurtTicks -= 1;
	    if ((this.noDamageTicks > 0) && (!(this instanceof EntityPlayer))) this.noDamageTicks -= 1;
	    if (getHealth() <= 0.0F) {
	      aE();
	    }
	    if (this.lastDamageByPlayerTime > 0) this.lastDamageByPlayerTime -= 1;
	    else this.killer = null;
	    if ((this.bn != null) && (!(this.bn.isAlive()))) this.bn = null;

	    if (this.lastDamager != null) {
	      if (!(this.lastDamager.isAlive()))
	        b(null);
	      else if (this.ticksLived - this.bm > 100) {
	        b(null);
	      }

	    }

	    aN();

	    this.aY = this.aX;

	    this.aN = this.aM;
	    this.aP = this.aO;
	    this.lastYaw = this.yaw;
	    this.lastPitch = this.pitch;

	    this.world.methodProfiler.b();
	  }

	  public boolean isBaby() {
	    return false;
	  }

	  protected void aE() {
	    this.deathTicks += 1;
	    if (this.deathTicks == 20) {
	      if ((!(this.world.isStatic)) && (((this.lastDamageByPlayerTime > 0) || (alwaysGivesExp()))) &&
	        (aF()) && (this.world.getGameRules().getBoolean("doMobLoot"))) {
	        i = getExpValue(this.killer);
	        while (i > 0) {
	          int j = EntityExperienceOrb.getOrbValue(i);
	          i -= j;
	          this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
	        }

	      }

	      die();
	      for (int i = 0; i < 20; ++i) {
	        double d1 = this.random.nextGaussian() * 0.02D;
	        double d2 = this.random.nextGaussian() * 0.02D;
	        double d3 = this.random.nextGaussian() * 0.02D;
	        this.world.addParticle("explode", this.locX + this.random.nextFloat() * this.width * 2.0F - this.width, this.locY + this.random.nextFloat() * this.length, this.locZ + this.random.nextFloat() * this.width * 2.0F - this.width, d1, d2, d3);
	      }
	    }
	  }

	  protected boolean aF() {
	    return (!(isBaby()));
	  }

	  protected int j(int paramInt) {
	    int i = EnchantmentManager.getOxygenEnchantmentLevel(this);
	    if ((i > 0) &&
	      (this.random.nextInt(i + 1) > 0))
	    {
	      return paramInt;
	    }

	    return (paramInt - 1);
	  }

	  protected int getExpValue(EntityHuman paramEntityHuman) {
	    return 0;
	  }

	  protected boolean alwaysGivesExp() {
	    return false;
	  }

	  public Random aH() {
	    return this.random;
	  }

	  public EntityLiving getLastDamager() {
	    return this.lastDamager;
	  }

	  public int aJ() {
	    return this.bm;
	  }

	  public void b(EntityLiving paramEntityLiving) {
	    this.lastDamager = paramEntityLiving;
	    this.bm = this.ticksLived;
	  }

	  public EntityLiving aK() {
	    return this.bn;
	  }

	  public int aL() {
	    return this.bo;
	  }

	  public void l(Entity paramEntity) {
	    if (paramEntity instanceof EntityLiving)
	      this.bn = ((EntityLiving)paramEntity);
	    else {
	      this.bn = null;
	    }
	    this.bo = this.ticksLived;
	  }

	  public int aM() {
	    return this.aU;
	  }

	  public void b(NBTTagCompound paramNBTTagCompound)
	  {
	    paramNBTTagCompound.setFloat("HealF", getHealth());
	    paramNBTTagCompound.setShort("Health", (short)(int)Math.ceil(getHealth()));
	    paramNBTTagCompound.setShort("HurtTime", (short)this.hurtTicks);
	    paramNBTTagCompound.setShort("DeathTime", (short)this.deathTicks);
	    paramNBTTagCompound.setShort("AttackTime", (short)this.attackTicks);
	    paramNBTTagCompound.setFloat("AbsorptionAmount", br());
	    Object localObject2;
	    for (localObject2 : getEquipment()) {
	      if (localObject2 == null) continue; this.d.a(localObject2.D());
	    }

	    paramNBTTagCompound.set("Attributes", GenericAttributes.a(bb()));

	    for (localObject2 : getEquipment()) {
	      if (localObject2 == null) continue; this.d.b(localObject2.D());
	    }

	    if (!(this.effects.isEmpty())) {
	      ??? = new NBTTagList();

	      for (MobEffect localMobEffect : this.effects.values()) {
	        ((NBTTagList)???).add(localMobEffect.a(new NBTTagCompound()));
	      }
	      paramNBTTagCompound.set("ActiveEffects", (NBTBase)???);
	    }
	  }

	  public void a(NBTTagCompound paramNBTTagCompound)
	  {
	    m(paramNBTTagCompound.getFloat("AbsorptionAmount"));

	    if ((paramNBTTagCompound.hasKeyOfType("Attributes", 9)) && (this.world != null) && (!(this.world.isStatic)))
	      GenericAttributes.a(bb(), paramNBTTagCompound.getList("Attributes", 10));
	    Object localObject;
	    if (paramNBTTagCompound.hasKeyOfType("ActiveEffects", 9)) {
	      localObject = paramNBTTagCompound.getList("ActiveEffects", 10);
	      for (int i = 0; i < ((NBTTagList)localObject).size(); ++i) {
	        NBTTagCompound localNBTTagCompound = ((NBTTagList)localObject).get(i);
	        MobEffect localMobEffect = MobEffect.b(localNBTTagCompound);
	        if (localMobEffect != null) {
	          this.effects.put(Integer.valueOf(localMobEffect.getEffectId()), localMobEffect);
	        }
	      }
	    }

	    if (paramNBTTagCompound.hasKeyOfType("HealF", 99)) {
	      setHealth(paramNBTTagCompound.getFloat("HealF"));
	    } else {
	      localObject = paramNBTTagCompound.get("Health");
	      if (localObject == null)
	        setHealth(getMaxHealth());
	      else if (((NBTBase)localObject).getTypeId() == 5)
	        setHealth(((NBTTagFloat)localObject).h());
	      else if (((NBTBase)localObject).getTypeId() == 2)
	      {
	        setHealth(((NBTTagShort)localObject).e());
	      }
	    }

	    this.hurtTicks = paramNBTTagCompound.getShort("HurtTime");
	    this.deathTicks = paramNBTTagCompound.getShort("DeathTime");
	    this.attackTicks = paramNBTTagCompound.getShort("AttackTime");
	  }

	  protected void aN() {
	    Iterator localIterator = this.effects.keySet().iterator();
	    while (localIterator.hasNext()) {
	      Integer localInteger = (Integer)localIterator.next();
	      MobEffect localMobEffect = (MobEffect)this.effects.get(localInteger);

	      if (!(localMobEffect.tick(this)))
	        if (!(this.world.isStatic)) {
	          localIterator.remove();
	          b(localMobEffect);
	        }
	      else if (localMobEffect.getDuration() % 600 == 0)
	      {
	        a(localMobEffect, false);
	      }
	    }

	    if (this.updateEffects) {
	      if (!(this.world.isStatic)) {
	        if (this.effects.isEmpty()) {
	          this.datawatcher.watch(8, Byte.valueOf(0));
	          this.datawatcher.watch(7, Integer.valueOf(0));
	          setInvisible(false);
	        } else {
	          i = PotionBrewer.a(this.effects.values());
	          this.datawatcher.watch(8, Byte.valueOf((PotionBrewer.b(this.effects.values())) ? 1 : 0));
	          this.datawatcher.watch(7, Integer.valueOf(i));
	          setInvisible(hasEffect(MobEffectList.INVISIBILITY.id));
	        }
	      }
	      this.updateEffects = false;
	    }
	    int i = this.datawatcher.getInt(7);
	    int j = (this.datawatcher.getByte(8) > 0) ? 1 : 0;

	    if (i > 0) {
	      boolean bool = false;

	      if (!(isInvisible())) {
	        bool = this.random.nextBoolean();
	      }
	      else {
	        bool = this.random.nextInt(15) == 0;
	      }

	      if (j != 0) bool &= this.random.nextInt(5) == 0;

	      if ((!(bool)) ||
	        (i <= 0)) return;
	      double d1 = (i >> 16 & 0xFF) / 255.0D;
	      double d2 = (i >> 8 & 0xFF) / 255.0D;
	      double d3 = (i >> 0 & 0xFF) / 255.0D;

	      this.world.addParticle((j != 0) ? "mobSpellAmbient" : "mobSpell", this.locX + (this.random.nextDouble() - 0.5D) * this.width, this.locY + this.random.nextDouble() * this.length - this.height, this.locZ + (this.random.nextDouble() - 0.5D) * this.width, d1, d2, d3);
	    }
	  }

	  public void removeAllEffects()
	  {
	    Iterator localIterator = this.effects.keySet().iterator();
	    while (localIterator.hasNext()) {
	      Integer localInteger = (Integer)localIterator.next();
	      MobEffect localMobEffect = (MobEffect)this.effects.get(localInteger);

	      if (!(this.world.isStatic)) {
	        localIterator.remove();
	        b(localMobEffect);
	      }
	    }
	  }

	  public Collection getEffects() {
	    return this.effects.values();
	  }

	  public boolean hasEffect(int paramInt) {
	    return this.effects.containsKey(Integer.valueOf(paramInt));
	  }

	  public boolean hasEffect(MobEffectList paramMobEffectList) {
	    return this.effects.containsKey(Integer.valueOf(paramMobEffectList.id));
	  }

	  public MobEffect getEffect(MobEffectList paramMobEffectList) {
	    return ((MobEffect)this.effects.get(Integer.valueOf(paramMobEffectList.id)));
	  }

	  public void addEffect(MobEffect paramMobEffect)
	  {
	    if (!(d(paramMobEffect))) {
	      return;
	    }

	    if (this.effects.containsKey(Integer.valueOf(paramMobEffect.getEffectId())))
	    {
	      ((MobEffect)this.effects.get(Integer.valueOf(paramMobEffect.getEffectId()))).a(paramMobEffect);
	      a((MobEffect)this.effects.get(Integer.valueOf(paramMobEffect.getEffectId())), true);
	    } else {
	      this.effects.put(Integer.valueOf(paramMobEffect.getEffectId()), paramMobEffect);
	      a(paramMobEffect);
	    }
	  }

	  public boolean d(MobEffect paramMobEffect) {
	    if (getMonsterType() == EnumMonsterType.UNDEAD) {
	      int i = paramMobEffect.getEffectId();
	      if ((i == MobEffectList.REGENERATION.id) || (i == MobEffectList.POISON.id)) {
	        return false;
	      }
	    }

	    return true;
	  }

	  public boolean aQ() {
	    return (getMonsterType() == EnumMonsterType.UNDEAD);
	  }

	  public void removeEffect(int paramInt)
	  {
	    MobEffect localMobEffect = (MobEffect)this.effects.remove(Integer.valueOf(paramInt));
	    if (localMobEffect == null) return; b(localMobEffect);
	  }

	  protected void a(MobEffect paramMobEffect) {
	    this.updateEffects = true;
	    if (this.world.isStatic) return; MobEffectList.byId[paramMobEffect.getEffectId()].b(this, bb(), paramMobEffect.getAmplifier());
	  }

	  protected void a(MobEffect paramMobEffect, boolean paramBoolean) {
	    this.updateEffects = true;
	    if ((paramBoolean) && (!(this.world.isStatic))) {
	      MobEffectList.byId[paramMobEffect.getEffectId()].a(this, bb(), paramMobEffect.getAmplifier());
	      MobEffectList.byId[paramMobEffect.getEffectId()].b(this, bb(), paramMobEffect.getAmplifier());
	    }
	  }

	  protected void b(MobEffect paramMobEffect) {
	    this.updateEffects = true;
	    if (this.world.isStatic) return; MobEffectList.byId[paramMobEffect.getEffectId()].a(this, bb(), paramMobEffect.getAmplifier());
	  }

	  public void heal(float paramFloat) {
	    float f = getHealth();
	    if (f > 0.0F)
	      setHealth(f + paramFloat);
	  }

	  public final float getHealth()
	  {
	    return this.datawatcher.getFloat(6);
	  }

	  public void setHealth(float paramFloat) {
	    this.datawatcher.watch(6, Float.valueOf(MathHelper.a(paramFloat, 0.0F, getMaxHealth())));
	  }

	  public boolean damageEntity(DamageSource paramDamageSource, float paramFloat)
	  {
	    if (isInvulnerable()) return false;
	    if (this.world.isStatic) return false;
	    this.aU = 0;
	    if (getHealth() <= 0.0F) return false;

	    if ((paramDamageSource.o()) && (hasEffect(MobEffectList.FIRE_RESISTANCE))) {
	      return false;
	    }

	    if ((((paramDamageSource == DamageSource.ANVIL) || (paramDamageSource == DamageSource.FALLING_BLOCK))) && (getEquipment(4) != null)) {
	      getEquipment(4).damage((int)(paramFloat * 4.0F + this.random.nextFloat() * paramFloat * 2.0F), this);
	      paramFloat *= 0.75F;
	    }

	    this.aF = 1.5F;

	    int i = 1;
	    if (this.noDamageTicks > this.maxNoDamageTicks / 2.0F) {
	      if (paramFloat <= this.lastDamage) return false;
	      d(paramDamageSource, paramFloat - this.lastDamage);
	      this.lastDamage = paramFloat;
	      i = 0;
	    } else {
	      this.lastDamage = paramFloat;
	      this.aw = getHealth();
	      this.noDamageTicks = this.maxNoDamageTicks;
	      d(paramDamageSource, paramFloat);
	      this.hurtTicks = (this.ay = 10);
	    }

	    this.az = 0.0F;

	    Entity localEntity = paramDamageSource.getEntity();
	    Object localObject;
	    if (localEntity != null) {
	      if (localEntity instanceof EntityLiving) {
	        b((EntityLiving)localEntity);
	      }

	      if (localEntity instanceof EntityHuman) {
	        this.lastDamageByPlayerTime = 100;
	        this.killer = ((EntityHuman)localEntity);
	      } else if (localEntity instanceof EntityWolf) {
	        localObject = (EntityWolf)localEntity;
	        if (((EntityWolf)localObject).isTamed()) {
	          this.lastDamageByPlayerTime = 100;
	          this.killer = null;
	        }
	      }
	    }
	    if (i != 0) {
	      this.world.broadcastEntityEffect(this, 2);
	      if (paramDamageSource != DamageSource.DROWN) P();
	      if (localEntity != null) {
	        double d1 = localEntity.locX - this.locX;
	        double d2 = localEntity.locZ - this.locZ;
	        while (d1 * d1 + d2 * d2 < 0.0001D) {
	          d1 = (Math.random() - Math.random()) * 0.01D;
	          d2 = (Math.random() - Math.random()) * 0.01D;
	        }
	        this.az = ((float)(Math.atan2(d2, d1) * 180.0D / 3.141592741012573D) - this.yaw);
	        a(localEntity, paramFloat, d1, d2);
	      } else {
	        this.az = ((int)(Math.random() * 2.0D) * 180);
	      }
	    }

	    if (getHealth() <= 0.0F) {
	      localObject = aT();
	      if ((i != 0) && (localObject != null)) {
	        makeSound((String)localObject, be(), bf());
	      }
	      die(paramDamageSource);
	    } else {
	      localObject = aS();
	      if ((i != 0) && (localObject != null)) {
	        makeSound((String)localObject, be(), bf());
	      }
	    }

	    return true;
	  }

	  public void a(ItemStack paramItemStack) {
	    makeSound("random.break", 0.8F, 0.8F + this.world.random.nextFloat() * 0.4F);

	    for (int i = 0; i < 5; ++i) {
	      Vec3D localVec3D1 = Vec3D.a((this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
	      localVec3D1.a(-this.pitch * 3.141593F / 180.0F);
	      localVec3D1.b(-this.yaw * 3.141593F / 180.0F);

	      Vec3D localVec3D2 = Vec3D.a((this.random.nextFloat() - 0.5D) * 0.3D, -this.random.nextFloat() * 0.6D - 0.3D, 0.6D);
	      localVec3D2.a(-this.pitch * 3.141593F / 180.0F);
	      localVec3D2.b(-this.yaw * 3.141593F / 180.0F);
	      localVec3D2 = localVec3D2.add(this.locX, this.locY + getHeadHeight(), this.locZ);
	      this.world.addParticle("iconcrack_" + Item.b(paramItemStack.getItem()), localVec3D2.a, localVec3D2.b, localVec3D2.c, localVec3D1.a, localVec3D1.b + 0.05D, localVec3D1.c);
	    }
	  }

	  public void die(DamageSource paramDamageSource) {
	    Entity localEntity = paramDamageSource.getEntity();
	    EntityLiving localEntityLiving = aW();
	    if ((this.ba >= 0) && (localEntityLiving != null)) localEntityLiving.b(this, this.ba);

	    if (localEntity != null) localEntity.a(this);

	    this.aT = true;
	    aV().g();

	    if (!(this.world.isStatic)) {
	      int i = 0;
	      if (localEntity instanceof EntityHuman) {
	        i = EnchantmentManager.getBonusMonsterLootEnchantmentLevel((EntityLiving)localEntity);
	      }
	      if ((aF()) && (this.world.getGameRules().getBoolean("doMobLoot"))) {
	        dropDeathLoot(this.lastDamageByPlayerTime > 0, i);
	        dropEquipment(this.lastDamageByPlayerTime > 0, i);
	        if (this.lastDamageByPlayerTime > 0) {
	          int j = this.random.nextInt(200) - i;
	          if (j < 5) {
	            getRareDrop((j <= 0) ? 1 : 0);
	          }
	        }
	      }
	    }

	    this.world.broadcastEntityEffect(this, 3);
	  }

	  protected void dropEquipment(boolean paramBoolean, int paramInt) {
	  }

	  public void a(Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2) {
	    if (this.random.nextDouble() < getAttributeInstance(GenericAttributes.c).getValue()) {
	      return;
	    }

	    this.al = true;
	    float f1 = MathHelper.sqrt(paramDouble1 * paramDouble1 + paramDouble2 * paramDouble2);
	    float f2 = 0.4F;

	    this.motX /= 2.0D;
	    this.motY /= 2.0D;
	    this.motZ /= 2.0D;

	    this.motX -= paramDouble1 / f1 * f2;
	    this.motY += f2;
	    this.motZ -= paramDouble2 / f1 * f2;

	    if (this.motY <= 0.4000000059604645D) return; this.motY = 0.4000000059604645D;
	  }

	  protected String aS() {
	    return "game.neutral.hurt";
	  }

	  protected String aT() {
	    return "game.neutral.die";
	  }

	  protected void getRareDrop(int paramInt)
	  {
	  }

	  protected void dropDeathLoot(boolean paramBoolean, int paramInt)
	  {
	  }

	  public boolean h_()
	  {
	    int i = MathHelper.floor(this.locX);
	    int j = MathHelper.floor(this.boundingBox.b);
	    int k = MathHelper.floor(this.locZ);
	    Block localBlock = this.world.getType(i, j, k);
	    return ((localBlock == Blocks.LADDER) || (localBlock == Blocks.VINE));
	  }

	  public boolean isAlive()
	  {
	    return ((!(this.dead)) && (getHealth() > 0.0F));
	  }

	  protected void b(float paramFloat)
	  {
	    super.b(paramFloat);
	    MobEffect localMobEffect = getEffect(MobEffectList.JUMP);
	    float f = (localMobEffect != null) ? localMobEffect.getAmplifier() + 1 : 0.0F;

	    int i = MathHelper.f(paramFloat - 3.0F - f);

	    if (i > 0) {
	      makeSound(o(i), 1.0F, 1.0F);
	      damageEntity(DamageSource.FALL, i);

	      int j = MathHelper.floor(this.locX);
	      int k = MathHelper.floor(this.locY - 0.2000000029802322D - this.height);
	      int l = MathHelper.floor(this.locZ);

	      Block localBlock = this.world.getType(j, k, l);
	      if (localBlock.getMaterial() != Material.AIR) {
	        StepSound localStepSound = localBlock.stepSound;
	        makeSound(localStepSound.getStepSound(), localStepSound.getVolume1() * 0.5F, localStepSound.getVolume2() * 0.75F);
	      }
	    }
	  }

	  protected String o(int paramInt) {
	    if (paramInt > 4) {
	      return "game.neutral.hurt.fall.big";
	    }
	    return "game.neutral.hurt.fall.small";
	  }

	  public int aU()
	  {
	    int i = 0;
	    for (ItemStack localItemStack : getEquipment()) {
	      if ((localItemStack != null) && (localItemStack.getItem() instanceof ItemArmor)) {
	        int l = ((ItemArmor)localItemStack.getItem()).c;
	        i += l;
	      }
	    }
	    return i;
	  }

	  protected void h(float paramFloat) {
	  }

	  protected float b(DamageSource paramDamageSource, float paramFloat) {
	    if (!(paramDamageSource.ignoresArmor())) {
	      int i = 25 - aU();
	      float f = paramFloat * i;
	      h(paramFloat);
	      paramFloat = f / 25.0F;
	    }
	    return paramFloat;
	  }

	  protected float c(DamageSource paramDamageSource, float paramFloat) {
	    if (paramDamageSource.h()) return paramFloat;

	    if (this instanceof EntityZombie)
	      paramFloat = paramFloat;
	    int j;
	    float f;
	    if ((hasEffect(MobEffectList.RESISTANCE)) && (paramDamageSource != DamageSource.OUT_OF_WORLD)) {
	      i = (getEffect(MobEffectList.RESISTANCE).getAmplifier() + 1) * 5;
	      j = 25 - i;
	      f = paramFloat * j;
	      paramFloat = f / 25.0F;
	    }

	    if (paramFloat <= 0.0F) return 0.0F;

	    int i = EnchantmentManager.a(getEquipment(), paramDamageSource);
	    if (i > 20) {
	      i = 20;
	    }
	    if ((i > 0) && (i <= 20)) {
	      j = 25 - i;
	      f = paramFloat * j;
	      paramFloat = f / 25.0F;
	    }

	    return paramFloat;
	  }

	  protected void d(DamageSource paramDamageSource, float paramFloat) {
	    if (isInvulnerable()) return;
	    paramFloat = b(paramDamageSource, paramFloat);
	    paramFloat = c(paramDamageSource, paramFloat);

	    float f1 = paramFloat;
	    paramFloat = Math.max(paramFloat - br(), 0.0F);
	    m(br() - (f1 - paramFloat));
	    if (paramFloat == 0.0F) return;

	    float f2 = getHealth();
	    setHealth(f2 - paramFloat);
	    aV().a(paramDamageSource, f2, paramFloat);
	    m(br() - paramFloat);
	  }

	  public CombatTracker aV() {
	    return this.combatTracker;
	  }

	  public EntityLiving aW() {
	    if (this.combatTracker.c() != null) return this.combatTracker.c();
	    if (this.killer != null) return this.killer;
	    if (this.lastDamager != null) return this.lastDamager;
	    return null;
	  }

	  public final float getMaxHealth() {
	    return (float)getAttributeInstance(GenericAttributes.a).getValue();
	  }

	  public final int aY() {
	    return this.datawatcher.getByte(9);
	  }

	  public final void p(int paramInt) {
	    this.datawatcher.watch(9, Byte.valueOf((byte)paramInt));
	  }

	  private int j() {
	    if (hasEffect(MobEffectList.FASTER_DIG)) {
	      return (6 - ((1 + getEffect(MobEffectList.FASTER_DIG).getAmplifier()) * 1));
	    }
	    if (hasEffect(MobEffectList.SLOWER_DIG)) {
	      return (6 + (1 + getEffect(MobEffectList.SLOWER_DIG).getAmplifier()) * 2);
	    }
	    return 6;
	  }

	  public void aZ() {
	    if ((!(this.at)) || (this.au >= j() / 2) || (this.au < 0)) {
	      this.au = -1;
	      this.at = true;

	      if (this.world instanceof WorldServer)
	        ((WorldServer)this.world).getTracker().a(this, new PacketPlayOutAnimation(this, 0));
	    }
	  }

	  protected void F()
	  {
	    damageEntity(DamageSource.OUT_OF_WORLD, 4.0F);
	  }

	  protected void ba() {
	    int i = j();
	    if (this.at) {
	      this.au += 1;
	      if (this.au >= i) {
	        this.au = 0;
	        this.at = false;
	      }
	    } else {
	      this.au = 0;
	    }

	    this.aD = (this.au / i);
	  }

	  public AttributeInstance getAttributeInstance(IAttribute paramIAttribute) {
	    return bb().a(paramIAttribute);
	  }

	  public AttributeMapBase bb() {
	    if (this.d == null) {
	      this.d = new AttributeMapServer();
	    }

	    return this.d;
	  }

	  public EnumMonsterType getMonsterType() {
	    return EnumMonsterType.UNDEFINED;
	  }

	  public abstract ItemStack bd();

	  public abstract ItemStack getEquipment(int paramInt);

	  public abstract void setEquipment(int paramInt, ItemStack paramItemStack);

	  public void setSprinting(boolean paramBoolean)
	  {
	    super.setSprinting(paramBoolean);

	    AttributeInstance localAttributeInstance = getAttributeInstance(GenericAttributes.d);
	    if (localAttributeInstance.a(b) != null) {
	      localAttributeInstance.b(c);
	    }
	    if (paramBoolean)
	      localAttributeInstance.a(c);
	  }

	  public abstract ItemStack[] getEquipment();

	  protected float be()
	  {
	    return 1.0F;
	  }

	  protected float bf() {
	    if (isBaby()) {
	      return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F);
	    }

	    return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
	  }

	  protected boolean bg() {
	    return (getHealth() <= 0.0F);
	  }

	  public void enderTeleportTo(double paramDouble1, double paramDouble2, double paramDouble3) {
	    setPositionRotation(paramDouble1, paramDouble2, paramDouble3, this.yaw, this.pitch);
	  }

	  public void m(Entity paramEntity)
	  {
	    double d1 = paramEntity.locX;
	    double d2 = paramEntity.boundingBox.b + paramEntity.length;
	    double d3 = paramEntity.locZ;
	    int i = 1;

	    for (int j = -i; j <= i; ++j) {
	      for (int k = -i; k < i; ++k) {
	        if ((j == 0) && (k == 0)) {
	          continue;
	        }

	        int l = (int)(this.locX + j);
	        int i1 = (int)(this.locZ + k);
	        AxisAlignedBB localAxisAlignedBB = this.boundingBox.c(j, 1.0D, k);

	        if (this.world.a(localAxisAlignedBB).isEmpty()) {
	          if (World.a(this.world, l, (int)this.locY, i1)) {
	            enderTeleportTo(this.locX + j, this.locY + 1.0D, this.locZ + k);
	            return; }
	          if ((World.a(this.world, l, (int)this.locY - 1, i1)) || (this.world.getType(l, (int)this.locY - 1, i1).getMaterial() == Material.WATER)) {
	            d1 = this.locX + j;
	            d2 = this.locY + 1.0D;
	            d3 = this.locZ + k;
	          }
	        }
	      }
	    }

	    enderTeleportTo(d1, d2, d3);
	  }

	  protected void bi()
	  {
	    this.motY = 0.4199999868869782D;
	    if (hasEffect(MobEffectList.JUMP)) {
	      this.motY += (getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F;
	    }
	    if (isSprinting()) {
	      float f = this.yaw * 0.01745329F;

	      this.motX -= MathHelper.sin(f) * 0.2F;
	      this.motZ += MathHelper.cos(f) * 0.2F;
	    }
	    this.al = true;
	  }

	  public void e(float paramFloat1, float paramFloat2) {
	    if ((L()) && (((!(this instanceof EntityHuman)) || (!(((EntityHuman)this).abilities.isFlying))))) {
	      d1 = this.locY;

	      a(paramFloat1, paramFloat2, (bj()) ? 0.04F : 0.02F);
	      move(this.motX, this.motY, this.motZ);

	      this.motX *= 0.800000011920929D;
	      this.motY *= 0.800000011920929D;
	      this.motZ *= 0.800000011920929D;
	      this.motY -= 0.02D;

	      if ((this.positionChanged) && (c(this.motX, this.motY + 0.6000000238418579D - this.locY + d1, this.motZ)))
	        this.motY = 0.300000011920929D;
	    }
	    else if ((O()) && (((!(this instanceof EntityHuman)) || (!(((EntityHuman)this).abilities.isFlying))))) {
	      d1 = this.locY;
	      a(paramFloat1, paramFloat2, 0.02F);
	      move(this.motX, this.motY, this.motZ);
	      this.motX *= 0.5D;
	      this.motY *= 0.5D;
	      this.motZ *= 0.5D;
	      this.motY -= 0.02D;

	      if ((this.positionChanged) && (c(this.motX, this.motY + 0.6000000238418579D - this.locY + d1, this.motZ)))
	        this.motY = 0.300000011920929D;
	    }
	    else {
	      float f1 = 0.91F;
	      if (this.onGround) {
	        f1 = this.world.getType(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ)).frictionFactor * 0.91F;
	      }

	      float f2 = 0.1627714F / f1 * f1 * f1;
	      float f3;
	      if (this.onGround)
	        f3 = bk() * f2;
	      else {
	        f3 = this.aQ;
	      }

	      a(paramFloat1, paramFloat2, f3);

	      f1 = 0.91F;
	      if (this.onGround) {
	        f1 = this.world.getType(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ)).frictionFactor * 0.91F;
	      }
	      if (h_()) {
	        float f4 = 0.15F;
	        if (this.motX < -f4) this.motX = (-f4);
	        if (this.motX > f4) this.motX = f4;
	        if (this.motZ < -f4) this.motZ = (-f4);
	        if (this.motZ > f4) this.motZ = f4;
	        this.fallDistance = 0.0F;
	        if (this.motY < -0.15D) this.motY = -0.15D;
	        int i = ((isSneaking()) && (this instanceof EntityHuman)) ? 1 : 0;
	        if ((i != 0) && (this.motY < 0.0D)) this.motY = 0.0D;
	      }

	      move(this.motX, this.motY, this.motZ);

	      if ((this.positionChanged) && (h_())) {
	        this.motY = 0.2D;
	      }

	      if ((!(this.world.isStatic)) || ((this.world.isLoaded((int)this.locX, 0, (int)this.locZ)) && (this.world.getChunkAtWorldCoords((int)this.locX, (int)this.locZ).d)))
	        this.motY -= 0.08D;
	      else if (this.locY > 0.0D)
	        this.motY = -0.1D;
	      else {
	        this.motY = 0.0D;
	      }
	      this.motY *= 0.9800000190734863D;
	      this.motX *= f1;
	      this.motZ *= f1;
	    }
	    this.aE = this.aF;
	    double d1 = this.locX - this.lastX;
	    double d2 = this.locZ - this.lastZ;
	    float f5 = MathHelper.sqrt(d1 * d1 + d2 * d2) * 4.0F;
	    if (f5 > 1.0F) f5 = 1.0F;
	    this.aF += (f5 - this.aF) * 0.4F;
	    this.aG += this.aF;
	  }

	  protected boolean bj() {
	    return false;
	  }

	  public float bk() {
	    if (bj()) {
	      return this.bp;
	    }
	    return 0.1F;
	  }

	  public void i(float paramFloat)
	  {
	    this.bp = paramFloat;
	  }

	  public boolean n(Entity paramEntity) {
	    l(paramEntity);
	    return false;
	  }

	  public boolean isSleeping() {
	    return false;
	  }

	  public void h()
	  {
	    super.h();

	    if (!(this.world.isStatic)) {
	      int i = aY();
	      if (i > 0) {
	        if (this.av <= 0) {
	          this.av = (20 * (30 - i));
	        }
	        this.av -= 1;
	        if (this.av <= 0) {
	          p(i - 1);
	        }
	      }

	      for (int j = 0; j < 5; ++j) {
	        ItemStack localItemStack1 = this.g[j];
	        ItemStack localItemStack2 = getEquipment(j);

	        if (!(ItemStack.matches(localItemStack2, localItemStack1))) {
	          ((WorldServer)this.world).getTracker().a(this, new PacketPlayOutEntityEquipment(getId(), j, localItemStack2));
	          if (localItemStack1 != null) this.d.a(localItemStack1.D());
	          if (localItemStack2 != null) this.d.b(localItemStack2.D());
	          this.g[j] = ((localItemStack2 == null) ? null : localItemStack2.cloneItemStack());
	        }
	      }

	      if (this.ticksLived % 20 == 0) aV().g();
	    }

	    e();

	    double d1 = this.locX - this.lastX;
	    double d2 = this.locZ - this.lastZ;

	    float f1 = (float)(d1 * d1 + d2 * d2);

	    float f2 = this.aM;

	    float f3 = 0.0F;
	    this.aV = this.aW;
	    float f4 = 0.0F;
	    if (f1 > 0.0025F) {
	      f4 = 1.0F;
	      f3 = (float)Math.sqrt(f1) * 3.0F;
	      f2 = (float)Math.atan2(d2, d1) * 180.0F / 3.141593F - 90.0F;
	    }
	    if (this.aD > 0.0F) {
	      f2 = this.yaw;
	    }
	    if (!(this.onGround)) {
	      f4 = 0.0F;
	    }
	    this.aW += (f4 - this.aW) * 0.3F;

	    this.world.methodProfiler.a("headTurn");

	    f3 = f(f2, f3);

	    this.world.methodProfiler.b();

	    this.world.methodProfiler.a("rangeChecks");
	    while (this.yaw - this.lastYaw < -180.0F)
	      this.lastYaw -= 360.0F;
	    while (this.yaw - this.lastYaw >= 180.0F) {
	      this.lastYaw += 360.0F;
	    }
	    while (this.aM - this.aN < -180.0F)
	      this.aN -= 360.0F;
	    while (this.aM - this.aN >= 180.0F) {
	      this.aN += 360.0F;
	    }
	    while (this.pitch - this.lastPitch < -180.0F)
	      this.lastPitch -= 360.0F;
	    while (this.pitch - this.lastPitch >= 180.0F) {
	      this.lastPitch += 360.0F;
	    }
	    while (this.aO - this.aP < -180.0F)
	      this.aP -= 360.0F;
	    while (this.aO - this.aP >= 180.0F)
	      this.aP += 360.0F;
	    this.world.methodProfiler.b();

	    this.aX += f3;
	  }

	  protected float f(float paramFloat1, float paramFloat2) {
	    float f1 = MathHelper.g(paramFloat1 - this.aM);
	    this.aM += f1 * 0.3F;

	    float f2 = MathHelper.g(this.yaw - this.aM);
	    int i = ((f2 < -90.0F) || (f2 >= 90.0F)) ? 1 : 0;
	    if (f2 < -75.0F) f2 = -75.0F;
	    if (f2 >= 75.0F) f2 = 75.0F;
	    this.aM = (this.yaw - f2);
	    if (f2 * f2 > 2500.0F) {
	      this.aM += f2 * 0.2F;
	    }

	    if (i != 0) {
	      paramFloat2 *= -1.0F;
	    }

	    return paramFloat2;
	  }

	  public void e() {
	    if (this.bq > 0) this.bq -= 1;
	    if (this.bg > 0) {
	      double d1 = this.locX + (this.bh - this.locX) / this.bg;
	      double d2 = this.locY + (this.bi - this.locY) / this.bg;
	      double d3 = this.locZ + (this.bj - this.locZ) / this.bg;

	      double d4 = MathHelper.g(this.bk - this.yaw);
	      EntityLiving tmp104_103 = this; tmp104_103.yaw = (float)(tmp104_103.yaw + d4 / this.bg);
	      EntityLiving tmp123_122 = this; tmp123_122.pitch = (float)(tmp123_122.pitch + (this.bl - this.pitch) / this.bg);

	      this.bg -= 1;
	      setPosition(d1, d2, tmp104_103);
	      b(this.yaw, this.pitch);
	    } else if (!(bq()))
	    {
	      this.motX *= 0.98D;
	      this.motY *= 0.98D;
	      this.motZ *= 0.98D;
	    }

	    if (Math.abs(this.motX) < 0.005D) this.motX = 0.0D;
	    if (Math.abs(this.motY) < 0.005D) this.motY = 0.0D;
	    if (Math.abs(this.motZ) < 0.005D) this.motZ = 0.0D;

	    this.world.methodProfiler.a("ai");
	    if (bg()) {
	      this.bc = false;
	      this.bd = 0.0F;
	      this.be = 0.0F;
	      this.bf = 0.0F;
	    }
	    else if (bq()) {
	      if (bj()) {
	        this.world.methodProfiler.a("newAi");
	        bm();
	        this.world.methodProfiler.b();
	      } else {
	        this.world.methodProfiler.a("oldAi");
	        bp();
	        this.world.methodProfiler.b();
	        this.aO = this.yaw;
	      }
	    }

	    this.world.methodProfiler.b();

	    this.world.methodProfiler.a("jump");
	    if (this.bc) {
	      if ((L()) || (O())) {
	        this.motY += 0.03999999910593033D;
	      } else if ((this.onGround) &&
	        (this.bq == 0)) {
	        bi();
	        this.bq = 10;
	      }
	    }
	    else {
	      this.bq = 0;
	    }
	    this.world.methodProfiler.b();

	    this.world.methodProfiler.a("travel");
	    this.bd *= 0.98F;
	    this.be *= 0.98F;
	    this.bf *= 0.9F;

	    e(this.bd, this.be);
	    this.world.methodProfiler.b();

	    this.world.methodProfiler.a("push");
	    if (!(this.world.isStatic)) {
	      bn();
	    }
	    this.world.methodProfiler.b();
	  }

	  protected void bm() {
	  }

	  protected void bn() {
	    List localList = this.world.getEntities(this, this.boundingBox.grow(0.2000000029802322D, 0.0D, 0.2000000029802322D));
	    if ((localList != null) && (!(localList.isEmpty())))
	      for (int i = 0; i < localList.size(); ++i) {
	        Entity localEntity = (Entity)localList.get(i);
	        if (!(localEntity.R())) continue; o(localEntity);
	      }
	  }

	  protected void o(Entity paramEntity)
	  {
	    paramEntity.collide(this);
	  }

	  public void aa()
	  {
	    super.aa();
	    this.aV = this.aW;
	    this.aW = 0.0F;
	    this.fallDistance = 0.0F;
	  }

	  protected void bo()
	  {
	  }

	  protected void bp()
	  {
	    this.aU += 1;
	  }

	  public void f(boolean paramBoolean) {
	    this.bc = paramBoolean;
	  }

	  public void receive(Entity paramEntity, int paramInt) {
	    if ((!(paramEntity.dead)) && (!(this.world.isStatic))) {
	      EntityTracker localEntityTracker = ((WorldServer)this.world).getTracker();
	      if (paramEntity instanceof EntityItem) {
	        localEntityTracker.a(paramEntity, new PacketPlayOutCollect(paramEntity.getId(), getId()));
	      }
	      if (paramEntity instanceof EntityArrow) {
	        localEntityTracker.a(paramEntity, new PacketPlayOutCollect(paramEntity.getId(), getId()));
	      }
	      if (paramEntity instanceof EntityExperienceOrb)
	        localEntityTracker.a(paramEntity, new PacketPlayOutCollect(paramEntity.getId(), getId()));
	    }
	  }

	  public boolean p(Entity paramEntity)
	  {
	    return (this.world.a(Vec3D.a(this.locX, this.locY + getHeadHeight(), this.locZ), Vec3D.a(paramEntity.locX, paramEntity.locY + paramEntity.getHeadHeight(), paramEntity.locZ)) == null);
	  }

	  public Vec3D af()
	  {
	    return j(1.0F);
	  }

	  public Vec3D j(float paramFloat) {
	    if (paramFloat == 1.0F) {
	      f1 = MathHelper.cos(-this.yaw * 0.01745329F - 3.141593F);
	      f2 = MathHelper.sin(-this.yaw * 0.01745329F - 3.141593F);
	      f3 = -MathHelper.cos(-this.pitch * 0.01745329F);
	      f4 = MathHelper.sin(-this.pitch * 0.01745329F);

	      return Vec3D.a(f2 * f3, f4, f1 * f3);
	    }
	    float f1 = this.lastPitch + (this.pitch - this.lastPitch) * paramFloat;
	    float f2 = this.lastYaw + (this.yaw - this.lastYaw) * paramFloat;

	    float f3 = MathHelper.cos(-f2 * 0.01745329F - 3.141593F);
	    float f4 = MathHelper.sin(-f2 * 0.01745329F - 3.141593F);
	    float f5 = -MathHelper.cos(-f1 * 0.01745329F);
	    float f6 = MathHelper.sin(-f1 * 0.01745329F);

	    return Vec3D.a(f4 * f5, f6, f3 * f5);
	  }

	  public boolean bq()
	  {
	    return (!(this.world.isStatic));
	  }

	  public boolean Q()
	  {
	    return (!(this.dead));
	  }

	  public boolean R()
	  {
	    return (!(this.dead));
	  }

	  public float getHeadHeight()
	  {
	    return (this.length * 0.85F);
	  }

	  protected void P()
	  {
	    this.velocityChanged = (this.random.nextDouble() >= getAttributeInstance(GenericAttributes.c).getValue());
	  }

	  public float getHeadRotation()
	  {
	    return this.aO;
	  }

	  public float br()
	  {
	    return this.br;
	  }

	  public void m(float paramFloat) {
	    if (paramFloat < 0.0F) paramFloat = 0.0F;
	    this.br = paramFloat;
	  }

	  public ScoreboardTeamBase getScoreboardTeam() {
	    return null;
	  }

	  public boolean c(EntityLiving paramEntityLiving) {
	    return a(paramEntityLiving.getScoreboardTeam());
	  }

	  public boolean a(ScoreboardTeamBase paramScoreboardTeamBase) {
	    if (getScoreboardTeam() != null) {
	      return getScoreboardTeam().isAlly(paramScoreboardTeamBase);
	    }
	    return false;
	  }

	  public void bt()
	  {
	  }

	  public void bu()
	  {
	  }
}
