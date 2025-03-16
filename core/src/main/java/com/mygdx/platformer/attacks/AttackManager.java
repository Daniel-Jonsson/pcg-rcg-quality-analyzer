package com.mygdx.platformer.attacks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.sound.AudioManager;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages all projectile attacks in the game world.
 * <p>
 * The {@code AttackManager} is responsible for the complete lifecycle of attack
 * objects:
 * <ul>
 * <li>Spawning different types of attacks (player and enemy)</li>
 * <li>Updating attack positions and states</li>
 * <li>Rendering active attacks</li>
 * <li>Removing attacks when they hit targets or leave the visible area</li>
 * <li>Scaling attack damage and speed based on difficulty level</li>
 * </ul>
 * </p>
 * <p>
 * The manager works with the Box2D physics system to create and destroy attack
 * bodies,
 * and integrates with the game's audio system to play appropriate sound effects
 * when
 * attacks are spawned.
 * </p>
 * <p>
 * Attack types are defined in {@link AppConfig.AttackType} and include player
 * throwing daggers,
 * goblin projectiles, and necromancer death bolts, each with their own visual
 * appearance,
 * damage values, and sound effects.
 * </p>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class AttackManager {
    /** List of all active attacks currently in the game world. */
    private final List<BaseAttack> attacks;

    /** Reference to the Box2D physics world. */
    private final World world;

    /** Difficulty multiplier that scales enemy attack damage and speed. */
    private float multiplier = 1.0f;

    /**
     * Constructs an instance of {@code AttackManager}, responsible for
     * handling attacks within the given world.
     *
     * @param world The Box2D world where attacks will be spawned and managed.
     */
    public AttackManager(World world) {
        this.world = world;
        this.attacks = new ArrayList<>();
    }

    /**
     * Spawns an attack at the given position with a specified direction.
     * <p>
     * This method creates a new attack object based on the specified type,
     * initializes it
     * with the appropriate properties (position, direction, damage, speed), plays
     * the
     * corresponding sound effect, and adds it to the list of active attacks.
     * </p>
     * <p>
     * Enemy attack damage and speed are scaled by the current difficulty
     * multiplier.
     * </p>
     *
     * @param position          The position where the attack should be created.
     * @param directionModifier The direction in which the attack moves (e.g
     *                          ., -1 for left, 1 for right).
     * @param isPlayerAttack    Whether the attack is a player attack.
     * @param attackType        The type of attack to spawn, defined in
     *                          {@link AppConfig.AttackType}.
     */
    public void spawnAttackAt(Vector2 position, int directionModifier, boolean isPlayerAttack,
            AppConfig.AttackType attackType) {
        BaseAttack attack;
        int dmg;
        int speed;
        switch (attackType) {
            case PLAYER_THROWING_DAGGER:
                attack = new PlayerAttack(world, position.x, position.y,
                        Assets.assetManager.get(Assets.THROWING_DAGGER_TEXTURE), directionModifier, isPlayerAttack);
                AudioManager.playSound("swoosh");
                break;
            case GOBLIN_THROWING_DAGGER:
                dmg = (int) (AppConfig.GOBLIN_ATTACK_POWER * multiplier);
                speed = (int) (AppConfig.GOBLIN_ATTACK_SPEED * multiplier);
                attack = new GoblinAttack(world, position.x, position.y, directionModifier, dmg, speed);
                AudioManager.playSound("swoosh2");
                break;
            case DEATH_BOLT:
                dmg = (int) (AppConfig.NECROMANCER_ATTACK_POWER * multiplier);
                speed = (int) (AppConfig.NECROMANCER_ATTACK_SPEED * multiplier);
                attack = new NecromancerAttack(world, position.x, position.y, directionModifier, dmg, speed);
                AudioManager.playSound("deathbolt");
                break;

            default:
                attack = new PlayerAttack(world, position.x, position.y,
                        Assets.assetManager.get(Assets.THROWING_DAGGER_TEXTURE), directionModifier, false);
                break;
        }

        attacks.add(attack);
    }

    /**
     * Convenience method to spawn an enemy attack.
     * <p>
     * This is a wrapper around {@link #spawnAttackAt} that automatically sets
     * the isPlayerAttack parameter to false.
     * </p>
     *
     * @param position          The position where the attack should be created.
     * @param directionModifier The direction in which the attack moves (-1 for
     *                          left, 1 for right).
     * @param attackType        The type of enemy attack to spawn.
     */
    public void spawnEnemyAttackAt(Vector2 position, int directionModifier, AppConfig.AttackType attackType) {
        spawnAttackAt(position, directionModifier, false, attackType);
    }

    /**
     * Updates all active attacks, removing any that should be destroyed.
     * <p>
     * This method iterates through all active attacks, updates their state, and
     * removes
     * attacks that have either hit a target or moved outside the current viewport.
     * When an attack is removed, its corresponding physics body is also destroyed.
     * </p>
     *
     * @param cameraX       The current X-position of the camera, used for
     *                      destroying
     *                      off-screen attacks.
     * @param viewPortWidth The width of the viewport, used to determine
     *                      visibility of attacks.
     */
    public void update(float cameraX, float viewPortWidth) {
        Iterator<BaseAttack> iterator = attacks.iterator();
        while (iterator.hasNext()) {
            BaseAttack attack = iterator.next();
            attack.update(cameraX, viewPortWidth);

            if (attack.shouldRemove()) {
                iterator.remove();
                world.destroyBody(attack.body);
            }
        }
    }

    /**
     * Renders all active attacks using the provided sprite batch.
     * <p>
     * This method iterates through all active attacks and calls their
     * individual render methods to draw them on screen.
     * </p>
     *
     * @param batch The sprite batch used for rendering attacks.
     */
    public void render(SpriteBatch batch) {
        for (BaseAttack attack : attacks) {
            attack.render(batch);
        }
    }

    /**
     * Increases the difficulty of enemy attacks by scaling their damage and speed.
     * <p>
     * This method adjusts the multiplier used to scale enemy attack properties
     * based on the current game difficulty level. Higher difficulty levels result
     * in more powerful and faster enemy attacks.
     * </p>
     *
     * @param difficulty The current difficulty level in the game.
     */
    public void increaseDifficulty(int difficulty) {
        multiplier = 1.0f + (difficulty * AppConfig.DIFFICULTY_INCREASE_AMOUNT);
    }
}
