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
    Platform initialize(World world, EnemyManager enemyManager);
    Platform generatePlatform(float lastPlatformX, float baseY);
    AppConfig.PlatformGeneratorType getGeneratorType();
}
