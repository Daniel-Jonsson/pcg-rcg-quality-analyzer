package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

public class Goblin extends BaseEnemy {

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> attackAnimation;

    private TextureAtlas textureAtlas;

    public Goblin(World world, Vector2 position) {
        super(world, position, AppConfig.GOBLIN_HEALTH, AppConfig.GOBLIN_ATTACK_POWER, AppConfig.GOBLIN_SPEED);
    }

    @Override
    public void render(SpriteBatch batch) {
        boolean flip = !facingRight;
        int offsetModifier = flip ? -1 : 1;

        batch.draw(
            currentFrame,
            body.getPosition().x - AppConfig.GOBLIN_WIDTH * offsetModifier,
            body.getPosition().y - AppConfig.GOBLIN_HITBOX_SIZE_Y,
            AppConfig.GOBLIN_WIDTH * AppConfig.GOBLIN_SCALE * offsetModifier,
            AppConfig.GOBLIN_HEIGHT * AppConfig.GOBLIN_SCALE
        );
    }

    /**
     * Temporary implementation for animation testing
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (Math.abs(body.getLinearVelocity().x) > 0.1f) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        }
    }

    @Override
    protected void setupAnimations() {
        textureAtlas = Assets.getGoblinAtlas();

        idleAnimation = new Animation<>(AppConfig.STANDARD_FRAME_DURATION,
            textureAtlas.findRegions("goblin_idle"), Animation.PlayMode.LOOP);
        walkAnimation = new Animation<>(AppConfig.WALK_FRAME_DURATION,
            textureAtlas.findRegions("goblin_walk"), Animation.PlayMode.LOOP);
        attackAnimation = new Animation<>(AppConfig.ATTACK_FRAME_DURATION,
            textureAtlas.findRegions("goblin_attack"), Animation.PlayMode.LOOP);
        currentFrame = idleAnimation.getKeyFrame(0);
    }

    @Override
    protected Vector2 getHitBoxSize() {
        return new Vector2(AppConfig.GOBLIN_HITBOX_SIZE_X, AppConfig.GOBLIN_HITBOX_SIZE_Y);
    }

}
