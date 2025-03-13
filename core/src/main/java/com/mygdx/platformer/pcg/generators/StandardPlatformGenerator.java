package com.mygdx.platformer.pcg.generators;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.EnemyManager;
import com.mygdx.platformer.pcg.Platform;
import com.mygdx.platformer.utilities.AppConfig;

import java.util.Random;

/**
 * Standard implementation of the IPlatformGenerator interface.
 * Generates platforms with random gaps, widths, and height variations.
 * 
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class StandardPlatformGenerator implements IPlatformGenerator {
    private World world;
    private EnemyManager enemyManager;
    private Random random = new Random();

    private float minGap = AppConfig.MIN_GAP;
    private float maxGap = AppConfig.MAX_GAP;
    private float minWidth = AppConfig.MIN_WIDTH;
    private float maxWidth = AppConfig.MAX_WIDTH;
    private float platformHeight = AppConfig.PLATFORM_HEIGHT;
    private float maxYvariation = AppConfig.MAX_Y_VARIATION;
    private float spawnProbability = AppConfig.BASE_SPAWN_PROBABILITY;

    @Override
    public Platform initialize(World world, EnemyManager enemyManager) {
        this.world = world;
        this.enemyManager = enemyManager;

        float initialWidth = AppConfig.FIRST_PLATFORM_WIDTH;
        float initialX = AppConfig.FIRST_PLATFORM_X;
        float initialY = AppConfig.PLATFORM_BASE_Y_POSITION;

        return new Platform(world, initialX, initialY, initialWidth, platformHeight);
    }

    @Override
    public Platform generatePlatform(float lastPlatformX, float baseY) {
        float gap = minGap + (float) Math.random() * (maxGap - minGap);
        float width = minWidth + (float) Math.round(Math.random() * (maxWidth - minWidth));
        float newX = lastPlatformX + gap + width / 2;
        float newY = baseY + ((float) Math.random() * 2 * maxYvariation - maxYvariation);

        Platform newPlatform = new Platform(world, newX, newY, width, platformHeight);

        if (random.nextFloat() < spawnProbability) {
            Vector2 enemySpawnPos = new Vector2(newX, newY + AppConfig.ENEMY_SPAWN_HEIGHT);
            enemyManager.spawnEnemyAt(enemySpawnPos);
        }

        return newPlatform;
    }

    @Override
    public AppConfig.PlatformGeneratorType getGeneratorType() {
        return AppConfig.PlatformGeneratorType.STANDARD;
    }
}
