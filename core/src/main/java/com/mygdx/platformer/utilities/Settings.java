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

    private static final String MUSIC_VOLUME_KEY = "musicVolume";
    private static final float DEFAULT_MUSIC_VOLUME = 0.3f;

    private static final String EFFECTS_VOLUME_KEY = "effectsVolume";
    private static final float DEFAULT_EFFECTS_VOLUME = 0.5f;

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

    /**
     * Saves the show FPS status.
     * @param showFPS The boolean setting indicating whether FPS should be shown in the UI.
     */
    public static void saveShowFPS(boolean showFPS) {
        preferences.putBoolean(SHOW_FPS_KEY, showFPS);
        preferences.flush();
    }

    /**
     * Saves the music volume to the settings.
     * @param musicVolume The new music volume.
     */
    public static void saveMusicVolume(float musicVolume) {
        preferences.putFloat(MUSIC_VOLUME_KEY, musicVolume);
        preferences.flush();
    }

    /**
     * Retrieves the music volume setting from settings.
     * @return Float value indicating the current music volume.
     */
    public static float getMusicVolume() {
        return preferences.getFloat(MUSIC_VOLUME_KEY, DEFAULT_MUSIC_VOLUME);
    }

    /**
     * Saves the effects volume to the settings.
     * @param effectsVolume The new effects volume.
     */
    public static void saveEffectsVolume(float effectsVolume) {
        preferences.putFloat(EFFECTS_VOLUME_KEY, effectsVolume);
        preferences.flush();
    }

    /**
     * Retrieves the effects volume from the settings.
     * @return Float indicating the current effects volume.
     */
    public static float getEffectsVolume() {
        return preferences.getFloat(EFFECTS_VOLUME_KEY, DEFAULT_EFFECTS_VOLUME);
    }

    /**
     * Retrieves the setting for whether FPS should be shown in the UI.
     * @return boolean flag for showing FPS.
     */
    public static boolean getShowFPS() {
        return preferences.getBoolean(SHOW_FPS_KEY, DEFAULT_SHOW_FPS);
    }
}
