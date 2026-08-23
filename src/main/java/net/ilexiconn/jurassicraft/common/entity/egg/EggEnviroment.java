package net.ilexiconn.jurassicraft.common.entity.egg;

import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public enum EggEnviroment {
    WET, COLD, WARM, OVERHEAT;

    public static List<EggEnviroment> getEnviroments(Entity egg) {
        List<EggEnviroment> enviroments = new ArrayList<EggEnviroment>();

        BlockPos eggPos = new BlockPos((int) egg.getX(), (int) egg.getY(), (int) egg.getZ());
        int light = egg.level().getLightEngine().getRawBrightness(eggPos, 0);

        boolean warm = light > 6;
        boolean overheat = light > 10;

        if (warm)
            enviroments.add(WARM);

        if (overheat)
            enviroments.add(OVERHEAT);

        if (egg.isInWaterRainOrBubble())
            enviroments.add(WET);

        if (!warm)
            enviroments.add(COLD);

        return enviroments;
    }
}
