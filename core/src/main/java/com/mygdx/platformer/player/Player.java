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
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;

/**
 * This class represents the player character in the game.
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class Player {

    /** The texture representing the player. */
    private final Texture texture;

    /** The sprite used for rendering the player. */
    private final Sprite sprite;

    /** The Box2D physics body of the player. */
    private final Body body;

    /** The movement speed of the player. */
    private float moveSpeed = AppConfig.PLAYER_MOVE_SPEED;

    /** The force applied when the player jumps. */
    private float jumpForce = AppConfig.PLAYER_JUMP_FORCE;

    /** Tracks whether the player is currently on the ground. */
    private boolean isGrounded = false;

    /** Tracks whether the player is currently jumping or not. i.e if the
     * spacebar is being held down.*/
    private boolean isJumping = false;

    /** Tracks how long time a jump has been going on for */
    private long jumpPressTime;


    /**
     * Instantiates the player in the game world.
     * @param world The Box2D world.
     * @param x Starting x-coordinate where the player spawns.
     * @param y Starting y-coordinate where the player spawns.
     */
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
        shape.setAsBox(playerWidth * AppConfig.PLAYER_HITBOX_SCALE / 2, playerHeight * AppConfig.PLAYER_HITBOX_SCALE / 2);


        // attach the polygon shape to the body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);
        shape.dispose();

        MassData massData = new MassData();
        massData.mass = AppConfig.PLAYER_MASS;
        body.setMassData(massData);
    }

    /**
     * Renders the player sprite.
     * @param batch SpriteBatch for rendering.
     */
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Updates the player state.
     */
    public void update() {
        handleInput();

       // this syncs the sprite position with the Box2D body
        sprite.setPosition(
            body.getPosition().x - sprite.getWidth() / 2,
            body.getPosition().y - sprite.getHeight() / 2);

    }

    /**
     * Handles player input.
     */
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
            isJumping = true;
            jumpPressTime = System.currentTimeMillis();
        }

        if (isJumping && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            long holdDuration = System.currentTimeMillis() - jumpPressTime;

            if (holdDuration <= AppConfig.MAX_JUMP_HOLD_TIME && body.getLinearVelocity().y > 0) {
                body.applyForce(new Vector2(0, AppConfig.JUMP_HOLD_FORCE),
                    body.getWorldCenter(),
                    true);
            } else {
                isJumping = false;
            }
        }
    }

    /**
     * Disposes the player texture to free resources.
     */
    public void dispose() {
        texture.dispose();
    }

    /**
     * Sets whether the player is on the ground or not.
     * @param grounded Boolean indicating whether the player is on the ground.
     */
    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    /**
     * Accessor for the player's physics body.
     * @return
     */
    public Body getBody() {
        return body;
    }
}
