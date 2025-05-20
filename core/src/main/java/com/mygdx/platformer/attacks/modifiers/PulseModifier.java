package com.mygdx.platformer.attacks.modifiers;

import com.badlogic.gdx.Gdx;
import com.mygdx.platformer.attacks.BaseAttack;

/**
 * An {@link AttackModifier} that causes an attack's visual size to pulse
 * between two scales.
 * <p>
 * The pulse effect alternates the attack's scale between a "shrink" and "grow"
 * state at a specified frequency,
 * creating a visual pulsing or throbbing effect. The amplitude parameter can be
 * used to control the intensity
 * of the effect, though in this implementation it is not directly used in the
 * scaling logic.
 * <p>
 * The modifier updates the attack's scale at regular intervals based on the
 * frequency, toggling between
 * two preset scale values (1.5 and 2.0).
 * </p>
 *
 * <h2>Usage</h2>
 * <pre>
 * // Attach a PulseModifier to an attack with a frequency of 2 Hz and amplitude
 * // of 0.5
 * attack.setAttackModifier(new PulseModifier(2f, 0.5f));
 * </pre>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 * @see AttackModifier
 * @see com.mygdx.platformer.attacks.BaseAttack
 */
public class PulseModifier implements AttackModifier {
    private final float frequency;
    private final float amplitude;

    private float timeSinceLastResize = 0f;
    private boolean shrink = false;

    public PulseModifier(float frequency, float amplitude) {
        this.frequency = frequency;
        this.amplitude = amplitude;
    }

    /**
     * Updates the attack's visual scale to create a pulsing effect.
     * <p>
     * Alternates the scale between two values at the specified frequency.
     * </p>
     *
     * @param attack The attack instance to modify.
     */
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

    /**
     * Returns a string of Java code representing the pulsing logic, suitable for
     * inlining
     * into generated source files.
     *
     * @return Java code implementing the pulsing effect.
     */
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
