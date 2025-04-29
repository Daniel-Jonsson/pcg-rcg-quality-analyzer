package com.mygdx.platformer.attacks.movement;

import com.mygdx.platformer.attacks.BaseAttack;
import com.badlogic.gdx.math.Vector2;

public class StraightMovement implements MovementPatternBehavior {
    @Override
    public void update(BaseAttack attack) {
        // straight movement
        attack.getBody().setLinearVelocity(attack.getSpeed(), 0);
    }

    @Override
    public String getInlineLogicCode(float speed) {
        return "        attack.getBody().setLinearVelocity(" + speed + "f, 0);";
    }
}
