package com.mygdx.platformer.ai.enemy;

import com.mygdx.platformer.ai.AIAgent;
import com.mygdx.platformer.attacks.AttackManager;
import com.mygdx.platformer.characters.BaseCharacter;

public class EnemyAIAgent extends AIAgent {
    private AttackManager attackManager;
    private float attackCooldown;

    public EnemyAIAgent(BaseCharacter character, float detectionRange, float attackRange, AttackManager attackManager, float attackCooldown) {
        super(character, detectionRange, attackRange);
        this.attackManager = attackManager;
        this.attackCooldown = attackCooldown;
    }

    @Override
    public void update(float deltaTime) {
        updateTargetDistance();
    }
}
