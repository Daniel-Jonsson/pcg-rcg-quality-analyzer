package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

/**
 * Fallback action for the autoplay AI.
 * @author Robert Kullman, Daniel Jönsson
 */
public class IdleTask extends LeafTask<Player> {

    /**
     * Executes the task.
     * @return Status of the task.
     */
    @Override
    public Status execute() {
        return Status.RUNNING;
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
