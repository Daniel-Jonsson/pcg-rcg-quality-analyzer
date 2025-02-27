package com.mygdx.platformer.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BaseAttack {
    protected int damage;
    protected float speed;
    protected float x, y;
    protected Sprite sprite;
    protected boolean shouldRemove;

    public BaseAttack(int damage, float speed, float x, float y,
                      Texture texture) {
        this.damage = damage;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(texture);

        sprite.setSize(0.6f, 0.3f);
        sprite.setPosition(x, y);
    }

    public BaseAttack(float x, float y, Texture texture) {
        this(20, 25, x, y, texture);
    }

    public void update(float deltaTime, float cameraX, float viewPortWidth) {
        x += speed * deltaTime;
        sprite.setPosition(x, y);

        if (speed < 0 && !sprite.isFlipX()) {
            sprite.flip(true, false);
        } else if (speed > 0 && sprite.isFlipX()) {
            sprite.flip(true, false);
        }

        float rightEdge = cameraX + viewPortWidth / 2;
        float leftEdge = cameraX - viewPortWidth / 2;

        if (x > rightEdge + sprite.getWidth() || x < leftEdge - sprite.getWidth()) {
            shouldRemove = true;
        }
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }
}
