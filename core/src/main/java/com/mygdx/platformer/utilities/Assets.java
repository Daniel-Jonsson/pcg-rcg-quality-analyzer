package com.mygdx.platformer.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public final class Assets {

    private Assets() { }  // private constructor to prevent instantiation

    public static final AssetManager assetManager = new AssetManager();

    public static final String PLAYER_TEXTURE = "textures/player.png";
    public static final String PLATFORM_TEXTURE = "textures/platform.png";
    public static final String THROWING_DAGGER_TEXTURE = "textures/throwing_dagger.png";
    public static final String GOBLIN_IDLE = "textures/goblin_idle/goblin_idle.png";
    public static final String NECROMANCER_1 = "textures/necromancer_idle_and_walk/necromancer1.png";

    public static void load() {
        assetManager.load(PLAYER_TEXTURE, Texture.class);
        assetManager.load(PLATFORM_TEXTURE, Texture.class);
        assetManager.load(THROWING_DAGGER_TEXTURE, Texture.class);
        assetManager.load(GOBLIN_IDLE, Texture.class);
        assetManager.load(NECROMANCER_1, Texture.class);



        assetManager.update();
        assetManager.finishLoading();  // block to make sure the manager has finished loading
    }

    public static void dispose() {
        assetManager.dispose();
    }
}
