package com.mygdx.platformer.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.platformer.utilities.Settings;

import java.util.HashMap;
import java.util.Map;

import static com.mygdx.platformer.sound.SoundType.*;

/**
 * This utility class assists in playing music and sound effects.
 *
 * @author Robert Kullman, Daniel Jönsson
 */
public class AudioManager {
    private static Music backgroundMusic;

    private static float effectsVolume = Settings.getEffectsVolume();

    private static Map<SoundType, Sound> sounds = new HashMap<>();

    /**
     * Starts playing the background music.
     */
    public static void playBackgroundMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(Settings.getMusicVolume());
        backgroundMusic.play();
    }

    /**
     * Stops the background music.
     */
    public static void stopBackgroundMusic() {
        backgroundMusic.stop();
    }

    /**
     * Loads the specified sound effect.
     * @param soundType Map key for the sound effect.
     * @param filePath Path to the sound file.
     */
    public static void loadSoundEffect(SoundType soundType, String filePath) {
        if (!sounds.containsKey(soundType)) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
            sounds.put(soundType, sound);
        }
    }

    /**
     * Plays the sound corresponding to the string key parameter.
     * @param soundType Sound key.
     */
    public static void playSound(SoundType soundType) {
        Sound sound = sounds.get(soundType);
        if (sound != null) {
            sound.play(effectsVolume);
        }
    }

    /**
     * Loads sound effects in to the map.
     */
    public static void loadSounds() {
        loadSoundEffect(DEATHBOLT, "sound/deathbolt.mp3");
        loadSoundEffect(SWOOSH, "sound/swoosh.mp3");
        loadSoundEffect(SWOOSH2, "sound/swoosh2.mp3");
        loadSoundEffect(BUTTONHOVER, "sound/button-hover.mp3");
        loadSoundEffect(BUTTONCLICK, "sound/button-click.mp3");
        loadSoundEffect(CHECKBOXCLICK, "sound/checkbox-clicked.mp3");
        loadSoundEffect(SLIDERCHANGE, "sound/slider-changed.mp3");
    }

    /**
     * Disposes of assets to free up resources.
     */
    public static void dispose() {
        backgroundMusic.dispose();
        sounds.values().forEach(Sound::dispose);
    }
}
