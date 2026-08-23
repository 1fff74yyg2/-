package net.ilexiconn.jurassicraft.common.entity.ai;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * A overwritten version of {@link PathNavigation} that performs validity checks before using a path.
 */
public class JCPathNavigate extends GroundPathNavigation {
    /**
     * If this search range is <code>> 0</code>, then {@link JCPathNavigate#getPathSearchRange()} returns it. Otherwise, the default search range is used.
     */
    private float masterSearchRange;
    private List<IPathValidator> validators;
    /**
     * The search range used when no master search range has been set.
     */
    private float defaultSearchRange = 16.0F;

    public JCPathNavigate(Mob entity, Level world) {
        super(entity, world);
        masterSearchRange = -1f;
        validators = Lists.newArrayList();
    }

    /**
     * Sets the master search range. If the given range is <code><= 0</code>, then the default search range is used.
     *
     * @param range The max value to which to search pathes.
     * @see {@link JCPathNavigate#masterSearchRange}
     */
    public void setMasterSearchRange(float range) {
        this.masterSearchRange = range;
    }

    public float getPathSearchRange() {
        if (masterSearchRange <= 0f) {
            return defaultSearchRange;
        }

        return masterSearchRange;
    }

    /**
     * The change from {@link PathNavigation#moveTo(Path, double)} is that this Navigator checks if the path is valid (aka. the Entity is able to perform it)
     */
    public boolean setPath(Path path, double speed) {
        if (isValid(path, speed)) {
            return super.moveTo(path, speed);
        }

        return false;
    }

    /**
     * The list of validators
     *
     * @return The list of validators used by this Navigator
     * @see {@link IPathValidator}
     */
    public List<IPathValidator> getValidators() {
        return validators;
    }

    /**
     * Adds a new validator used to check the validity of a path
     *
     * @param validator The validator to add to the validators used to check pathes
     * @see {@link IPathValidator}
     */
    public void addValidator(IPathValidator validator) {
        this.validators.add(validator);
    }

    /**
     * Removes the given validator from {@link JCPathNavigate#getValidators()}
     *
     * @param validator The validator to remove
     * @see {@link IPathValidator}
     */
    public void removeValidator(IPathValidator validator) {
        this.validators.remove(validator);
    }

    /**
     * Checks if the given path is valid for this given Navigator
     *
     * @param path The path to follow
     * @param speed The speed at which the path will be executed
     * @return A boolean equal to <code>true</code> if the path is considered valid by all validators.
     * @see {@link IPathValidator#validatePath(PathNavigation, Path, double)}
     */
    public boolean isValid(Path path, double speed) {
        boolean valid = true;

        for (IPathValidator validator : validators) {
            valid = valid && validator.validatePath(this, path, speed);
        }

        return valid;
    }

}
