package net.ilexiconn.jurassicraft.common.entity.ai;

import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

/**
 * An interface that checks if the given path is valid
 */
public interface IPathValidator {
    /**
     * Validates a path.
     *
     * @param navigator The navigator that created the path
     * @param path The path to follow
     * @param speed The speed at which to perform the path
     * @return A boolean equal to <code>true</code> if the path is valid, returns <code>false</code> otherwise.
     */
    boolean validatePath(PathNavigation navigator, Path path, double speed);
}
