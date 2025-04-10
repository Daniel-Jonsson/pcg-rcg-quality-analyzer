package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.ai.enemy.EnemyAIAgent;
import com.mygdx.platformer.attacks.AttackManager;
import com.mygdx.platformer.attacks.pcg.CompoundAttack;
import com.mygdx.platformer.utilities.AppConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Manages the spawning, updating, and rendering of enemies in the game. This
 * class handles the creation of different enemy types ({@link Goblin} and
 * {@link Necromancer}) at specified positions and updates their state each
 * frame. It also removes enemies from the game world when they are defeated.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public class EnemyManager {

    /** The Box2D world where enemies exist. */
    private final World world;

    /** The attack manager for the game. */
    private final AttackManager attackManager;

    /** List of active enemies in the game. */
    private final List<BaseEnemy> enemies;

    /** List of active AI agents in the game. */
    private final List<EnemyAIAgent> aiAgents;

    /** The target position for the enemies to pursue. */
    private Vector2 targetPosition;

    /** Random number generator used for determining enemy types when
     * spawning. */
    private final Random random;

    /** Multiplier that increases with difficulty */

    private float multiplier = 1.0f;

    /**
     * Constructs an EnemyManager to handle enemy creation and management in
     * the game world.
     *
     * @param world The Box2D world where enemies will be spawned and updated.
     * @param attackManager The attack manager within the game
     * @param targetPosition The position of the target
     */
    public EnemyManager(World world, AttackManager attackManager, Vector2 targetPosition) {
        this.world = world;
        this.enemies = new ArrayList<>();
        this.aiAgents = new ArrayList<>();
        this.random = new Random();
        this.attackManager = attackManager;
        this.targetPosition = targetPosition;
    }

    /**
     * Spawns a new enemy at the specified position. The type of enemy
     * spawned (Goblin or Necromancer) is randomly determined.
     *
     * @param position The {@link Vector2} position where the enemy will be
     *                 created.
     */
    public void spawnEnemyAt(Vector2 position) {
        BaseEnemy enemy;
        float detectionRange;
        float attackRange;
        float attackCooldown;
        if (random.nextBoolean()) {
            int hp = (int) (AppConfig.GOBLIN_HEALTH * multiplier);
            float speed = AppConfig.GOBLIN_SPEED * multiplier;
            enemy = new Goblin(world, position, hp, speed);
            detectionRange = AppConfig.GOBLIN_DETECTION_RANGE;
            attackRange = AppConfig.GOBLIN_ATTACK_RANGE;
            attackCooldown = AppConfig.GOBLIN_ATTACK_COOLDOWN;
        } else {
            int hp = (int) (AppConfig.NECROMANCER_HEALTH * multiplier);
            float speed = AppConfig.NECROMANCER_SPEED * multiplier;
            CompoundAttack attackPattern =
                attackManager.getRandomCompoundAttack(world);
            enemy = new Necromancer(world, position, hp, speed);
            detectionRange = AppConfig.NECROMANCER_DETECTION_RANGE;
            attackRange = AppConfig.NECROMANCER_ATTACK_RANGE;
            attackCooldown = AppConfig.NECROMANCER_ATTACK_COOLDOWN;
        }

        enemies.add(enemy);
        aiAgents.add(new EnemyAIAgent(enemy, detectionRange, attackRange, attackManager, attackCooldown, world));
    }

    /**
     * Renders all active enemies.
     *
     * @param batch The {@link SpriteBatch} used for rendering enemy sprites.
     */
    public void render(SpriteBatch batch) {
        for (BaseEnemy enemy : enemies) {
            enemy.render(batch);
            enemy.renderHealthBar(batch);
        }
    }

    /**
     * Updates all active enemies and removes those that have been defeated.
     * Dead enemies are removed from the list and their physics bodies are
     * destroyed.
     * @param deltaTime The time elapsed since the last frame, used for
     *                  animation updates.
     */
    public void update(float deltaTime) {
        Iterator<BaseEnemy> iterator = enemies.iterator();
        Iterator<EnemyAIAgent> aiIterator = aiAgents.iterator();
        while (iterator.hasNext() && aiIterator.hasNext()) {
            BaseEnemy enemy = iterator.next();
            EnemyAIAgent aiAgent = aiIterator.next();
            if (enemy.getBody().getPosition().y < 0) {
                world.destroyBody(enemy.getBody());
                iterator.remove();
                aiIterator.remove();
                continue;
            }
            aiAgent.setTargetPosition(targetPosition);
            aiAgent.update(deltaTime);
            enemy.update(deltaTime);
        }
    }


    /**
     * Mutator method for setting the target position
     * @param targetPosition The {@link Vector2} representing targets position.
     */
    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition = targetPosition;
    }

    /**
     * Increases difficulty level of enemies.
     *
     * @param difficultyLevel The current difficulty level.
     */
    public void increaseDifficulty(int difficultyLevel) {
        multiplier = 1.0f + (difficultyLevel * AppConfig.DIFFICULTY_INCREASE_AMOUNT);
    }

}
