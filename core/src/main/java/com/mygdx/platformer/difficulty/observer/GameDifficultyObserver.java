package com.mygdx.platformer.difficulty.observer;

/**
 * Observer interface for game difficulty changes.
 * <p>
 * This interface implements the Observer pattern for game difficulty changes.
 * Classes that need to respond to changes in game difficulty should implement
 * this interface and register themselves with a difficulty subject/publisher.
 * </p>
 * <p>
 * When the game's difficulty level changes, the subject will notify all
 * registered
 * observers by calling their {@code onDifficultyChanged} method with the new
 * difficulty level.
 * </p>
 * <p>
 * Typical implementations might include:
 * <ul>
 * <li>Enemy spawners that adjust spawn rates or enemy types</li>
 * <li>Attack managers that scale damage or projectile speed</li>
 * <li>UI components that display the current difficulty</li>
 * <li>Level generators that modify level complexity</li>
 * </ul>
 * </p>
 * 
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public interface GameDifficultyObserver {

    /**
     * Called when the game difficulty changes.
     * <p>
     * Implementing classes should use this method to adjust their behavior
     * based on the new difficulty level. Higher difficulty levels typically
     * result in more challenging gameplay elements.
     * </p>
     *
     * @param difficultyLevel The new difficulty level, where higher values
     *                        represent increased difficulty
     */
    void onDifficultyChanged(int difficultyLevel);
}
