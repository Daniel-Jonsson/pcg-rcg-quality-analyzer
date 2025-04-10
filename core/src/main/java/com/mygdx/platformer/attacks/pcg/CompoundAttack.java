package com.mygdx.platformer.attacks.pcg;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.attacks.BaseAttack;
import com.mygdx.platformer.attacks.NecromancerAttack;

import java.util.ArrayList;
import java.util.List;

public class CompoundAttack {
    private final int projectiles;
    private final int damage;
    private final float speed;
    private final List<BaseAttack> attackPattern;

    public CompoundAttack(int projectileNumber, float speed, int damage) {
        projectiles = projectileNumber;
        this.speed = speed;
        this.damage = damage;
        this.attackPattern = new ArrayList<>();
        initializeAttacks();
    }

    public void initializeAttacks(World world) {
        for (int i = 0; i < projectiles; i++) {
            attackPattern.add(new NecromancerAttack(world, damage, speed));
        }
    }


    public void setWorld() {

    }
}
