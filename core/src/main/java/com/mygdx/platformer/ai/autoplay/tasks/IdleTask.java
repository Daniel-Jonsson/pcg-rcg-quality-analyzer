package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

/**
 * Fallback action for the autoplay AI.
 * @author Robert Kullman, Daniel Jönsson
 */
public class IdleTask extends LeafTask<Player> {
    @Override
    public Status execute() {
        return Status.RUNNING;
    }

    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return null;
    }
}
