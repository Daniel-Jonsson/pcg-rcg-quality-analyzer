package com.mygdx.platformer.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Abstract class representing a base character with shared logic for both
 * players and enemies. Handles physics, maxHealth, rendering, and movement.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public abstract class BaseCharacter implements CharacterActions {

    /** The physics body of the character. */
    protected Body body;
    /** The hit points of the character. */
    protected int maxHealth;

    protected int currentHealth;
    /** The movement speed of the character. */
    protected float movementSpeed;
    /** Whether the character is dead. */
    protected boolean isDead;
    /** Tracks elapsed time for animations. */
    protected float stateTime;
    /** The direction in which the character is moving. */
    protected float moveDirection;
    /** Whether the character is facing right. */
    protected boolean facingRight;
    /** The current animation frame of the character. */
    protected TextureRegion currentFrame;
    /** The width of the character. */
    protected final float width;
    /** The height of the character. */
    protected final float height;
    /** The hitbox dimensions of the character. */
    private final Vector2 hitBox;
    /** The scale of the character. */
    private final float scale;


    /**
     * Constructs a new BaseCharacter instance with the specified attributes.
     *
     * @param world The Box2D world.
     * @param position The initial position of the character.
     * @param maxHealth The maxHealth points of the character.
     * @param movementSpeed The movement speed of the character.
     * @param width The width of the character.
     * @param height The height of the character.
     */
    public BaseCharacter(World world, Vector2 position, int maxHealth,
                         float movementSpeed, float width, float height) {
        this.maxHealth = this.currentHealth = maxHealth;
        this.movementSpeed = movementSpeed;
        this.isDead = false;
        this.width = width;
        this.height = height;
        this.hitBox = getHitBoxSize();
        this.scale = getScale();
        setupPhysics(world, position);
        setupAnimations();
    }

    /**
     * Sets up the physics body and collision properties.
     *
     * @param world The Box2D world.
     * @param position The initial position.
     */
    private void setupPhysics(World world, Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hitBox.x, hitBox.y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0f;

        fixtureDef.filter.categoryBits = getCollisionCategory();
        fixtureDef.filter.maskBits = getCollisionMask();

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    /**
     * Renders the character sprite.
     *
     * @param batch The SpriteBatch used for rendering.
     */
    @Override
    public void render(SpriteBatch batch) {
        boolean flip = !facingRight;
        int offsetModifier = flip ? -1 : 1;
        batch.draw(
            currentFrame,
            body.getPosition().x - width * offsetModifier,
            body.getPosition().y - hitBox.y,
            width * scale * offsetModifier,
            height * scale
        );
    }

    /**
     * Updates the character's state.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        stateTime += deltaTime;
        body.setLinearVelocity(moveDirection * movementSpeed, body.getLinearVelocity().y);
    }

    /**
     * Reduces maxHealth when taking damage and checks if the character is dead.
     *
     * @param damage The damage taken.
     */
    @Override
    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
            isDead = true;
        }
    }

    /**
     * Checks if the character is dead.
     *
     * @return {@code true} if the character has 0 or negative maxHealth,
     * otherwise {@code false}.
     */
    @Override
    public boolean isDead() {
        return isDead;
    }

    /**
     * Gets the character's physics body.
     *
     * @return The Box2D body.
     */
    public Body getBody() {
        return body;
    }

    /**
     * Must be implemented by subclasses to define their hitbox size.
     *
     * @return A {@code Vector2} representing the hitbox width and height.
     */
    protected abstract Vector2 getHitBoxSize();

    /**
     * Returns the collision category for the character.
     *
     * @return A short representing the collision category.
     */
    protected abstract short getCollisionCategory();

    /**
     * Returns the collision mask for the character.
     *
     * @return A short representing what the character can collide with.
     */
    protected abstract short getCollisionMask();

    /**
     * Returns the scale factor of the character.
     *
     * @return The scale factor.
     */
    protected abstract float getScale();

    /**
     * Sets up animations for the enemy.
     * This method must be implemented by concrete classes.
     */
    protected abstract void setupAnimations();
}
