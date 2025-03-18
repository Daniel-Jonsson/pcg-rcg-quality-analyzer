package com.mygdx.platformer.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * A utility class for managing persistent game settings.
 * <p>
 * This class provides a centralized system for storing and retrieving user
 * preferences
 * that persist between game sessions. It handles:
 * <ul>
 * <li>UI scaling preferences for accessibility</li>
 * <li>Audio volume settings for music and sound effects</li>
 * <li>Display options such as FPS counter visibility</li>
 * </ul>
 * </p>
 * <p>
 * The class uses LibGDX's Preferences API to store settings in a
 * platform-specific
 * location appropriate for the current device. All methods are static for easy
 * access
 * throughout the codebase.
 * </p>
 * <p>
 * Usage example:
 * 
 * <pre>
 * // Get current UI scale
 * float scale = Settings.getUIScale();
 * 
 * // Save new UI scale
 * Settings.saveUIScale(1.5f);
 * 
 * // Check if FPS should be displayed
 * if (Settings.getShowFPS()) {
 *     renderFPSCounter();
 * }
 * </pre>
 * </p>
 *
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class Settings {

    /**
     * The name of the preferences file where settings are stored.
     * <p>
     * This identifier is used by LibGDX's Preferences system to create and access
     * the appropriate storage location for the game's settings.
     * </p>
     */
    private static final String PREF_NAME = AppConfig.SETTINGS_PREFERENCES_NAME;

    /**
     * The key used to store and retrieve the UI scale value.
     * <p>
     * This key identifies the UI scale setting in the preferences storage.
     * </p>
     */
    private static final String UI_SCALE_KEY = AppConfig.SETTINGS_UI_SCALE_KEY;

    /**
     * The default UI scale value used when no setting has been saved.
     * <p>
     * This value is retrieved from AppConfig to ensure consistency across the
     * application.
     * </p>
     */
    private static final float DEFAULT_UI_SCALE = AppConfig.DEFAULT_UI_SCALE;

    /**
     * The key used to store and retrieve the show FPS setting.
     * <p>
     * This key identifies the FPS display setting in the preferences storage.
     * </p>
     */
    private static final String SHOW_FPS_KEY = AppConfig.SETTINGS_SHOW_FPS_KEY;

    /**
     * The default value for showing FPS when no setting has been saved.
     * <p>
     * By default, the FPS counter is visible to help with performance monitoring.
     * </p>
     */
    private static final boolean DEFAULT_SHOW_FPS = AppConfig.SETTINGS_DEFAULT_SHOW_FPS;

    /**
     * The key used to store and retrieve the music volume setting.
     * <p>
     * This key identifies the music volume setting in the preferences storage.
     * </p>
     */
    private static final String MUSIC_VOLUME_KEY = AppConfig.SETTINGS_MUSIC_VOLUME_KEY;

    /**
     * The default music volume used when no setting has been saved.
     * <p>
     * This value ranges from 0.0 (silent) to 1.0 (full volume).
     * </p>
     */
    private static final float DEFAULT_MUSIC_VOLUME = AppConfig.SETTINGS_DEFAULT_MUSIC_VOLUME;

    /**
     * The key used to store and retrieve the sound effects volume setting.
     * <p>
     * This key identifies the effects volume setting in the preferences storage.
     * </p>
     */
    private static final String EFFECTS_VOLUME_KEY = AppConfig.SETTINGS_EFFECTS_VOLUME_KEY;

    /**
     * The default sound effects volume used when no setting has been saved.
     * <p>
     * This value ranges from 0.0 (silent) to 1.0 (full volume).
     * </p>
     */
    private static final float DEFAULT_EFFECTS_VOLUME = AppConfig.SETTINGS_DEFAULT_EFFECTS_VOLUME;

    /**
     * The preferences instance used to access the persistent storage.
     * <p>
     * This object is initialized in the static block and provides methods
     * for reading and writing preference values.
     * </p>
     */
    private static Preferences preferences;

    /**
     * Static initializer that creates the preferences instance.
     * <p>
     * This block runs when the class is first loaded, ensuring the preferences
     * are available before any methods are called.
     * </p>
     */
    static {
        preferences = Gdx.app.getPreferences(PREF_NAME);
    }

    /**
     * Saves the UI scale to persistent storage.
     * <p>
     * This method stores the provided scale value and immediately flushes
     * the preferences to ensure the change is persisted. The scale value
     * affects the size of UI elements throughout the game.
     * </p>
     *
     * @param scale The UI scale value to save, typically between 0.5 and 2.0
     */
    public static void saveUIScale(float scale) {
        preferences.putFloat(UI_SCALE_KEY, scale);
        preferences.flush();
    }

    /**
     * Retrieves the UI scale from persistent storage.
     * <p>
     * This method returns the stored UI scale value or the default value
     * if no setting has been saved. The scale value determines the size
     * of UI elements throughout the game.
     * </p>
     *
     * @return The stored UI scale value, or the default if none set
     */
    public static float getUIScale() {
        return preferences.getFloat(UI_SCALE_KEY, DEFAULT_UI_SCALE);
    }

    /**
     * Saves the show FPS status to persistent storage.
     * <p>
     * This method stores the provided boolean value and immediately flushes
     * the preferences to ensure the change is persisted. This setting determines
     * whether the frames-per-second counter is displayed during gameplay.
     * </p>
     * 
     * @param showFPS True to show the FPS counter, false to hide it
     */
    public static void saveShowFPS(boolean showFPS) {
        preferences.putBoolean(SHOW_FPS_KEY, showFPS);
        preferences.flush();
    }

    /**
     * Saves the music volume to persistent storage.
     * <p>
     * This method stores the provided volume value and immediately flushes
     * the preferences to ensure the change is persisted. The volume value
     * affects the loudness of background music in the game.
     * </p>
     * 
     * @param musicVolume The music volume to save, between 0.0 (silent) and 1.0
     *                    (full volume)
     */
    public static void saveMusicVolume(float musicVolume) {
        preferences.putFloat(MUSIC_VOLUME_KEY, musicVolume);
        preferences.flush();
    }

    /**
     * Retrieves the music volume from persistent storage.
     * <p>
     * This method returns the stored music volume value or the default value
     * if no setting has been saved. The volume value determines the loudness
     * of background music in the game.
     * </p>
     * 
     * @return The stored music volume, or the default if none set, between 0.0
     *         (silent) and 1.0 (full volume)
     */
    public static float getMusicVolume() {
        return preferences.getFloat(MUSIC_VOLUME_KEY, DEFAULT_MUSIC_VOLUME);
    }

    /**
     * Saves the sound effects volume to persistent storage.
     * <p>
     * This method stores the provided volume value and immediately flushes
     * the preferences to ensure the change is persisted. The volume value
     * affects the loudness of sound effects in the game.
     * </p>
     * 
     * @param effectsVolume The effects volume to save, between 0.0 (silent) and 1.0
     *                      (full volume)
     */
    public static void saveEffectsVolume(float effectsVolume) {
        preferences.putFloat(EFFECTS_VOLUME_KEY, effectsVolume);
        preferences.flush();
    }

    /**
     * Retrieves the sound effects volume from persistent storage.
     * <p>
     * This method returns the stored effects volume value or the default value
     * if no setting has been saved. The volume value determines the loudness
     * of sound effects in the game.
     * </p>
     * 
     * @return The stored effects volume, or the default if none set, between 0.0
     *         (silent) and 1.0 (full volume)
     */
    public static float getEffectsVolume() {
        return preferences.getFloat(EFFECTS_VOLUME_KEY, DEFAULT_EFFECTS_VOLUME);
    }

    /**
     * Retrieves the show FPS setting from persistent storage.
     * <p>
     * This method returns the stored show FPS value or the default value
     * if no setting has been saved. This setting determines whether the
     * frames-per-second counter is displayed during gameplay.
     * </p>
     * 
     * @return True if the FPS counter should be shown, false otherwise
     */
    public static boolean getShowFPS() {
        return preferences.getBoolean(SHOW_FPS_KEY, DEFAULT_SHOW_FPS);
    }
}
