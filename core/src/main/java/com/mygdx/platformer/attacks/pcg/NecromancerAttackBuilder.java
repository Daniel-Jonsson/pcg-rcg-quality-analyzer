package com.mygdx.platformer.attacks.pcg;

public class NecromancerAttackBuilder implements Builder {
    private int projectiles;
    private float speed;
    private int damage;

    /**
     *
     */
    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     *
     */
    @Override
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     *
     */
    @Override
    public void setProjectileNumber(int projectileNumber) {
        this.projectiles = projectileNumber;
    }

    public CompoundAttack getResult() {
        return new CompoundAttack(projectiles, speed, damage);
    }
}
