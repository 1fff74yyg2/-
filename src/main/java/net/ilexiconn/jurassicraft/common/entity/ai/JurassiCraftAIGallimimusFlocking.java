package net.ilexiconn.jurassicraft.common.entity.ai;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftSmart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;

public class JurassiCraftAIGallimimusFlocking extends Goal {
    private EntityJurassiCraftSmart herdCreature;
    private ArrayList<EntityJurassiCraftSmart> herd = new ArrayList<EntityJurassiCraftSmart>();
    private ArrayList<Double> followingDistanceOfTheHerd = new ArrayList<Double>();
    private double searchDistance;
    private double movementSpeed;
    private int flockingDistance;
    private int flockingDistanceVariation;
    private int maxTimeTryingToMove;
    private int numberOfGallimimus;
    private int timeTryingToMove;

    /**
     * @author RafaMv
     */
    public JurassiCraftAIGallimimusFlocking(EntityJurassiCraftSmart creature, int minimumNumberOfGallimimus, double distanceToSearch, int distanceToFlock, int distanceToFlockVariation, double speed) {
        this.herdCreature = creature;
        this.numberOfGallimimus = minimumNumberOfGallimimus;
        this.searchDistance = distanceToSearch;
        this.flockingDistance = distanceToFlock;
        this.flockingDistanceVariation = distanceToFlockVariation;
        this.movementSpeed = speed;
        this.timeTryingToMove = 0;

        this.setFlags(java.util.EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
    }

    @Override
    public boolean isInterruptable() {
        return this.timeTryingToMove > 3.0D * this.maxTimeTryingToMove / 4.0D;
    }

    public boolean canUse() {
        if (this.herdCreature.getTarget() != null || this.herdCreature.getRandom().nextInt(501) < 500 || this.herdCreature.isSitting()) {
            return false;
        }

        List<Entity> nearEntityList = this.herdCreature.level().getEntities(this.herdCreature, this.herdCreature.getBoundingBox().inflate(this.searchDistance, 8.0D, this.searchDistance), e -> true);
        this.herd.add(this.herdCreature);

        if (!nearEntityList.isEmpty()) {
            for (int i = 0; i < nearEntityList.size(); i++) {
                if (nearEntityList.get(i) instanceof EntityJurassiCraftSmart && nearEntityList.get(i).getClass() == herdCreature.getClass() && !((EntityJurassiCraftSmart) nearEntityList.get(i)).isSitting()) {
                    this.herd.add((EntityJurassiCraftSmart) nearEntityList.get(i));
                }
            }

            if (this.herd.size() >= this.numberOfGallimimus) {
                return this.herdCreature.isAlive();
            }
        }

        return false;
    }

    public void start() {
        double xDistance = this.herdCreature.getRandom().nextInt(this.flockingDistance);
        double zDistance = this.flockingDistance - xDistance;

        if (this.herdCreature.getRandom().nextBoolean())
            xDistance = -xDistance;

        if (this.herdCreature.getRandom().nextBoolean())
            zDistance = -zDistance;

        for (int i = 0; i < herd.size(); i++) {
            this.followingDistanceOfTheHerd.add(this.herd.get(i).getAttribute(Attributes.FOLLOW_RANGE).getValue());
            this.herd.get(i).getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(this.flockingDistance + this.flockingDistanceVariation + 1);
            double xDistanceExtra = this.herd.get(i).getRandom().nextInt(this.flockingDistanceVariation);
            double zDistanceExtra = this.herd.get(i).getRandom().nextInt(this.flockingDistanceVariation);
            double targetX = this.herd.get(i).getX() + xDistance + xDistanceExtra;
            double targetZ = this.herd.get(i).getZ() + zDistance + zDistanceExtra;
            int targetY = this.herd.get(i).level().getHeight(Heightmap.Types.MOTION_BLOCKING, (int) targetX, (int) targetZ);
            Path path = this.herd.get(i).getNavigation().createPath(BlockPos.containing(targetX, targetY, targetZ), 1);
            this.herd.get(i).getNavigation().moveTo(path, this.movementSpeed);
        }

        double centerX = this.herdCreature.getX() + xDistance;
        double centerZ = this.herdCreature.getZ() + zDistance;
        int centerY = this.herdCreature.level().getHeight(Heightmap.Types.MOTION_BLOCKING, (int) centerX, (int) centerZ);
        this.maxTimeTryingToMove = (int) (10.0D * Math.sqrt(this.herdCreature.distanceToSqr(centerX, centerY, centerZ)));
        super.start();
    }

    public void tick() {
        this.timeTryingToMove++;
    }

    public boolean canContinueToUse() {
        return (!this.herdCreature.getNavigation().isDone() || this.timeTryingToMove < this.maxTimeTryingToMove);
    }

    public void stop() {
        for (int i = 0; i < herd.size(); i++) {
            if (this.herd.get(i).isAlive()) {
                this.herd.get(i).getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(this.followingDistanceOfTheHerd.get(i));
                this.herd.get(i).getNavigation().stop();
            }
        }

        this.herd.clear();
        this.followingDistanceOfTheHerd.clear();
        this.timeTryingToMove = 0;
    }
}
