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
    World world;

    private Sprite healthBarSprite;

    protected boolean isAttacking = false;
    private float attackAnimationTime;
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
        stateTime += deltaTime;

        if (isGrounded()) {
            body.setLinearVelocity(moveDirection * movementSpeed, body.getLinearVelocity().y);
        } else {
            float currentXVelocity = getBody().getLinearVelocity().x;
            float currentYVelocity = getBody().getLinearVelocity().y;

            if (Math.abs(currentXVelocity) < movementSpeed * 3) {
                float direction = facingRight ? 1f : -1f;
                body.setLinearVelocity(direction * (movementSpeed * 3), currentYVelocity);
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
    public boolean isGroundAhead(float direction) {
        Vector2 enemyPosition = getBody().getPosition();
        float rayLength = 1f;

        Vector2 rayStart = new Vector2(enemyPosition.x + (direction * 0.2f), enemyPosition.y - (height/2));
        Vector2 rayEnd = new Vector2(rayStart.x, rayStart.y - rayLength);

        return checkForGround(rayStart, rayEnd);
    }



    public void startAttack() {
        isAttacking = true;
        attackAnimationTime = 0; // Reset animation timer
        onAttackStart();
    }

    public void stopAttack() {
        isAttacking = false;
        onAttackEnd();
    }

    protected abstract void onAttackStart();

    protected abstract void onAttackEnd();

    public boolean isAttacking() {
        return isAttacking;
    }

    protected float getAttackDuration() {
        return 0.5f;
    }

    public void setFacingDirection(float moveDirection) {
        facingRight = moveDirection > 0;
    }

    public boolean canJumpToPlatform(float direction) {
        Vector2 enemyPosition = getBody().getPosition();
        float jumpCheckDistance = 6f;

        Vector2 rayStart = new Vector2(enemyPosition.x + (direction * jumpCheckDistance), enemyPosition.y);
        Vector2 rayEnd = new Vector2(rayStart.x, rayStart.y - 1.0f);

        return checkForGround(rayStart, rayEnd);
    }

    public void jump() {
        if (isGrounded() && !hasJumped) {
            hasJumped = true;
            float jumpForce = 70f;
            float forwardBoost = 20f;

            float forwardDirection = facingRight ? 1f : -1f;

            float currentXVelocity = getBody().getLinearVelocity().x;

            getBody().applyLinearImpulse(new Vector2(currentXVelocity + (forwardDirection * forwardBoost), jumpForce),
                getBody().getWorldCenter(), true);
        }
    }

    public boolean isGrounded() {
        Vector2 enemyPosition = getBody().getPosition();
        float rayLength = 1.0f;

        Vector2 rayStart = new Vector2(enemyPosition.x, enemyPosition.y - (height / 2));
        Vector2 rayEnd = new Vector2(rayStart.x, rayStart.y - rayLength);

        return checkForGround(rayStart, rayEnd);
    }

    private boolean checkForGround(Vector2 start, Vector2 end) {
        final boolean[] isGrounded = {false};

        world.rayCast((fixture, point, normal, fraction) -> {
            if (fixture.getBody().getUserData() != null && fixture.getBody().getUserData().equals("ground")) {
                isGrounded[0] = true;

                return 0;
            }
            return -1;
        }, start, end);

        return isGrounded[0];
    }

}
