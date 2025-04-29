package com.mygdx.platformer.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;

/**
 * Represents a base class for all attack types in the game.
 * Handles common attack behavior such as movement, and rendering.
 * This class provides the foundation for concrete implementations (e.g.,
 * {@link GoblinAttack}).
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public abstract class BaseAttack {
    /** The damage caused by the attack. */
    protected int damage;
    /** The speed of the attack. */
    protected float speed;
    /** The position of the attack. */
    protected float x, y;
    /** The attack sprite. */
    protected Sprite sprite;
    /** Indicates whether the attack should be removed from the world (i.e. if
     *  it hit a target). */
    protected boolean shouldRemove;
    /** The Box2D world where the attack resides. */
    protected final World world;
    /** The Box2D body of the attack. */
    protected final Body body;
    /** Indicates whether the attack is a player attack or not.*/
    protected boolean isPlayerAttack;

    /**
     * Constructs a new attack instance with specified parameters.
     *
     * @param world The Box2D world where the attack is created.
     * @param damage The damage the attack deals when colliding with target.
     * @param speed The speed at which the attack moves.
     * @param x The initial x-coordinate of the attack.
     * @param y The initial y-coordinate of the attack.
     * @param texture The texture used for the attack's sprite.
     * @param isPlayerAttack Whether the attack is a player attack.
     */
    public BaseAttack(World world, int damage, float speed, float x, float y,
                      Texture texture, boolean isPlayerAttack) {
        this.world = world;
        this.damage = damage;
        this.isPlayerAttack = isPlayerAttack;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(texture);

        sprite.setSize(AppConfig.ATTACK_SPRITE_WIDTH, AppConfig.ATTACK_SPRITE_HEIGHT);
        sprite.setPosition(x, y);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        this.body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = AppConfig.CATEGORY_ATTACK;

        if (isPlayerAttack) {
            fixtureDef.filter.maskBits = AppConfig.CATEGORY_PLATFORM | AppConfig.CATEGORY_ENEMY;
        } else {
            fixtureDef.filter.maskBits = AppConfig.CATEGORY_PLATFORM | AppConfig.CATEGORY_PLAYER;
        }

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setLinearVelocity(speed, 0);
    }

    /**
     * Updates the attack's position and determines if it should be removed.
     * The attack is removed if it moves outside the camera's viewport.
     *
     * @param cameraX The X-position of the camera.
     * @param viewPortWidth The width of the viewport.
     */
    public void update(float cameraX, float viewPortWidth) {
        Vector2 pos = body.getPosition();
        sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getHeight() / 2);

        float rightEdge = cameraX + viewPortWidth / 2;
        float leftEdge = cameraX - viewPortWidth / 2;
        if (pos.x > rightEdge + sprite.getWidth() || pos.x < leftEdge - sprite.getWidth()) {
            shouldRemove = true;
        }
    }

    /**
     * Renders the attack using the provided sprite batch.
     *
     * @param batch The sprite batch used for rendering.
     */
    public void render(SpriteBatch batch) {
        if (speed < 0) {
            sprite.setFlip(true, false);
        }
        sprite.draw(batch);
    }

    /**
     * Checks if the attack should be removed from the game world.
     *
     * @return {@code true} if the attack should be removed, otherwise {@code
     * false}.
     */
    public boolean shouldRemove() {
        return shouldRemove;
    }

    /**
     * Sets whether the attack should be removed.
     *
     * @param shouldRemove {@code true} if the attack should be removed,
     *                                 otherwise {@code false}.
     */
    public void setShouldRemove(boolean shouldRemove) {
        this.shouldRemove = shouldRemove;
    }

    /**
     * Retrieves the damage value of the attack.
     *
     * @return The damage dealt by the attack.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Checks if the attack is a enemy attack.
     *
     * @return {@code true} if the attack is a enemy attack, otherwise {@code
     * false}.
     */
    public boolean isEnemyAttack() {
        return !isPlayerAttack;
    }

    /**
     * Accessor for the attack body.
     * @return The attack body.
     */
    public Body getBody() {
        return body;
    }

    public float getSpeed() {
        return speed;
    }
}
