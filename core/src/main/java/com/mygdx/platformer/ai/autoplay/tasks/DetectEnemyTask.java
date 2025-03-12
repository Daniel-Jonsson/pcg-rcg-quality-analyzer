package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

public class DetectEnemyTask extends LeafTask<Player> {

    /**
     * Executes the task.
     * @return Status of the task.
     */
    @Override
    public Status execute() {
        Player player = getObject();

        // detect backward
        if (player.getBody().getLinearVelocity().x == 0 && player.getBody().getLinearVelocity().y == 0 && player.hasEnemiesNearby(-1)) {
            player.setFacingRight(false);
            return Status.SUCCEEDED;
        }

        // detect forward
        if (player.hasEnemiesNearby(1)) {
            player.setFacingRight(true);
            return Status.SUCCEEDED;
        }
        return Status.RUNNING;
    }

    /**
     * Copies the task to a new task.
     * @param task the task to be filled.
     * @return The new task.
     */
    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return new DetectEnemyTask();
    }
}
