package com.mygdx.platformer.pcg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is responsible for procedurally generating the platforms in a level.
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class PlatformGenerator {
    private List<Platform> platforms;

    private World world;
    private float lastPlatformX; // right edge of the last platform

    private float minGap = AppConfig.MIN_GAP;
    private float maxGap = AppConfig.MAX_GAP;
    private float minWidth = AppConfig.MIN_WIDTH;
    private float maxWidth = AppConfig.MAX_WIDTH;
    private float platformHeight = AppConfig.PLATFORM_HEIGHT;
    private float baseY = AppConfig.BASE_Y;
    private float maxYvariation = AppConfig.MAX_Y_VARIATION;
    private float rightOffscreenMargin = AppConfig.RIGHT_OFFSCREEN_MARGIN;

    /**
     * Constructor for the PlatformGenerator class. This method initializes the game world instance reference,
     * and generates an initial starting platform.
     * @param gameWorld The Box2D physics world.
     */
    public PlatformGenerator(World gameWorld) {
        this.world = gameWorld;
        platforms = new ArrayList<>();

        float initialWidth = AppConfig.FIRST_PLATFORM_WIDTH;
        float initialX = AppConfig.FIRST_PLATFORM_X;
        float initialY = baseY;
        Platform initialPlatform = new Platform(gameWorld, initialX, initialY, initialWidth, platformHeight);
        platforms.add(initialPlatform);
        lastPlatformX = initialX + initialWidth / 2;
    }

    /**
     * Generates new platforms until the offscreen limit is reached.
     * @param cameraX x-axis position of the camera.
     * @param viewportWidth the width of the viewport (in world units).
     */
    public void update( float cameraX, float viewportWidth) {
        while (lastPlatformX < cameraX + viewportWidth / 2 + rightOffscreenMargin) {
            float gap = minGap + (float) Math.random() * (maxGap - minGap);
            float width = minWidth + (float) Math.random() * (maxWidth - minWidth);
            float newX = lastPlatformX + gap + width / 2;
            float newY = baseY + ((float) Math.random() * 2 * maxYvariation - maxYvariation);
            Platform newPlatform = new Platform(world, newX, newY, width, platformHeight);
            platforms.add(newPlatform);
            lastPlatformX = newX + width / 2;
        }

        // dispose platforms when they move offscreen.
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
     * Renders the platforms.
     * @param batch SpriteBatch for rendering.
     */
    public void render(SpriteBatch batch) {
        for (Platform platform : platforms) {
            platform.render(batch);
        }
    }

    /**
     * Frees resources by disposing of the textures for all currently active platforms.
     */
    public void dispose() {
        for (Platform platform : platforms) {
            platform.dispose();
        }
        platforms.clear();
    }
}
