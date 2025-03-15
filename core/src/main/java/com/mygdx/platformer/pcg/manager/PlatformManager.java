package com.mygdx.platformer.pcg.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.platformer.EnemyManager;
import com.mygdx.platformer.difficulty.GameDifficultyManager;
import com.mygdx.platformer.difficulty.observer.GameDifficultyObserver;
import com.mygdx.platformer.pcg.Platform;
import com.mygdx.platformer.pcg.factory.PlatformGeneratorFactory;
import com.mygdx.platformer.pcg.generators.IPlatformGenerator;
import com.mygdx.platformer.utilities.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Manages platform generation and lifecycle.
 * This class is responsible for creating, updating, rendering, and disposing
 * platforms.
 * It uses an IPlatformGenerator to generate new platforms.
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class PlatformManager implements GameDifficultyObserver {
    private List<Platform> platforms;
    private Map<AppConfig.PlatformGeneratorType, IPlatformGenerator> generators;
    private IPlatformGenerator currentGenerator;
    private AppConfig.PlatformGeneratorType currentGeneratorType;
    private Random random = new Random();
    private EnemyManager enemyManager;

    private World world;
    private float lastPlatformX; // right edge of the last platform
    private float rightOffscreenMargin = AppConfig.RIGHT_OFFSCREEN_MARGIN;

    private float minGap;
    private float maxGap;
    private float minWidth;
    private float maxWidth;
    private float maxYvariation;
    private float spawnProbability;
    private float minYPosition;
    private float maxYPosition;

    // Difficulty-based variables
    private float difficultyGapMultiplier = 1.0f;
    private float difficultyWidthMultiplier = 1.0f;
    private float difficultyYvariationMultiplier = 1.0f;
    private float difficultySpawnProbabilityMultiplier = 1.0f;

    /**
     * Constructs a new PlatformManager with the specified world and enemy manager.
     *
     * @param world        The Box2D physics world
     * @param enemyManager The enemy manager for spawning enemies on platforms
     */
    public PlatformManager(World world, EnemyManager enemyManager) {
        this.world = world;
        this.platforms = new ArrayList<>();
        this.generators = new HashMap<>();
        this.enemyManager = enemyManager;

        initializePlatformParameters();

        for (AppConfig.PlatformGeneratorType type : PlatformGeneratorFactory.getAvailableGeneratorTypes()) {
            IPlatformGenerator generator = PlatformGeneratorFactory.createGenerator(type);
            if (generator != null) {
                registerGenerator(generator);
            }
        }

        setCurrentGenerator(AppConfig.PlatformGeneratorType.STANDARD);

        Platform initialPlatform = currentGenerator.initialize(world);
        platforms.add(initialPlatform);
        lastPlatformX = initialPlatform.getBody().getPosition().x + initialPlatform.getWidth() / 2;
        GameDifficultyManager.getInstance().registerObserver(this);
    }

    /**
     * Initializes platform generation parameters with default values from
     * AppConfig.
     */
    private void initializePlatformParameters() {
        minGap = AppConfig.MIN_GAP;
        maxGap = AppConfig.MAX_GAP;
        minWidth = AppConfig.MIN_WIDTH;
        maxWidth = AppConfig.MAX_WIDTH;
        maxYvariation = AppConfig.INITIAL_MAX_Y_VARIATION;
        spawnProbability = AppConfig.BASE_SPAWN_PROBABILITY;
        minYPosition = AppConfig.PLATFORM_MIN_Y_POSITION;
        maxYPosition = AppConfig.PLATFORM_MAX_Y_POSITION;
    }

    /**
     * Registers a new platform generator.
     *
     * @param generator The platform generator to register
     */
    public void registerGenerator(IPlatformGenerator generator) {
        generators.put(generator.getGeneratorType(), generator);
    }

    /**
     * Sets the current platform generator by type.
     *
     * @param generatorType The type of generator to use
     * @return true if the generator was found and set, false otherwise
     */
    public boolean setCurrentGenerator(AppConfig.PlatformGeneratorType generatorType) {
        IPlatformGenerator generator = generators.get(generatorType);
        if (generator != null) {
            currentGenerator = generator;
            currentGeneratorType = generatorType;
            return true;
        }
        return false;
    }

    /**
     * Gets the current generator type.
     *
     * @return The current generator type
     */
    public AppConfig.PlatformGeneratorType getCurrentGeneratorType() {
        return currentGeneratorType;
    }

    /**
     * Gets an array of all registered generator types.
     *
     * @return An array of all registered generator types
     */
    public String[] getAvailableGeneratorTypes() {
        return generators.keySet().toArray(new String[0]);
    }

    /**
     * Updates the platform system, generating new platforms and removing old ones.
     *
     * @param cameraX       The x-coordinate of the camera
     * @param viewportWidth The width of the viewport
     */
    public void update(float cameraX, float viewportWidth) {

        while (lastPlatformX < cameraX + viewportWidth / 2 + rightOffscreenMargin) {
            long inputTime = TimeUtils.nanoTime();
            float newBaseY = platforms.getLast().getBody().getPosition().y;
            Platform newPlatform = generatePlatform(lastPlatformX, newBaseY);
            platforms.add(newPlatform);
            lastPlatformX = newPlatform.getBody().getPosition().x + newPlatform.getWidth() / 2;
            float resultTime = (float) (TimeUtils.nanoTime() - inputTime) / 1000000;
            System.out.println("[Test] platform generation time: " + resultTime + " ms");
        }

        Iterator<Platform> iter = platforms.iterator();
        while (iter.hasNext()) {
            Platform platform = iter.next();
            float platformRightEdge = platform.getBody().getPosition().x + platform.getWidth() / 2;
            if (platformRightEdge < cameraX - viewportWidth / 2) {
                world.destroyBody(platform.getBody());
                platform.dispose();
                iter.remove();
            }
        }
    }

    /**
     * Generates a new platform based on current parameters.
     *
     * @param lastPlatformX The x-coordinate of the right edge of the last platform
     * @param baseY         The base y-coordinate for platform generation
     * @return The newly generated platform
     */
    private Platform generatePlatform(float lastPlatformX, float baseY) {
        float currentMinGap = minGap * difficultyGapMultiplier;
        float currentMaxGap = maxGap * difficultyGapMultiplier;
        float currentMinWidth = minWidth * difficultyWidthMultiplier;
        float currentMaxWidth = maxWidth * difficultyWidthMultiplier;
        float currentMaxYVariation = maxYvariation * difficultyYvariationMultiplier;
        float currentSpawnProbability = spawnProbability * difficultySpawnProbabilityMultiplier;

        float normalizedHeight = (baseY - minYPosition) / (maxYPosition - minYPosition);
        float bias = 1.0f - normalizedHeight;

        float yVariation;
        if (random.nextFloat() < bias) {
            yVariation = random.nextFloat() * currentMaxYVariation;
        } else {
            yVariation = -random.nextFloat() * currentMaxYVariation;
        }

        float absYVariation = Math.abs(yVariation);


        float minRequiredGap = absYVariation;

        float adjustedMinGap = Math.max(currentMinGap, minRequiredGap);

        float gap;
        if (adjustedMinGap >= currentMaxGap) {
            // If the required minimum gap exceeds the maximum, use the minimum
            gap = adjustedMinGap;
        } else {
            // Otherwise, generate a random gap between the adjusted minimum and maximum
            gap = adjustedMinGap + random.nextFloat() * (currentMaxGap - adjustedMinGap);
        }

        float width = currentMinWidth + random.nextFloat() * (currentMaxWidth - currentMinWidth);
        float newX = lastPlatformX + gap + width / 2;
        float newY = baseY + yVariation;

        newY = Math.max(minYPosition, Math.min(AppConfig.FINAL_MAX_Y_VARIATION, newY));

        Platform newPlatform = currentGenerator.generatePlatform(newX, newY, width);

        if (random.nextFloat() < currentSpawnProbability) {
            Vector2 enemySpawnPos = new Vector2(newX, newY + AppConfig.ENEMY_SPAWN_HEIGHT);
            enemyManager.spawnEnemyAt(enemySpawnPos);
        }

        return newPlatform;
    }

    /**
     * Renders all platforms.
     *
     * @param batch The SpriteBatch to use for rendering
     */
    public void render(SpriteBatch batch) {
        for (Platform platform : platforms) {
            platform.render(batch);
        }
    }

    /**
     * Disposes of all platforms and resources.
     */
    public void dispose() {
        for (Platform platform : platforms) {
            platform.dispose();
        }
        platforms.clear();
    }

    /**
     * Gets the list of active platforms.
     *
     * @return The list of active platforms
     */
    public List<Platform> getPlatforms() {
        return platforms;
    }

    @Override
    public void onDifficultyChanged(int difficultyLevel) {

        float gapIncrease = 1.0f + (difficultyLevel * AppConfig.DIFFICULTY_INCREASE_AMOUNT);
        float widthDecrease = 1.0f - (difficultyLevel * AppConfig.DIFFICULTY_INCREASE_AMOUNT);
        float yVariationIncrease = 1.0f + (difficultyLevel * AppConfig.DIFFICULTY_INCREASE_AMOUNT);
        float spawnRateIncrease = 1.0f + (difficultyLevel * AppConfig.DIFFICULTY_INCREASE_AMOUNT);

        difficultyGapMultiplier = gapIncrease;
        difficultyWidthMultiplier = Math.max(AppConfig.MIN_PLATFORM_WIDTH_MULTIPLIER, widthDecrease);
        difficultyYvariationMultiplier = yVariationIncrease;
        difficultySpawnProbabilityMultiplier = spawnRateIncrease;
    }
}
