package com.mygdx.platformer.ai.autoplay.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.mygdx.platformer.characters.player.Player;

public class DetectEnemyTask extends LeafTask<Player> {
    @Override
    public Status execute() {
        return null;
    }

    @Override
    protected Task<Player> copyTo(Task<Player> task) {
        return null;
    }
}
