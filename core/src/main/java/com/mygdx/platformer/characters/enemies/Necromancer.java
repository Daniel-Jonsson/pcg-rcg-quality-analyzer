package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Necromancer extends BaseEnemy {

    public Necromancer(World world, Vector2 position) {
        super(world, position, 30, 15, 2f);
    }
}
