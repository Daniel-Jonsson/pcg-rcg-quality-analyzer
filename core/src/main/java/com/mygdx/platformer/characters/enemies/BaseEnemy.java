package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.platformer.utilities.AppConfig;

import java.util.Iterator;

public abstract class BaseEnemy {

    protected Body body;
    protected int health;
    protected int attackPower;
    protected float speed;
    protected boolean isOnGround;
    protected Sprite sprite;
    protected Texture texture;
    private boolean isDead;

    private float moveDirection = 1; // 1 = right, -1 = left (temporary for movement testing)
    private float moveTime = 0; // Tracks how long the enemy has moved in one direction, temporary for testing purposes
    protected final float switchTime = 1f; // Time before switching direction,
    // temporary for testing purposes.
    protected boolean facingRight = true;

    protected TextureRegion currentFrame;


    protected float stateTime = 0f;

    public BaseEnemy(World world, Vector2 position, int health, int attackPower, float speed) {

        this.health = health;
        this.attackPower = attackPower;
        this.speed = speed;
        this.isOnGround = false;

        setupPhysics(world, position);
        setupAnimations();
    }


    private void setupPhysics(World world, Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        Vector2 hitBox = getHitBoxSize();
        shape.setAsBox(hitBox.x, hitBox.y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0f;

        fixtureDef.filter.categoryBits = AppConfig.CATEGORY_ENEMY;
        fixtureDef.filter.maskBits =
            AppConfig.CATEGORY_PLAYER | AppConfig.CATEGORY_PLATFORM | AppConfig.CATEGORY_ATTACK;

        body.createFixture(fixtureDef);
        shape.dispose();

        MassData massData = new MassData();
        massData.mass = AppConfig.ENEMY_MASS;
        body.setMassData(massData);
        body.setUserData(this);
    }

    public void render(SpriteBatch batch) {
        if (sprite != null && body != null) {
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,
                body.getPosition().y - sprite.getHeight() / 2);
            sprite.draw(batch);
        }
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        moveTime += deltaTime;

        if (moveTime >= switchTime) {
            moveDirection *= -1;
            moveTime = 0; // Reset movement timer
            facingRight = moveDirection > 0;
        }

        body.setLinearVelocity(moveDirection * speed, body.getLinearVelocity().y);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            isDead = true;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public Body getBody() {
        return body;
    }

    protected abstract void setupAnimations();
    protected abstract Vector2 getHitBoxSize();
}
