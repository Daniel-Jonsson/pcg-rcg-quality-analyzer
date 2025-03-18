package com.mygdx.platformer.pcg.generators;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.pcg.Platform;
import com.mygdx.platformer.utilities.AppConfig;


/**
 * Standard implementation of the IPlatformGenerator interface.
 * Generates platforms with random gaps, widths, and height variations.
 *
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class StandardPlatformGenerator implements IPlatformGenerator {

    /** The Box2D world where {@link Platform} platforms should be generated. */
    private World world;

    /** The height of the platform. */
    private final float platformHeight = AppConfig.PLATFORM_HEIGHT;

    /**
     * {@inheritDoc}
     * @param world The Box2D world
     * @return The initial generated Platform
     */
    @Override
    public Platform initialize(World world) {
        this.world = world;

        float initialWidth = AppConfig.FIRST_PLATFORM_WIDTH;
        float initialX = AppConfig.FIRST_PLATFORM_X;
        float initialY = AppConfig.PLATFORM_BASE_Y_POSITION;

        return new Platform(world, initialX, initialY, initialWidth, platformHeight);
    }

    /**
     * {@inheritDoc}
     * @param x The x coordinate where the platform should be generated.
     * @param y The y coordinate where the platform should be generated.
     * @param width The width of the platform
     * @return A new {@link Platform} instance of the spawned platform
     */
    @Override
    public Platform generatePlatform(float x, float y, float width) {
        return new Platform(world, x, y, Math.round(width), platformHeight);
    }

    /**
     * {@inheritDoc}
     * @return The platform generator type.
     */
    @Override
    public AppConfig.PlatformGeneratorType getGeneratorType() {
        return AppConfig.PlatformGeneratorType.STANDARD;
    }
}
