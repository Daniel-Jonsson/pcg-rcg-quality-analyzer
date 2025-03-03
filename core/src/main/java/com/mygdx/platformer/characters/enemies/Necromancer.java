package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

public class Necromancer extends BaseEnemy {

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> attackAnimation;

    private TextureAtlas textureAtlas;

    public Necromancer(World world, Vector2 position) {
        super(world, position, AppConfig.NECROMANCER_HEALTH,
            AppConfig.NECROMANCER_ATTACK_POWER, AppConfig.NECROMANCER_SPEED);
    }

    @Override
    public void render(SpriteBatch batch) {
        boolean flip = !facingRight;
        int offsetModifier = flip ? -1 : 1;

        batch.draw(
            currentFrame,
            body.getPosition().x - AppConfig.NECROMANCER_WIDTH * offsetModifier,
            body.getPosition().y - AppConfig.NECROMANCER_HITBOX_SIZE_Y,
            AppConfig.NECROMANCER_WIDTH * AppConfig.NECROMANCER_SCALE * offsetModifier,
            AppConfig.NECROMANCER_HEIGHT * AppConfig.NECROMANCER_SCALE
        );
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentFrame = idleAnimation.getKeyFrame(stateTime);
    }

    @Override
    protected void setupAnimations() {
        textureAtlas = Assets.getNecromancerAtlas();

        idleAnimation = new Animation<>(AppConfig.STANDARD_FRAME_DURATION,
            textureAtlas.findRegions("necromancer_idle"),
            Animation.PlayMode.LOOP);
        attackAnimation = new Animation<>(AppConfig.ATTACK_FRAME_DURATION,
            textureAtlas.findRegions("necromancer_attack"),
            Animation.PlayMode.LOOP);
        currentFrame = idleAnimation.getKeyFrame(0);
    }

    @Override
    protected Vector2 getHitBoxSize() {
        return new Vector2(AppConfig.NECROMANCER_HITBOX_SIZE_X,
            AppConfig.NECROMANCER_HITBOX_SIZE_Y);
    }
}
