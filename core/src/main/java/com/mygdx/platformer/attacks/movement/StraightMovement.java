package com.mygdx.platformer.attacks.movement;

import com.mygdx.platformer.attacks.BaseAttack;
import com.badlogic.gdx.math.Vector2;

/**
 * A {@link MovementPatternBehavior} that moves an attack in a straight
 * horizontal line.
 * <p>
 * This movement pattern sets the attack's velocity to a constant speed along
 * the X-axis,
 * with no vertical (Y-axis) movement. It is the simplest movement pattern,
 * suitable for
 * projectiles or attacks that should travel in a direct, unaltered path.
 * </p>
 *
 * <h2>Usage</h2>
 *
 * <pre>
 * // Attach to an attack to make it move straight horizontally
 * attack.setMovementBehavior(new StraightMovement());
 * </pre>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 * @see MovementPatternBehavior
 * @see com.mygdx.platformer.attacks.BaseAttack
 */
public class StraightMovement implements MovementPatternBehavior {

    /**
     * Updates the attack's velocity to maintain straight horizontal movement.
     *
     * @param attack The attack instance whose movement is being updated.
     */
    @Override
    public void update(BaseAttack attack) {
        // straight movement
        attack.getBody().setLinearVelocity(attack.getSpeed(), 0);
    }

    /**
     * Returns a string of Java code representing the straight movement logic,
     * suitable for inlining
     * into generated source files.
     *
     * @param speed The speed to use for the attack's horizontal movement.
     * @return Java code implementing the straight movement pattern.
     */
    @Override
    public String getInlineLogicCode(float speed) {
        return "        attack.getBody().setLinearVelocity(" + speed + "f, 0);";
    }
}
