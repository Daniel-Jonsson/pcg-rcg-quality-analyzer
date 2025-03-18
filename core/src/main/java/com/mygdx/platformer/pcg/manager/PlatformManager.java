package com.mygdx.platformer.pcg.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.characters.enemies.EnemyManager;
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
 * Manages procedural platform generation and lifecycle in the game world.
 * <p>
 * This class is responsible for the complete lifecycle of platforms:
 * <ul>
 * <li>Creating platforms using different generator strategies</li>
 * <li>Dynamically generating new platforms as the player progresses</li>
 * <li>Adjusting platform parameters based on difficulty level</li>
 * <li>Removing platforms that are no longer visible</li>
 * <li>Coordinating with the enemy manager to spawn enemies on platforms</li>
 * </ul>
 * </p>
 * <p>
 * The platform generation uses a procedural approach with parameters that
 * control:
 * <ul>
 * <li>Platform width and gap distances</li>
 * <li>Vertical variation between platforms</li>
 * <li>Enemy spawn probability</li>
 * </ul>
 * </p>
 * <p>
 * As the game's difficulty increases, these parameters are adjusted to create
 * more challenging level layouts with wider gaps, narrower platforms, greater
 * height variations, and more enemies.
 * </p>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class PlatformManager {
    /** List of all active platforms in the game world. */
    private final List<Platform> platforms;

    /** Map of available platform generators indexed by their type. */
    private final Map<AppConfig.PlatformGeneratorType, IPlatformGenerator> generators;

    /** The currently active platform generator. */
    private IPlatformGenerator currentGenerator;

    /** The type of the currently active platform generator. */
    private AppConfig.PlatformGeneratorType currentGeneratorType;

    /** Random number generator for procedural generation. */
    private final Random random = new Random();

    /** Reference to the enemy manager for spawning enemies on platforms. */
    private final EnemyManager enemyManager;

    /** Reference to the Box2D physics world. */
    private final World world;

    /** X-coordinate of the right edge of the last generated platform. */
    private float lastPlatformX;

    /** Minimum gap distance between platforms. */
    private float minGap;

    /** Maximum gap distance between platforms. */
    private float maxGap;

    /** Minimum width of generated platforms. */
    private float minWidth;

    /** Maximum width of generated platforms. */
    private float maxWidth;

    /** Maximum vertical variation between adjacent platforms. */
    private float maxYvariation;

    /** Base probability of spawning an enemy on a platform. */
    private float spawnProbability;

    /** Minimum allowed Y position for platforms. */
    private float minYPosition;

    /** Maximum allowed Y position for platforms. */
    private float maxYPosition;

    // Difficulty-based variables
    /** Multiplier for gap size that increases with difficulty. */
    private float difficultyGapMultiplier = 1.0f;

    /** Multiplier for platform width that decreases with difficulty. */
    private float difficultyWidthMultiplier = 1.0f;

    /** Multiplier for vertical variation that increases with difficulty. */
    private float difficultyYvariationMultiplier = 1.0f;

    /** Multiplier for enemy spawn probability that increases with difficulty. */
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
            registerGenerator(generator);
        }

        setCurrentGenerator(AppConfig.PlatformGeneratorType.STANDARD);

        Platform initialPlatform = currentGenerator.initialize(world);
        platforms.add(initialPlatform);
        lastPlatformX = initialPlatform.getBody().getPosition().x + initialPlatform.getWidth() / 2;
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
     */
    public void setCurrentGenerator(AppConfig.PlatformGeneratorType generatorType) {
        IPlatformGenerator generator = generators.get(generatorType);
        if (generator != null) {
            currentGenerator = generator;
            currentGeneratorType = generatorType;
        }
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
     * @r
     * 
     *    return generators.keySet().toArray(new
     *    AppConfig.PlatformGeneratorType[0]);
     *    }
     * 
     *    /**
     *    Updates the platform system, generating new platforms and removing old
     *    ones.
     *
     * @param cameraX       The x-coordinate of the camera
     * @param viewportWidth The width of the viewport
     */
    public void update(float cameraX, float viewportWidth) {
        float rightOffscreenMargin = AppConfig.RIGHT_OFFSCREEN_MARGIN;
        while (lastPlatformX < cameraX + viewportWidth / 2 + rightOffscreenMargin) {
            float newBaseY = platforms.getLast().getBody().getPosition().y;
            Platform newPlatform = generatePlatform(lastPlatformX, newBaseY);
            platforms.add(newPlatform);
            lastPlatformX = newPlatform.getBody().getPosition().x + newPlatform.getWidth() / 2;
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

        float minRequiredGap = Math.abs(yVariation);

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

    /**
     * Adjusts platform generation parameters based on the current difficulty level.
     * <p>
     * As the difficulty increases:
     * <ul>
     * <li>Gaps between platforms become wider</li>
     * <li>Platforms become narrower</li>
     * <li>Vertical variation between platforms increases</li>
     * <li>Enemy spawn probability increases</li>
     * </ul>
     * </p>
     * <p>
     * This method is typically called when the game's difficulty level changes,
     * making the platforming challenges progressively harder as the player
     * advances.
     * </p>
     *
     * @param difficultyLevel The current difficulty level of the game
     */
    public void increaseDifficulty(int difficultyLevel) {
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
