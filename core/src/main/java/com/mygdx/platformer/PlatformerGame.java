package com.mygdx.platformer;

import com.badlogic.gdx.Game;
import com.mygdx.platformer.screens.StartScreen;
import com.mygdx.platformer.utilities.Assets;

/**
 * The main game class that extends {@link Game}, acting as the entry point
 * for the platformer game. This class is responsible for initializing game
 * assets and setting the initial screen. It also ensures that game resources
 * are properly disposed of when the application closes. The {@code
 * PlatformerGame} class is the starting point of the game and is shared
 * across all platforms.
 *
 * @author Robert Kullman, Daniel Jönsson
 */
public class PlatformerGame extends Game {

    /**
     * Entry point of application.
     */
    @Override
    public void create() {
        Assets.load();
        setScreen(new StartScreen(this));
    }

    /**
     * Disposes of game resources when application is closing. This method
     * ensures that all loaded assets are properly released to free up memory.
     */
    @Override
    public void dispose() {
         super.dispose();
         Assets.dispose();
    }
}
