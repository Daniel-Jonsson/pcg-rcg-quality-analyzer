package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

/**
 * This task attempts to dodge an attack if detected.
 * @author Robert Kullman, Daniel Jönsson.
 */
public class DodgeTask extends LeafTask<Player> {
    @Override
    public Status execute() {
        return null;
    }

    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return null;
    }
}
