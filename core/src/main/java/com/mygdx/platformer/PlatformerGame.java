package com.mygdx.platformer;

import com.badlogic.gdx.Game;
import com.mygdx.platformer.utilities.Assets;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class PlatformerGame extends Game {

    /**
     * Entry point of application.
     */
    @Override
    public void create() {
        Assets.load();
        setScreen(new StartScreen(this));
    }

    @Override
    public void dispose() {
         super.dispose();
         Assets.dispose();
    }
}
