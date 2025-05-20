package com.mygdx.platformer.attacks.pcg;

import com.mygdx.platformer.attacks.NecromancerAttackTemplate;
import com.mygdx.platformer.attacks.modifiers.PulseModifier;
import com.mygdx.platformer.attacks.movement.AccelerateMovement;
import com.mygdx.platformer.attacks.movement.MixedMovement;
import com.mygdx.platformer.attacks.movement.MovementPatternBehavior;
import com.mygdx.platformer.attacks.movement.StraightMovement;
import com.mygdx.platformer.attacks.movement.ZigZagMovement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The CompoundAttack class represents a compound attack, which is a collection of
 * individual attacks that are generated using a builder pattern.
 * 
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class CompoundAttack {
    private final int numberOfAttacks;
    private final int damage;
    private final float speed;
    private final List<NecromancerAttackTemplate> attackPattern;

    private Random random = new Random();

    /**
     * Creates a new CompoundAttack with the specified number of projectiles, speed, and damage.
     * This constructor initializes the attack pattern with randomly selected movement behaviors.
     *
     * @param projectileNumber The number of projectiles in this compound attack
     * @param speed The speed at which the projectiles will move
     * @param damage The amount of damage each projectile will deal
     */
    public CompoundAttack(int projectileNumber, float speed, int damage) {
        numberOfAttacks = projectileNumber;
        this.speed = speed;
        this.damage = damage;
        this.attackPattern = generateAttackPattern();
    }


    /**
     * Generates the attack pattern for the compound attack.
     * <p>
     * This method randomly selects a movement behavior and creates a list of
     * NecromancerAttackTemplate objects with the specified number of projectiles,
     * speed, and damage.
     *
     * @return A list of NecromancerAttackTemplate objects representing the attack pattern
     */
    private List<NecromancerAttackTemplate> generateAttackPattern() {
        int movementSelector = random.nextInt(3);


        MovementPatternBehavior pattern = switch (movementSelector) {
            case 0 -> new ZigZagMovement();
            case 1 -> new AccelerateMovement();
            case 2 -> new MixedMovement();
            default -> new StraightMovement();
        };

        List<NecromancerAttackTemplate> attackTemplates = new ArrayList<>();
        // TODO: Change arc and projectileCount so it uses correct value later
        for (int i = 0; i < numberOfAttacks; i++) {
            attackTemplates.add(new NecromancerAttackTemplate(45, speed,
                damage, 5, pattern, new PulseModifier(2, 0.5f)));
        }
        return attackTemplates;
    }

    /**
     * Returns the number of attacks (projectiles) in this compound attack.
     *
     * @return The size of the attack pattern list.
     */
    public int getAttackSize() {
        return attackPattern.size();
    }

    /**
     * Returns the attack pattern for this compound attack.
     *
     * @return The list of NecromancerAttackTemplate objects representing the attack pattern
     */
    public List<NecromancerAttackTemplate> getAttackPattern() {
        return attackPattern;
    }
}
