package com.mygdx.platformer.ai.enemy.tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.platformer.ai.AIAgent;
import com.mygdx.platformer.attacks.AttackManager;
import com.mygdx.platformer.attacks.BaseAttack;
import com.mygdx.platformer.attacks.NecromancerAttackTemplate;
import com.mygdx.platformer.characters.BaseCharacter;
import com.mygdx.platformer.characters.enemies.BaseEnemy;
import com.mygdx.platformer.characters.enemies.Necromancer;
import com.mygdx.platformer.utilities.AppConfig;

/**
 * Represents a behavior tree task for executing an attack.
 *
 * @author Daniel Jönsson, Robert Kullman
 */
public class AttackTask extends LeafTask<AIAgent> {

    /** The cooldown time before the AI can attack again. **/
    private float attackCooldown;

    /** Manages the spawning of attacks. **/
    private AttackManager attackManager;

    /** Map that tracks the last attack time for enemies. **/
    private Map<BaseCharacter, Long> attackCooldowns;

    /**
     * Constructor for the AttackTask.
     * @param attackCooldown cooldown in seconds.
     * @param attackManager the attackmanager responsible for spawning attacks.
     */
    public AttackTask(float attackCooldown, AttackManager attackManager) {
        this.attackCooldown = attackCooldown;
        this.attackManager = attackManager;
        this.attackCooldowns = new HashMap<>();
    }

    /**
     * Executes the attack type pertinent to the corresponding enemy unit.
     * @return STATUS indicating success or failure.
     */
    @Override
    public Status execute() {
        AIAgent agent = getObject();
        BaseCharacter character = agent.getCharacter();
        BaseEnemy enemy = (BaseEnemy) character;
        long lastAttackTime = attackCooldowns.getOrDefault(character, 0L);

         // TODO: Access the list of AttackTemplate and run them with cooldown.

        long currentTime = System.currentTimeMillis();

        long cooldownMs = (long) (attackCooldown * 1000);

        long timeSinceLastAttack = currentTime - lastAttackTime;

        if (timeSinceLastAttack < cooldownMs) {
            return Status.FAILED;
        }


        Vector2 targetPosition = agent.getTargetPosition();

        if (targetPosition == null) {
            return Status.FAILED;
        }

        int direction = targetPosition.x > character.getBody().getPosition().x ? 1 : -1;

        AppConfig.AttackType attackType;
        AppConfig.CharacterType characterType = character.getCharacterType();

        attackType = switch (characterType) {
            case GOBLIN -> AppConfig.AttackType.GOBLIN_THROWING_DAGGER;
            case NECROMANCER -> AppConfig.AttackType.DEATH_BOLT;
            default -> AppConfig.AttackType.PLAYER_THROWING_DAGGER;
        };

        if (characterType.equals(AppConfig.CharacterType.NECROMANCER)) {
            Necromancer necromancer = (Necromancer) character;
            List<NecromancerAttackTemplate> attackTemplates = necromancer.getAttackSequence().getAttackPattern();
            for (NecromancerAttackTemplate attackTemplate : attackTemplates) {
                attackManager.spawnNecroAttackAt(attackTemplate,
                    necromancer.getBody().getPosition(), direction);
            }
        } else {
            attackManager.spawnEnemyAttackAt(character.getBody().getPosition(), direction, attackType);
        }


        lastAttackTime = currentTime;

        attackCooldowns.put(character, lastAttackTime);
        enemy.startAttack();

        return Status.SUCCEEDED;
    }

    /**
     * Copies the current task to a new instance.
     * @param task Target task to copy the current task to.
     * @return The new task instance.
     */
    @Override
    protected Task<AIAgent> copyTo(Task<AIAgent> task) {
        AttackTask attackTask = (AttackTask) task;
        attackTask.attackCooldown = attackCooldown;
        attackTask.attackManager = attackManager;
        attackTask.attackCooldowns = new HashMap<>(attackCooldowns);
        return task;
    }

}
