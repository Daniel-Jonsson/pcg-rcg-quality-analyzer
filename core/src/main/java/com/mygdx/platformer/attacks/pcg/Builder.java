package com.mygdx.platformer.attacks.pcg;

/**
 * The Builder interface defines the methods for building different types of attacks.
 * It provides a way to set the number of projectiles, speed, and damage for an attack.
 * 
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public interface Builder {
//    void setArc();
//    void setMovementPattern();

    /**
     * Sets the number of projectiles for the attack.
     * 
     * @param projectileNumber The number of projectiles to set.
     */
    void setProjectileNumber(int projectileNumber);

    /**
     * Sets the speed of the attack.
     * 
     * @param speed The speed to set.
     */
    void setSpeed(float speed);

    /**
     * Sets the damage for the attack.
     * 
     * @param damage The damage to set.
     */
    void setDamage(int damage);

}
