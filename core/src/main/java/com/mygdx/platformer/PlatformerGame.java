package com.mygdx.platformer;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class PlatformerGame extends Game {

    /**
     * Entry point of application.
     */
    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
         super.dispose();
    }
}
