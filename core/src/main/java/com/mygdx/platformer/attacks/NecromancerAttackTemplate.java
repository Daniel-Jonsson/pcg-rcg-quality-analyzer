package com.mygdx.platformer.attacks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.attacks.modifiers.AttackModifier;
import com.mygdx.platformer.attacks.movement.MovementPatternBehavior;

/**
 * Represents a template for creating Necromancer attacks.
 * This class defines the structure of a Necromancer attack, including its arc, speed, damage, projectile count, movement pattern, and modifier.
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 */ 
public class NecromancerAttackTemplate {

    private final int arc;
    private final float speed;
    private final int damage;
    private final int projectileCount;
    private final MovementPatternBehavior movementPattern;
    private AttackModifier modifier;

    /**
     * Constructs a new NecromancerAttackTemplate with the specified parameters.
     *
     * @param arc The arc of the attack.
     * @param speed The speed of the attack.
     * @param damage The damage of the attack.
     * @param projectileCount The number of projectiles in the attack.
     * @param movementPattern The movement pattern of the attack.
     * @param attackModifier The modifier of the attack.
     */
    public NecromancerAttackTemplate(int arc, float speed, int damage,
                                     int projectileCount, MovementPatternBehavior movementPattern, AttackModifier attackModifier) {
        this.arc = arc;
        this.speed = speed;
        this.damage = damage;
        this.projectileCount = projectileCount;
        this.movementPattern = movementPattern;
        this.modifier = attackModifier;
    }

    /**
     * Executes the attack template to create a new NecromancerAttack.
     *
     * @param world The world in which the attack is created.
     * @param initialPos The initial position of the attack.
     * @param directionModifier The direction modifier for the attack.
     * @param multiplier The multiplier for the attack.
     * @return A new NecromancerAttack instance.
     */
    public BaseAttack execute(World world, Vector2 initialPos,
                              int directionModifier, float multiplier) {
        BaseAttack attack = new NecromancerAttack(world, Math.round(damage * multiplier), speed,
            initialPos.x, initialPos.y, directionModifier);

        attack.setMovementBehavior(this.movementPattern);
        attack.setAttackModifier(this.modifier);

        // use pattern to update attack
        if (movementPattern != null) {
            movementPattern.update(attack);
        }

        return attack;
    }

    /**
     * Accessor for the damage of the attack.
     *
     * @return The damage of the attack.
     */ 
    public int getDamage() {
        return damage;
    }

    /**
     * Accessor for the speed of the attack.
     *
     * @return The speed of the attack.
     */  
    public float getSpeed() {
        return speed;
    }

    /**
     * Accessor for the movement pattern of the attack.
     *
     * @return The movement pattern of the attack.
     */  
    public MovementPatternBehavior getMovementPattern() {
        return movementPattern;
    }

    /**
     * Accessor for the modifier of the attack.
     *
     * @return The modifier of the attack.
     */   
    public AttackModifier getModifier() {
        return modifier;
    }

    /**
     * Accessor for the movement logic code of the attack.
     *
     * @return The movement logic code of the attack.
     */    
    public String getMovementLogicCode() {
        return movementPattern != null ? movementPattern.getInlineLogicCode(speed) : "";
    }

    /**
     * Accessor for the modifier logic code of the attack.
     *
     * @return The modifier logic code of the attack.
     */     
    public String getModifierLogicCode() {
        return modifier != null ? modifier.getInlineLogicCode() : "";
    }
}
