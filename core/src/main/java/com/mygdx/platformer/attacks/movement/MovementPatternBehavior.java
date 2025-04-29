package com.mygdx.platformer.attacks.movement;

import com.mygdx.platformer.attacks.BaseAttack;

public interface MovementPatternBehavior {
    void update(BaseAttack attack);
    String getInlineLogicCode(float speed);

}
