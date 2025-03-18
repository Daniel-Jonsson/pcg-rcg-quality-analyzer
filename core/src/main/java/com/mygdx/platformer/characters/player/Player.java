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
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.platformer.ai.autoplay.AutoPlayAgent;
import com.mygdx.platformer.attacks.AttackManager;
import com.mygdx.platformer.attacks.BaseAttack;
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

    OrthographicCamera camera;

    private boolean attackTriggered = false;

    private float attackAnimationTimer = 0.0f;

    private boolean isDodging = false;

    public long inputTimestamp = 0;

    /**
     * Instantiates the player in the game world.
     * @param world The Box2D world.
     * @param position The player position.
     * @param health The player maxHealth.
     * @param movementSpeed The movement speed of the player.
     * @param manager the AttackManager for spawning attacks.
     */
    public Player(World world, Vector2 position,
                  int health, float movementSpeed, AttackManager manager, boolean autoPlay, OrthographicCamera camera) {
        super(world, position, health, movementSpeed, AppConfig.PLAYER_WIDTH,
            AppConfig.PLAYER_HEIGHT);
        this.attackManager = manager;
        this.camera = camera;

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
        if (attackTriggered) {
            currentFrame = attackAnimation.getKeyFrame(stateTime);
            attackAnimationTimer += deltaTime;
            if (attackAnimationTimer > 0.1f) {
                attackTriggered = false;
                attackAnimationTimer = 0.0f;
            }
        } else if (!isGrounded) {
            currentFrame = jumpAnimation.getKeyFrame(stateTime);
        }  else if (moveDirection != 0) {
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
     * @param deltaTime Time passed since last physics update.
     */
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
            inputTimestamp = TimeUtils.nanoTime();
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
        return AppConfig.CATEGORY_PLATFORM | AppConfig.CATEGORY_ATTACK;
    }

    /**
     * Returns the player scale used for rendering sprites.
     * @return The player scale.
     */
    @Override
    protected float getScale() {
        return AppConfig.PLAYER_SCALE;
    }

    /**
     * Triggers a jump by setting the pertinent flags.
     */
    public void jump() {
        if (isGrounded) {
            jumpRequested = true;
            jumpHolding = true;
        }
    }

    /**
     * Performs an attack by spawning an attack via the attackManager instance. The method also
     * sets the attackTriggered flag to allow correct animation handling in the update() method.
     */
    public void attack() {
        attackManager.spawnAttackAt(
            new Vector2(body.getPosition().x, body.getPosition().y + AppConfig.PLAYER_ATTACK_Y_OFFSET),
            getFacingDirection(),
            true,
            AppConfig.AttackType.PLAYER_THROWING_DAGGER
        );
        attackTriggered = true;
    }

    /**
     * Moves the player forward.
     */
    public void moveForward() {
        moveDirection = 1;
        facingRight = true;

    }

    /**
     * Moves the player backward.
     */
    public void moveBackward() {
        moveDirection = -1;
        facingRight = false;
    }

    /**
     * Stops player movement.
     */
    public void stop() {
        moveDirection = 0;
    }

    /**
     * Makes the player dodge an incoming projectile.
     */
    public void dodge() {

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
        float rayLength = AppConfig.AUTO_PLAY_ENEMY_DETECTION_RANGE;

        Vector2 rayStart = new Vector2(playerPosition.x-1, playerPosition.y);
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

    /**
     * Accessor method for the facingRight flag.
     * @return The current value of the boolean facingRight flag.
     */
    public int getFacingDirection() {
        return facingRight ? 1 : -1;
    }

    /**
     * Accessor method for the gameTime field.
     * @return The current value of the gameTime field.
     */
    public float getGameTime() {
        return gameTime;
    }

    /**
     * Uses raycasting to check if the enemy unit is nearing an edge.
     * @param direction indicates the direction in which to check for ground.
     * @return
     */
    public boolean isGroundAhead(float direction) {
        Vector2 position = body.getPosition();
        float rayLength = 3f;

        boolean isGround = false;
        // first ray
        Vector2 rayStart = new Vector2(position.x + AppConfig.PLAYER_GROUNDCHECK_FORWARD_OFFSET, position.y - (height / 2));
        Vector2 rayEnd = new Vector2(rayStart.x, rayStart.y - rayLength);

        Vector2 rayStart2 = new Vector2(position.x, position.y - (height / 2));
        Vector2 rayEnd2 = new Vector2(rayStart.x, rayStart.y - rayLength);


        if(checkForGround(gameWorld, rayStart, rayEnd) && checkForGround(gameWorld, rayStart2, rayEnd2)){
            isGround = true;
        }



        return isGround;
    }

    /**
     * Accessor method for the isGrounded flag.
     * @return The current value of the boolean flag indicating grounding.
     */
    public boolean isGrounded() {
        return isGrounded;
    }

    /**
     * Uses raycasting to detect the edge of the next platform. This method serves
     * to stop the player in air during auto-play to ensure a safe landing when jumping.
     * @return A Vector2 position indicating the point where the ray hits a platform.
     */
    public Vector2 getNextPlatformPosition() {
        Vector2 playerPos = getBody().getPosition();

        Vector2 rayStart = new Vector2(playerPos.x, playerPos.y);

        Vector2 rayEnd = new Vector2(rayStart.x, rayStart.y-10);

        final Vector2[] platformPos = { null };

        gameWorld.rayCast((fixture, point, normal, fraction) -> {
            // Check if the fixture belongs to a platform
            if (fixture.getBody().getUserData() != null &&
                fixture.getBody().getUserData().equals("ground")) {
                platformPos[0] = new Vector2(point.x, point.y);
                return 0; // Stop the raycast after the first hit
            }
            return -1; // Continue raycasting
        }, rayStart, rayEnd);;

        return platformPos[0];
    }

    /**
     * Mutator for the facingRight flag.
     * @param facingRight The new value to be set to the facingRight boolean.
     */
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    /**
     * Accessor for player's current health.
     * @return The current health of the player.
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Accessor for the player's maximum health.
     * @return The max health of the player.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Detects incoming projectiles in both directions using raycasting. Multiple
     * rays are used to cover the height of the player.
     * @return boolean indicating if an incoming projectile is detected.
     */
    public boolean detectIncomingProjectile() {
        int NUM_RAYS = AppConfig.AUTO_PLAY_NUMBER_OF_PROJECTILE_DETECTION_RAYS;      // Number of rays along the player's height.
        float RAY_LENGTH = AppConfig.AUTO_PLAY_PROJECTILE_DETECTION_RANGE; // How far to cast each ray (in world units).

        Vector2 playerPos = getBody().getPosition();

        float playerHeight = getHitBoxSize().y;
        boolean detected = false;

        for (int i = 0; i < NUM_RAYS; i++) {
            // Distribute rays
            float fraction =  (float) i / (NUM_RAYS - 1);
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
     * Helper method that casts a ray between two points and returns true if an incoming projectile is detected.
     */
    private boolean castRayForProjectile(Vector2 start, Vector2 end) {
        final boolean[] hit = { false };
        int facing = getFacingDirection();
        gameWorld.rayCast(new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                Object data = fixture.getBody().getUserData();
                if (data instanceof BaseAttack attack) {
                    if (!attack.isPlayerAttack()) {

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
     * Mutator for the dodging flag.
     * @param dodging the value to set the isDodging flag to.
     */
    public void setDodging(boolean dodging) {
        isDodging = dodging;
    }

    /**
     * Accessor for the isDodging flag.
     * @return The current value of the boolean isDodging flag.
     */
    public boolean isDodging() {
        return isDodging;
    }

    public boolean jumpTriggered() {
        return this.jumpRequested;
    }

    public void setJumpTriggered(boolean jumpTriggered) {
        this.jumpRequested = jumpTriggered;
    }
}
