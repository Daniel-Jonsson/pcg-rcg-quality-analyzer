package com.mygdx.platformer.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Interface defining core character actions.
 * @author Daniel Jönsson, Robert Kullman
 */
public interface CharacterActions {

    /**
     * Abstract update method for the character.
     * @param deltaTime Time since last frame.
     */
    void update(float deltaTime);

    /**
     * Renders the character.
     * @param batch The SpriteBatch to be used for rendering.
     */
    void render(SpriteBatch batch);

    /**
     * Declares what should happen when a character takes damage.
     * @param damage Damage amount.
     */
    void takeDamage(int damage);
}
