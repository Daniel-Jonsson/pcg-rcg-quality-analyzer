package com.mygdx.difficulty;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.difficulty.observer.GameDifficultyObserver;

public class GameDifficultyManager {
    public static GameDifficultyManager INSTANCE;
    private int difficultyLevel;
    private int maxDifficultyLevel = 10;
    private float difficultyIncreaseInterval = 10f;
    private final List<GameDifficultyObserver> observers = new ArrayList<>();

    private GameDifficultyManager() {
        throw new UnsupportedOperationException("This class is a singleton and should not be instantiated directly.");
    }

    public static GameDifficultyManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameDifficultyManager();
        }
        return INSTANCE;
    }

    public void updateDifficulty(float normalizedDifficulty) {
        difficultyLevel = (int) (normalizedDifficulty * maxDifficultyLevel);
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public void registerObserver(GameDifficultyObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameDifficultyObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (GameDifficultyObserver observer : observers) {
            observer.onDifficultyChanged(difficultyLevel);
        }
    }
}
