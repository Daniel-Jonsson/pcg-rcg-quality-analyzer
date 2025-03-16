package com.mygdx.platformer.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;
import com.mygdx.platformer.utilities.Settings;

import java.util.HashMap;
import java.util.Map;

import static com.mygdx.platformer.sound.SoundType.*;

/**
 * A centralized audio management system for the platformer game.
 * <p>
 * This utility class provides a unified interface for handling all audio
 * aspects of the game:
 * <ul>
 * <li>Background music playback and control</li>
 * <li>Sound effect loading, caching, and playback</li>
 * <li>Volume management based on user settings</li>
 * <li>Resource management for audio assets</li>
 * </ul>
 * </p>
 * <p>
 * The class uses LibGDX's audio system and implements a singleton pattern with
 * static methods for easy access throughout the codebase. Sound effects are
 * cached in a map using the SoundType enum as keys to improve performance and
 * reduce memory usage.
 * </p>
 * <p>
 * Usage example:
 * 
 * <pre>
 * // Initialize sounds at game startup
 * AudioManager.loadSounds();
 * 
 * // Start background music
 * AudioManager.playBackgroundMusic();
 * 
 * // Play a sound effect
 * AudioManager.playSound(SoundType.BUTTONCLICK);
 * 
 * // Clean up resources when done
 * AudioManager.dispose();
 * </pre>
 * </p>
 *
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class AudioManager {
    /**
     * The background music instance.
     * <p>
     * This is the continuous music that plays in the background during gameplay.
     * Only one background music track can be active at a time.
     * </p>
     */
    private static Music backgroundMusic;

    /**
     * The volume level for sound effects.
     * <p>
     * This value is retrieved from user settings and applied to all sound effects
     * when they are played. The value ranges from 0.0 (silent) to 1.0 (full
     * volume).
     * </p>
     */
    private static final float effectsVolume = Settings.getEffectsVolume();

    /**
     * Map of cached sound effects.
     * <p>
     * Sound effects are loaded once and stored in this map for efficient playback.
     * The SoundType enum is used as the key to identify different sound effects.
     * </p>
     */
    private static final Map<SoundType, Sound> sounds = new HashMap<>();

    /**
     * Starts playing the background music.
     * <p>
     * Loads the background music file, configures it to loop continuously,
     * sets its volume based on user settings, and begins playback.
     * If called while music is already playing, the current track will
     * continue playing.
     * </p>
     */
    public static void playBackgroundMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(Assets.BACKGROUND_MUSIC));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(Settings.getMusicVolume());
        backgroundMusic.play();
    }

    /**
     * Stops the background music.
     * <p>
     * Immediately halts playback of the current background music track.
     * The music instance remains in memory and can be restarted with
     * playBackgroundMusic().
     * </p>
     */
    public static void stopBackgroundMusic() {
        backgroundMusic.stop();
    }

    /**
     * Loads a specific sound effect into the cache.
     * <p>
     * Checks if the sound is already loaded before creating a new instance
     * to prevent duplicate loading. The sound is stored in the sounds map
     * using the provided SoundType as the key.
     * </p>
     *
     * @param soundType The enum identifier for the sound effect
     * @param filePath  The path to the sound file resource
     */
    public static void loadSoundEffect(SoundType soundType, String filePath) {
        if (!sounds.containsKey(soundType)) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
            sounds.put(soundType, sound);
        }
    }

    /**
     * Plays a sound effect.
     * <p>
     * Retrieves the requested sound from the cache and plays it at the
     * volume level specified in the user settings. If the sound is not
     * found in the cache, no sound will play.
     * </p>
     *
     * @param soundType The enum identifier for the sound effect to play
     */
    public static void playSound(SoundType soundType) {
        Sound sound = sounds.get(soundType);
        if (sound != null) {
            sound.play(effectsVolume);
        }
    }

    /**
     * Loads all game sound effects into the cache.
     * <p>
     * This method should be called during game initialization to preload
     * all sound effects. It loads each sound effect defined in the SoundType
     * enum using file paths from AppConfig.
     * </p>
     */
    public static void loadSounds() {
        loadSoundEffect(DEATHBOLT, AppConfig.SOUND_DEATHBOLT);
        loadSoundEffect(SWOOSH, AppConfig.SOUND_SWOOSH);
        loadSoundEffect(SWOOSH2, AppConfig.SOUND_SWOOSH2);
        loadSoundEffect(BUTTONHOVER, AppConfig.SOUND_BUTTON_HOVER);
        loadSoundEffect(BUTTONCLICK, AppConfig.SOUND_BUTTON_CLICK);
        loadSoundEffect(CHECKBOXCLICK, AppConfig.SOUND_CHECKBOX_CLICKED);
        loadSoundEffect(SLIDERCHANGE, AppConfig.SOUND_SLIDER_CHANGED);
    }

    /**
     * Releases all audio resources.
     * <p>
     * This method should be called when the game is shutting down or
     * transitioning to a state where audio is no longer needed. It
     * disposes of the background music and all cached sound effects
     * to prevent memory leaks.
     * </p>
     */
    public static void dispose() {
        backgroundMusic.dispose();
        sounds.values().forEach(Sound::dispose);
    }
}
