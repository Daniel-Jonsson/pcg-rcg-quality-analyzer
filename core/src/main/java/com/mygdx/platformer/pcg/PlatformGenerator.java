package com.mygdx.platformer.pcg;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class PlatformGenerator {
    private List<Platform> platforms;

    public PlatformGenerator(World world) {
        platforms = new ArrayList<>();

        Platform platform1 = new Platform(world, 6f, 2f, 4f, 0.5f);
        Platform platform2 = new Platform(world, 16f, 4f, 3f, 0.5f);

        platforms.add(platform1);
        platforms.add(platform2);
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
    }
}
