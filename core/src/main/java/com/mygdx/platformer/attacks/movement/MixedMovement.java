package com.mygdx.platformer.attacks.movement;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.platformer.attacks.BaseAttack;

/**
 * A {@link MovementPatternBehavior} that alternates an attack's movement
 * between straight and zigzag patterns.
 * <p>
 * The movement mode switches at regular intervals (every 1 second), randomly
 * choosing between
 * a straight horizontal movement and a zigzag pattern. In zigzag mode, the
 * attack's vertical
 * velocity alternates between positive and negative values based on its
 * horizontal position,
 * creating a wave-like path. In straight mode, the attack moves horizontally
 * with no vertical offset.
 * </p>
 *
 * <h2>Usage</h2>
 * 
 * <pre>
 * // Attach to an attack to make it alternate between straight and zigzag
 * // movement
 * attack.setMovementBehavior(new MixedMovement());
 * </pre>
 *
 * @see MovementPatternBehavior
 * @see com.mygdx.platformer.attacks.BaseAttack
 */
public class MixedMovement implements MovementPatternBehavior {

    private float timeSinceLastSwitch = 0f;
    private boolean useZigZag = false;

    /**
     * Updates the attack's movement pattern, alternating between straight and
     * zigzag modes.
     * <p>
     * Every second, the movement mode is randomly switched. In zigzag mode, the
     * attack's vertical
     * velocity alternates between positive and negative values based on its
     * horizontal position,
     * creating a zigzag or wave-like path. In straight mode, the attack moves
     * horizontally with no
     * vertical offset.
     * </p>
     *
     * @param attack The attack instance whose movement is being updated.
     */
    @Override
    public void update(BaseAttack attack) {
        timeSinceLastSwitch += com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        if (timeSinceLastSwitch >= 1.0f) {
            useZigZag = Math.random() < 0.5;
            timeSinceLastSwitch = 0f;
        }

        Vector2 pos = attack.getBody().getPosition();

        if (useZigZag) {
            float cycleLength = 2.0f;
            float offsetY;
            if (((int)(pos.x / cycleLength)) % 2 == 0) {
                offsetY = 2f;
            } else {
                offsetY = -2f;
            }
            attack.getBody().setLinearVelocity(attack.getSpeed(), offsetY);
        } else {
            attack.getBody().setLinearVelocity(attack.getSpeed(), 0f);
        }
    }

    /**
     * Returns a string representing the code logic of this movement pattern. This method is used for
     * analysis purposes.
     * @param speed Speed.
     * @return String representing the code logic.
     */
    @Override
    public String getInlineLogicCode(float speed) {
        return
            "float timeSinceLastSwitch = 0f;\n" +
                "boolean useZigZag = false;\n" +
                "timeSinceLastSwitch += com.badlogic.gdx.Gdx.graphics.getDeltaTime();\n" +
                "if (timeSinceLastSwitch >= 1.0f) {\n" +
                "    useZigZag = Math.random() < 0.5;\n" +
                "    timeSinceLastSwitch = 0f;\n" +
                "}\n" +
                "Vector2 pos = attack.getBody().getPosition();\n" +
                "if (useZigZag) {\n" +
                "    float cycleLength = 2.0f;\n" +
                "    float offsetY = ((int)(pos.x / cycleLength)) % 2 == 0 ? 2f : -2f;\n" +
                "    attack.getBody().setLinearVelocity(" + "speed, offsetY);\n" +
                "} else {\n" +
                "    attack.getBody().setLinearVelocity(" + "speed, 0f);\n" +
                "}";
    }
}

