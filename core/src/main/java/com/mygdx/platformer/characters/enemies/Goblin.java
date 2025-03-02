package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
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

public class Goblin extends BaseEnemy {

    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> walkAnimation;
    private final Animation<TextureRegion> attackAnimation;

    private float moveDirection = 1; // 1 = right, -1 = left (temporary for movement testing)
    private float moveTime = 0; // Tracks how long the enemy has moved in one direction, temporary for testing purposes
    private final float switchTime = 1f; // Time before switching direction, temporary for testing purposes.


    private float stateTime = 0f;
    private boolean facingRight = true;
    private TextureRegion currentFrame;

    private TextureAtlas textureAtlas;

    public Goblin(World world, Vector2 position) {
        super(world, position, AppConfig.GOBLIN_HEALTH, AppConfig.GOBLIN_ATTACK_POWER, AppConfig.GOBLIN_SPEED);

        textureAtlas = Assets.getGoblinAtlas();



        idleAnimation = new Animation<>(AppConfig.STANDARD_FRAME_DURATION, textureAtlas.findRegions("goblin_idle"), Animation.PlayMode.LOOP);
        walkAnimation = new Animation<>(AppConfig.WALK_FRAME_DURATION, textureAtlas.findRegions("goblin_walk"), Animation.PlayMode.LOOP);
        attackAnimation = new Animation<>(AppConfig.ATTACK_FRAME_DURATION, textureAtlas.findRegions("goblin_attack"), Animation.PlayMode.LOOP);

        currentFrame = idleAnimation.getKeyFrame(0);

        // physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef); // add player body to game world

        // collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(AppConfig.GOBLIN_WIDTH * AppConfig.GOBLIN_HITBOX_SCALE , AppConfig.GOBLIN_HEIGHT * AppConfig.GOBLIN_HITBOX_SCALE);

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
            body.getPosition().x - AppConfig.GOBLIN_WIDTH * offsetModifier ,
            body.getPosition().y - AppConfig.CHARACTER_Y_OFFSET,
            AppConfig.GOBLIN_WIDTH * AppConfig.GOBLIN_SCALE * offsetModifier,
            AppConfig.GOBLIN_HEIGHT * AppConfig.GOBLIN_SCALE
        );
    }

    @Override
    public void update(float deltaTime) {
        stateTime += deltaTime;
        moveTime += deltaTime;

        if (moveTime >= switchTime) {
            moveDirection *= -1;
            moveTime = 0; // Reset movement timer
            facingRight = moveDirection > 0;
        }

        body.setLinearVelocity(moveDirection * AppConfig.GOBLIN_SPEED, body.getLinearVelocity().y);

        if (Math.abs(body.getLinearVelocity().x) > 0.1f) {

            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        } else {
            ;
            currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        }
    }

}
