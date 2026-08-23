package net.ilexiconn.jurassicraft.common.entity.dinosaurs;

import net.ilexiconn.jurassicraft.common.entity.EntityJurassiCraftRidable;
import net.ilexiconn.jurassicraft.common.entity.ai.JurassiCraftAIWander;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class EntitySanta extends EntityJurassiCraftRidable {
    public EntitySanta(EntityType<? extends EntitySanta> type, Level world) {
        super(type, world);
    }

    public EntitySanta(Level world) {
        super(world);
    }

    @Override
    protected void registerAI() {
    this.goalSelector.addGoal(0, new FloatGoal(this));
            this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F * this.getCreatureSpeed(), false));
            this.goalSelector.addGoal(3, new JurassiCraftAIWander(this, 40, 0.8D * this.getCreatureSpeed()));
            this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 16.0F));
            this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    
            this.setCreatureExperiencePoints(1000);
    }

    public int getTalkInterval() {
        return 350;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        int i = Mth.floor(this.getX());
        int j = Mth.floor(this.getY());
        int k = Mth.floor(this.getZ());

        for (int l = 0; l < 4; ++l) {
            i = Mth.floor(this.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
            j = Mth.floor(this.getY());
            k = Mth.floor(this.getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));

            BlockState state = this.level().getBlockState(new BlockPos(i, j, k));

            if ((state.isAir() || state.is(Blocks.GRASS_BLOCK)) && Blocks.SNOW.defaultBlockState().canSurvive(this.level(), new BlockPos(i, j, k))) {
                this.level().setBlockAndUpdate(new BlockPos(i, j, k), Blocks.SNOW.defaultBlockState());
                this.level().setBlockAndUpdate(new BlockPos(i + 1, j, k), Blocks.SNOW.defaultBlockState());
                this.level().setBlockAndUpdate(new BlockPos(i + 1, j, k + 1), Blocks.SNOW.defaultBlockState());
                this.level().setBlockAndUpdate(new BlockPos(i + 1, j, k - 1), Blocks.SNOW.defaultBlockState());
                this.level().setBlockAndUpdate(new BlockPos(i, j, k - 1), Blocks.SNOW.defaultBlockState());
                this.level().setBlockAndUpdate(new BlockPos(i, j, k + 1), Blocks.SNOW.defaultBlockState());
                this.level().setBlockAndUpdate(new BlockPos(i - 1, j, k + 1), Blocks.SNOW.defaultBlockState());
                this.level().setBlockAndUpdate(new BlockPos(i - 1, j, k - 1), Blocks.SNOW.defaultBlockState());
                this.level().setBlockAndUpdate(new BlockPos(i - 1, j, k), Blocks.SNOW.defaultBlockState());
            }
        }
    }
}
