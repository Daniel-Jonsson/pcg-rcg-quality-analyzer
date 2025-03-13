package com.mygdx.platformer.pcg.generators;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.EnemyManager;
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
    Platform initialize(World world, EnemyManager enemyManager); 
    Platform generatePlatform(float lastPlatformX, float baseY);
    AppConfig.PlatformGeneratorType getGeneratorType();
}
