package com.mygdx.platformer.pcg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public void render(SpriteBatch batch) {
        for (Platform platform : platforms) {
            platform.render(batch);
        }
    }

    public void dispose() {
        for (Platform platform : platforms) {
            platform.dispose();
        }
        platforms.clear();
    }
}
