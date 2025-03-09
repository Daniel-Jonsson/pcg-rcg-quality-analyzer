package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

/**
 * This task checks if the path is clear.
 * @author Robert Kullman, Daniel Jönsson
 */
public class CheckPathClearTask extends LeafTask<Player> {
    @Override
    public Status execute() {
        return null;
    }

    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return null;
    }
}
