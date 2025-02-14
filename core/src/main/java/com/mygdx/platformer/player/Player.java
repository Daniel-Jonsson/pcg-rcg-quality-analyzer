package com.mygdx.platformer.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
        sprite.setSize(50f, 50f);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void update(float deltaTime) {
        float move = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            move = -movementSpeed * deltaTime;
        } else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            move = movementSpeed * deltaTime;
        }

        sprite.translateX(move);
    }

    public void dispose() {
        texture.dispose();
    }

}
