package com.mygdx.platformer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.characters.enemies.BaseEnemy;
import com.mygdx.platformer.characters.enemies.Goblin;
import com.mygdx.platformer.characters.enemies.Necromancer;

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
    private World world;

    /** List of active enemies in the game. */
    private List<BaseEnemy> enemies;

    /** Random number generator used for determining enemy types when
     * spawning. */
    private Random random;

    /**
     * Constructs an EnemyManager to handle enemy creation and management in
     * the game world.
     *
     * @param world The Box2D world where enemies will be spawned and updated.
     */
    public EnemyManager(World world) {
        this.world = world;
        this.enemies = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Spawns a new enemy at the specified position. The type of enemy
     * spawned (Goblin or Necromancer) is randomly determined.
     *
     * @param position The {@link Vector2} position where the enemy will be
     *                 created.
     */
    public void spawnEnemyAt(Vector2 position) {

        if (random.nextBoolean()) {
            Goblin goblin = new Goblin(world, position);
            enemies.add(goblin);
        } else {
            Necromancer necromancer = new Necromancer(world, position);
            enemies.add(necromancer);
        }

        System.out.println("Spawned enemy at " + position);
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
        while (iterator.hasNext()) {
            BaseEnemy enemy = iterator.next();
            enemy.update(deltaTime);
            if (enemy.isDead()) {
                iterator.remove();
                world.destroyBody(enemy.getBody());
            }
        }
    }

}
