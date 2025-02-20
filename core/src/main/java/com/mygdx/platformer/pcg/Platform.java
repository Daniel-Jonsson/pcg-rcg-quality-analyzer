package com.mygdx.platformer.pcg;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Platform {

    private Texture texture;
    private Sprite sprite;
    private Body body;

    public Platform(World world, float x, float y, float width, float height) {
        texture = new Texture("platform.png");
        sprite = new Sprite(texture);
        sprite.setSize(width, height);

        // physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y); // position in world units (meters)
        body = world.createBody(bodyDef);

        // collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void render(SpriteBatch batch) {
        // Sync sprite position
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,
            body.getPosition().y - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    public void dispose() {
        texture.dispose();
    }


    public Body getBody() {
        return body;
    }

    public float getWidth() {
        return sprite.getWidth();
    }

}
