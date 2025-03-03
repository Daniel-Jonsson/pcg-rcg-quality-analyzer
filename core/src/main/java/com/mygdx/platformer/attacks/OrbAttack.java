package com.mygdx.platformer.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Represents a projectile attack in the form of an orb.
 * This attack moves in a specified direction and interacts with targets.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public class OrbAttack extends BaseAttack {

    /**
     * Creates an OrbAttack instance that moves in the specified direction.
     *
     * @param world The Box2D world where the attack exists.
     * @param x The initial x-coordinate of the orb.
     * @param y The initial y-coordinate of the orb.
     * @param texture The texture representing the orb's appearance.
     * @param directionModifier The direction in which the orb moves (e.g.,
     *                          -1 for left, 1 for right).
     */
    public OrbAttack(World world, float x, float y, Texture texture, int directionModifier) {
        super(world, 20, 7 * directionModifier, x, y, texture);
        super.body.setUserData(this);
    }
}
