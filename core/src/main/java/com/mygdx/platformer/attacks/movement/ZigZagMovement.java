package com.mygdx.platformer.attacks.movement;

import com.mygdx.platformer.attacks.BaseAttack;
import com.badlogic.gdx.math.Vector2;

/**
 * A {@link MovementPatternBehavior} that moves an attack in a zigzag pattern along the X-axis.
 * <p>
 * This movement pattern alternates the attack's vertical (Y-axis) velocity between positive and negative
 * values based on its current horizontal position, creating a zigzag or wave-like path as it travels.
 * The direction of the vertical offset switches every fixed distance (cycle length) along the X-axis.
 * </p>
 *
 * <h2>Usage</h2>
 * <pre>
 * // Attach to an attack to make it move in a zigzag pattern
 * attack.setMovementBehavior(new ZigZagMovement());
 * </pre>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 * @see MovementPatternBehavior
 * @see com.mygdx.platformer.attacks.BaseAttack
 */
public class ZigZagMovement implements MovementPatternBehavior {
    /**
     * Updates the attack's velocity to create a zigzag movement pattern.
     * <p>
     * The vertical velocity alternates between positive and negative values
     * depending on the attack's
     * horizontal position, resulting in a zigzag path as the attack moves
     * horizontally.
     * </p>
     *
     * @param attack The attack instance whose movement is being updated.
     */
    @Override
    public void update(BaseAttack attack) {
        Vector2 pos = attack.getBody().getPosition();
        float cycleLength = 2.0f;
        float offsetY;

        if (((int)(pos.x / cycleLength)) % 2 == 0) {
            offsetY = 2f;
        } else {
            offsetY = -2f;
        }

        attack.getBody().setLinearVelocity(attack.getSpeed(), offsetY);
    }

    
    /**
     * Returns a string of Java code representing the zigzag movement logic,
     * suitable for inlining
     * into generated source files.
     * <p>
     * The generated code reproduces the zigzag pattern by alternating the vertical
     * velocity
     * based on the attack's horizontal position.
     * </p>
     *
     * @param speed The speed to use for the attack's horizontal movement.
     * @return Java code implementing the zigzag movement pattern.
     */
    @Override
    public String getInlineLogicCode(float speed) {
        return
            "        Vector2 pos = attack.getBody().getPosition();\n" +
                "        float cycleLength = 2.0f;\n" +
                "        float offsetY;\n" +
                "        if (((int)(pos.x / cycleLength)) % 2 == 0) {\n" +
                "            offsetY = 2f;\n" +
                "        } else {\n" +
                "            offsetY = -2f;\n" +
                "        }\n" +
                "        attack.getBody().setLinearVelocity(" + speed + "f, offsetY);";
    }
}
