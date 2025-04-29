package com.mygdx.platformer.attacks.pcg;

import com.mygdx.platformer.attacks.NecromancerAttackTemplate;
import com.mygdx.platformer.attacks.movement.AccelerateMovement;
import com.mygdx.platformer.attacks.movement.MovementPatternBehavior;
import com.mygdx.platformer.attacks.movement.StraightMovement;
import com.mygdx.platformer.attacks.movement.ZigZagMovement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CompoundAttack {
    private final int numberOfAttacks;
    private final int damage;
    private final float speed;
    private final List<NecromancerAttackTemplate> attackPattern;

    private Random random = new Random();

    public CompoundAttack(int projectileNumber, float speed, int damage) {
        numberOfAttacks = projectileNumber;
        this.speed = speed;
        this.damage = damage;
        this.attackPattern = generateAttackPattern();
    }

    private List<NecromancerAttackTemplate> generateAttackPattern() {
        int movementSelector = random.nextInt(2);
        MovementPatternBehavior pattern = switch (movementSelector) {
            case 0 -> new ZigZagMovement();
            case 1 -> new AccelerateMovement();
            default -> new StraightMovement();
        };

        List<NecromancerAttackTemplate> attackTemplates = new ArrayList<>();
        // TODO: Change arc and projectileCount so it uses correct value later
        for (int i = 0; i < numberOfAttacks; i++) {
            attackTemplates.add(new NecromancerAttackTemplate(45, speed,
                damage, 5, pattern));
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
