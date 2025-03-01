package com.mygdx.platformer.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class OrbAttack extends BaseAttack {

    public OrbAttack(World world, float x, float y, Texture texture) {
        super(world, 20, 7, x, y, texture);
        super.body.setUserData(this);
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public boolean shouldRemove() {
        return shouldRemove;
    }

    public Body getBody() {
        return body;
    }
}
