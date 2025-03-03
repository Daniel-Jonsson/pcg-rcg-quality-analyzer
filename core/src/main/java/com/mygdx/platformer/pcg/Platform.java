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
 * This class represents a platform in the game world and is composed of
 * multiple segments (start, middle, and end). It uses a Box2D body for
 * physics and a sprite for rendering.
 * @author Robert Kullman
 * @author Daniel Jönsson
 */
public class Platform {

    /** Texture for the start, middle, and end segment of the platform. */
    private Texture startTexture, middleTexture, endTexture;

    /** Sprite for the start, middle, and end segment of the platform. */
    private Sprite startSprite, middleSprite, endSprite;

    /** Width of each middle segment of the platform. */
    private float segmentWidth;

    /** Number of middle segments forming the platform. */
    private float segmentCount;

    /** Total width of the platform. */
    private float platformWidth;

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
        texture = Assets.assetManager.get(Assets.PLATFORM_MIDDLE, Texture.class);
        sprite = new Sprite(texture);
        sprite.setSize(width, height);

        platformWidth = width;
        segmentWidth = AppConfig.PLATFORM_MIDDLE_SEGMENT_WIDTH;
        segmentCount = width / segmentWidth;

        // textures
        startTexture = Assets.assetManager.get(Assets.PLATFORM_START, Texture.class);
        middleTexture = Assets.assetManager.get(Assets.PLATFORM_MIDDLE, Texture.class);
        endTexture = Assets.assetManager.get(Assets.PLATFORM_END, Texture.class);

        startSprite = new Sprite(startTexture);
        middleSprite = new Sprite(middleTexture);
        endSprite = new Sprite(endTexture);

        startSprite.setSize(.1f, AppConfig.PLATFORM_HEIGHT);
        middleSprite.setSize(segmentWidth, AppConfig.PLATFORM_HEIGHT);
        endSprite.setSize(.1f, AppConfig.PLATFORM_HEIGHT);

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
        // start segment
        startSprite.setPosition(body.getPosition().x - platformWidth / 2 -0.05f,
            body.getPosition().y - AppConfig.PLATFORM_HEIGHT / 2);

        // middle segments
        for (int i = 0; i < segmentCount; i++) {
            middleSprite.setPosition((body.getPosition().x - platformWidth / 2) + (i * (segmentWidth)),
                body.getPosition().y - AppConfig.PLATFORM_HEIGHT / 2);
            middleSprite.draw(batch);
        }

        // end segment
        endSprite.setPosition(body.getPosition().x + platformWidth / 2,
            body.getPosition().y - sprite.getHeight() / 2);
        startSprite.draw(batch);

        endSprite.draw(batch);
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

}
