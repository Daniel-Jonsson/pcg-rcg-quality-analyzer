package com.mygdx.platformer.analysistool;

import com.mygdx.platformer.attacks.NecromancerAttackTemplate;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AttackExporter {

    private static final String OUTPUT_DIR = "out/generated/";

    public static void exportCompounds(List<List<NecromancerAttackTemplate>> compounds, int generation, String method) {
        String baseFolderPath = OUTPUT_DIR + method.toLowerCase() + "/gen" + generation;
        new File(baseFolderPath).mkdirs();

        for (int compoundId = 0; compoundId < compounds.size(); compoundId++) {
            List<NecromancerAttackTemplate> compound = compounds.get(compoundId);
            String compoundFolder = baseFolderPath + "/compound_" + compoundId;
            new File(compoundFolder).mkdirs();

            for (int attackId = 0; attackId < compound.size(); attackId++) {
                NecromancerAttackTemplate attack = compound.get(attackId);
                String filename = compoundFolder + "/NecromancerAttack_" + attackId + ".java";

                try (FileWriter writer = new FileWriter(filename)) {
                    writer.write(convertAttackToJavaClass(attack, attackId, generation, compoundId, method));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String convertAttackToJavaClass(NecromancerAttackTemplate attack, int attackId, int generation, int compoundId, String method) {
        String packageName = "generated." + method.toLowerCase() + ".gen" + generation + ".compound_" + compoundId + ";";
        System.out.println(packageName);
        return "package " + packageName + "\n\n" +
                "import com.badlogic.gdx.math.Vector2;\n" +
            "import com.badlogic.gdx.physics.box2d.World;\n" +
            "import com.mygdx.platformer.attacks.BaseAttack;\n" +
            "import com.mygdx.platformer.attacks.NecromancerAttack;\n" +
            "import com.badlogic.gdx.physics.box2d.Body;\n\n" +
            "public class NecromancerAttack_" + attackId + " {\n\n" +
            "    public BaseAttack execute(World world, Vector2 initialPos, int directionModifier, float multiplier) {\n" +
            "        int damage = " + attack.getDamage() + ";\n" +
            "        float speed = " + attack.getSpeed() + "f;\n" +
            "        BaseAttack attack = new NecromancerAttack(world, Math.round(damage * multiplier), speed, initialPos.x, initialPos.y, directionModifier);\n" +
            attack.getMovementLogicCode() + "\n" +
                attack.getModifierLogicCode() + "\n" +
            "        return attack;\n" +
            "    }\n" +
            "}\n";
    }
}
