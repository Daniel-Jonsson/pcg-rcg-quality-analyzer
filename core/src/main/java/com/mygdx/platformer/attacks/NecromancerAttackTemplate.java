package com.mygdx.platformer.attacks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class NecromancerAttackTemplate {

    private final int arc;
    private final float speed;
    private final int damage;
    private final int projectileCount;

    public NecromancerAttackTemplate(int arc, float speed, int damage,
                                     int projectileCount) {
        this.arc = arc;
        this.speed = speed;
        this.damage = damage;
        this.projectileCount = projectileCount;
    }

    public BaseAttack execute(World world, Vector2 initialPos,
                              int directionModifier, float multiplier) {
        // TODO: Add projectile count as argument when instantiating?
        return new NecromancerAttack(world, Math.round(damage * multiplier), speed,
            initialPos.x,
            initialPos.y,
            directionModifier);
    }

}
