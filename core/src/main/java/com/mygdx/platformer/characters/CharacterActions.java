package com.mygdx.platformer.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Interface defining core character actions.
 * @author Daniel Jönsson, Robert Kullman
 */
public interface CharacterActions {
    void update(float deltaTime);
    void render(SpriteBatch batch);
    void takeDamage(int damage);
    boolean isDead();
}
