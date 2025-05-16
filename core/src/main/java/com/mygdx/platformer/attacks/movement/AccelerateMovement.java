package com.mygdx.platformer.attacks.movement;

import com.mygdx.platformer.attacks.BaseAttack;
import com.badlogic.gdx.math.Vector2;

//TODO: check that the logic works as intended in game, I'm not sure it does
public class AccelerateMovement implements MovementPatternBehavior {
    @Override
    public void update(BaseAttack attack) {
        Vector2 pos = attack.getBody().getPosition();
        float speedX = attack.getBody().getLinearVelocity().x;
        float direction = Math.signum(speedX);
        float absSpeedX = Math.abs(speedX);
        float newAbsSpeedX;

        if (absSpeedX < 1.5f) {
            newAbsSpeedX = absSpeedX + 0.2f;
        } else if (absSpeedX < 3.0f) {
            if (pos.y > 50) {
                newAbsSpeedX = absSpeedX * 1.1f;
            } else {
                newAbsSpeedX = absSpeedX * 1.05f;
            }
        } else if (absSpeedX > 5.0f) {
            newAbsSpeedX = absSpeedX * 0.95f;
        } else {
            newAbsSpeedX = absSpeedX;
        }
        attack.getBody().setLinearVelocity(newAbsSpeedX * direction, 0);
    }

    @Override
    public String getInlineLogicCode(float speed) {
        return
            "        Vector2 pos = attack.getBody().getPosition();\n" +
                "        float currentSpeed = attack.getBody().getLinearVelocity().x;\n" +
                "        float newSpeed;\n" +
                "        if (currentSpeed < 1.5f) {\n" +
                "            newSpeed = currentSpeed + 0.2f;\n" +
                "        } else if (currentSpeed < 3.0f) {\n" +
                "            if (pos.y > 50) {\n" +
                "                newSpeed = currentSpeed * 1.1f;\n" +
                "            } else {\n" +
                "                newSpeed = currentSpeed * 1.05f;\n" +
                "            }\n" +
                "        } else if (currentSpeed > 5.0f) {\n" +
                "            newSpeed = currentSpeed * 0.95f;\n" +
                "        } else {\n" +
                "            newSpeed = currentSpeed;\n" +
                "        }\n" +
                "        attack.getBody().setLinearVelocity(newSpeed, 0);";
    }
}
