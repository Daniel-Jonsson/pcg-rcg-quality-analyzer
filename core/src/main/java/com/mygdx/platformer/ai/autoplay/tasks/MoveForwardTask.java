package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

/**
 * This task moved the player forward if the path is clear.
 * @author Robert Kullman, Daniel Jönsson
 */
public class MoveForwardTask extends LeafTask<Player> {
    @Override
    public Status execute() {
        Player player = getObject();
        player.moveForward();
        return Status.SUCCEEDED;
    }

    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return null;
    }
}
