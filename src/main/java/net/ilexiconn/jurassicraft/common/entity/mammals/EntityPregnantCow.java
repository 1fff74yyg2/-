package net.ilexiconn.jurassicraft.common.entity.mammals;

import net.minecraft.world.entity.animal.Cow;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityPregnantCow {
    public final static String PREGNANT_COW_PROPERTY = "EntityPregnantCowJC";
    private static final Map<UUID, EntityPregnantCow> PREGNANT_COWS = new HashMap<UUID, EntityPregnantCow>();

    private String dnaSequence;
    private String mammalName;
    private int pregnancyProgress;
    private int pregnancySpeed;
    private int dnaQuality;

    public EntityPregnantCow() {
        this.mammalName = "noEmbryo";
        this.dnaQuality = 0;
        this.dnaSequence = "";
        this.pregnancySpeed = 0;
    }

    public static final void register(Cow entity) {
        PREGNANT_COWS.put(entity.getUUID(), new EntityPregnantCow());
    }

    public static final EntityPregnantCow get(Cow entity) {
        EntityPregnantCow pregnant = PREGNANT_COWS.get(entity.getUUID());
        if (pregnant != null) {
            pregnant.loadFromNbt(entity);
        }
        return pregnant;
    }

    public String getDNASequence() {
        return dnaSequence;
    }

    public void setDNASequence(String dna) {
        this.dnaSequence = dna;
    }

    public String getMammalName() {
        return mammalName;
    }

    public void setMammalName(String mammal) {
        this.mammalName = mammal;
    }

    public int getPregnancyProgress() {
        return pregnancyProgress;
    }

    public void setPregnancyProgress(int progress) {
        this.pregnancyProgress = progress;
    }

    public void increasePregnancyProgress() {
        this.pregnancyProgress = this.getPregnancyProgress() + 1;
    }

    public int getPregnancySpeed() {
        return pregnancySpeed;
    }

    public void setPregnancySpeed(int speed) {
        this.pregnancySpeed = speed;
    }

    public int getDNAQuality() {
        return dnaQuality;
    }

    public void setDNAQuality(int quality) {
        this.dnaQuality = quality;
    }

    public int getPregnancyProgressScaled(int barSize) {
        if (this.getPregnancySpeed() <= 0)
            this.setPregnancySpeed(2048);

        return (this.getPregnancyProgress() * barSize) / this.getPregnancySpeed();
    }
    private boolean loadedFromNbt = false;

    /**
     * 1.20.1: pregnancy state is persisted into the entity's Forge persistent
     * data (entity.getPersistentData()) so it survives server restarts and
     * world reloads. Called lazily from get().
     */
    public void loadFromNbt(Cow entity) {
        if (this.loadedFromNbt)
            return;
        this.loadedFromNbt = true;
        net.minecraft.nbt.CompoundTag tag = entity.getPersistentData();
        if (tag.contains("CowPregnantJC")) {
            net.minecraft.nbt.CompoundTag data = tag.getCompound("CowPregnantJC");
            this.mammalName = data.getString("MammalName");
            this.dnaQuality = data.getInt("DNAQuality");
            this.dnaSequence = data.getString("DNASequence");
            this.pregnancyProgress = data.getInt("PregnancyProgress");
            this.pregnancySpeed = data.getInt("PregnancySpeed");
        }
    }

    public void saveToNbt(Cow entity) {
        net.minecraft.nbt.CompoundTag data = new net.minecraft.nbt.CompoundTag();
        data.putString("MammalName", this.mammalName);
        data.putInt("DNAQuality", this.dnaQuality);
        data.putString("DNASequence", this.dnaSequence);
        data.putInt("PregnancyProgress", this.pregnancyProgress);
        data.putInt("PregnancySpeed", this.pregnancySpeed);
        entity.getPersistentData().put("CowPregnantJC", data);
    }
}

