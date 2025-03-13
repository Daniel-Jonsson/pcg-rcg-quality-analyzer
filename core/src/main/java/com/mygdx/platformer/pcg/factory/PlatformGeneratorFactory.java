package com.mygdx.platformer.pcg.factory;

import com.mygdx.platformer.pcg.generators.IPlatformGenerator;
import com.mygdx.platformer.pcg.generators.StandardPlatformGenerator;

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
    public static IPlatformGenerator createGenerator(String type) {
        // This can be extended to include more types of generators in the future.
        switch (type.toLowerCase()) {
            case "standard":
                return new StandardPlatformGenerator();
            default:
                return new StandardPlatformGenerator();
        }
    }

    /**
     * Gets an array of all available generator types.
     * 
     * @return An array of all available generator types
     */
    public static String[] getAvailableGeneratorTypes() {
        return new String[] {
                "Standard",
        };
    }
}