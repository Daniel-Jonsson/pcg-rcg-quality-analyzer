package com.mygdx.platformer.attacks.pcg;

import com.mygdx.platformer.attacks.NecromancerAttackTemplate;

import java.util.ArrayList;
import java.util.List;

public class CompoundAttack {
    private final int numberOfAttacks;
    private final int damage;
    private final float speed;
    private final List<NecromancerAttackTemplate> attackPattern;

    public CompoundAttack(int projectileNumber, float speed, int damage) {
        numberOfAttacks = projectileNumber;
        this.speed = speed;
        this.damage = damage;
        this.attackPattern = generateAttackPattern();
    }

    private List<NecromancerAttackTemplate> generateAttackPattern() {
        List<NecromancerAttackTemplate> attackTemplates = new ArrayList<>();
        // TODO: Change arc and projectileCount so it uses correct value later
        for (int i = 0; i < numberOfAttacks; i++) {
            attackTemplates.add(new NecromancerAttackTemplate(45, speed,
                damage, 5));
        }
        return attackTemplates;
    }

    public int getAttackSize() {
        return attackPattern.size();
    }

    public List<NecromancerAttackTemplate> getAttackPattern() {
        return attackPattern;
    }
}
