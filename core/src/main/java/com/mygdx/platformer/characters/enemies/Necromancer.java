package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

public class Necromancer extends BaseEnemy {

    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> attackAnimation;

    private float moveDirection = 1; // 1 = right, -1 = left (temporary for movement testing)
    private float moveTime = 0; // Tracks how long the enemy has moved in one direction, temporary for testing purposes
    private final float switchTime = 2f; // Time before switching direction, temporary for testing purposes.


    private float stateTime = 0f;
    private boolean facingRight = true;
    private TextureRegion currentFrame;

    private TextureAtlas textureAtlas;

    public Necromancer(World world, Vector2 position) {
        super(world, position, AppConfig.NECROMANCER_HEALTH, AppConfig.NECROMANCER_ATTACK_POWER, AppConfig.NECROMANCER_SPEED);

        textureAtlas = Assets.getNecromancerAtlas();



        idleAnimation = new Animation<>(AppConfig.STANDARD_FRAME_DURATION, textureAtlas.findRegions("necromancer_idle"), Animation.PlayMode.LOOP);
        attackAnimation = new Animation<>(AppConfig.ATTACK_FRAME_DURATION, textureAtlas.findRegions("necromancer_attack"), Animation.PlayMode.LOOP);

        currentFrame = idleAnimation.getKeyFrame(0);

        // physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef); // add player body to game world

        // collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(AppConfig.NECROMANCER_WIDTH * AppConfig.NECROMANCER_HITBOX_SCALE , AppConfig.NECROMANCER_HEIGHT * AppConfig.NECROMANCER_HITBOX_SCALE);

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

    @Override
    public void render(SpriteBatch batch) {
        boolean flip = !facingRight;
        int offsetModifier = flip ? -1 : 1;

        batch.draw(
            currentFrame,
            body.getPosition().x - AppConfig.NECROMANCER_WIDTH * offsetModifier ,
            body.getPosition().y - AppConfig.CHARACTER_Y_OFFSET,
            AppConfig.NECROMANCER_WIDTH * AppConfig.NECROMANCER_SCALE * offsetModifier,
            AppConfig.NECROMANCER_HEIGHT * AppConfig.NECROMANCER_SCALE
        );
    }

    @Override
    public void update(float deltaTime) {
        stateTime += deltaTime;
        moveTime += deltaTime;

        if (moveTime >= switchTime) {
            moveDirection *= -1;
            moveTime = 0;
            facingRight = moveDirection > 0;
        }

        body.setLinearVelocity(moveDirection * AppConfig.NECROMANCER_SPEED, body.getLinearVelocity().y);

        currentFrame = idleAnimation.getKeyFrame(stateTime);
    }
}
