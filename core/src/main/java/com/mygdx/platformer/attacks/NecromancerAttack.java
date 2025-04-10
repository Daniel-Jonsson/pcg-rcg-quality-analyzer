package com.mygdx.platformer.attacks;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

/**
 * Represents a necromancer projectile attack.
 * This attack moves in a specified direction and interacts with targets.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public class NecromancerAttack extends BaseAttack {

    /**
     * Creates a Necromancer attack instance that moves in the specified
     * direction.
     * @param world The Box2D world where the attack exists.
     * @param x The initial x-coordinate of the attack.
     * @param y The initial y-coordinate of the attack.
     * @param directionModifier The direction in which the attack moves (e.g.,
     *                          -1 for left, 1 for right).
     * @param dmg The damage the attack causes when hitting target
     * @param speed The speed of which the attack moves.
     */

    public NecromancerAttack(World world, int dmg, float speed) {
        super(world, dmg, speed,
            Assets.assetManager.get(Assets.DEATH_BOLT), false);
        sprite.setSize(sprite.getWidth() * AppConfig.NECROMANCER_ATTACK_SCALE,
            sprite.getHeight() * AppConfig.NECROMANCER_ATTACK_SCALE);
        super.body.setUserData(this);
    }
}


