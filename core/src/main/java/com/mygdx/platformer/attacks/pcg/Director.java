package com.mygdx.platformer.attacks.pcg;

import com.mygdx.platformer.attacks.NecromancerAttackTemplate;

import java.util.List;
import java.util.Random;

public class Director {
    private final Random rand = new Random();

    public void constructNecromancerPCGAttack(Builder attackBuilder) {
        attackBuilder.setProjectileNumber(rand.nextInt(1, 5));
        // TODO: Make the damage and speed depend on current difficulty level
        attackBuilder.setDamage(rand.nextInt(20, 30));
        attackBuilder.setSpeed(rand.nextFloat(1, 3));
    }

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

    private CompoundAttack recombine(CompoundAttack host, CompoundAttack donor) {
        int bound = Math.min(donor.getAttackSize(), host.getAttackSize());
        int indexToChange = rand.nextInt(bound);
        NecromancerAttackTemplate attackToDonate =
            donor.getAttackPattern().get(indexToChange);
        host.getAttackPattern().set(indexToChange, attackToDonate);
        return host;
    }
}
