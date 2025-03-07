package com.mygdx.platformer.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Utility class for managing game assets. This class provides a centralized
 * way to load, retrieve, and dispose of textures and texture atlases using
 * {@link AssetManager}.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public final class Assets {

    private Assets() { }  // Private constructor to prevent instantiation

    /** The asset manager responsible for loading and managing game assets. */
    public static final AssetManager assetManager = new AssetManager();

    /** File path for the player texture. */
    public static final String PLAYER_TEXTURE = "textures/player.png";

    public static final String HEALTHBAR_TEXTURE = "textures/healthbar.png";

    /** File path for the throwing dagger texture. */
    public static final String THROWING_DAGGER_TEXTURE = "textures/throwing_dagger.png";

    /** File path for the goblin idle texture. */
    public static final String GOBLIN_IDLE = "textures/goblin_idle/goblin_idle.png";

    /** File path for the platform texture. */
    public static final String PLATFORM_TEXTURE = "textures/platform.png";

    /** File path for the start segment of a platform. */
    public static final String PLATFORM_START = "textures/platform/platform_start.png";

    /** File path for the end segment of a platform. */
    public static final String PLATFORM_END = "textures/platform/platform_end.png";

    /** File path for the middle segment of a platform. */
    public static final String PLATFORM_MIDDLE = "textures/platform/platform_middle.png";

    /** File path for the player sprite atlas. */
    public static final String PLAYER_ATLAS = "atlas/player_sprites.atlas";

    /** File path for the goblin sprite atlas. */
    public static final String GOBLIN_ATLAS = "atlas/goblin_sprites.atlas";

    /** File path for the necromancer sprite atlas. */
    public static final String NECROMANCER_ATLAS = "atlas/necromancer_sprites.atlas";

    public static final String DEATH_BOLT = "textures/projectiles/death_bolt.png";
    /**
     * Load all game assets using asset manager. This method enqueues assets
     * for loading and blocks until they are fully loaded.
     */
    public static void load() {
        assetManager.load(PLAYER_ATLAS, TextureAtlas.class);
        assetManager.load(GOBLIN_ATLAS, TextureAtlas.class);
        assetManager.load(NECROMANCER_ATLAS, TextureAtlas.class);

        assetManager.load(PLAYER_TEXTURE, Texture.class);
        assetManager.load(THROWING_DAGGER_TEXTURE, Texture.class);
        assetManager.load(GOBLIN_IDLE, Texture.class);
        assetManager.load(PLATFORM_TEXTURE, Texture.class);
        assetManager.load(DEATH_BOLT, Texture.class);

        assetManager.load(PLATFORM_START, Texture.class);
        assetManager.load(PLATFORM_END, Texture.class);
        assetManager.load(PLATFORM_MIDDLE, Texture.class);

        assetManager.update();
        assetManager.finishLoading();  // Block to ensure assets are loaded
    }

    /**
     * Retrieves the player sprite atlas.
     *
     * @return The {@link TextureAtlas} containing player sprites.
     */
    public static TextureAtlas getPlayerAtlas() {
        return assetManager.get(PLAYER_ATLAS, TextureAtlas.class);
    }

    /**
     * Retrieves the goblin sprite atlas.
     *
     * @return The {@link TextureAtlas} containing goblin sprites.
     */
    public static TextureAtlas getGoblinAtlas() {
        return assetManager.get(GOBLIN_ATLAS, TextureAtlas.class);
    }

    /**
     * Retrieves the necromancer sprite atlas.
     *
     * @return The {@link TextureAtlas} containing necromancer sprites.
     */
    public static TextureAtlas getNecromancerAtlas() {
        return assetManager.get(NECROMANCER_ATLAS, TextureAtlas.class);
    }

    /**
     * Disposes of all assets managed by the asset manager.
     * This method should be called when the game is shutting down to free
     * resources.
     */
    public static void dispose() {
        assetManager.dispose();
    }
}
