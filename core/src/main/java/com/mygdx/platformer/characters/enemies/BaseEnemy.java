package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
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

    /** The Box2D world where the {@code BaseEnemy} exist.*/
    World world;
    /** The health bar sprite of the {@code BaseEnemy}.*/
    private final Sprite healthBarSprite;
    /** Indicates whether the enemy is attacking or not.*/
    protected boolean isAttacking = false;
    /**Indicates the elapsed time during enemy attack. Used to display
     * correct animation.*/
    private float attackAnimationTime;
    /** Indicates if the enemy has jumped or not.*/
    private boolean hasJumped = false;


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

        this.world = world;
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
            body.getPosition().y + (height * getScale() / 2) + AppConfig.PLAYER_HEALTHBAR_Y_OFFSET
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
        stateTime += deltaTime;

        if (isGrounded()) {
            body.setLinearVelocity(moveDirection * movementSpeed, body.getLinearVelocity().y);
        } else {
            float currentXVelocity = getBody().getLinearVelocity().x;
            float currentYVelocity = getBody().getLinearVelocity().y;

            if (Math.abs(currentXVelocity) < movementSpeed * AppConfig.ENEMY_JUMP_FORWARD_BOOST_MULTIPLIER) {
                float direction = facingRight ? 1f : -1f;
                body.setLinearVelocity(direction * (movementSpeed * AppConfig.ENEMY_JUMP_FORWARD_BOOST_MULTIPLIER), currentYVelocity);
            }
        }

        if (isAttacking) {
            attackAnimationTime += deltaTime;
            if (attackAnimationTime >= getAttackDuration()) {
                stopAttack();
            }
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


    /**
     * Sets the move direction of the enemy.
     * @param moveDirection The new move direction.
     */
    public void setMoveDirection(float moveDirection) {
        this.moveDirection = moveDirection;
        facingRight = moveDirection > 0;
    }

    /**
     * Uses raycasting to check if the enemy unit is nearing an edge.
     * @param direction indicates the direction in which to check for ground.
     * @return
     */
    public boolean isNotGroundAhead(float direction) {
        Vector2 enemyPosition = getBody().getPosition();
        float rayLength = 1f;

        Vector2 rayStart = new Vector2(enemyPosition.x + (direction * AppConfig.ENEMY_GROUNDCHECK_FORWARD_OFFSET), enemyPosition.y - (height / 2));
        Vector2 rayEnd = new Vector2(rayStart.x, rayStart.y - rayLength);

        return !checkForGround(world, rayStart, rayEnd);
    }

    /**
     * Sets the isAttacking flag to true and resets the animation timer.
     */
    public void startAttack() {
        isAttacking = true;
        attackAnimationTime = 0; // Reset animation timer
        onAttackStart();
    }

    /**
     * Sets the isAttacking flag to false and stops the attack.
     */
    public void stopAttack() {
        isAttacking = false;
        onAttackEnd();
    }

    /**
     * Implemented in concrete classes to set animation starting frame and
     * resetting animation stateTime.
     */
    protected abstract void onAttackStart();

    /**
     * Implemented in concrete classes to reset stateTime when
     * an attack ends.
     */
    protected abstract void onAttackEnd();


    /**
     * Accessor for the isAttacking flag.
     * @return boolean indicating attacking status.
     */
    public boolean isAttacking() {
        return isAttacking;
    }

    /**
     * Accessor for the animation attack duration.
     * @return animation attack duration (in seconds).
     */
    protected abstract float getAttackDuration();

    public void setFacingDirection(float moveDirection) {
        facingRight = moveDirection > 0;
    }

    /**
     * Uses raycasting to find platforms which can be reached by jumping.
     * @param direction the direction in which the enemy is facing.
     * @return boolean indicating jump possibility.
     */
    public boolean canJumpToPlatform(float direction) {
        Vector2 enemyPosition = getBody().getPosition();
        float jumpCheckDistance = 6f;

        Vector2 rayStart = new Vector2(enemyPosition.x + (direction * jumpCheckDistance), enemyPosition.y);
        Vector2 rayEnd = new Vector2(rayStart.x, rayStart.y - 1.0f);

        return checkForGround(world, rayStart, rayEnd);
    }

    /**
     * Performs a jump, given that the enemy is grounded.
     */
    public void jump() {
        if (isGrounded() && !hasJumped) {
            hasJumped = true;
            float jumpForce = AppConfig.ENEMY_JUMP_FORCE;
            float forwardBoost = AppConfig.ENEMY_JUMP_FORWARD_BOOST;

            float forwardDirection = facingRight ? 1f : -1f;

            float currentXVelocity = getBody().getLinearVelocity().x;

            getBody().applyLinearImpulse(new Vector2(currentXVelocity + (forwardDirection * forwardBoost), jumpForce),
                getBody().getWorldCenter(), true);
        }
    }

    /**
     * Uses raycasting to determine whether the enemy is grounded.
     * @return boolean indicating grounding status.
     */
    public boolean isGrounded() {
        Vector2 enemyPosition = getBody().getPosition();
        float rayLength = 1.0f;

        Vector2 rayStart = new Vector2(enemyPosition.x, enemyPosition.y - (height / 2));
        Vector2 rayEnd = new Vector2(rayStart.x, rayStart.y - rayLength);

        return checkForGround(world, rayStart, rayEnd);
    }

}
