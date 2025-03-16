package com.mygdx.platformer.sound;

/**
 * Enumeration of all sound effect types used in the platformer game.
 * <p>
 * This enum serves as a type-safe identifier system for all sound effects,
 * providing several benefits:
 * <ul>
 * <li>Prevents errors from using incorrect string identifiers</li>
 * <li>Enables IDE auto-completion for available sound effects</li>
 * <li>Centralizes the definition of all sound types in one place</li>
 * <li>Serves as keys in the AudioManager's sound cache map</li>
 * </ul>
 * </p>
 * <p>
 * Each enum constant represents a distinct sound effect that can be loaded
 * and played through the AudioManager. The enum is used in conjunction with
 * the AudioManager to load, cache, and play sound effects:
 * </p>
 * <p>
 * Usage example:
 *
 * <pre>
 * // Load a specific sound effect
 * AudioManager.loadSoundEffect(SoundType.BUTTONCLICK, "path/to/sound.wav");
 *
 * // Play a sound effect
 * AudioManager.playSound(SoundType.BUTTONCLICK);
 * </pre>
 * </p>
 *
 * @see com.mygdx.platformer.sound.AudioManager
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public enum SoundType {

    SWOOSH,
    SWOOSH2,
    DEATHBOLT,
    BUTTONHOVER,
    BUTTONCLICK,
    CHECKBOXCLICK,
    SLIDERCHANGE
}
