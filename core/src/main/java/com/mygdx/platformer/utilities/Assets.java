package com.mygdx.platformer.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class Assets {

    private Assets() { }  // Private constructor to prevent instantiation

    public static final AssetManager assetManager = new AssetManager();


    public static final String PLAYER_TEXTURE = "textures/player.png";
    public static final String THROWING_DAGGER_TEXTURE = "textures/throwing_dagger.png";
    public static final String GOBLIN_IDLE = "textures/goblin_idle/goblin_idle.png";
    public static final String PLATFORM_TEXTURE = "textures/platform.png";

    public static final String PLAYER_ATLAS = "atlas/player_sprites.atlas";
    public static final String GOBLIN_ATLAS = "atlas/goblin_sprites.atlas";
    public static final String NECROMANCER_ATLAS = "atlas/necromancer_sprites.atlas";

    public static void load() {
        assetManager.load(PLAYER_ATLAS, TextureAtlas.class);
        assetManager.load(GOBLIN_ATLAS, TextureAtlas.class);
        assetManager.load(NECROMANCER_ATLAS, TextureAtlas.class);

        assetManager.load(PLAYER_TEXTURE, Texture.class);
        assetManager.load(THROWING_DAGGER_TEXTURE, Texture.class);
        assetManager.load(GOBLIN_IDLE, Texture.class);
        assetManager.load(PLATFORM_TEXTURE, Texture.class);

        assetManager.update();
        assetManager.finishLoading();  // Block to ensure assets are loaded
    }

    public static TextureAtlas getPlayerAtlas() {
        return assetManager.get(PLAYER_ATLAS, TextureAtlas.class);
    }
    public static TextureAtlas getGoblinAtlas() {
        return assetManager.get(GOBLIN_ATLAS, TextureAtlas.class);
    }

    public static TextureAtlas getNecromancerAtlas() {
        return assetManager.get(NECROMANCER_ATLAS, TextureAtlas.class);
    }

    public static void dispose() {
        assetManager.dispose();
    }
}
