package net.ilexiconn.jurassicraft.common.entity.ai.herds;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature;

public class HerdAIGroupAttack extends EntityAIHerd {
    public HerdAIGroupAttack(EntityJurassiCraftCreature creature) {
        super(creature, true);
    }

    public void start() {
        super.start();

        if (getHerd() != null)
            getHerd().attack(getCreature().getTarget());
    }

    public boolean canUse() {
        return getCreature().getTarget() != null;
    }

    public boolean canContinueToUse() {
        return getCreature().getTarget() != null && getCreature().getTarget().isAlive();
    }
}
