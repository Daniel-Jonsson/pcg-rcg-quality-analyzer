package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

public abstract class BaseEnemy {

    protected Body body;
    protected int health;
    protected int attackPower;
    protected float speed;
    protected boolean isOnGround;
    protected Sprite sprite;
    protected Texture texture;

    public BaseEnemy(World world, Vector2 position, int health, int attackPower, float speed) {

        this.health = health;
        this.attackPower = attackPower;
        this.speed = speed;
        this.isOnGround = false;



        // Create Box2D body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f); // Half-width and half-height of enemy

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0f; // No bouncing

        body.createFixture(fixtureDef);
        shape.dispose();
    }


}
