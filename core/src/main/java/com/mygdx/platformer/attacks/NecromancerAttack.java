package com.mygdx.platformer.attacks;

import com.badlogic.gdx.graphics.Texture;
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
     * Creates an PlayerAttack instance that moves in the specified direction.
     *
     * @param world The Box2D world where the attack exists.
     * @param x The initial x-coordinate of the attack.
     * @param y The initial y-coordinate of the attack.
     * @param texture The texture representing the attack's appearance.
     * @param directionModifier The direction in which the attack moves (e.g.,
     *                          -1 for left, 1 for right).
     * @param isPlayerAttack Whether the attack is a player attack.
     */
    public NecromancerAttack(World world, float x, float y, Texture texture, int directionModifier, boolean isPlayerAttack) {
        super(world, AppConfig.NECROMANCER_ATTACK_POWER, AppConfig.NECROMANCER_ATTACK_SPEED * directionModifier,
            x + AppConfig.NECROMANCER_ATTACK_X_OFFSET * directionModifier,
            y + AppConfig.NECROMANCER_ATTACK_Y_OFFSET, texture, isPlayerAttack);
        sprite.setSize(sprite.getWidth() * AppConfig.NECROMANCER_ATTACK_SCALE,
            sprite.getHeight() * AppConfig.NECROMANCER_ATTACK_SCALE);

        super.body.setUserData(this);
    }

    /**
     * Creates an Necromancer attack instance that moves in the specified
     * direction.
     * @param world The Box2D world where the attack exists.
     * @param x The initial x-coordinate of the attack.
     * @param y The initial y-coordinate of the attack.
     * @param directionModifier The direction in which the attack moves (e.g.,
     *                          -1 for left, 1 for right).
     * @param dmg The damage the attack causes when hitting target
     * @param speed The speed of which the attack moves.
     */

    public NecromancerAttack(World world, float x, float y,
                             int directionModifier, int dmg, int speed) {
        super(world, dmg, speed * directionModifier,
            x + AppConfig.NECROMANCER_ATTACK_X_OFFSET * directionModifier,
            y + AppConfig.NECROMANCER_ATTACK_Y_OFFSET,
            Assets.assetManager.get(Assets.DEATH_BOLT), false);
        sprite.setSize(sprite.getWidth() * AppConfig.NECROMANCER_ATTACK_SCALE,
            sprite.getHeight() * AppConfig.NECROMANCER_ATTACK_SCALE);
        super.body.setUserData(this);
    }
}


