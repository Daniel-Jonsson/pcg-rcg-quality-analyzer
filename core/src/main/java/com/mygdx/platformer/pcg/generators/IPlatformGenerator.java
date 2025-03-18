package com.mygdx.platformer.pcg.generators;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.pcg.Platform;
import com.mygdx.platformer.utilities.AppConfig;

/**
 * Interface for platform generators. Implementations of this interface
 * are responsible for generating platforms with different characteristics.
 *
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public interface IPlatformGenerator {
    // The return type is Platform for now but can be made more modular and allow
    // different types of platforms to be generated in the future.

    /**
     * Initializes the generator and generates the initial platform.
     * @param world The Box2D world
     * @return The initial generated Platform
     */
    Platform initialize(World world);

    /**
     * Generates a {@link Platform} at the given x and y position with the given
     * width.
     * @param x The x coordinate where the platform should be generated.
     * @param y The y coordinate where the platform should be generated.
     * @param width The width of the platform
     * @return A new {@link Platform} instance of the spawned platform
     */
    Platform generatePlatform(float x, float y, float width);

    /**
     * Gets the type of the platform generator.
     * @return The platform generator type.
     */
    AppConfig.PlatformGeneratorType getGeneratorType();
}
