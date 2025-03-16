package com.mygdx.platformer.pcg.factory;

import com.mygdx.platformer.pcg.generators.IPlatformGenerator;
import com.mygdx.platformer.pcg.generators.StandardPlatformGenerator;
import com.mygdx.platformer.utilities.AppConfig;

/**
 * Factory class for creating platform generators.
 * This class provides a centralized way to create different types of platform
 * generators.
 *
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class PlatformGeneratorFactory {

    /**
     * Creates a platform generator of the specified type.
     *
     * @param type The type of platform generator to create
     * @return A new instance of the requested platform generator, or null if the
     *         type is not recognized
     */
    public static IPlatformGenerator createGenerator(AppConfig.PlatformGeneratorType type) {
        // This can be extended to include more types of generators in the future.
        return switch (type) {
            case STANDARD -> new StandardPlatformGenerator();
        };
    }

    /**
     * Gets an array of all available generator types.
     *
     * @return An array of all available generator types
     */
    public static AppConfig.PlatformGeneratorType[] getAvailableGeneratorTypes() {
        return new AppConfig.PlatformGeneratorType[] {
                AppConfig.PlatformGeneratorType.STANDARD,
        };
    }
}
