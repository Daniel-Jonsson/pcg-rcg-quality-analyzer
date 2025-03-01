package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
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
        this.texture = Assets.assetManager.get(Assets.PLAYER_TEXTURE);

        float playerWidth = AppConfig.PLAYER_WIDTH;
        float playerHeight = AppConfig.PLAYER_HEIGHT;

        sprite = new Sprite(texture);
        sprite.setSize(playerWidth, playerHeight);

        // physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef); // add player body to game world

        // collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(playerWidth * AppConfig.PLAYER_HITBOX_SCALE / 2, playerHeight * AppConfig.PLAYER_HITBOX_SCALE / 2);

        // attach the polygon shape to the body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0f;

        // Collision filtering
        fixtureDef.filter.categoryBits = AppConfig.CATEGORY_ENEMY;
        fixtureDef.filter.maskBits =
            AppConfig.CATEGORY_PLAYER | AppConfig.CATEGORY_PLATFORM | AppConfig.CATEGORY_ATTACK;

        body.createFixture(fixtureDef);
        shape.dispose();



        MassData massData = new MassData();
        massData.mass = AppConfig.ENEMY_MASS;
        body.setMassData(massData);
    }

    public void render(SpriteBatch batch) {
        if (sprite != null && body != null) {
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,
                body.getPosition().y - sprite.getHeight() / 2);
            sprite.draw(batch);
        }
    }

    public void update(float delta) {
        if (health <= 0) {

        }
    }

    public void takeDamage(int damage) {
        health -= damage;
    }
}
