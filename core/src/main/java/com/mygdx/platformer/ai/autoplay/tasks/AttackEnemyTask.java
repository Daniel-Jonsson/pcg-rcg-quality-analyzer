package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

/**
 * This task executes an attack if an enemy is detected.
 * @author Robert Kullman, Daniel Jönsson
 */
public class AttackEnemyTask extends LeafTask<Player> {
    private float attackCooldown = 0.2f;
    private float lastAttackTime = -attackCooldown;

    /**
     * Executes the task.
     * @return Status of the task.
     */
    @Override
    public Status execute() {
        Player player = getObject();
        float currentTime = player.getGameTime();

        if (currentTime - lastAttackTime >= attackCooldown) {
            player.attack();
            lastAttackTime = currentTime;
            return Status.SUCCEEDED;
        }
        return Status.RUNNING;  // run until cooldown has passed and the attack can be performed.
    }

    /**
     * Copies the task to a new task.
     * @param task the task to be filled.
     * @return The new task.
     */
    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return new AttackEnemyTask();
    }
}
