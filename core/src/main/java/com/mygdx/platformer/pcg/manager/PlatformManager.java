package com.mygdx.platformer.pcg.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.EnemyManager;
import com.mygdx.platformer.pcg.Platform;
import com.mygdx.platformer.pcg.factory.PlatformGeneratorFactory;
import com.mygdx.platformer.pcg.generators.IPlatformGenerator;
import com.mygdx.platformer.utilities.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Manages platform generation and lifecycle.
 * This class is responsible for creating, updating, rendering, and disposing
 * platforms.
 * It uses an IPlatformGenerator to generate new platforms.
 * 
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class PlatformManager {
    private List<Platform> platforms;
    private Map<AppConfig.PlatformGeneratorType, IPlatformGenerator> generators;
    private IPlatformGenerator currentGenerator;
    private AppConfig.PlatformGeneratorType currentGeneratorType;

    private World world;
    private float lastPlatformX; // right edge of the last platform
    private float rightOffscreenMargin = AppConfig.RIGHT_OFFSCREEN_MARGIN;

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

        for (AppConfig.PlatformGeneratorType type : PlatformGeneratorFactory.getAvailableGeneratorTypes()) {
            IPlatformGenerator generator = PlatformGeneratorFactory.createGenerator(type);
            if (generator != null) {
                registerGenerator(generator);
            }
        }

        setCurrentGenerator(AppConfig.PlatformGeneratorType.STANDARD);

        Platform initialPlatform = currentGenerator.initialize(world, enemyManager);
        platforms.add(initialPlatform);
        lastPlatformX = initialPlatform.getBody().getPosition().x + initialPlatform.getWidth() / 2;
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
            float newBaseY = platforms.getLast().getBody().getPosition().y;
            Platform newPlatform = currentGenerator.generatePlatform(lastPlatformX, newBaseY);
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
}
