package net.ilexiconn.jurassicraft.common.entity.ai.animation;

import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ai.AIAnimation;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityHypsilophodon;
import net.minecraft.world.phys.Vec3;

public class AnimationAIHypsilophodonPlaying extends AIAnimation {
    private EntityHypsilophodon hypsilophodon;
    private double destX;
    private double destZ;
    private double friendSpeedX;
    private double friendSpeedZ;
    private double friendPrevPosX;
    private double friendPrevPosZ;

    public AnimationAIHypsilophodonPlaying(EntityHypsilophodon hypsilophodon) {
        super(hypsilophodon);

        this.hypsilophodon = hypsilophodon;
    }

    public int getAnimationId() {
        return JurassiCraftAnimationIDs.PLAYING.animID();
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 70;
    }

    public void start() {
        super.start();
    }

    public void tick() {
        if (this.hypsilophodon.getCreatureToAttack() != null && this.hypsilophodon.getCreatureToAttack() instanceof EntityHypsilophodon) {
            EntityHypsilophodon friend = (EntityHypsilophodon) this.hypsilophodon.getCreatureToAttack();

            if (this.hypsilophodon.getAnimationTick() < 5)
                this.hypsilophodon.getLookControl().setLookAt(friend, 30F, 30F);

            if (this.hypsilophodon.getAnimationTick() == 6) {
                this.friendPrevPosX = friend.getX();
                this.friendPrevPosZ = friend.getZ();
            }

            if (hypsilophodon.getAnimationTick() == 7) {
                this.friendSpeedX = friend.getX() - this.friendPrevPosX;
                this.friendSpeedZ = friend.getZ() - this.friendPrevPosZ;

                this.destX = friend.getX() + this.friendSpeedX * 12.0D;
                this.destZ = friend.getZ() + this.friendSpeedZ * 12.0D;

                double d = Math.sqrt((this.destX - this.hypsilophodon.getX()) * (this.destX - this.hypsilophodon.getX()) + (this.destZ - this.hypsilophodon.getZ()) * (this.destZ - this.hypsilophodon.getZ()));
                double a = Math.atan2((this.destZ - this.hypsilophodon.getZ()), (this.destX - this.hypsilophodon.getX()));

                this.hypsilophodon.setDeltaMovement(new Vec3((d / 6.0D) * Math.cos(a), this.hypsilophodon.getDeltaMovement().y, this.hypsilophodon.getDeltaMovement().z));
                this.hypsilophodon.setDeltaMovement(new Vec3(this.hypsilophodon.getDeltaMovement().x, this.hypsilophodon.getDeltaMovement().y, (d / 6.0D) * Math.sin(a)));
                this.hypsilophodon.setDeltaMovement(new Vec3(this.hypsilophodon.getDeltaMovement().x, 0.3D, this.hypsilophodon.getDeltaMovement().z));
            }
        }
    }

    public void stop() {
        super.stop();

        this.hypsilophodon.setCreatureToAttack(null);
    }
}
