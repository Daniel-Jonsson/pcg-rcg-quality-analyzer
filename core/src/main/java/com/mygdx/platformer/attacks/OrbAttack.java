package com.mygdx.platformer.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.platformer.utilities.AppConfig;

public class OrbAttack extends BaseAttack {
    private final World world;
    private final Body body;

    public OrbAttack(World world, float x, float y, Texture texture) {
        super(20, 7, x, y, texture);
        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = AppConfig.CATEGORY_ATTACK;
        fixtureDef.filter.maskBits = AppConfig.CATEGORY_PLATFORM | AppConfig.CATEGORY_ENEMY;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setLinearVelocity(speed, 0);

        body.setUserData(this);
    }

    @Override
    public void update(float deltaTime, float cameraX, float viewPortWidth) {
        Vector2 pos = body.getPosition();
        sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getHeight() / 2);

        float rightEdge = cameraX + viewPortWidth / 2;
        float leftEdge = cameraX - viewPortWidth / 2;
        if (pos.x > rightEdge + sprite.getWidth() || pos.x < leftEdge - sprite.getWidth()) {
            shouldRemove = true;
            world.destroyBody(body);
        }
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
