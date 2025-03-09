package com.mygdx.platformer.ai.autoplay;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.Selector;
import com.badlogic.gdx.ai.btree.decorator.Repeat;
import com.mygdx.platformer.characters.player.Player;

/**
 * This class represents the base of the auto-play AI feature. It sets up the
 * behavior tree node structure and runs the BT continuously.
 *
 * @author Robert Kullman, Daniel Jönsson
 */
public class AutoPlayAgent {

    private BehaviorTree behaviorTree;

    /**
     * Constructor for the AutoPlayAgent class, which initializes the
     * AI with a behavior tree.
     * @param player The player character which is to be controlled by the AI.
     */
    public AutoPlayAgent(Player player) {
        behaviorTree = new BehaviorTree<>(createTree(), player);
    }

    /**
     * Creates the tree structure for the autoplay AI.
     * @return The root task of the tree.
     */
    private Task<Player> createTree() {
        Selector<Player> root = new Selector<>();

        return new Repeat<>(root);
    }

}
