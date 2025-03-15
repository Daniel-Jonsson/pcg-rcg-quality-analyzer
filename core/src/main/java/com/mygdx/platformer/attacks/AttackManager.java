package com.mygdx.platformer.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.difficulty.GameDifficultyManager;
import com.mygdx.platformer.difficulty.observer.GameDifficultyObserver;
import com.mygdx.platformer.sound.AudioManager;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The {@code AttackManager} manages all active attacks in the game. It is
 * responsible for spawning, updating, rendering, and removing attacks
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public class AttackManager {
    private final List<BaseAttack> attacks;
    private final World world;

    /**
     * Constructs an instance of {@code AttackManager}, responsible for
     * handling attacks within the given world.
     *
     * @param world The Box2D world where attacks will be spawned and managed.
     */
    public AttackManager(World world) {
        this.world = world;
        this.attacks = new ArrayList<>();
        this.orbTexture = new Texture(Assets.THROWING_DAGGER_TEXTURE);
        GameDifficultyManager.getInstance().registerObserver(this);
    }

    /**
     * Spawns an attack at the given position with a specified direction.
     *
     * @param position The position where the attack should be created.
     * @param directionModifier The direction in which the attack moves (e.g
     *                          ., -1 for left, 1 for right).
     * @param isPlayerAttack Whether the attack is a player attack.
     * @param attackType AttackType enum.
     */
    public void spawnAttackAt(Vector2 position, int directionModifier, boolean isPlayerAttack, AppConfig.AttackType attackType) {
        BaseAttack attack;

        switch (attackType) {
            case PLAYER_THROWING_DAGGER:
                attack = new PlayerAttack(world, position.x, position.y, Assets.assetManager.get(Assets.THROWING_DAGGER_TEXTURE), directionModifier, isPlayerAttack);
                AudioManager.playSound("swoosh");
                break;
            case GOBLIN_THROWING_DAGGER:
                attack = new GoblinAttack(world, position.x, position.y, Assets.assetManager.get(Assets.THROWING_DAGGER_TEXTURE), directionModifier, isPlayerAttack);
                AudioManager.playSound("swoosh2");
                break;
            case DEATH_BOLT:
                attack = new NecromancerAttack(world, position.x, position.y, Assets.assetManager.get(Assets.DEATH_BOLT), directionModifier, isPlayerAttack);
                AudioManager.playSound("deathbolt");
                break;

            default:
                attack = new PlayerAttack(world, position.x, position.y, Assets.assetManager.get(Assets.THROWING_DAGGER_TEXTURE), directionModifier, false);
                break;
        }

        attacks.add(attack);
    }

    public void spawnEnemyAttackAt(Vector2 position, int directionModifier, AppConfig.AttackType attackType) {
        spawnAttackAt(position, directionModifier, false, attackType);
    }


    /**
     * Updates all active attacks, removing any that should be destroyed. In
     * this case it is if they either 1) Hit a target or 2) Is outside current
     * viewport.
     *
     * @param cameraX The current X-position of the camera, used for destroying
     *                off-screen attacks.
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
     *
     * @param batch The sprite batch used for rendering attacks.
     */
    public void render(SpriteBatch batch) {
        for (BaseAttack attack : attacks) {
            attack.render(batch);
        }
    }

    public void increaseDifficulty(int difficulty) {

    }
}
