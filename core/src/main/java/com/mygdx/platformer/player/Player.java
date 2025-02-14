package com.mygdx.platformer.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;

public class Player {

    private Texture texture;
    private Sprite sprite;
    private Body body;


    public Player(World world, float x, float y) {
        texture = new Texture("player.png");
        sprite = new Sprite(texture);

        // physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / AppConfig.PPM, y / AppConfig.PPM);
        body = world.createBody(bodyDef); // add player body to game world

        // collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / AppConfig.PPM, sprite.getHeight() / 2 / AppConfig.PPM);

        // attach the polygon shape to the body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, sprite.getX(), sprite.getY());
    }

    public void update() {
       // this syncs the sprite position with the Box2D body
        sprite.setPosition(
            (body.getPosition().x * AppConfig.PPM) - sprite.getWidth() / 2,
            (body.getPosition().y * AppConfig.PPM) - sprite.getHeight() / 2);

    }

    public void dispose() {
        texture.dispose();
    }

}
