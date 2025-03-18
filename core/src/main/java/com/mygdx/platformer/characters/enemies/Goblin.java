package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

/**
 * Represents a Goblin enemy in the game.
 * The Goblin has animations for idle, walking, and attacking.
 * It moves in a predefined pattern and updates its animation based on
 * movement.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public class Goblin extends BaseEnemy {

    /** The idle animation for Goblin class. **/
    private Animation<TextureRegion> idleAnimation;

    /** The walk animation for Goblin class. **/
    private Animation<TextureRegion> walkAnimation;

    /** The attack animation for Goblin class. **/
    private Animation<TextureRegion> attackAnimation;

    /**
     * Constructor for the Goblin class.
     * @param world The game world.
     * @param position The spawn position.
     * @param health Starting health.
     * @param speed The movement speed.
     */
    public Goblin(World world, Vector2 position, int health, float speed) {
        super(world, position, health, speed, AppConfig.GOBLIN_WIDTH, AppConfig.GOBLIN_HEIGHT);
    }

    /**
     * Updates the goblin's behavior and animation state. The animation
     * changes based on whether the goblin is moving or idle.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isAttacking()) {
            currentFrame = attackAnimation.getKeyFrame(stateTime, false);
        } else if (Math.abs(body.getLinearVelocity().x) > 0) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        }
    }

    /**
     * Initializes the goblin's animations from the texture atlas. Sets up
     * idle, walk, and attack animations.
     */
    @Override
    protected void setupAnimations() {
        TextureAtlas textureAtlas = Assets.getGoblinAtlas();

        idleAnimation = new Animation<>(AppConfig.STANDARD_FRAME_DURATION,
            textureAtlas.findRegions(AppConfig.GOBLIN_IDLE), Animation.PlayMode.LOOP);
        walkAnimation = new Animation<>(AppConfig.WALK_FRAME_DURATION,
            textureAtlas.findRegions(AppConfig.GOBLIN_WALK), Animation.PlayMode.LOOP);
        attackAnimation = new Animation<>(AppConfig.ATTACK_FRAME_DURATION,
            textureAtlas.findRegions(AppConfig.GOBLIN_ATTACK), Animation.PlayMode.LOOP);
        currentFrame = idleAnimation.getKeyFrame(0);
    }

    /**
     * Gets the character type.
     *
     * @return The character type.
     */
    @Override
    public AppConfig.CharacterType getCharacterType() {
        return AppConfig.CharacterType.GOBLIN;
    }

    /**
     * Returns the size of the goblin's hitbox.
     *
     * @return A {@code Vector2} representing the width and height of the
     * hitbox.
     */
    @Override
    protected Vector2 getHitBoxSize() {
        return new Vector2(AppConfig.GOBLIN_HITBOX_SIZE_X, AppConfig.GOBLIN_HITBOX_SIZE_Y);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    protected float getScale() {
        return AppConfig.GOBLIN_SCALE;
    }

    protected float getAttackDuration() {
        return attackAnimation.getAnimationDuration();
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    protected void onAttackStart() {
        currentFrame = attackAnimation.getKeyFrame(0);
        stateTime = 0;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    protected void onAttackEnd() {
        stateTime = 0;
    }

}
