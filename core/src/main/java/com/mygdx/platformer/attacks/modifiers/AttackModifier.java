package com.mygdx.platformer.attacks.modifiers;

import com.mygdx.platformer.attacks.BaseAttack;

/**
 * Interface for defining attack modifier behaviors in the game.
 * <p>
 * Implementations of this interface can be attached to
 * {@link com.mygdx.platformer.attacks.BaseAttack}
 * objects to dynamically alter their properties or behavior during their
 * lifecycle (e.g., adding
 * effects such as pulsing, splitting, or changing speed).
 * <p>
 * Attack modifiers are used both in runtime gameplay and in code generation for
 * analysis, allowing
 * for flexible and extensible attack logic.
 * </p>
 *
 * @author Daniel Jönsson
 * @author Robert Kullman
 * @see com.mygdx.platformer.attacks.BaseAttack
 */
public interface AttackModifier {
    abstract void update(BaseAttack attack);
    String getInlineLogicCode();
}
