package com.mygdx.platformer.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.platformer.attacks.BaseAttack;
import com.mygdx.platformer.characters.enemies.BaseEnemy;
import com.mygdx.platformer.characters.player.Player;
import com.mygdx.platformer.utilities.AppConfig;

/**
 * Handles all Box2D contact events for the platformer game.
 * <p>
 * This class is responsible for detecting and handling collisions between
 * different game entities such as:
 * <ul>
 * <li>Player and platforms (for detecting when player is grounded)</li>
 * <li>Attacks and enemies (for dealing damage)</li>
 * <li>Enemy attacks and player (for handling player damage)</li>
 * </ul>
 * </p>
 * <p>
 * By separating collision handling into its own class, we adhere to the
 * single responsibility principle and make the code more maintainable.
 * </p>
 */
public class GameContactListener implements ContactListener {

    /**
     * Reference to the player for detecting collisions and applying effects.
     */
    private final Player player;

    /**
     * Creates a new GameContactListener.
     *
     * @param player The player instance to use for collision detection
     */
    public GameContactListener(Player player) {
        this.player = player;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        Object aUserData = a.getBody().getUserData();
        Object bUserData = b.getBody().getUserData();

        Body playerBody = player.getBody();

        if (a.getBody() == playerBody || b.getBody() == playerBody) {
            if (a.getFilterData().categoryBits == AppConfig.CATEGORY_PLATFORM
                    || b.getFilterData().categoryBits == AppConfig.CATEGORY_PLATFORM
                    || a.getFilterData().categoryBits == AppConfig.CATEGORY_ENEMY
                    || b.getFilterData().categoryBits == AppConfig.CATEGORY_ENEMY) {
                player.setGrounded(true);
            }
        }

        // Attack -> Enemy collision
        if ((a.getFilterData().categoryBits == AppConfig.CATEGORY_ATTACK
                && b.getFilterData().categoryBits == AppConfig.CATEGORY_ENEMY)
                || (b.getFilterData().categoryBits == AppConfig.CATEGORY_ATTACK
                        && a.getFilterData().categoryBits == AppConfig.CATEGORY_ENEMY)) {

            if (aUserData instanceof BaseAttack || bUserData instanceof BaseAttack) {
                BaseAttack attack = (aUserData instanceof BaseAttack) ? (BaseAttack) aUserData
                        : (BaseAttack) bUserData;

                attack.setShouldRemove(true);
                BaseEnemy enemy = (aUserData instanceof BaseEnemy) ? (BaseEnemy) aUserData
                        : (BaseEnemy) bUserData;

                enemy.takeDamage(attack.getDamage());
            }
        }

        if ((a.getFilterData().categoryBits == AppConfig.CATEGORY_ATTACK
                && b.getFilterData().categoryBits == AppConfig.CATEGORY_PLAYER)
                || (b.getFilterData().categoryBits == AppConfig.CATEGORY_ATTACK
                        && a.getFilterData().categoryBits == AppConfig.CATEGORY_PLAYER)) {
            BaseAttack attack = (aUserData instanceof BaseAttack) ? (BaseAttack) aUserData
                    : (BaseAttack) bUserData;

            if (attack.isEnemyAttack()) {
                attack.setShouldRemove(true);
                player.takeDamage(attack.getDamage());
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        Body playerBody = player.getBody();

        if (a.getBody() == playerBody || b.getBody() == playerBody) {
            if (a.getFilterData().categoryBits == AppConfig.CATEGORY_PLATFORM ||
                    b.getFilterData().categoryBits == AppConfig.CATEGORY_PLATFORM
                    || a.getFilterData().categoryBits == AppConfig.CATEGORY_ENEMY
                    || b.getFilterData().categoryBits == AppConfig.CATEGORY_ENEMY) {
                player.setGrounded(false);
            }
        }
    }

    @Override
    public void preSolve(final Contact contact, final Manifold manifold) {
        
    }

    @Override
    public void postSolve(final Contact contact, final ContactImpulse contactImpulse) {
        
    }
}