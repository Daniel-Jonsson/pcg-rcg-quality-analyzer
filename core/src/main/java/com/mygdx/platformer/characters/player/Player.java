package com.mygdx.platformer.characters.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.attacks.AttackManager;
import com.mygdx.platformer.characters.BaseCharacter;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

/**
 * This class represents the player character in the game.
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class Player extends BaseCharacter {

    /** The force applied when the player jumps. */
    private float jumpForce = AppConfig.PLAYER_JUMP_FORCE;

    /** Tracks whether the player is currently on the ground. */
    private boolean isGrounded = false;

    /** Tracks whether the player is currently jumping or not. i.e if the
     * spacebar is being held down.*/
    private boolean isJumping = false;

    /** Whether a jump request has been triggered. */
    private boolean jumpRequested = false;

    /** Whether the jump button is currently being held down. */
    private boolean jumpHolding = false;

    /** Tracks whether the jump key was pressed in the last frame. */
    private boolean wasJumpKeyPressed = false;

    /** Tracks how long the jump button has been held down. */
    private float jumpHoldTime = 0;

    /** Manages the player's attacks. */
    private AttackManager attackManager;

    /** Animation for the idle state. */
    private Animation<TextureRegion> idleAnimation;

    /** Animation for the walking state. */
    private Animation<TextureRegion> walkAnimation;

    /** Animation for the jumping state. */
    private Animation<TextureRegion> jumpAnimation;

    /** Animation for the attacking state. */
    private Animation<TextureRegion> attackAnimation;

    /** The texture atlas containing the player's animations. */
    private TextureAtlas playerAtlas;


    /**
     * Instantiates the player in the game world.
     * @param world The Box2D world.
     * @param position The player position.
     * @param health The player health.
     * @param movementSpeed The movement speed of the player.
     * @param manager the AttackManager for spawning attacks.
     */
    public Player(World world, Vector2 position,
                  int health, float movementSpeed, AttackManager manager) {
        super(world, position, health, movementSpeed, AppConfig.PLAYER_WIDTH,
            AppConfig.PLAYER_HEIGHT);
        this.attackManager = manager;

        MassData massData = new MassData();
        massData.mass = AppConfig.PLAYER_MASS;
        body.setMassData(massData);
        facingRight = true;
    }

    /**
     * Updates the player state.
     * @param deltaTime time since last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (jumpRequested) {
            body.applyLinearImpulse(new Vector2(0, jumpForce), body.getWorldCenter(), true);
            jumpRequested = false;
            jumpHoldTime = 0;
        }

        if (jumpHolding && jumpHoldTime < AppConfig.MAX_JUMP_HOLD_TIME && body.getLinearVelocity().y > 0) {
            body.applyForce(new Vector2(0, AppConfig.JUMP_HOLD_FORCE), body.getWorldCenter(), true);
            jumpHoldTime += deltaTime;
        } else {
            jumpHolding = false;
        }

        // Determine animation state
        if (!isGrounded) {
            currentFrame = jumpAnimation.getKeyFrame(stateTime);
        } else if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            currentFrame = attackAnimation.getKeyFrame(stateTime);
        } else if (moveDirection != 0) {
            currentFrame = walkAnimation.getKeyFrame(stateTime);
        } else {
            currentFrame = idleAnimation.getKeyFrame(stateTime);
        }

    }

    /**
     * Sets up the animations for the Player. Loads the sprite regions
     * specified in the atlas.
     */
    @Override
    protected void setupAnimations() {
        playerAtlas = Assets.getPlayerAtlas();

        idleAnimation = new Animation<>(AppConfig.STANDARD_FRAME_DURATION,
            playerAtlas.findRegions("player_idle"), Animation.PlayMode.LOOP);
        walkAnimation = new Animation<>(AppConfig.WALK_FRAME_DURATION,
            playerAtlas.findRegions("player_walk"), Animation.PlayMode.LOOP);
        jumpAnimation = new Animation<>(AppConfig.STANDARD_FRAME_DURATION,
            playerAtlas.findRegions("player_jump"), Animation.PlayMode.NORMAL);
        attackAnimation = new Animation<>(AppConfig.ATTACK_FRAME_DURATION,
            playerAtlas.findRegions("player_attack"), Animation.PlayMode.NORMAL);

        currentFrame = idleAnimation.getKeyFrame(0);

    }

    /**
     * Handles player input.
     */
    public void handleInput() {
        moveDirection = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveDirection = -1;
            facingRight = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveDirection = 1;
            facingRight = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            int attackDirectionModifier = facingRight ? 1 : -1;
            attackManager.spawnAttackAt(
                new Vector2(body.getPosition().x,
                body.getPosition().y + AppConfig.PLAYER_Y_ATTACK_OFFSET),
                    attackDirectionModifier
                );
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isGrounded) {
            jumpRequested = true;
            jumpHolding = true;
        }

        if (wasJumpKeyPressed && !Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            jumpHolding = false;
        }

        wasJumpKeyPressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
    }

    /**
     * Disposes the player texture to free resources.
     */
    public void dispose() {
        playerAtlas.dispose();
    }

    /**
     * Sets whether the player is on the ground or not.
     * @param grounded Boolean indicating whether the player is on the ground.
     */
    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    /**
     * Accessor for the player's physics body.
     * @return The Box2D physics body of the player.
     */
    public Body getBody() {
        return body;
    }

    /**
     * Must be implemented by subclasses to define their hitbox size.
     *
     * @return A {@code Vector2} representing the hitbox width and height.
     */
    @Override
    protected Vector2 getHitBoxSize() {
        return new Vector2(AppConfig.PLAYER_HITBOX_SIZE_X,
            AppConfig.PLAYER_HITBOX_SIZE_Y);
    }

    /**
     * Returns the collision category for the character.
     *
     * @return A short representing the collision category.
     */
    @Override
    protected short getCollisionCategory() {
        return AppConfig.CATEGORY_PLAYER;
    }

    /**
     * Returns the collision mask for the character.
     *
     * @return A short representing what the character can collide with.
     */
    @Override
    protected short getCollisionMask() {
        return AppConfig.CATEGORY_PLATFORM | AppConfig.CATEGORY_ATTACK | AppConfig.CATEGORY_ENEMY;
    }

    /**
     * Returns the player scale used for rendering sprites.
     * @return The player scale.
     */
    @Override
    protected float getScale() {
        return AppConfig.PLAYER_SCALE;
    }
}
