package net.ilexiconn.jurassicraft.common.entity.ai.animation;

import net.ilexiconn.jurassicraft.common.api.IAnimatedEntity;
import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftAnimationIDs;
import net.ilexiconn.jurassicraft.common.entity.ai.AIAnimation;
import net.minecraft.world.entity.Mob;

public class AnimationAIHypsilophodonScratchHead extends AIAnimation {
    public AnimationAIHypsilophodonScratchHead(IAnimatedEntity entity) {
        super(entity);
    }

    public int getAnimationId() {
        return JurassiCraftAnimationIDs.SCRATCH.animID();
    }

    public boolean isAutomatic() {
        return false;
    }

    public int getDuration() {
        return 35;
    }

    public boolean shouldAnimate() {
        Mob living = getEntity();
        IAnimatedEntity entity = (IAnimatedEntity) living;

        return entity.getAnimationId() == 0 && living.getRandom().nextInt(60) == 0;
    }
}
