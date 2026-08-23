package net.ilexiconn.jurassicraft.common.entity.ai;

import com.google.common.collect.Lists;
import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftCreature;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class JurassiCraftAIGoNearWater extends Goal {
    private double speed;
    private EntityJurassiCraftCreature creature;
    private Level world;
    private boolean foundPool;
    private float poolX;
    private float poolY;
    private float poolZ;
    private int maxDist;
    private long lastTimeExecuted;
    private float maxTime;

    public JurassiCraftAIGoNearWater(EntityJurassiCraftCreature creature, double creatureSpeed) {
        this(creature, creatureSpeed, ((60 * 6 + 35) * 1f / 20f)); // 6 minutes and 35 seconds
    }

    public JurassiCraftAIGoNearWater(EntityJurassiCraftCreature creature, double creatureSpeed, float maxTime) {
        this(creature, creatureSpeed, 25, maxTime);
    }

    public JurassiCraftAIGoNearWater(EntityJurassiCraftCreature creature, double creatureSpeed, int maxSearchDistance, float maxTime) {
        this.creature = creature;
        this.world = creature.level();
        this.speed = creatureSpeed;
        this.maxDist = maxSearchDistance;
        this.poolY = -64;
        this.maxTime = maxTime;
    }

    public boolean canContinueToUse() {
        return !foundPool && Math.sqrt(creature.distanceToSqr(poolX, poolY, poolZ)) > 5.0D;
    }

    public void start() {
        ArrayList<Vec3> waterBlocks = Lists.newArrayList();

        int startX = (int) Math.floor(creature.getX());
        int startY = (int) Math.floor(creature.getY());
        int startZ = (int) Math.floor(creature.getZ());

        for (int x = -maxDist / 2 + startX; x < maxDist / 2 + startX; x++) {
            for (int y = -maxDist / 2 + startY; y < maxDist / 2 + startY; y++) {
                for (int z = -maxDist / 2 + startZ; z < maxDist / 2 + startZ; z++) {
                    if (world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.WATER) {
                        waterBlocks.add(new Vec3(x, y, z));
                    }
                }
            }
        }
        // Now that we have all the blocks of water around the creature, we sort them to get blobs of water and then get their center

        List<List<Vec3>> blobsList = Lists.newArrayList();
        blocksList:
        for (Vec3 waterPos : waterBlocks) {
            for (List<Vec3> list : blobsList) {
                for (Vec3 pos : list) {
                    if (isNextTo(pos, waterPos)) {
                        list.add(waterPos);
                        continue blocksList;
                    }
                }
            }
            // If we are here, that means no blocks where found near this block

            List<Vec3> blob = Lists.newArrayList();
            blob.add(waterPos);
            blobsList.add(blob);
        }

        // Then we merge blobs that are next to each other because the previous algorithm might separate some

        List<List<Vec3>> finalList = Lists.newArrayList();
        finalList.addAll(blobsList);

        blobList:
        for (List<Vec3> blob : blobsList) {
            for (Vec3 pos : blob) {
                for (List<Vec3> otherBlob : blobsList) {
                    for (Vec3 otherPos : otherBlob) {
                        if (isNextTo(pos, otherPos) && finalList.contains(blob) && finalList.contains(otherBlob)) {
                            List<Vec3> mergedBlob = Lists.newArrayList();
                            mergedBlob.addAll(blob);
                            mergedBlob.addAll(otherBlob);
                            finalList.add(mergedBlob);
                            finalList.remove(blob);
                            finalList.remove(otherBlob);
                            continue blobList;
                        }
                    }
                }
            }
        }
        // Finally, we calculate the center of each pool and get the nearest one.
        for (List<Vec3> blob : finalList) {
            double centerX = 0;
            double centerY = 0;
            double centerZ = 0;

            for (Vec3 pos : blob) {
                centerX += pos.x;
                centerY += pos.y;
                centerZ += pos.z;
            }

            centerX /= blob.size();
            centerY /= blob.size();
            centerZ /= blob.size();

            if (Math.sqrt(creature.distanceToSqr(centerX, centerY, centerZ)) <= Math.sqrt(creature.distanceToSqr(poolX, poolY, poolZ)) || poolY <= 0) {
                poolX = (float) Math.floor(centerX);
                poolY = (float) Math.floor(centerY);
                poolZ = (float) Math.floor(centerZ);
                lastTimeExecuted = world.getGameTime(); // TODO: Check if entity can go to this pool
                this.foundPool = true;
            }
        }
    }

    public void tick() {
        if (foundPool) {
            PathNavigation navigator = creature.getNavigation();
            Path path = navigator.createPath(new BlockPos((int) poolX, (int) poolY, (int) poolZ), 1);

            if (path != null)
                navigator.moveTo(path, speed);

            lastTimeExecuted = world.getGameTime();
        }
    }

    @Override
    public boolean isInterruptable() {
        return true;
    }

    private boolean isNextTo(Vec3 a, Vec3 b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        double dz = a.z - b.z;
        return (dx + dy + dz) == 1f;
    }

    public void stop() {
        super.stop();

        this.foundPool = false;

        this.poolX = 0;
        this.poolY = -64;
        this.poolZ = 0;

        lastTimeExecuted = world.getGameTime();
    }

    public boolean canUse() {
        return world.getGameTime() - lastTimeExecuted >= maxTime && Math.random() < 0.10;
    }
}
