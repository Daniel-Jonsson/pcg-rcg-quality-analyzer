package com.mygdx.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.characters.enemies.BaseEnemy;
import com.mygdx.platformer.characters.enemies.Goblin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyManager {

    private World world;
    private List<BaseEnemy> enemies;
    private Random random;

    public EnemyManager(World world) {
        this.world = world;
        this.enemies = new ArrayList<>();
        this.random = new Random();
    }

    public void spawnEnemyAt(Vector2 position) {
        Goblin goblin = new Goblin(world, position);


        enemies.add(goblin);
        System.out.println("Spawned enemy at " + position);
    }

    public void render(SpriteBatch batch) {
        for (BaseEnemy enemy : enemies) {
            enemy.render(batch);
            enemy.update(Gdx.graphics.getDeltaTime());

        }
    }

}
