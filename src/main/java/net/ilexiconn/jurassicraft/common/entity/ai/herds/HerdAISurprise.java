package net.ilexiconn.jurassicraft.common.entity.ai.herds;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature;
import net.ilexiconn.jurassicraft.common.entity.dinosaurs.EntityVelociraptor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class HerdAISurprise extends EntityAIHerd {
    private LivingEntity theChosenOne;
    private VelociraptorHerd velociraptorHerd;

    public HerdAISurprise(EntityJurassiCraftCreature creature) {
        super(creature, false); // false, because we don't need (and don't want) the attack targets to be overwritten
    }

    @SuppressWarnings("unchecked")
    public void start() {
        super.start();

        CreatureHerd herd = getHerd();

        if (herd != null) {
            velociraptorHerd = (VelociraptorHerd) getHerd();

            if (getCreature().level() != null) {
                if (getCreature().getBoundingBox() != null) {
                    if (!velociraptorHerd.isSneakingUp()) {
                        List<LivingEntity> entities = getCreature().level().getEntitiesOfClass(LivingEntity.class, getCreature().getBoundingBox().inflate(32, 32, 32));

                        int i = 0;

                        while (theChosenOne == null || theChosenOne instanceof EntityVelociraptor) {
                            theChosenOne = entities.get((int) (entities.size() * Math.random()));
                            i++;

                            if (i >= 100) {
                                theChosenOne = null;
                                break;
                            }
                        }

                        if (theChosenOne != null)
                            velociraptorHerd.attack(theChosenOne);
                    } else
                        theChosenOne = velociraptorHerd.getCurrentTarget();
                }
            }
        }
    }

    public boolean canContinueToUse() {
        if (theChosenOne == null || theChosenOne.isRemoved())
            return false;

        EntityJurassiCraftCreature creature = getCreature();

        int index = getHerd().indexOf(creature);
        float targetAngle = (float) Math.toRadians((360f / getHerd().size()) * index);

        Vec3 chosenPos = new Vec3(theChosenOne.getX(), theChosenOne.getY(), theChosenOne.getZ());
        Vec3 creaturePos = new Vec3(creature.getX(), creature.getY(), creature.getZ());

        float angle = (float) Math.acos(Math.max(-1.0D, Math.min(1.0D, chosenPos.dot(creaturePos) / (chosenPos.length() * creaturePos.length()))));
        float delta = Math.abs(targetAngle - angle);

        if (delta < Math.PI / 8f) // We are close enough
        {
            creature.setShiftKeyDown(true);
            Path path = creature.getNavigation().createPath(theChosenOne.blockPosition(), 1);

            if (path != null)
                creature.getNavigation().moveTo(path, creature.getCreatureSpeed() * 4);
        } else {
            float speed = (float) (Math.PI / 128f);
            float angleToGo = speed + angle * (Math.signum(targetAngle - angle));
            float dist = 20f;
            float xPos = (float) (Math.cos(angleToGo) * dist - 5 + theChosenOne.getX());
            float zPos = (float) (dist * Math.sin(angleToGo) - 5 + theChosenOne.getZ());

            Path path = creature.getNavigation().createPath(BlockPos.containing(xPos, theChosenOne.getY(), zPos), 1);

            if (path != null)
                creature.getNavigation().moveTo(path, creature.getCreatureSpeed() * 4);
            else
                return false;
        }

        return true;
    }

    public boolean canUse() {
        return (getCreature().getTarget() == null && Math.random() < 1.10) || velociraptorHerd.isSneakingUp(); // TODO: change proba
    }

    protected CreatureHerd createHerd() {
        return new VelociraptorHerd();
    }
}
