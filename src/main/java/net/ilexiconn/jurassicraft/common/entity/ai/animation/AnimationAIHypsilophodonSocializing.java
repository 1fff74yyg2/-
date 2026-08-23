package net.ilexiconn.jurassicraft.common.entity.ai.animation;

import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ai.AIAnimation;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityHypsilophodon;

public class AnimationAIHypsilophodonSocializing extends AIAnimation {
    private EntityHypsilophodon hypsilophodon;

    public AnimationAIHypsilophodonSocializing(EntityHypsilophodon hypsilophodon) {
        super(hypsilophodon);
        this.hypsilophodon = hypsilophodon;
    }

    public int getAnimationId() {
        return JurassiCraftAnimationIDs.SOCIALIZING.animID();
    }

    public boolean isAutomatic() {
        return true;
    }

    public int getDuration() {
        return 70;
    }

    public void start() {
        super.start();
        this.hypsilophodon.setSitting(false, null);
    }

    public void tick() {
        if (this.hypsilophodon.getCreatureToAttack() != null && this.hypsilophodon.getCreatureToAttack() instanceof EntityHypsilophodon) {
            EntityHypsilophodon friend = (EntityHypsilophodon) this.hypsilophodon.getCreatureToAttack();

            if (this.hypsilophodon.getAnimationTick() < 5) {
                this.hypsilophodon.getLookControl().setLookAt(friend, 30F, 30F);
            }
            // TODO Do stuff
        }
    }

    public void stop() {
        super.stop();

        this.hypsilophodon.setCreatureToAttack(null);
    }
}
