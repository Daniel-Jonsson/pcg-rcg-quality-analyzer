package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.platformer.characters.BaseCharacter;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;


/**
 * Represents a base class for all enemy characters in the game.
 * Handles common behavior such as movement, maxHealth, damage handling,
 * and physics interactions.
 * This class provides shared functionality for rendering, and
 * animations.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public abstract class BaseEnemy extends BaseCharacter {

    private float moveTime = 0; // Tracks how long the enemy has moved in one direction, temporary for testing purposes
    protected final float switchTime = 1f; // Time before switching direction,

    private Sprite healthBarSprite;


    /**
     * Constructs a new enemy instance with the specified attributes.
     *
     * @param world The Box2D world where the enemy exists.
     * @param position The initial position of the enemy.
     * @param health The maxHealth points of the enemy.
     * @param speed The movement speed of the enemy.
     */
    public BaseEnemy(World world, Vector2 position, int health, float speed,
                     float width, float height) {
        super(world, position, health, speed, width, height);
        MassData massData = new MassData();
        massData.mass = AppConfig.ENEMY_MASS;
        body.setMassData(massData);
        body.setUserData(this);
        moveDirection = -1;
        facingRight = false;
        healthBarSprite = new Sprite(new Texture(Assets.HEALTHBAR_TEXTURE));
        healthBarSprite.setSize(AppConfig.HEALTHBAR_SPRITE_WIDTH,
            AppConfig.HEALTHBAR_SPRITE_HEIGHT);
    }

    /**
     * Renders the enemy's health bar above its sprite. The health bar width
     * dynamically scales based on the enemy's current health percentage.
     *
     * @param batch The {@link SpriteBatch} used for rendering the health bar.
     */
    public void renderHealthBar(SpriteBatch batch) {
        float healthPercentage = (float) currentHealth / maxHealth;
        float barWidth = AppConfig.HEALTHBAR_SPRITE_WIDTH * healthPercentage;
        healthBarSprite.setPosition(
            (body.getPosition().x - (barWidth / 2)),
            body.getPosition().y + (height * getScale() / 2) + 0.3f
        );

        healthBarSprite.setSize(barWidth, AppConfig.ATTACK_SPRITE_HEIGHT);

        healthBarSprite.draw(batch);
    }

    /**
     * Updates the enemy's behavior, including movement direction switching.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        moveTime += deltaTime;
        if (moveTime >= switchTime) {
            moveDirection *= -1;
            moveTime = 0; // Reset movement timer
            facingRight = moveDirection > 0;
        }
    }

    /**
     * Gets the collision category for enemies
     * @return The collision category.
     */
    @Override
    protected short getCollisionCategory() {
        return AppConfig.CATEGORY_ENEMY;
    }

    /**
     * The collision masks in which enemy's should collide with
     * @return The collision masks
     */
    @Override
    protected short getCollisionMask() {
        return AppConfig.CATEGORY_PLAYER | AppConfig.CATEGORY_PLATFORM | AppConfig.CATEGORY_ATTACK;
    }

    /**
     * Returns the size of the enemy's hitbox. Will be enemy-specific
     *
     * @return A {@code Vector2} representing the width and height of the
     * hitbox.
     */
    protected abstract Vector2 getHitBoxSize();
}
