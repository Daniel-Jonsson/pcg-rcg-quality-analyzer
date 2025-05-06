package com.mygdx.platformer.attacks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.attacks.modifiers.AttackModifier;
import com.mygdx.platformer.attacks.movement.MovementPatternBehavior;

public class NecromancerAttackTemplate {

    private final int arc;
    private final float speed;
    private final int damage;
    private final int projectileCount;
    private final MovementPatternBehavior movementPattern;
    private AttackModifier modifier;

    public NecromancerAttackTemplate(int arc, float speed, int damage,
                                     int projectileCount, MovementPatternBehavior movementPattern, AttackModifier attackModifier) {
        this.arc = arc;
        this.speed = speed;
        this.damage = damage;
        this.projectileCount = projectileCount;
        this.movementPattern = movementPattern;
        this.modifier = attackModifier;
    }

    public BaseAttack execute(World world, Vector2 initialPos,
                              int directionModifier, float multiplier) {
        BaseAttack attack = new NecromancerAttack(world, Math.round(damage * multiplier), speed,
            initialPos.x, initialPos.y, directionModifier);

        attack.setMovementBehavior(this.movementPattern);
        attack.setAttackModifier(this.modifier);

        // use pattern to update attack
        if (movementPattern != null) {
            movementPattern.update(attack);
        }

        return attack;
    }

    public int getDamage() {
        return damage;
    }

    public float getSpeed() {
        return speed;
    }

    public MovementPatternBehavior getMovementPattern() {
        return movementPattern;
    }

    public AttackModifier getModifier() {
        return modifier;
    }

    public String getMovementLogicCode() {
        return movementPattern != null ? movementPattern.getInlineLogicCode(speed) : "";
    }

    public String getModifierLogicCode() {
        return modifier != null ? modifier.getInlineLogicCode() : "";
    }
}
