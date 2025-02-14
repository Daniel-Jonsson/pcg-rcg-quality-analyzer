package com.mygdx.platformer.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

    private Texture texture;
    private Sprite sprite;
    private float movementSpeed = 200f;

    public Player(float x, float y) {
        texture = new Texture("player.png");
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void dispose() {
        texture.dispose();
    }

}
