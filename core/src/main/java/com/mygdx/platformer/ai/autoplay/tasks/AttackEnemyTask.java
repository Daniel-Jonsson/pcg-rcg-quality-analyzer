package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

/**
 * This task executes an attack if an enemy is detected.
 * @author Robert Kullman, Daniel Jönsson
 */
public class AttackEnemyTask extends LeafTask<Player> {
    @Override
    public Status execute() {
        return null;
    }

    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return null;
    }
}
