package com.mygdx.platformer.characters.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.ai.autoplay.AutoPlayAgent;
import com.mygdx.platformer.attacks.AttackManager;
import com.mygdx.platformer.characters.BaseCharacter;
import com.mygdx.platformer.characters.enemies.BaseEnemy;
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

    /** Indicates whether auto-play is enabled**/
    private boolean autoPlayEnabled;

    private AutoPlayAgent autoPlayAgent;

    World gameWorld;

    private float gameTime = 0.0f;


    /**
     * Instantiates the player in the game world.
     * @param world The Box2D world.
     * @param position The player position.
     * @param health The player maxHealth.
     * @param movementSpeed The movement speed of the player.
     * @param manager the AttackManager for spawning attacks.
     */
    public Player(World world, Vector2 position,
                  int health, float movementSpeed, AttackManager manager, boolean autoPlay) {
        super(world, position, health, movementSpeed, AppConfig.PLAYER_WIDTH,
            AppConfig.PLAYER_HEIGHT);
        this.attackManager = manager;

        MassData massData = new MassData();
        massData.mass = AppConfig.PLAYER_MASS;
        body.setMassData(massData);
        facingRight = true;

        autoPlayEnabled = autoPlay;
        this.gameWorld = world;

        if (autoPlayEnabled) {
            this.autoPlayAgent = new AutoPlayAgent(this);
        }
    }

    /**
     * Updates the player state.
     * @param deltaTime time since last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        gameTime += deltaTime;


        if (gameTime > 10000f) {
            gameTime = 0;
        }

        if(autoPlayEnabled && autoPlayAgent != null) {
            autoPlayAgent.update(deltaTime);
        }

       checkJumpStatus(deltaTime);

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

    private void checkJumpStatus(float deltaTime) {
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
        if (!autoPlayEnabled) {
            moveDirection = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveBackward();
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
           moveForward();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
           attack();
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

    public void jump() {
        if (isGrounded) {
            jumpRequested = true;
            jumpHolding = true;

        }
    }

    public void attack() {
        int attackDirection = facingRight ? 1 : -1;
        attackManager.spawnAttackAt(
            new Vector2(body.getPosition().x, body.getPosition().y + AppConfig.PLAYER_ATTACK_Y_OFFSET),
            attackDirection,
            true,
            AppConfig.AttackType.PLAYER_THROWING_DAGGER
        );
    }

    /**
     * Moves the player forward, used by the AI.
     */
    public void moveForward() {
        moveDirection = 1;
        facingRight = true;

    }

    public void moveBackward() {
        moveDirection = -1;
        facingRight = false;
    }

    /**
     * Makes the player dodge an incoming projectile.
     */
    public void dodge() {

    }

    /**
     * Detects incoming projectiles.
     * @return True if a projectile is approaching.
     */
    public boolean detectIncomingProjectile() {
        return false;
    }

    /**
     * Checks if the path ahead is clear.
     * @return True if the path is clear.
     */
    public boolean isPathClear() {

        return false;
    }

    /**
     * Uses raycasting to check if an enemy is within sight range.
     * @param direction The direction to check (1 for right, -1 for left).
     * @return True if an enemy is detected in the direction.
     */
    public boolean hasEnemiesNearby(float direction) {
        Vector2 playerPosition = body.getPosition();
        float rayLength = 10;

        Vector2 rayStart = new Vector2(playerPosition.x, playerPosition.y);
        Vector2 rayEnd = new Vector2(playerPosition.x + (direction * rayLength), playerPosition.y);

        return checkForEnemy(rayStart, rayEnd);
    }

    /**
     * Casts a ray in a given direction to detect enemies.
     * @param start The start position of the ray.
     * @param end The end position of the ray.
     * @return True if an enemy is detected.
     */
    private boolean checkForEnemy(Vector2 start, Vector2 end) {
        final boolean[] enemyDetected = {false};

        gameWorld.rayCast((fixture, point, normal, fraction) -> {
            if (fixture.getBody().getUserData() instanceof BaseEnemy) {
                enemyDetected[0] = true;
                return 0; // Stop raycast after finding an enemy
            }
            return -1;
        }, start, end);

        return enemyDetected[0];
    }

    public int getDirection() {
        return facingRight ? 1 : -1;
    }

    public float getGameTime() {
        return gameTime;
    }

    /**
     *
     * @param start raycasting starting point.
     * @param end raycasting end point.
     * @return boolean indicating grounding status.
     */
    private boolean checkForGround(Vector2 start, Vector2 end) {
        final boolean[] isGrounded = {false};

        gameWorld.rayCast((fixture, point, normal, fraction) -> {
            if (fixture.getBody().getUserData() != null && fixture.getBody().getUserData().equals("ground")) {
                isGrounded[0] = true;

                return 0;
            }
            return -1;
        }, start, end);

        return isGrounded[0];
    }

    /**
     * Uses raycasting to check if the enemy unit is nearing an edge.
     * @param direction indicates the direction in which to check for ground.
     * @return
     */
    public boolean isGroundAhead(float direction) {
        Vector2 position = body.getPosition();
        float rayLength = 3f;

        Vector2 rayStart = new Vector2(position.x + (direction * AppConfig.ENEMY_GROUNDCHECK_FORWARD_OFFSET), position.y - (height / 2));
        Vector2 rayEnd = new Vector2(rayStart.x, rayStart.y - rayLength);

        return checkForGround(rayStart, rayEnd);
    }

    public boolean isGrounded() {
        return isGrounded;
    }
}
