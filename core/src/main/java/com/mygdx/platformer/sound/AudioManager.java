package com.mygdx.platformer.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static Music backgroundMusic;

    private static float effectsVolume = 0.5f;

    private static Map<String, Sound> sounds = new HashMap<>();

    public static void playBackgroundMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.2f);
        backgroundMusic.play();
    }

    public static void stopBackgroundMusic() {
        backgroundMusic.stop();
    }

    public static void setMusicVolume(float volume) {
        backgroundMusic.setVolume(volume);
    }

    public static void loadSoundEffect(String soundName, String filePath) {
        if (!sounds.containsKey(sounds)) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
            sounds.put(soundName, sound);
        }
    }

    public static void playSound(String soundName) {
        Sound sound = sounds.get(soundName);
        if (sound != null) {
            sound.play(effectsVolume);
        }
    }

    public static void loadSounds() {
        loadSoundEffect("deathbolt", "sound/deathbolt.mp3");
        loadSoundEffect("swoosh", "sound/swoosh.mp3");
        loadSoundEffect("swoosh2", "sound/swoosh2.mp3");
    }

    public static void dispose() {
        backgroundMusic.dispose();
        sounds.values().forEach(Sound::dispose);
    }
}
