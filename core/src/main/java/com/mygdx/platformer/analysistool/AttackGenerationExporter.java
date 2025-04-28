package com.mygdx.platformer.analysistool;

import com.mygdx.platformer.attacks.pcg.CompoundAttack;
import com.mygdx.platformer.attacks.pcg.Director;
import com.mygdx.platformer.attacks.pcg.NecromancerAttackBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AttackGenerationExporter {

    private static final int INITIAL_ATTACKS = 20;
    private static final int GENERATIONS = 10;
    private static final String OUTPUT_DIR = "out/generated/";

    private static final Random random = new Random();

    public static void main(String[] args) {
        generateAndExportAttacks();
    }

    public static void generateAndExportAttacks() {
        // PCG Generation
        List<CompoundAttack> currentGeneration = generatePCGGeneration();
        saveGeneration(currentGeneration, 0, "PCG");

        // RCG Generations
        for (int gen = 1; gen <= GENERATIONS; gen++) {
            currentGeneration = generateRCGGeneration(currentGeneration);
            saveGeneration(currentGeneration, gen, "RCG");
        }
    }

    private static List<CompoundAttack> generatePCGGeneration() {
        Director director = new Director();
        NecromancerAttackBuilder builder = new NecromancerAttackBuilder();

        List<CompoundAttack> attacks = new ArrayList<>();
        for (int i = 0; i < INITIAL_ATTACKS; i++) {
            director.constructNecromancerPCGAttack(builder);
            attacks.add(builder.getResult());
        }
        return attacks;
    }

    private static List<CompoundAttack> generateRCGGeneration(List<CompoundAttack> previousGeneration) {
        List<CompoundAttack> newGeneration = new ArrayList<>();

        for (int i = 0; i < previousGeneration.size(); i++) {
            CompoundAttack host = previousGeneration.get(i);
            CompoundAttack donor = previousGeneration.get(random.nextInt(previousGeneration.size()));

            CompoundAttack mutated = mutateAttack(host, donor);
            newGeneration.add(mutated);
        }

        return newGeneration;
    }

    private static CompoundAttack mutateAttack(CompoundAttack host, CompoundAttack donor) {

        int newAttackSize = donor.getAttackSize();
        return new CompoundAttack(newAttackSize, 5.0f, 10); // placeholder vörden
    }

    private static void saveGeneration(List<CompoundAttack> generation, int generationNumber, String method) {
        String folderPath = OUTPUT_DIR + method + "/gen" + generationNumber;
        new File(folderPath).mkdirs();

        for (int i = 0; i < generation.size(); i++) {
            CompoundAttack attack = generation.get(i);
            String filename = folderPath + "/CompoundAttack_" + i + ".java";

            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(convertAttackToJavaClass(attack, i, generationNumber, method));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String convertAttackToJavaClass(CompoundAttack attack, int attackId, int genNumber, String method) {
        return "package generated." + method.toLowerCase() + ".gen" + genNumber + ";\n\n" +
            "import com.mygdx.platformer.attacks.pcg.CompoundAttack;\n" +
            "import com.mygdx.platformer.attacks.NecromancerAttackTemplate;\n" +
            "import java.util.*;\n\n" +
            "public class CompoundAttack_" + attackId + " extends CompoundAttack {\n\n" +
            "    public CompoundAttack_" + attackId + "() {\n" +
            "        super(" + attack.getAttackSize() + ", 5.0f, 10);\n" +
            "    }\n" +
            "}\n";
    }
}
