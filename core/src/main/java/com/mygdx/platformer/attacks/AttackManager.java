package com.mygdx.platformer.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.Assets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttackManager {
    private final List<BaseAttack> attacks;
    private final Texture orbTexture;
    private final World world;

    public AttackManager(World world) {
        this.world = world;
        this.attacks = new ArrayList<>();
        this.orbTexture = new Texture(Assets.THROWING_DAGGER_TEXTURE);
    }

    public void spawnAttackAt(Vector2 position) {
        OrbAttack orbAttack = new OrbAttack(world, position.x, position.y, orbTexture);
        attacks.add(orbAttack);
        System.out.println("Spawned orb at " + position);
    }

    public void update(float cameraX, float viewPortWidth) {
        Iterator<BaseAttack> iterator = attacks.iterator();
        while (iterator.hasNext()) {
            BaseAttack attack = iterator.next();
            attack.update(cameraX, viewPortWidth);

            if (attack.shouldRemove()) {
                iterator.remove();
                world.destroyBody(attack.body);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (BaseAttack attack : attacks) {
            attack.render(batch);
        }
    }
}
