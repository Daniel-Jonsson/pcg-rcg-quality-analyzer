package com.mygdx.platformer.attacks.modifiers;

import com.mygdx.platformer.attacks.BaseAttack;


public interface AttackModifier {
    abstract void update(BaseAttack attack);
}
