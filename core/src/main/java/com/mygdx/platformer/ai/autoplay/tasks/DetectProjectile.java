package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

/**
 * This task detects incoming projectiles.
 * @author Robert Kullman, Daniel Jönsson
 */
public class DetectProjectile extends LeafTask<Player> {

    /**
     * Executes the task.
     * @return Status of the task.
     */
    @Override
    public Status execute() {
        Player player = getObject();
        System.out.println("Detecting projectile");
       // return player.detectIncomingProjectile() ? Status.SUCCEEDED : Status.FAILED;
        return Status.FAILED;
    }

    /**
     * Copies the task to a new task.
     * @param task the task to be filled.
     * @return The new task.
     */
    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return null;
    }
}
