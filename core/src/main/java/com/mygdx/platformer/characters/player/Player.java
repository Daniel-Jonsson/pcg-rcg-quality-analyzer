package com.mygdx.platformer.characters.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.ai.autoplay.AutoPlayAgent;
import com.mygdx.platformer.attacks.AttackManager;
import com.mygdx.platformer.attacks.BaseAttack;
import com.mygdx.platformer.characters.BaseCharacter;
import com.mygdx.platformer.characters.enemies.BaseEnemy;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

/**
 * Represents the player character in the game world.
 * <p>
 * The Player class extends BaseCharacter and implements all functionality
 * specific
 * to the player-controlled character, including:
 * <ul>
 * <li>Movement controls and physics</li>
 * <li>Variable-height jumping mechanics</li>
 * <li>Attack capabilities</li>
 * <li>Animation state management</li>
 * <li>Collision detection with platforms and enemies</li>
 * <li>Auto-play AI integration for demo mode</li>
 * </ul>
 * </p>
 * <p>
 * The player can be controlled either through keyboard input or via the
 * AutoPlayAgent when auto-play mode is enabled.
 * </p>
 *
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class Player extends BaseCharacter {

    /** Tracks whether the player is currently on the ground. */
    private boolean isGrounded = false;

    /** Whether a jump request has been triggered. */
    private boolean jumpRequested = false;

    /** Whether the jump button is currently being held down. */
    private boolean jumpHolding = false;

    /** Tracks whether the jump key was pressed in the last frame. */
    private boolean wasJumpKeyPressed = false;

    /** Tracks how long the jump button has been held down. */
    private float jumpHoldTime = 0;

    /** Manages the player's attacks. */
    private final AttackManager attackManager;

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

    /** Indicates whether auto-play is enabled. */
    private final boolean autoPlayEnabled;

    /** The AI agent that controls the player in auto-play mode. */
    private AutoPlayAgent autoPlayAgent;

    /** Reference to the game world for physics and raycasting operations. */
    World gameWorld;

    /** Tracks the total game time for various time-based effects. */
    private float gameTime = 0.0f;

    /** Reference to the game camera for positioning and viewport calculations. */
    OrthographicCamera camera;

    /** Flag indicating whether an attack has been triggered. */
    private boolean attackTriggered = false;

    /** Timer for controlling attack animation duration. */
    private float attackAnimationTimer = 0.0f;

    /** Flag indicating whether the player is currently dodging. */
    private boolean isDodging = false;

    /**
     * Instantiates the player in the game world.
     *
     * @param world         The Box2D world for physics simulation.
     * @param position      The initial position of the player.
     * @param health        The player's maximum health.
     * @param movementSpeed The movement speed of the player.
     * @param manager       The AttackManager for spawning attacks.
     * @param autoPlay      Whether auto-play mode is enabled.
     * @param camera        The game camera for viewport calculations.
     */
    public Player(World world, Vector2 position,
            int health, float movementSpeed, AttackManager manager, boolean autoPlay, OrthographicCamera camera) {
        super(world, position, health, movementSpeed, AppConfig.PLAYER_WIDTH,
                AppConfig.PLAYER_HEIGHT);
        this.attackManager = manager;
        this.camera = camera;
        body.setUserData(this);

        MassData massData = new MassData();
        massData.mass = AppConfig.PLAYER_MASS;
        body.setMassData(massData);
        facingRight = true;

        autoPlayEnabled = autoPlay;
        this.gameWorld = world;

        if (autoPlayEnabled) {
            this.autoPlayAgent = new AutoPlayAgent(this, camera);
        }
    }

    /**
     * Updates the player state for the current frame.
     * <p>
     * This method handles animation updates, jump mechanics, and delegates to
     * the auto-play agent when enabled.
     * </p>
     *
     * @param deltaTime Time elapsed since the last frame in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        gameTime += deltaTime;

        if (gameTime > 10000f) {
            gameTime = 0;
        }

        if (autoPlayEnabled && autoPlayAgent != null) {
            autoPlayAgent.update(deltaTime);
        }

        checkJumpStatus(deltaTime);

        // Determine animation state
        if (attackTriggered) {
            currentFrame = attackAnimation.getKeyFrame(stateTime);
            attackAnimationTimer += deltaTime;
            if (attackAnimationTimer > 0.1f) {
                attackTriggered = false;
                attackAnimationTimer = 0.0f;
            }
        } else if (!isGrounded) {
            currentFrame = jumpAnimation.getKeyFrame(stateTime);
        } else if (moveDirection != 0) {
            currentFrame = walkAnimation.getKeyFrame(stateTime);
        } else {
            currentFrame = idleAnimation.getKeyFrame(stateTime);
        }

    }

    /**
     * Checks whether a jump has been requested, subsequently applying force to
     * execute the jump. Because jump height is variable, a check is also performed
     * to determine whether the jump button is being held down, and
     * additional force should be applied.
     *
     * @param deltaTime Time passed since last physics update in seconds.
     */
    private void checkJumpStatus(float deltaTime) {
        if (jumpRequested) {
            float jumpForce = AppConfig.PLAYER_JUMP_FORCE;
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
     * Sets up the animations for the Player.
     * <p>
     * Loads the sprite regions from the texture atlas and initializes
     * animations for different player states (idle, walking, jumping, attacking).
     * </p>
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
     * Returns the character type of this entity.
     *
     * @return The character type, which is {@link AppConfig.CharacterType#PLAYER}.
     */
    @Override
    public AppConfig.CharacterType getCharacterType() {
        return AppConfig.CharacterType.PLAYER;
    }

    /**
     * Handles player input from keyboard.
     * <p>
     * Processes key presses for movement (A/D), jumping (SPACE), and
     * attacking (R). This method is only effective when auto-play is disabled.
     * </p>
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
     * Releases resources used by the player.
     * <p>
     * This method should be called when the player is no longer needed
     * to prevent memory leaks.
     * </p>
     */
    public void dispose() {
        playerAtlas.dispose();
    }

    /**
     * Sets whether the player is on the ground or not.
     *
     * @param grounded Boolean indicating whether the player is on the ground.
     */
    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    /**
     * Accessor for the player's physics body.
     *
     * @return The Box2D physics body of the player.
     */
    public Body getBody() {
        return body;
    }

    /**
     * Defines the hitbox size for the player character.
     *
     * @return A {@code Vector2} representing the hitbox width and height.
     */
    @Override
    protected Vector2 getHitBoxSize() {
        return new Vector2(AppConfig.PLAYER_HITBOX_SIZE_X,
                AppConfig.PLAYER_HITBOX_SIZE_Y);
    }

    /**
     * Returns the collision category for the player character.
     *
     * @return A short representing the collision category.
     */
    @Override
    protected short getCollisionCategory() {
        return AppConfig.CATEGORY_PLAYER;
    }

    /**
     * Returns the collision mask for the player character.
     *
     * @return A short representing what the player can collide with.
     */
    @Override
    protected short getCollisionMask() {
        return AppConfig.CATEGORY_PLATFORM | AppConfig.CATEGORY_ATTACK;
    }

    /**
     * Returns the player scale used for rendering sprites.
     *
     * @return The player scale factor.
     */
    @Override
    protected float getScale() {
        return AppConfig.PLAYER_SCALE;
    }

    /**
     * Triggers a jump by setting the pertinent flags.
     * <p>
     * This method will only initiate a jump if the player is currently grounded.
     * </p>
     */
    public void jump() {
        if (isGrounded) {
            jumpRequested = true;
            jumpHolding = true;
        }
    }

    /**
     * Performs an attack by spawning a projectile.
     * <p>
     * This method uses the attackManager to create a throwing dagger projectile
     * and sets the attackTriggered flag to trigger the attack animation.
     * </p>
     */
    public void attack() {
        attackManager.spawnAttackAt(
                new Vector2(body.getPosition().x, body.getPosition().y + AppConfig.PLAYER_ATTACK_Y_OFFSET),
                getFacingDirection(),
                true,
                AppConfig.AttackType.PLAYER_THROWING_DAGGER);
        attackTriggered = true;
    }

    /**
     * Moves the player forward (right).
     * <p>
     * Sets the movement direction and updates the facing direction.
     * </p>
     */
    public void moveForward() {
        moveDirection = 1;
        facingRight = true;

    }

    /**
     * Moves the player backward (left).
     * <p>
     * Sets the movement direction and updates the facing direction.
     * </p>
     */
    public void moveBackward() {
        moveDirection = -1;
        facingRight = false;
    }

    /**
     * Stops player movement.
     * <p>
     * Sets the movement direction to zero, causing the player to stop moving.
     * </p>
     */
    public void stop() {
        moveDirection = 0;
    }

    /**
     * Makes the player dodge an incoming projectile.
     * <p>
     * This method is used by the auto-play AI to avoid enemy attacks.
     * </p>
     */
    public void dodge() {
        // Implementation to be added
    }

    /**
     * Checks if the path ahead is clear of obstacles.
     * <p>
     * This method is used by the auto-play AI for navigation.
     * </p>
     *
     * @return True if the path is clear, false otherwise.
     */
    public boolean isPathClear() {
        // Implementation to be added
        return false;
    }

    /**
     * Uses raycasting to check if an enemy is within sight range.
     *
     * @param direction The direction to check (1 for right, -1 for left).
     * @return True if an enemy is detected in the specified direction.
     */
    public boolean hasEnemiesNearby(float direction) {
        Vector2 playerPosition = body.getPosition();
        float rayLength = AppConfig.AUTO_PLAY_ENEMY_DETECTION_RANGE;

        Vector2 rayStart = new Vector2(playerPosition.x, playerPosition.y);
        Vector2 rayEnd = new Vector2(playerPosition.x + (direction * rayLength), playerPosition.y);

        return checkForEnemy(rayStart, rayEnd);
    }

    /**
     * Casts a ray in a given direction to detect enemies.
     *
     * @param start The start position of the ray.
     * @param end   The end position of the ray.
     * @return True if an enemy is detected.
     */
    private boolean checkForEnemy(Vector2 start, Vector2 end) {
        final boolean[] enemyDetected = { false };

        gameWorld.rayCast((fixture, point, normal, fraction) -> {
            if (fixture.getBody().getUserData() instanceof BaseEnemy) {
                enemyDetected[0] = true;
                return 0; // Stop raycast after finding an enemy
            }
            return -1;
        }, start, end);

        return enemyDetected[0];
    }

    /**
     * Gets the player's facing direction as an integer.
     *
     * @return 1 if facing right, -1 if facing left.
     */
    public int getFacingDirection() {
        return facingRight ? 1 : -1;
    }

    /**
     * Gets the current game time.
     *
     * @return The current game time in seconds.
     */
    public float getGameTime() {
        return gameTime;
    }

    /**
     * Uses raycasting to check if there is ground ahead in the specified direction.
     * <p>
     * This method is used to detect edges and prevent the player from walking off
     * platforms.
     * </p>
     *
     * @param direction The direction to check (1 for right, -1 for left).
     * @return True if ground is detected ahead, false otherwise.
     */
    public boolean isNotGroundAhead(float direction) {
        Vector2 position = body.getPosition();
        float rayLength = 3f;

        boolean isGround = false;
        // first ray
        Vector2 rayStart = new Vector2(position.x + AppConfig.PLAYER_GROUNDCHECK_FORWARD_OFFSET,
                position.y - (height / 2));
        Vector2 rayEnd = new Vector2(rayStart.x, rayStart.y - rayLength);

        Vector2 rayStart2 = new Vector2(position.x, position.y - (height / 2));
        Vector2 rayEnd2 = new Vector2(rayStart.x, rayStart.y - rayLength);

        if (checkForGround(gameWorld, rayStart, rayEnd) && checkForGround(gameWorld, rayStart2, rayEnd2)) {
            isGround = true;
        }

        return !isGround;
    }

    /**
     * Checks if the player is currently on the ground.
     *
     * @return True if the player is grounded, false if in the air.
     */
    public boolean isGrounded() {
        return isGrounded;
    }

    /**
     * Uses raycasting to detect the edge of the next platform.
     * <p>
     * This method serves to stop the player in air during auto-play to ensure
     * a safe landing when jumping.
     * </p>
     *
     * @return A Vector2 position indicating the point where the ray hits a
     *         platform,
     *         or null if no platform is detected.
     */
    public Vector2 getNextPlatformPosition() {
        Vector2 playerPos = getBody().getPosition();

        Vector2 rayStart = new Vector2(playerPos.x, playerPos.y);

        Vector2 rayEnd = new Vector2(rayStart.x, rayStart.y - 10);

        final Vector2[] platformPos = { null };

        gameWorld.rayCast((fixture, point, normal, fraction) -> {
            // Check if the fixture belongs to a platform
            if (fixture.getBody().getUserData() != null &&
                    fixture.getBody().getUserData().equals("ground")) {
                platformPos[0] = new Vector2(point.x, point.y);
                return 0; // Stop the raycast after the first hit
            }
            return -1; // Continue raycasting
        }, rayStart, rayEnd);

        return platformPos[0];
    }

    /**
     * Sets the player's facing direction.
     *
     * @param facingRight True to face right, false to face left.
     */
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    /**
     * Gets the player's current health.
     *
     * @return The current health value.
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Gets the player's maximum health.
     *
     * @return The maximum health value.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Detects incoming projectiles using raycasting.
     * <p>
     * Multiple rays are used to cover the height of the player, checking in both
     * forward and backward directions for enemy projectiles.
     * </p>
     *
     * @return True if an incoming projectile is detected, false otherwise.
     */
    public boolean detectIncomingProjectile() {
        int NUM_RAYS = AppConfig.AUTO_PLAY_NUMBER_OF_PROJECTILE_DETECTION_RAYS; // Number of rays along the player's
                                                                                // height.
        float RAY_LENGTH = AppConfig.AUTO_PLAY_PROJECTILE_DETECTION_RANGE; // How far to cast each ray (in world units).

        Vector2 playerPos = getBody().getPosition();

        float playerHeight = getHitBoxSize().y;
        boolean detected = false;

        for (int i = 0; i < NUM_RAYS; i++) {
            // Distribute rays
            float fraction = (float) i / (NUM_RAYS - 1);
            float offsetY = fraction * playerHeight;
            Vector2 rayStart = new Vector2(playerPos.x, playerPos.y + offsetY);

            // Check in both forward and backward directions.
            int facingDir = getFacingDirection();

            // Forward detection
            Vector2 rayEndForward = new Vector2(rayStart.x + (facingDir * RAY_LENGTH), rayStart.y);
            if (castRayForProjectile(rayStart, rayEndForward)) {
                detected = true;
                break;
            }

            // Backward detection
            Vector2 rayEndBackward = new Vector2(rayStart.x - (facingDir * RAY_LENGTH), rayStart.y);
            if (castRayForProjectile(rayStart, rayEndBackward)) {
                detected = true;
                break;
            }
        }
        return detected;
    }

    /**
     * Helper method that casts a ray to detect enemy projectiles.
     * <p>
     * This method checks if there are any enemy attacks along the ray path
     * that are moving toward the player.
     * </p>
     *
     * @param start The start position of the ray.
     * @param end   The end position of the ray.
     * @return True if an incoming projectile is detected, false otherwise.
     */
    private boolean castRayForProjectile(Vector2 start, Vector2 end) {
        final boolean[] hit = { false };
        int facing = getFacingDirection();
        gameWorld.rayCast(new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                Object data = fixture.getBody().getUserData();
                if (data instanceof BaseAttack attack) {
                    if (attack.isEnemyAttack()) {

                        Vector2 projVelocity = attack.getBody().getLinearVelocity();
                        if ((body.getPosition().x < attack.getBody().getPosition().x && projVelocity.x < 0)
                                || (body.getPosition().x > attack.getBody().getPosition().x && projVelocity.x > 0)) {
                            hit[0] = true;
                            return 0; // Stop the raycast.
                        }
                    }
                }
                return -1;
            }
        }, start, end);
        return hit[0];
    }

    /**
     * Sets whether the player is currently dodging.
     *
     * @param dodging True if the player is dodging, false otherwise.
     */
    public void setDodging(boolean dodging) {
        isDodging = dodging;
    }

    /**
     * Checks if the player is currently dodging.
     *
     * @return True if the player is dodging, false otherwise.
     */
    public boolean isDodging() {
        return isDodging;
    }
}
