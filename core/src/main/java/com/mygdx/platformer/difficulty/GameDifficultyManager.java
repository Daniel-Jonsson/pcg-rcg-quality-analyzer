package com.mygdx.platformer.difficulty;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.platformer.difficulty.observer.GameDifficultyObserver;
import com.mygdx.platformer.utilities.AppConfig;

/**
 * Manages the game's difficulty progression system.
 * <p>
 * This class implements the Singleton and Observer design patterns to provide
 * a centralized difficulty management system. The difficulty level increases
 * automatically over time, and registered observers are notified of these
 * changes.
 * </p>
 * <p>
 * The difficulty progression affects various game elements such as:
 * <ul>
 * <li>Enemy spawn rates and types</li>
 * <li>Attack damage and speed</li>
 * <li>Level complexity</li>
 * </ul>
 * </p>
 * <p>
 * Game components that need to respond to difficulty changes should implement
 * the {@link GameDifficultyObserver} interface and register with this manager.
 * </p>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class GameDifficultyManager {
    /** Singleton instance of the GameDifficultyManager. */
    public static GameDifficultyManager INSTANCE;

    /** Current difficulty level of the game. */
    private int difficultyLevel;

    /** List of observers that will be notified when difficulty changes. */
    private final List<GameDifficultyObserver> observers = new ArrayList<>();

    /** Time counter tracking progress toward the next difficulty increase. */
    private float timeSinceLastDifficultyIncrease = 0f;

    /**
     * Gets the singleton instance of the GameDifficultyManager.
     * <p>
     * If the instance doesn't exist yet, it will be created.
     * </p>
     *
     * @return The singleton instance of GameDifficultyManager
     */
    public static GameDifficultyManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameDifficultyManager();
        }
        return INSTANCE;
    }

    /**
     * Updates the difficulty manager state.
     * <p>
     * This method should be called every frame to track time and
     * automatically increase difficulty at the specified intervals.
     * </p>
     *
     * @param deltaTime Time elapsed since the last frame in seconds
     */
    public void update(float deltaTime) {
        timeSinceLastDifficultyIncrease += deltaTime;
        int maxDifficultyLevel = AppConfig.MAX_DIFFICULTY_LEVEL;
        float difficultyIncreaseInterval = AppConfig.DIFFICULTY_INCREASE_INTERVAL;
        if (timeSinceLastDifficultyIncrease >= difficultyIncreaseInterval && difficultyLevel < maxDifficultyLevel) {
            increaseDifficulty();
            timeSinceLastDifficultyIncrease = 0f;
        }
    }

    /**
     * Increases the difficulty level by one and notifies all observers.
     * <p>
     * This is called automatically by the update method when the appropriate
     * time interval has passed.
     * </p>
     */
    private void increaseDifficulty() {
        difficultyLevel++;
        notifyObservers();
    }

    /**
     * Gets the current difficulty level.
     *
     * @return The current difficulty level
     */
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    /**
     * Registers an observer to be notified of difficulty changes.
     * <p>
     * Game components that need to adjust their behavior based on difficulty
     * should implement the GameDifficultyObserver interface and register
     * using this method.
     * </p>
     *
     * @param observer The observer to register
     */
    public void registerObserver(GameDifficultyObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the notification list.
     * <p>
     * Call this method when an observer should no longer receive difficulty
     * change notifications, such as when the observer is being destroyed.
     * </p>
     *
     * @param observer The observer to remove
     */
    public void removeObserver(GameDifficultyObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers of the current difficulty level.
     * <p>
     * This method is called automatically when the difficulty changes,
     * but can also be called manually to force a notification.
     * </p>
     */
    public void notifyObservers() {
        for (GameDifficultyObserver observer : observers) {
            observer.onDifficultyChanged(difficultyLevel);
        }
    }

    /**
     * Resets the difficulty level to zero.
     * <p>
     * This method should be called when starting a new game or
     * when the player restarts after a game over.
     * </p>
     */
    public void resetDifficulty() {
        difficultyLevel = 0;
        timeSinceLastDifficultyIncrease = 0f;
        notifyObservers();
    }
}
