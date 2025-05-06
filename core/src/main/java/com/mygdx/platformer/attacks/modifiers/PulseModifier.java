package com.mygdx.platformer.attacks.modifiers;

import com.badlogic.gdx.Gdx;
import com.mygdx.platformer.attacks.BaseAttack;

public class PulseModifier implements AttackModifier {
    private final float frequency;
    private final float amplitude;

    private float timeSinceLastResize = 0f;
    private boolean shrink = false;

    public PulseModifier(float frequency, float amplitude) {
        this.frequency = frequency;
        this.amplitude = amplitude;
    }

    @Override
    public void update(BaseAttack attack) {
        timeSinceLastResize += Gdx.graphics.getDeltaTime();

        float interval = 1.0f / frequency;

        if (timeSinceLastResize >= interval) {
            shrink = !shrink;
            timeSinceLastResize = 0f;
        }

        float visualScale;
        if (shrink) {
            visualScale = 1.5f;
        } else {
            visualScale = 2.0f;
        }

        attack.setVisualScale(visualScale);
    }

    public String getInlineLogicCode() {
        return
            "        float interval = 1.0f / " + frequency + "f;\n" +
                "float timeSinceLastResize = 0f;" + "\n" +
                "boolean shrink = false;" + "\n" +
                "        timeSinceLastResize += com.badlogic.gdx.Gdx.graphics.getDeltaTime();\n" +
                "        if (timeSinceLastResize >= interval) {\n" +
                "            shrink = !shrink;\n" +
                "            timeSinceLastResize = 0f;\n" +
                "        }\n" +
                "        float visualScale;\n" +
                "        if (shrink) {\n" +
                "            visualScale = 1.5f;\n" +
                "        } else {\n" +
                "            visualScale = 2.0f;\n" +
                "        }\n" +
                "        attack.setVisualScale(visualScale);\n";
    }
}
