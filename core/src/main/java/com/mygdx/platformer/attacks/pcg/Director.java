package com.mygdx.platformer.attacks.pcg;

import java.util.Random;

public class Director {
    private final Random rand = new Random();

    public void constructNecromancerPCGAttack(Builder attackBuilder) {
        attackBuilder.setProjectileNumber(rand.nextInt(5));
        // TODO: Make the damage and speed depend on current difficulty level
        attackBuilder.setDamage(rand.nextInt(20, 30));
        attackBuilder.setSpeed(rand.nextFloat(1, 3));
    }
}
