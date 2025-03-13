package com.mygdx.platformer.difficulty;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.platformer.difficulty.observer.GameDifficultyObserver;

public class GameDifficultyManager {
    public static GameDifficultyManager INSTANCE;
    private int difficultyLevel;
    private int maxDifficultyLevel = 10;
    private float difficultyIncreaseInterval = 10f;
    private final List<GameDifficultyObserver> observers = new ArrayList<>();
    private float timeSinceLastDifficultyIncrease = 0f;

    public static GameDifficultyManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameDifficultyManager();
        }
        return INSTANCE;
    }

    public void update(float deltaTime) {
        timeSinceLastDifficultyIncrease += deltaTime;
        if (timeSinceLastDifficultyIncrease >= difficultyIncreaseInterval && difficultyLevel < maxDifficultyLevel) {
            increaseDifficulty();
            timeSinceLastDifficultyIncrease = 0f;
        }
    }

    private void increaseDifficulty() {
        difficultyLevel++;
        notifyObservers();
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

    public void resetDifficulty() {
        difficultyLevel = 0;
        timeSinceLastDifficultyIncrease = 0f;
        notifyObservers();
    }
}
