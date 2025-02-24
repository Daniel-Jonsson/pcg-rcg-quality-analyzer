package com.mygdx.platformer.characters.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

public class Necromancer extends BaseEnemy {

    public Necromancer(World world, Vector2 position) {
        super(world, position, AppConfig.NECROMANCER_HEALTH, AppConfig.NECROMANCER_ATTACK_POWER, AppConfig.NECROMANCER_SPEED);

        this.texture = Assets.assetManager.get(Assets.PLAYER_TEXTURE, Texture.class);
        this.sprite = new Sprite(texture);
        sprite.setSize(AppConfig.PLAYER_WIDTH, AppConfig.PLAYER_HEIGHT);
    }
}
