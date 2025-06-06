package com.mygdx.platformer.analysistool;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;

/**
 * Utility class used for extracting coverage from a Jacoco report, and export
 * it to a csv file. Only generated attack classes are included in the
 * export. Line and branch coverage is included.
 * @author Daniel Jönsson
 * @author Robert Kullman
 */
public class CoverageParser {

    /**
     * Extracts coverage data from jacoco XML and writes it to a csv file.
     */
    public static void extractCoverageToCSV() {
        String xmlPath = "core/build/reports/jacoco/test/jacocoTestReport.xml";
        String outputCsvPath = "core/build/reports/jacoco/coverage_report.csv";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // set to false to avoid trying to upload the dtd file (will result in a file not found exception)
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setValidating(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlPath));
            doc.getDocumentElement().normalize();

            FileWriter csvWriter = new FileWriter(outputCsvPath);
            csvWriter.append("Class;LineCoverage;BranchCoverage\n");

            NodeList packageNodes = doc.getElementsByTagName("package");
            for (int i = 0; i < packageNodes.getLength(); i++) {
                Element packageElement = (Element) packageNodes.item(i);
                String packageName = packageElement.getAttribute("name");

                if (!packageName.startsWith("generated/")) continue;

                NodeList classNodes = packageElement.getElementsByTagName("class");
                for (int j = 0; j < classNodes.getLength(); j++) {
                    Element classElement = (Element) classNodes.item(j);
                    String className = classElement.getAttribute("name");

                    int coveredLines = 0, missedLines = 0;
                    int coveredBranches = 0, missedBranches = 0;

                    NodeList counterNodes = classElement.getElementsByTagName("counter");
                    for (int k = 0; k < counterNodes.getLength(); k++) {
                        Element counter = (Element) counterNodes.item(k);
                        String type = counter.getAttribute("type");

                        if (type.equals("LINE")) {
                            coveredLines = Integer.parseInt(counter.getAttribute("covered"));
                            missedLines = Integer.parseInt(counter.getAttribute("missed"));
                        } else if (type.equals("BRANCH")) {
                            coveredBranches = Integer.parseInt(counter.getAttribute("covered"));
                            missedBranches = Integer.parseInt(counter.getAttribute("missed"));
                        }
                    }

                    double lineCoverage = percentage(coveredLines, missedLines);
                    double branchCoverage = percentage(coveredBranches, missedBranches);

                    csvWriter.append(String.format("%s;%.2f;%.2f\n",
                        className, lineCoverage, branchCoverage));
                }
            }

            csvWriter.flush();
            csvWriter.close();
            System.out.println("Coverage report written to: " + outputCsvPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method for calculating coverage percentage.
     * @param covered the number of covered lines.
     * @param missed The number of missed lines.
     * @return Coverage percentage.
     */
    private static double percentage(int covered, int missed) {
        int total = covered + missed;
        return total == 0 ? 0 : (100.0 * covered) / total;
    }
}
