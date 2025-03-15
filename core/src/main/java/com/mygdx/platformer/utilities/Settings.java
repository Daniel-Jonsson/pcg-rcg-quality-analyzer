package com.mygdx.platformer.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * This class handles all the persistent settings in the game.
 *
 * @author Robert Kullman, Daniel Jönsson
 */
public class Settings {

    private static final String PREF_NAME = "com.mygdx.platformer.settings";
    private static final String UI_SCALE_KEY = "uiScale";
    private static final float DEFAULT_UI_SCALE = AppConfig.DEFAULT_UI_SCALE;

    private static final String SHOW_FPS_KEY = "showFPS";
    private static final boolean DEFAULT_SHOW_FPS = true;

    private static Preferences preferences;

    static {
        preferences = Gdx.app.getPreferences(PREF_NAME);
    }

    /**
     * Saves the UI scale to persistent storage.
     *
     * @param scale The UI scale value.
     */
    public static void saveUIScale(float scale) {
        preferences.putFloat(UI_SCALE_KEY, scale);
        preferences.flush();
    }

    /**
     * Retrieves the UI scale from persistent storage.
     *
     * @return The stored UI scale value, or the default if none set.
     */
    public static float getUIScale() {
        return preferences.getFloat(UI_SCALE_KEY, DEFAULT_UI_SCALE);
    }

    public static void saveShowFPS(boolean showFPS) {
        preferences.putBoolean(SHOW_FPS_KEY, showFPS);
        preferences.flush();
    }

    public static boolean getShowFPS() {
        return preferences.getBoolean(SHOW_FPS_KEY, DEFAULT_SHOW_FPS);
    }
}
