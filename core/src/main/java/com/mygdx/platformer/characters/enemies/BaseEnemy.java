package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

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



    }

    public void render(SpriteBatch batch) {
        if (sprite != null && body != null) {
            sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,
                body.getPosition().y - sprite.getHeight() / 2);
            sprite.draw(batch);
        }
    }

    public void update(float delta) {

    }

    public void takeDamage(int damage) {
        health -= damage;
    }
}
