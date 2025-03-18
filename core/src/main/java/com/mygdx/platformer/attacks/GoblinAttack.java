package com.mygdx.platformer.attacks;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.platformer.utilities.AppConfig;
import com.mygdx.platformer.utilities.Assets;

/**
 * Represents a goblin projectile attack.
 * This attack moves in a specified direction and interacts with targets.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public class GoblinAttack extends BaseAttack {

    /**
     * Constructor for the GoblinAttack.
     * @param world The game world in which to spawn the attack.
     * @param x x position.
     * @param y y position.
     * @param directionModifier Travel direction for the attack.
     * @param dmg Attack damage.
     * @param speed Movement speed of the attack.
     */
    public GoblinAttack(World world, float x, float y, int directionModifier,
                        int dmg, float speed) {
        super(world, dmg, speed * directionModifier,
            x + AppConfig.GOBLIN_ATTACK_X_OFFSET, y + AppConfig.GOBLIN_ATTACK_Y_OFFSET,
            Assets.assetManager.get(Assets.THROWING_DAGGER_TEXTURE), false);
        super.body.setUserData(this);
    }
}

