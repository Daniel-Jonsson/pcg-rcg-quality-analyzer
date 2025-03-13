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
    private World world;
    private float platformHeight = AppConfig.PLATFORM_HEIGHT;

    @Override
    public Platform initialize(World world) {
        this.world = world;

        float initialWidth = AppConfig.FIRST_PLATFORM_WIDTH;
        float initialX = AppConfig.FIRST_PLATFORM_X;
        float initialY = AppConfig.PLATFORM_BASE_Y_POSITION;

        return new Platform(world, initialX, initialY, initialWidth, platformHeight);
    }

    @Override
    public Platform generatePlatform(float x, float y, float width) {
        return new Platform(world, x, y, width, platformHeight);
    }

    @Override
    public AppConfig.PlatformGeneratorType getGeneratorType() {
        return AppConfig.PlatformGeneratorType.STANDARD;
    }
}
