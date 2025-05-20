package com.mygdx.platformer.attacks.pcg;

/**
 * The NecromancerAttackBuilder class implements the Builder interface and
 * provides methods to set the number of projectiles, speed, and damage for an attack.
 * 
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class NecromancerAttackBuilder implements Builder {
    private int projectiles;
    private float speed;
    private int damage;

    /**
     * Sets the speed of the attack.
     *
     * @param speed The speed to set.
     */
    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Sets the damage of the attack.
     *
     * @param damage The damage to set.
     */
    @Override
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Sets the number of projectiles for the attack.
     *
     * @param projectileNumber The number of projectiles to set.
     */
    @Override
    public void setProjectileNumber(int projectileNumber) {
        this.projectiles = projectileNumber;
    }

    /**
     * Returns a new CompoundAttack object with the specified number of projectiles, speed, and damage.
     *
     * @return A new CompoundAttack object.
     */
    public CompoundAttack getResult() {
        return new CompoundAttack(projectiles, speed, damage);
    }
}
