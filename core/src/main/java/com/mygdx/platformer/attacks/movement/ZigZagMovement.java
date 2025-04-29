package com.mygdx.platformer.attacks.movement;

import com.mygdx.platformer.attacks.BaseAttack;
import com.badlogic.gdx.math.Vector2;

public class ZigZagMovement implements MovementPatternBehavior {
    @Override
    public void update(BaseAttack attack) {
        Vector2 pos = attack.getBody().getPosition();
        float cycleLength = 2.0f;
        float offsetY;

        //TODO: this needs to be fixed, right now it is just an angled attack, it fails to zigzag
        if (((int)(pos.x / cycleLength)) % 2 == 0) {
            offsetY = 2f;
        } else {
            offsetY = -2f;
        }

        attack.getBody().setLinearVelocity(attack.getSpeed(), offsetY);
    }

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
