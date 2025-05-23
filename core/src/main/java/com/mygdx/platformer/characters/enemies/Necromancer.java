package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.attacks.pcg.CompoundAttack;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

/**
 * Represents a Necromancer enemy in the game.
 * The Necromancer has idle and attack animations and follows a predefined movement pattern.
 * It extends {@link BaseEnemy}, inheriting core enemy behaviors such as physics and health management.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public class Necromancer extends BaseEnemy {

    /** The animation for idling. */
    private Animation<TextureRegion> idleAnimation;
    /** The animation for attacking. */
    private Animation<TextureRegion> attackAnimation;
    private CompoundAttack attackSequence;

    /**
     * Creates a new Necromancer enemy at the specified position, health, and
     * speed in the game world.
     *
     * @param world The Box2D world where the necromancer exists.
     * @param position The initial position of the necromancer.
     * @param health The Necromancers health.
     * @param speed The Necromancers speed.
     */
    public Necromancer(World world, Vector2 position, int health, float speed
        , CompoundAttack attackSequence) {
        super(world, position, health, speed, AppConfig.NECROMANCER_WIDTH,
            AppConfig.NECROMANCER_HEIGHT);
        this.attackSequence = attackSequence;
    }

    /**
     * Updates the necromancer's state and animation. The animation updates
     * based on the elapsed time.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (isAttacking()) {
            currentFrame = attackAnimation.getKeyFrame(stateTime, false);
        } else {
            currentFrame = idleAnimation.getKeyFrame(stateTime);
        }
    }

    /**
     * Initializes the necromancer's animations from the texture atlas. Sets
     * up idle and attack animations.
     */
    @Override
    protected void setupAnimations() {
        TextureAtlas textureAtlas = Assets.getNecromancerAtlas();

        idleAnimation = new Animation<>(AppConfig.STANDARD_FRAME_DURATION,
            textureAtlas.findRegions(AppConfig.NECROMANCER_IDLE),
            Animation.PlayMode.LOOP);
        attackAnimation = new Animation<>(AppConfig.ATTACK_FRAME_DURATION,
            textureAtlas.findRegions(AppConfig.NECROMANCER_ATTACK),
            Animation.PlayMode.NORMAL);
        currentFrame = idleAnimation.getKeyFrame(0);
    }

    /**
     * Gets the character type.
     *
     * @return The character type.
     */
    @Override
    public AppConfig.CharacterType getCharacterType() {
        return AppConfig.CharacterType.NECROMANCER;
    }

    /**
     * Returns the size of the necromancer's hitbox.
     *
     * @return A {@code Vector2} representing the width and height of the
     * hitbox.
     */
    @Override
    protected Vector2 getHitBoxSize() {
        return new Vector2(AppConfig.NECROMANCER_HITBOX_SIZE_X,
            AppConfig.NECROMANCER_HITBOX_SIZE_Y);
    }

    /**
     * Accessor method for getting the sprite scale.
     * @return The scaling factor for the necromancer sprite.
     */
    @Override
    protected float getScale() {
        return AppConfig.NECROMANCER_SCALE;
    }

    /**
     * {@inheritDoc}
     * @return
     */
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

    /**
     * Accessor method for getting the attack sequence.
     * @return The attack sequence.
     */ 
    public CompoundAttack getAttackSequence() {
        return attackSequence;
    }
}
