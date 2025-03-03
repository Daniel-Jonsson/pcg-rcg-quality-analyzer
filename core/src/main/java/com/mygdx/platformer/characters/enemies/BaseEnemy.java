package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.platformer.utilities.AppConfig;


/**
 * Represents a base class for all enemy characters in the game.
 * Handles common behavior such as movement, health, damage handling,
 * and physics interactions.
 * This class provides shared functionality for rendering, and
 * animations.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public abstract class BaseEnemy {

    protected Body body;
    protected int health;
    protected int attackPower;
    protected float speed;
    protected boolean isOnGround;
    protected Sprite sprite;
    protected Texture texture;
    private boolean isDead;

    private float moveDirection = 1; // 1 = right, -1 = left (temporary for movement testing)
    private float moveTime = 0; // Tracks how long the enemy has moved in one direction, temporary for testing purposes
    protected final float switchTime = 1f; // Time before switching direction,
    // temporary for testing purposes.
    protected boolean facingRight = true;

    protected TextureRegion currentFrame;


    protected float stateTime = 0f;

    /**
     * Constructs a new enemy instance with the specified attributes.
     *
     * @param world The Box2D world where the enemy exists.
     * @param position The initial position of the enemy.
     * @param health The health points of the enemy.
     * @param attackPower The attack power of the enemy.
     * @param speed The movement speed of the enemy.
     */
    public BaseEnemy(World world, Vector2 position, int health, int attackPower, float speed) {

        this.health = health;
        this.attackPower = attackPower;
        this.speed = speed;
        this.isOnGround = false;

        setupPhysics(world, position);
        setupAnimations();
    }


    /**
     * Initializes the physics body and collision properties for the enemy.
     *
     * @param world The Box2D world where the enemy exists.
     * @param position The initial position of the enemy.
     */
    private void setupPhysics(World world, Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        Vector2 hitBox = getHitBoxSize();
        shape.setAsBox(hitBox.x, hitBox.y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0f;

        fixtureDef.filter.categoryBits = AppConfig.CATEGORY_ENEMY;
        fixtureDef.filter.maskBits =
            AppConfig.CATEGORY_PLAYER | AppConfig.CATEGORY_PLATFORM | AppConfig.CATEGORY_ATTACK;

        body.createFixture(fixtureDef);
        shape.dispose();

        MassData massData = new MassData();
        massData.mass = AppConfig.ENEMY_MASS;
        body.setMassData(massData);
        body.setUserData(this);
    }

    /**
     * Renders the enemy using the provided sprite batch.
     *
     * @param batch The sprite batch used for rendering.
     */
    public void render(SpriteBatch batch) {
        if (sprite != null && body != null) {
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,
                body.getPosition().y - sprite.getHeight() / 2);
            sprite.draw(batch);
        }
    }

    /**
     * Updates the enemy's behavior, including movement direction switching.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    public void update(float deltaTime) {
        stateTime += deltaTime;
        moveTime += deltaTime;

        if (moveTime >= switchTime) {
            moveDirection *= -1;
            moveTime = 0; // Reset movement timer
            facingRight = moveDirection > 0;
        }

        body.setLinearVelocity(moveDirection * speed, body.getLinearVelocity().y);
    }

    /**
     * Reduces the enemy's health when taking damage.
     * If health reaches zero or below, the enemy is marked as dead.
     *
     * @param damage The amount of damage taken.
     */
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            isDead = true;
        }
    }

    /**
     * Checks if the enemy is dead.
     *
     * @return {@code true} if the enemy has zero or negative health,
     * otherwise {@code false}.
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Retrieves the enemy's physics body.
     *
     * @return The Box2D body associated with this enemy.
     */
    public Body getBody() {
        return body;
    }

    /**
     * Sets up animations for the enemy.
     * This method must be implemented by concrete classes.
     */
    protected abstract void setupAnimations();

    /**
     * Returns the size of the enemy's hitbox. Will be enemy-specific
     *
     * @return A {@code Vector2} representing the width and height of the
     * hitbox.
     */
    protected abstract Vector2 getHitBoxSize();
}
