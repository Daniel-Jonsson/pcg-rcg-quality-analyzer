package com.mygdx.platformer.attacks.pcg;

import com.mygdx.platformer.attacks.NecromancerAttackTemplate;

import java.util.List;
import java.util.Random;

/**
 * The Director class is responsible for directing the construction of attacks
 * using the Builder pattern. It provides methods to construct different types
 * of attacks and to recombine existing attacks to create new ones.
 * 
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class Director {
    private final Random rand = new Random();

    /**
     * Constructs a new Necromancer PCG attack using the provided builder.
     * <p>
     * This method sets the number of projectiles, damage, and speed for the attack
     * based on random values. The damage and speed are generated within specified
     * ranges to ensure the attack is balanced and challenging.
     *
     * @param attackBuilder The builder to use for constructing the attack.
     */
    public void constructNecromancerPCGAttack(Builder attackBuilder) {
        attackBuilder.setProjectileNumber(rand.nextInt(1, 5));
        // TODO: Make the damage and speed depend on current difficulty level
        attackBuilder.setDamage(rand.nextInt(20, 30));
        attackBuilder.setSpeed(rand.nextFloat(1, 3));
    }

    /**
     * Constructs a new Necromancer PCG attack using the provided builder.
     * <p>
     * This method sets the number of projectiles, damage, and speed for the attack
     * based on random values. The damage and speed are generated within specified
     * ranges to ensure the attack is balanced and challenging.
     *
     * @param attacks The list of existing attacks to recombine.
     * @param attackIndex The index of the attack to host the recombined attack.
     * @return A new CompoundAttack object representing the recombined attack.
     */
    public CompoundAttack constructNecromancerRCGAttack(List<CompoundAttack> attacks,
                                              int attackIndex) {
        CompoundAttack host = attacks.get(attackIndex);
        CompoundAttack donor;
        int donorIndex = rand.nextInt(0, attacks.size() - 1);
        while (donorIndex == attackIndex) {
            donorIndex = rand.nextInt(0, attacks.size() - 1);
        }
        donor = attacks.get(donorIndex);
        return recombine(host, donor);
    }

    /**
     * Recombines two CompoundAttack objects to create a new CompoundAttack.
     * <p>
     * This method selects a random index in the donor's attack pattern and
     * replaces the corresponding attack in the host's attack pattern with the
     * donor's attack.
     *
     * @param host The host CompoundAttack object.
     * @param donor The donor CompoundAttack object.
     * @return A new CompoundAttack object representing the recombined attack.
     */
    private CompoundAttack recombine(CompoundAttack host, CompoundAttack donor) {
        int bound = Math.min(donor.getAttackSize(), host.getAttackSize());
        int indexToChange = rand.nextInt(bound);
        NecromancerAttackTemplate attackToDonate =
            donor.getAttackPattern().get(indexToChange);
        host.getAttackPattern().set(indexToChange, attackToDonate);
        return host;
    }
}
