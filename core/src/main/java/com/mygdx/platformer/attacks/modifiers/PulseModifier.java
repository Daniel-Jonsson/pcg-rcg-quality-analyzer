package com.mygdx.platformer.attacks.modifiers;

import com.badlogic.gdx.Gdx;
import com.mygdx.platformer.attacks.BaseAttack;

public class PulseModifier implements AttackModifier {
    private final float frequency;
    private final float amplitude;

    private float timeSinceLastSwitch = 0f;
    private boolean shrink = false;

    public PulseModifier(float frequency, float amplitude) {
        this.frequency = frequency;
        this.amplitude = amplitude;
    }

    @Override
    public void update(BaseAttack attack) {
        timeSinceLastSwitch += Gdx.graphics.getDeltaTime();

        float interval = 1.0f / frequency;

        if (timeSinceLastSwitch >= interval) {
            shrink = !shrink;
            timeSinceLastSwitch = 0f;
        }

        float visualScale;
        if (shrink) {
            visualScale = 1.5f;
        } else {
            visualScale = 2.0f;
        }

        attack.setVisualScale(visualScale);
    }
}
