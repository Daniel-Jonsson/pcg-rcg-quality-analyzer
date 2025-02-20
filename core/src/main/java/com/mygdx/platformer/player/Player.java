package com.mygdx.platformer.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class Player {

    private final Texture texture;
    private final Sprite sprite;
    private final Body body;

    private float moveSpeed = AppConfig.PLAYER_MOVE_SPEED;
    private float jumpForce = AppConfig.PLAYER_JUMP_FORCE;

    private boolean isGrounded = false;

    public Player(World world, final float x, final float y) {
        texture = new Texture("player.png");

        float playerWidth = AppConfig.PLAYER_WIDTH;
        float playerHeight = AppConfig.PLAYER_HEIGHT;

        sprite = new Sprite(texture);
        sprite.setSize(playerWidth, playerHeight);

        // physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef); // add player body to game world

        // collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(playerWidth / 2, playerHeight / 2);

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
        sprite.draw(batch);
    }

    public void update() {
        handleInput();

       // this syncs the sprite position with the Box2D body
        sprite.setPosition(
            body.getPosition().x - sprite.getWidth() / 2,
            body.getPosition().y - sprite.getHeight() / 2);

    }

    private void handleInput() {
        float move = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            move = -moveSpeed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            move = moveSpeed;
        }

        body.setLinearVelocity(move, body.getLinearVelocity().y);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isGrounded) {
            body.applyLinearImpulse(new Vector2(0, jumpForce), body.getWorldCenter(), true);
            isGrounded = false;
        }
    }

    public void dispose() {
        texture.dispose();
    }

    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }
    public Body getBody() {
        return body;
    }
}
