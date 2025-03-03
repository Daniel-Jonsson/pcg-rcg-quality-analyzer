package com.mygdx.platformer.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;

public abstract class BaseAttack {
    protected int damage;
    protected float speed;
    protected float x, y;
    protected Sprite sprite;
    protected boolean shouldRemove;
    protected final World world;
    protected final Body body;

    public BaseAttack(World world, int damage, float speed, float x, float y,
                      Texture texture) {
        this.world = world;
        this.damage = damage;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(texture);

        sprite.setSize(AppConfig.ATTACK_SPRITE_WIDTH, AppConfig.ATTACK_SPRITE_HEIGHT);
        sprite.setPosition(x, y);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        this.body = world.createBody(bodyDef);

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
    }

    public BaseAttack(World world, float x, float y, Texture texture) {
        this(world, AppConfig.BASE_ATTACK_DEFAULT_DMG,
            AppConfig.BASE_ATTACK_DEFAULT_SPEED, x, y,
            texture);
    }

    public void update(float cameraX, float viewPortWidth) {
        Vector2 pos = body.getPosition();
        sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getHeight() / 2);

        float rightEdge = cameraX + viewPortWidth / 2;
        float leftEdge = cameraX - viewPortWidth / 2;
        if (pos.x > rightEdge + sprite.getWidth() || pos.x < leftEdge - sprite.getWidth()) {
            shouldRemove = true;
        }
    }

    public void render(SpriteBatch batch) {
        if (speed < 0) {
            sprite.setFlip(true, false);
        }
        sprite.draw(batch);
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    public void setShouldRemove(boolean shouldRemove) {
        this.shouldRemove = shouldRemove;
    }
}
