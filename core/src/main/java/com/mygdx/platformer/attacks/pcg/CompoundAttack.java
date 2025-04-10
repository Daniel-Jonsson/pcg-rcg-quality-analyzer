package com.mygdx.platformer.attacks.pcg;

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
        initializeAttacks();
    }

    private void initializeAttacks() {
        attackPattern = new ArrayList<>();
        for (int i = 0; i < projectiles; i++) {
            attackPattern.add(new NecromancerAttack());
        }
    }
}
