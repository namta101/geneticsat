package src;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    private static int numberOfClauses;
    private static int numberOfVariables;
    private static ArrayList<Clause> formula;

    private static long startTime;
    private static long stopTime;
    private static long elapsedTime;

    public static void main(String[] args) {
        String currentDir = System.getProperty("user.dir");
        System.out.println(currentDir);
        startTimer();
        formula = new ArrayList<Clause>();
        readDIMACSFile(args[0]);
        Solver satSolver = new Solver(formula, numberOfVariables);
        stopTimer();
        System.out.println("Completed in: " + elapsedTime + "ms");
    }

    // Reads the inputted DIMACS file, and populates the class variables with the
    // information from the file
    private static void readDIMACSFile(String dimacsFileIn) {
        try {
            File file = new File("satsolver/cnf/" + dimacsFileIn);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.length() >= 1) {
                    if (line.charAt(0) == ('c')) {
                        System.out.println(line.substring(1, line.length())); // Print out comments of the file
                    } else if (line.charAt(0) == ('p')) {
                        String[] pcnfline = line.split("\\s+"); // Assigns the number of variables and clauses from file
                        numberOfVariables = Integer.parseInt(pcnfline[2]);
                        numberOfClauses = Integer.parseInt(pcnfline[3]);
                        System.out.print("Number of variables " + numberOfVariables + "\n");
                        System.out.print("Number of clauses: " + numberOfClauses + "\n");
                    } else { // create the formula by inserting the split up clauses into an arraylist
                        String[] clauseLineVariables = line.split("\\s+");
                        int[] clauseLineVariablesInt = convertStringArrayToIntArray(clauseLineVariables);
                        formula.add(new Clause(clauseLineVariablesInt));
                    }

                }

            }
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File is not found, please check file exists or arguments have been typed in correctly");
            e.printStackTrace();
        }
    }

    public static void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public static void stopTimer() {
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
    }

    public static int[] convertStringArrayToIntArray(String[] stringArray) {
        int size = stringArray.length;
        int[] intArray = new int[size];
        for (int i = 0; i < size; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }
        return intArray;
    }
}