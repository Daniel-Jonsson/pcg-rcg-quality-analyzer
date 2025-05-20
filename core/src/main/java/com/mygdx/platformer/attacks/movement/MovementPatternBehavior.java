package com.mygdx.platformer.attacks.movement;

import com.mygdx.platformer.attacks.BaseAttack;

/**
 * Interface for defining movement patterns for attacks in the game.
 * <p>
 * Implementations of this interface encapsulate the logic for updating an
 * attack's movement
 * each frame, allowing for a variety of movement behaviors such as straight,
 * zigzag, accelerating,
 * or mixed patterns. This enables flexible and extensible attack movement logic
 * that can be
 * dynamically assigned to different attacks.
 * <p>
 * The interface also provides a method for exporting the movement logic as Java
 * code, which is
 * useful for code generation and static analysis purposes.
 * </p>
 *
 * @see com.mygdx.platformer.attacks.BaseAttack
 */
public interface MovementPatternBehavior {

    /**
     * Updates the movement of the given attack according to the specific pattern.
     * <p>
     * This method is called each frame (or tick) to apply the movement logic to the
     * attack.
     * </p>
     *
     * @param attack The attack instance whose movement is being updated.
     */
    void update(BaseAttack attack);

    /**
     * Returns a string of Java code representing the movement logic, suitable for
     * inlining
     * into generated source files for analysis.
     *
     * @param speed The initial speed of the attack (may be used in the generated
     *              code).
     * @return Java code implementing the movement pattern.
     */
    String getInlineLogicCode(float speed);

}
