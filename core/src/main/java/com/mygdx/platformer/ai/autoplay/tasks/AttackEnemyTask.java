package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

/**
 * This task executes an attack if an enemy is detected.
 * @author Robert Kullman, Daniel Jönsson
 */
public class AttackEnemyTask extends LeafTask<Player> {
    private float attackCooldown = 0.3f;
    private float lastAttackTime = -attackCooldown;

    @Override
    public Status execute() {
        Player player = getObject();
        float currentTime = player.getGameTime();

        if (currentTime - lastAttackTime >= attackCooldown) {
            System.out.println("Attacking enemy!");
            player.attack();
            lastAttackTime = currentTime;
            return Status.SUCCEEDED;
        }

        return Status.RUNNING;
    }

    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return new AttackEnemyTask();
    }
}
