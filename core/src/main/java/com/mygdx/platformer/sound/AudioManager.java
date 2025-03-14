package com.mygdx.platformer.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class AudioManager {
    private static Music backgroundMusic;

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

    public void dispose() {
        backgroundMusic.dispose();
    }
}
