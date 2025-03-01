package com.mygdx.platformer.pcg;

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
import com.mygdx.platformer.utilities.Assets;

/**
 * This class represents a platform in the game world. It uses a
 * Box2D body for physics and a sprite for rendering.
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class Platform {

    /** Texture used to render the platform. */
    private Texture texture;

    /** Sprite representation of the platform for rendering. */
    private Sprite sprite;

    /** Box2D body representing the platform in the physics world. */
    private Body body;

    /**
     * Constructs a new platform at the given position with the specified dimensions.
     *
     * @param world The Box2D world where the platform exists.
     * @param x The x-coordinate (center) of the platform in world units.
     * @param y The y-coordinate (center) of the platform in world units.
     * @param width The width of the platform in world units.
     * @param height The height of the platform in world units.
     */
    public Platform(World world, float x, float y, float width, float height) {
        texture = Assets.assetManager.get(Assets.PLATFORM_TEXTURE, Texture.class);
        sprite = new Sprite(texture);
        sprite.setSize(width, height);

        // physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y); // position in world units (meters)
        body = world.createBody(bodyDef);

        // collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.categoryBits = AppConfig.CATEGORY_PLATFORM;
        fixtureDef.filter.maskBits =
            AppConfig.CATEGORY_ENEMY | AppConfig.CATEGORY_PLAYER | AppConfig.CATEGORY_ATTACK;

        body.createFixture(fixtureDef);
        shape.dispose();
        body.setUserData(this);

    }

    /**
     * Renders the platform by drawing the sprite.
     * @param batch SpriteBatch for rendering.
     */
    public void render(SpriteBatch batch) {
        // Sync sprite position
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,
            body.getPosition().y - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    /**
     * Disposes of the platform's texture to free resources.
     */
    public void dispose() {
        // no need to dispose here because of AssetManager
    }

    /**
     * Accessor for the Box2D body associated with this platform.
     *
     * @return The platform's physics body.
     */
    public Body getBody() {
        return body;
    }

    /**
     * Accessor for the width of the platform in world units.
     *
     * @return The width of the platform.
     */
    public float getWidth() {
        return sprite.getWidth();
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

}
