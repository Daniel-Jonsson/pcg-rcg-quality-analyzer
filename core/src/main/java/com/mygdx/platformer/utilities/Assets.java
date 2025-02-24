package com.mygdx.platformer.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public final class Assets {

    private Assets() { }  // private constructor to prevent instantiation

    public static final AssetManager assetManager = new AssetManager();

    public static final String PLAYER_TEXTURE = "textures/player.png";
    public static final String PLATFORM_TEXTURE = "textures/platform.png";

    public static void load() {
        assetManager.load(PLAYER_TEXTURE, Texture.class);
        assetManager.load(PLATFORM_TEXTURE, Texture.class);
        assetManager.update();
        assetManager.finishLoading();  // block to make sure the manager has finished loading
    }

    public static void dispose() {
        assetManager.dispose();
    }
}
