package com.mygdx.platformer.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class OrbAttack extends BaseAttack {

    public OrbAttack(World world, float x, float y, Texture texture, int directionModifier) {
        super(world, 20, 7 * directionModifier, x, y, texture);
        super.body.setUserData(this);
    }
}
