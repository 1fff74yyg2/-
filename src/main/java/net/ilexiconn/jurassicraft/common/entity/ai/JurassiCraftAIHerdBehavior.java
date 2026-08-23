package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.ArrayList;
import java.util.List;

public class JurassiCraftAIHerdBehavior extends Goal {
    private EntityJurassiCraftSmart lostCreature;
    private EntityJurassiCraftSmart herdCreature;
    private double lostCreatureOldFollowRange;
    private double searchDistance;
    private double distanceToHerd;
    private double maxDistanceToHerd;
    private double movementSpeed;
    private int timeTryingToMove;
    private int maxTimeTryingToMove;

    /**
     * @author RafaMv
     */
    public JurassiCraftAIHerdBehavior(EntityJurassiCraftSmart creature, double distance, int maxNumberOfTicksTrying, double distanceToConsiderHerd, double speed) {
        this.lostCreature = creature;
        this.searchDistance = distance;
        this.maxTimeTryingToMove = maxNumberOfTicksTrying;
        this.maxDistanceToHerd = distanceToConsiderHerd;
        this.movementSpeed = speed;
        this.timeTryingToMove = 0;
        this.setFlags(java.util.EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
    }

    @Override
    public boolean isInterruptable() {
        return this.timeTryingToMove > this.maxTimeTryingToMove / 2.0D;
    }

    public boolean canUse() {
        if (this.lostCreature.getTarget() != null || this.lostCreature.getRandom().nextInt(100) < 99 || !this.lostCreature.isAlive())
            return false;

        ArrayList<EntityJurassiCraftSmart> nearCreaturesList = new ArrayList<EntityJurassiCraftSmart>();
        List<Entity> nearEntityList = this.lostCreature.level().getEntities(this.lostCreature, this.lostCreature.getBoundingBox().inflate(this.searchDistance, 8.0D, this.searchDistance), e -> true);

        if (!nearEntityList.isEmpty()) {
            for (int i = nearEntityList.size() - 1; i > -1; i--) {
                if (nearEntityList.get(i) instanceof EntityJurassiCraftSmart && nearEntityList.get(i).getClass() == lostCreature.getClass() && nearEntityList.get(i).getUUID() != lostCreature.getUUID())
                    nearCreaturesList.add((EntityJurassiCraftSmart) nearEntityList.get(i));
            }

            if (nearCreaturesList.size() < 4) {
                double minDistance = this.searchDistance + 1;

                for (int j = 0; j < nearCreaturesList.size(); j++) {
                    double distance = nearCreaturesList.get(j).distanceTo(this.lostCreature);

                    if (distance < minDistance) {
                        minDistance = distance;
                        this.herdCreature = nearCreaturesList.get(j);
                    }
                }
            } else {
                ArrayList<Integer> nearCreatureAndNumberOfOtherNearCreatures = new ArrayList<Integer>();

                int neighbourCount = 0;

                for (int j = 0; j < nearCreaturesList.size(); j++) {
                    neighbourCount = 0;

                    for (int k = 0; k < nearCreaturesList.size(); k++) {
                        if (nearCreaturesList.get(j).distanceTo(nearCreaturesList.get(k)) < this.maxDistanceToHerd)
                            neighbourCount++;
                    }

                    nearCreatureAndNumberOfOtherNearCreatures.add(neighbourCount);
                }

                neighbourCount = 0;

                for (int i = 0; i < nearCreatureAndNumberOfOtherNearCreatures.size(); i++) {
                    if (nearCreatureAndNumberOfOtherNearCreatures.get(i) > neighbourCount) {
                        neighbourCount = nearCreatureAndNumberOfOtherNearCreatures.get(i);
                        this.herdCreature = nearCreaturesList.get(i);
                    }
                }
            }

            if (this.herdCreature != null && this.herdCreature.isAlive() && this.lostCreature.isAlive()) {
                this.distanceToHerd = this.lostCreature.distanceTo(herdCreature);
                return !this.lostCreature.isSitting() && this.distanceToHerd > this.maxDistanceToHerd;
            }
        }

        return false;
    }

    public void start() {
        this.lostCreatureOldFollowRange = this.lostCreature.getAttribute(Attributes.FOLLOW_RANGE).getValue();
        this.lostCreature.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(this.searchDistance);
        this.lostCreature.getNavigation().moveTo(this.herdCreature, this.movementSpeed);

        super.start();
    }

    public void tick() {
        if (this.timeTryingToMove == this.maxTimeTryingToMove / 2.0D || this.timeTryingToMove == this.maxTimeTryingToMove / 4.0D || this.timeTryingToMove == 3.0D * this.maxTimeTryingToMove / 4.0D) {
            this.lostCreature.getNavigation().moveTo(this.herdCreature, this.movementSpeed);
        }

        this.lostCreature.distanceToSqr(herdCreature);
        this.timeTryingToMove++;
    }

    public boolean canContinueToUse() {
        return (!this.lostCreature.getNavigation().isDone() && this.timeTryingToMove < this.maxTimeTryingToMove && this.distanceToHerd > this.maxDistanceToHerd / 2.0D && this.lostCreature.isAlive() && this.herdCreature.isAlive());
    }

    public void stop() {
        this.lostCreature.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(this.lostCreatureOldFollowRange);
        this.lostCreature.getNavigation().stop();
        this.herdCreature = null;
        this.timeTryingToMove = 0;
    }
}