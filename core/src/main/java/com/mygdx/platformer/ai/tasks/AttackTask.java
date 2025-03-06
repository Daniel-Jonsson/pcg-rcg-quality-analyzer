package com.mygdx.platformer.ai.tasks;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.platformer.ai.AIAgent;
import com.mygdx.platformer.attacks.AttackManager;
import com.mygdx.platformer.characters.BaseCharacter;
import com.mygdx.platformer.characters.enemies.BaseEnemy;

public class AttackTask extends LeafTask<AIAgent> {
    private float attackCooldown;
    private AttackManager attackManager;
    private Map<BaseCharacter, Long> attackCooldowns;

    public AttackTask(float attackCooldown, AttackManager attackManager) {
        this.attackCooldown = attackCooldown;
        this.attackManager = attackManager;
        this.attackCooldowns = new HashMap<>();
    }

    @Override
    public Status execute() {
        AIAgent agent = getObject();
        BaseCharacter character = agent.getCharacter();
        BaseEnemy enemy = (BaseEnemy) agent.getCharacter();
        long lastAttackTime = attackCooldowns.getOrDefault(character, 0L);

        long currentTime = System.currentTimeMillis();

        long cooldownMs = (long) (attackCooldown * 1000);

        long timeSinceLastAttack = currentTime - lastAttackTime;

        if (timeSinceLastAttack < cooldownMs) {
            return Status.FAILED;
        }


        Vector2 targetPosition = agent.getTargetPosition();

        if (targetPosition == null) {
            return Status.FAILED;
        }

        int direction = targetPosition.x > character.getBody().getPosition().x ? 1 : -1;

        attackManager.spawnEnemyAttackAt(character.getBody().getPosition(), direction);

        lastAttackTime = currentTime;

        attackCooldowns.put(character, lastAttackTime);
        enemy.startAttack();

        return Status.SUCCEEDED;
    }

    @Override
    protected Task<AIAgent> copyTo(Task<AIAgent> task) {
        AttackTask attackTask = (AttackTask) task;
        attackTask.attackCooldown = attackCooldown;
        attackTask.attackManager = attackManager;
        attackTask.attackCooldowns = new HashMap<>(attackCooldowns);
        return task;
    }

}
