package net.ilexiconn.jurassicraft.common.entity.egg;

import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraft.network.FriendlyByteBuf;
import net.ilexiconn.jurassicraft.JurassiCraft;
import net.ilexiconn.jurassicraft.common.entity.Creature;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.ilexiconn.jurassicraft.common.entity.JCEntityRegistry;
import net.ilexiconn.jurassicraft.common.handler.CreatureHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityDinoEgg extends Entity implements IEntityAdditionalSpawnData {
    private static final EntityDataAccessor<String> DNA_SEQUENCE = SynchedEntityData.defineId(EntityDinoEgg.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> FROZE = SynchedEntityData.defineId(EntityDinoEgg.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DRIED = SynchedEntityData.defineId(EntityDinoEgg.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CURRENT_SPAWN_TIME = SynchedEntityData.defineId(EntityDinoEgg.class, EntityDataSerializers.INT);
    // 1.20.1: the creature name is synced through the entity data so the client
    // renderer can pick the right model/texture even when the creature object is
    // not resolvable client-side (e.g. old saves with a shifted creatureID).
    private static final EntityDataAccessor<String> CREATURE_NAME = SynchedEntityData.defineId(EntityDinoEgg.class, EntityDataSerializers.STRING);

    public Creature creature;
    public int quality;
    public int currentSpawnTime;
    public int spawnTime;
    public int rockAmount;
    public boolean froze;
    public boolean dried;

    public EntityDinoEgg(EntityType<? extends EntityDinoEgg> type, Level world) {
        super(type, world);
        this.setMaxUpStep(1.0F);
    }

    /**
     * Compatibility constructor used by the old reflective spawn code paths.
     */
    public EntityDinoEgg(Level world) {
        this(JCEntityRegistry.DINO_EGG.get(), world);
    }

    public EntityDinoEgg(Level world, Creature creature, int spawnTime) {
        this(world);
        this.setCreature(creature);
        this.spawnTime = spawnTime;
    }

    public EntityDinoEgg(Level world, Creature creature, int quality, String dna, int spawnTime, double x, double y, double z) {
        this(world, creature, spawnTime);
        this.moveTo(x + 0.5F, y, z + 0.5F);
        this.quality = quality;
        this.setDNASequence(dna);
    }

    public void setCreature(Creature creature) {
        this.creature = creature;
        if (creature != null) {
            this.getEntityData().set(CREATURE_NAME, creature.getCreatureName());
        }
    }

    /**
     * The synced creature name used by the client renderer; falls back to the
     * creature object when available.
     */
    public String getSyncedCreatureName() {
        String synced = this.getEntityData().get(CREATURE_NAME);
        if (synced != null && !synced.isEmpty()) {
            return synced;
        }
        return this.creature != null ? this.creature.getCreatureName() : "";
    }

    /**
     * Sets the creature DNA quality.
     */
    public void setQuality(int quality) {
        this.quality = quality;
    }

    /**
     * Returns the creature DNA quality.
     */
    public int getDNAQuality() {
        return this.quality;
    }

    /**
     * Returns the creature DNA sequence.
     */
    public String getDNASequence() {
        return this.getEntityData().get(DNA_SEQUENCE);
    }

    /**
     * Sets the creature DNA sequence.
     */
    public void setDNASequence(String dna) {
        this.getEntityData().set(DNA_SEQUENCE, String.valueOf(dna));
    }

    public void setCurrentSpawnTime(int currentSpawnTime) {
        this.currentSpawnTime = currentSpawnTime;
    }

    public void setSpawnTime(int spawnTime) {
        this.spawnTime = spawnTime;
    }

    @Override
    public boolean hurt(DamageSource damage, float amount) {
        if (!this.isInvulnerableTo(damage)) {
            // if (this.level().isClientSide)
            // {
            // if (amount > 0)
            // {
            // Minecraft mc = Minecraft.getMinecraft();
            //
            // Random random = new Random();
            //
            // for (int currentParticle = 0; currentParticle < 50; ++currentParticle)
            // {
            // float f3 = Mth.randomFloatClamp(random, 0.0F, ((float) Math.PI * 2F));
            // double d5 = (double) Mth.randomFloatClamp(random, 0.75F, 1.0F);
            // double velY = 0.20000000298023224D + 1 / 100.0D;
            // double velX = (double) (Mth.cos(f3) * 0.2F) * d5 * d5 * (1 + 0.2D);
            // double velZ = (double) (Mth.sin(f3) * 0.2F) * d5 * d5 * (1 + 0.2D);
            // // mc.theWorld.addParticle("blockdust_" + Block.getIdFromBlock(Blocks.sandstone) + "_0", (double) ((float) this.getX()), (double) ((float) this.getY()), (double) ((float) this.getZ()), velX, velY, velZ);
            // }
            // }
            // }
            this.discard();
        }

        return super.hurt(damage, amount);
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be pushable on contact, like boats or minecarts.
     */
    public AABB getCollisionBox(Entity entity) {
        return entity.getBoundingBox();
    }

    /**
     * returns the bounding box for this entity
     */
    public AABB getCollisionBoundingBox() {
        return null;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.isRemoved()) {
            if (this.level().isClientSide) {
                this.froze = this.getEntityData().get(FROZE) != 0;

                this.dried = this.getEntityData().get(DRIED) != 0;

                this.currentSpawnTime = this.getEntityData().get(CURRENT_SPAWN_TIME);
            }

            if (!this.onGround())
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y - 0.05F, this.getDeltaMovement().z));

            if (this.getDeltaMovement().y < -0.8F)
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, -0.8F, this.getDeltaMovement().z));

            if (this.onGround()) {
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x * 0.5F, this.getDeltaMovement().y, this.getDeltaMovement().z));
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z * 0.5F));
            } else {
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x * 0.7F, this.getDeltaMovement().y, this.getDeltaMovement().z));
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z * 0.7F));
            }

            if (!this.level().isClientSide) {
                // 1.20.1: creature can be null when the egg lacks DNA data (e.g. old
                // saves with a shifted creatureID, or eggs without NBT); skip the
                // hatching logic instead of crashing.
                if (this.creature == null) {
                    return;
                }

                int amountToIncrease = 0;

                List<EggEnviroment> enviroments = EggEnviroment.getEnviroments(this);

                boolean wet = enviroments.contains(EggEnviroment.WET);
                boolean warm = enviroments.contains(EggEnviroment.WARM);
                boolean overheat = enviroments.contains(EggEnviroment.OVERHEAT);
                boolean cold = enviroments.contains(EggEnviroment.COLD);

                if (this.creature.isWaterCreature()) {
                    if (!wet) {
                        if (overheat)
                            amountToIncrease = -2;
                        else
                            amountToIncrease = -1;
                    } else {
                        amountToIncrease = 2;
                    }
                } else {
                    if (warm && !wet) {
                        amountToIncrease = 1;
                    } else {
                        if (cold && wet)
                            amountToIncrease = -2;
                        else
                            amountToIncrease = -1;
                    }
                }

                this.currentSpawnTime += amountToIncrease;

                if (this.currentSpawnTime < -500) {
                    if (this.creature.isWaterCreature())
                        this.dried = true;
                    else
                        this.froze = true;
                }

                if (this.currentSpawnTime >= this.spawnTime) {
                    Class dinoToSpawnClass = this.creature.getCreatureClass();

                    try {
                        // 1.20.1: spawn through the registered per-creature EntityType so the
                        // client can find the matching renderer and dimensions.
                        net.minecraft.world.entity.EntityType<?> spawnType = this.creature.getEntityType();
                        Entity dinoToSpawn = spawnType != null
                                ? spawnType.create(this.level())
                                : (Entity) dinoToSpawnClass.getConstructor(Level.class).newInstance(this.level());

                        if (dinoToSpawn instanceof EntityJurassiCraftCreature) {
                            EntityJurassiCraftCreature baby = (EntityJurassiCraftCreature) dinoToSpawn;
                            baby.setGenetics(this.quality, this.getDNASequence());

                            EntityJurassiCraftSmart smartBaby = (EntityJurassiCraftSmart) baby;
                            if (dinoToSpawn instanceof EntityJurassiCraftSmart && smartBaby.canBeTamedUponSpawning()) {
                                Player owner = this.level().getNearestPlayer(this, 6.0D);

                                if (owner != null) {
                                    smartBaby.setTamed(true, owner);
                                    smartBaby.setOwner(owner.getName().getString());

                                    this.level().broadcastEntityEvent(baby, (byte) 7);
                                }
                            }

                            baby.moveTo(this.getX(), this.getY(), this.getZ());

                            this.level().addFreshEntity(baby);
                            this.currentSpawnTime = 0;
                            this.discard();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (this.currentSpawnTime < (this.spawnTime - 100)) {
                if (!this.dried && !this.froze) {
                    if (this.getXRot() >= 5)
                        this.rockAmount = -1;
                    else if (this.getXRot() <= -5)
                        this.rockAmount = 1;

                    this.setXRot(this.getXRot() + (this.rockAmount / 2.0F));
                }
            }

            if (!this.level().isClientSide) {
                this.getEntityData().set(FROZE, this.froze ? 1 : 0);
                this.getEntityData().set(DRIED, this.dried ? 1 : 0);
                this.getEntityData().set(CURRENT_SPAWN_TIME, this.currentSpawnTime);
            }

            this.move(MoverType.SELF, new Vec3(this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z));
        }
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        boolean flag = super.causeFallDamage(fallDistance, damageMultiplier, source);

        if (fallDistance > 10 && this.onGround())
            this.hurt(this.level().damageSources().fall(), 1.0F);

        return flag;
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DNA_SEQUENCE, "");
        this.getEntityData().define(FROZE, 0);
        this.getEntityData().define(DRIED, 0);
        this.getEntityData().define(CURRENT_SPAWN_TIME, this.rockAmount);
        this.getEntityData().define(CREATURE_NAME, "");
    }

    public ResourceLocation getTexture() {
        // 1.20.1: ResourceLocation paths must be lowercase; the egg textures are
        // stored under lowercase names (eggankylosaurus.png etc.).
        // Use the synced creature name so the client always renders the correct egg
        // even when the creature object is missing (old saves / shifted creatureID).
        String name = this.getSyncedCreatureName().toLowerCase();
        if (name.isEmpty()) {
            return new ResourceLocation(JurassiCraft.getModId() + "textures/eggs/eggdilophosaurus.png");
        }
        return new ResourceLocation(JurassiCraft.getModId() + "textures/eggs/egg" + name + ".png");
    }

    public int getHatchingProgressScaled(int i) {
        if (this.spawnTime > 0) {
            return i * this.currentSpawnTime / this.spawnTime;
        }

        return 0;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.getMainHandItem().isEmpty()) {
            if (this.creature == null) {
                // 1.20.1: cannot pick up an egg without a creature mapping.
                return InteractionResult.PASS;
            }

            ItemStack stack = new ItemStack(this.creature.getEgg());

            if (!player.level().isClientSide) {
                CompoundTag compound = new CompoundTag();

                compound.putInt("EggQuality", this.quality);
                compound.putString("EggDNA", this.getDNASequence());

                stack.setTag(compound);

                if (player.getInventory().add(stack)) {
                    this.level().playSound(player, this.blockPosition(), SoundEvent.createVariableRangeEvent(new ResourceLocation("random.pop")), SoundSource.PLAYERS, 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    this.discard();
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        if (this.creature != null) {
            nbt.putInt("CreatureID", this.creature.getCreatureID());
            // 1.20.1: the numeric creatureID is a static counter that shifts when the
            // creature list changes; save the name too so old saves keep resolving.
            nbt.putString("CreatureName", this.creature.getCreatureName());
        } else if (!this.getSyncedCreatureName().isEmpty()) {
            nbt.putString("CreatureName", this.getSyncedCreatureName());
        }
        nbt.putString("DNASequence", this.getDNASequence());
        nbt.putInt("Quality", this.quality);
        nbt.putInt("SpawnTime", this.spawnTime);
        nbt.putInt("CurrentSpawnTime", this.currentSpawnTime);
        nbt.putBoolean("Froze", this.froze);
        nbt.putBoolean("Dried", this.dried);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.creature = null;
        String creatureName = "";
        if (nbt.contains("CreatureName")) {
            creatureName = nbt.getString("CreatureName");
            this.creature = CreatureHandler.getCreatureFromName(creatureName);
        }
        if (this.creature == null && nbt.contains("CreatureID")) {
            this.creature = CreatureHandler.getCreatureFromId(nbt.getInt("CreatureID"));
        }
        if (this.creature != null) {
            creatureName = this.creature.getCreatureName();
        }
        this.getEntityData().set(CREATURE_NAME, creatureName);
        this.setDNASequence(nbt.getString("DNASequence"));
        this.setQuality(nbt.getInt("Quality"));
        this.spawnTime = nbt.getInt("SpawnTime");
        this.currentSpawnTime = nbt.getInt("CurrentSpawnTime");
        this.froze = nbt.getBoolean("Froze");
        this.dried = nbt.getBoolean("Dried");
    }

    @Override
    public void writeSpawnData(net.minecraft.network.FriendlyByteBuf buffer) {
        buffer.writeInt(this.creature != null ? this.creature.getCreatureID() : -1);
        buffer.writeUtf(this.getSyncedCreatureName());
        buffer.writeInt(this.quality);
        buffer.writeInt(this.spawnTime);
    }

    @Override
    public void readSpawnData(net.minecraft.network.FriendlyByteBuf additionalData) {
        int id = additionalData.readInt();
        String name = additionalData.readUtf();
        this.creature = null;
        if (!name.isEmpty()) {
            this.creature = CreatureHandler.getCreatureFromName(name);
        }
        if (this.creature == null && id >= 0) {
            this.creature = CreatureHandler.getCreatureFromId(id);
        }
        this.getEntityData().set(CREATURE_NAME, name);
        this.quality = additionalData.readInt();
        this.spawnTime = additionalData.readInt();
    }
}
