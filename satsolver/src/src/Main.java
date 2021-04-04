package src;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static int numberOfClauses;
    private static int numberOfVariables;
    private static Formula formula;

    private static long startTime;
    private static long stopTime;
    private static long elapsedTime;

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());


    /**
     * Starts the program by taking in the user parameters, deciding whether to create a new formula from the
     * parameters or solve the pre-inputted problem file (which needs to be placed in the CNF folder).
     */
    public static void main(String[] args) throws Exception {
        startTimer();
        formula = new Formula();

        setupUserConfigurations(args);
        System.out.print(GACombination.getConfigurationValues());

        // Create randomly generated SAT problem (if user wants)
        if(args[0].equalsIgnoreCase("--new")) {
            createEmptyFile(args[1]);
            writeSATProblemToFile(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            readDIMACSFile(args[1]);
        } else {
            readDIMACSFile(args[0]);
        }
        Solver satSolver = new Solver(formula, numberOfVariables);
        int[] solution = satSolver.solve();

        stopTimer();
        if(args[0].equalsIgnoreCase("--new")){
            writeSATStatisticsToFile(args[1], elapsedTime, solution);
        } else {
            writeSATStatisticsToFile(args[0], elapsedTime, solution);
        }
        System.out.println("Completed in: " + elapsedTime + "ms");
        LOGGER.log(Level.FINEST, "Run complete");
    }

    /**
     * Adjusts the configuration for the run using the user input
     */
    public static void setupUserConfigurations(String[] userInput) {
        for (int i = 0; i<userInput.length-1; i++){
            setConfiguration(userInput[i], userInput[i+1]);
        }
    }

    /**
     * Sets the configuration key with the value (eg, mutation rate to be 0.05)
     */
    public static void setConfiguration(String mode, String value){
        if(mode.equalsIgnoreCase("-ms")) {
            if(value.equalsIgnoreCase("roulettewheel")) {
                GACombination.setPARENTSELECTION(GACombination.ParentSelection.RouletteWheel);
            }
            if(value.equalsIgnoreCase("rank")) {
                GACombination.setPARENTSELECTION(GACombination.ParentSelection.Rank);
            }
        }
        if(mode.equalsIgnoreCase("-mc")){
            if(value.equalsIgnoreCase("uniform")) {
                GACombination.setCROSSOVER(GACombination.Crossover.Uniform);
            }
            if(value.equalsIgnoreCase("twopoint")) {
                GACombination.setCROSSOVER(GACombination.Crossover.TwoPoint);
            }
        }
        if(mode.equalsIgnoreCase("-mm")) {
            if(value.equalsIgnoreCase("Greedy")){
                GACombination.setMUTATION(GACombination.Mutation.Greedy);
            }
            if(value.equalsIgnoreCase("Random")){
                GACombination.setMUTATION(GACombination.Mutation.Random);
            }
        }
        if(mode.equalsIgnoreCase("-pop")) {
            GACombination.setPopulationSize(Integer.parseInt(value));
            if (Integer.parseInt(value) < 2) { // User should not put a pop. size below 2
                GACombination.setPopulationSize(2);
            }
        }
        if(mode.equalsIgnoreCase("-rm")) {
            GACombination.setMutationRate(Double.parseDouble(value));
        }
        if(mode.equalsIgnoreCase("-re")) {
            GACombination.setElitismRate(Double.parseDouble(value));
            if(Double.parseDouble(value) > 1 ) { // User should not put an elitism rate above 1
                GACombination.setElitismRate(1); // It would mean that more than total population stay on to next generation
            }
        }
        if(mode.equalsIgnoreCase("-rr") || value.equalsIgnoreCase("-rr")) {
            GACombination.turnOnRestartPolicy();
            }
        }

    /** Reads the inputted DIMACS file, and populates the class variables: formula, numberOfVariables,
     * and numberOfClauses with the information from the file
     */
    public static void readDIMACSFile(String dimacsFile) throws Exception {
        try {
            File currentDirectory = new File(System.getProperty("user.dir"));
            String filePath = String.format("%s/cnf/%s", currentDirectory.getParentFile().toString(), dimacsFile);
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.length() >= 1) {
                    if (line.charAt(0) == ('c')) {
                        System.out.println(line.substring(1)); // Print out comments of the file
                    } else if (line.charAt(0) == ('p')) {
                        String[] pcnfline = line.split("\\s+"); // Assigns the number of variables and clauses from file
                        numberOfVariables = Integer.parseInt(pcnfline[2]);
                        numberOfClauses = Integer.parseInt(pcnfline[3]);
                        System.out.print("Number of variables " + numberOfVariables + "\n");
                        System.out.print("Number of clauses: " + numberOfClauses + "\n");
                    } else { // create the formula by inserting the split up clauses into an arraylist
                        String[] clauseLineVariables = line.split("\\s+");
                        int[] clauseLineVariablesInt = Utility.convertStringArrayToIntArray(clauseLineVariables);
                        formula.addClause(new Clause(clauseLineVariablesInt));
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException exception) {
            LOGGER.log(Level.SEVERE, "Fail to find or read file, check arguments, working directory, and file format", exception);
            throw exception;
        }
    }

    /**
     * Create an empty file in the CNF folder
     */
    public static void createEmptyFile(String fileName) {
        File currentDirectory = new File(System.getProperty("user.dir"));
        String filePath = String.format("%s/cnf/%s", currentDirectory.getParentFile().toString(), fileName);
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Fail to create file", exception);
        }
    }

    /**
     * Create a 3-SAT problem and write it to the file
     */
    public static void writeSATProblemToFile(String fileName, int numberOfVariables, int numberOfClauses) {
        try{
            File currentDirectory = new File(System.getProperty("user.dir"));
            String filePath = String.format("%s/cnf/%s", currentDirectory.getParentFile().toString(), fileName);
            PrintStream fileWriter = new PrintStream(filePath);
            fileWriter.println("p cnf " + numberOfVariables + " " + numberOfClauses);
            Random rand = new Random();
            for(int i = 0; i<numberOfClauses; i++){
                int numberOfVariablesInClause = 3;
                for(int j= 0; j<numberOfVariablesInClause; j++) {
                    if(rand.nextBoolean()) {
                        fileWriter.print((rand.nextInt(numberOfVariables) + 1) + " ");
                    } else{
                        fileWriter.print("-" + (rand.nextInt(numberOfVariables) + 1) + " ");
                    }
                }
                fileWriter.print("\n");
            }
            fileWriter.close();
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Fail to write SAT problem to the given file", exception);

        }
    }

    /**
     * Append the statistics of solving the SAT problem to the file
     */
    public static void writeSATStatisticsToFile(String fileName, long timeToSolve, int[] solution){
        try {
            File currentDirectory = new File(System.getProperty("user.dir"));
            String filePath = String.format("%s/cnf/%s", currentDirectory.getParentFile().toString(), fileName);
            PrintStream fileWriter = new PrintStream(new FileOutputStream(filePath, true));
            if (solution.length<1) { // Solution of length 0 indicates no solution was found
                fileWriter.println("c " + "No solution found" + "Time spent trying to find solution: " + timeToSolve + "ms");
            } else {
                fileWriter.println("c " + "Solution found: " + Arrays.toString(solution) + " " + "Time taken to solve: " + timeToSolve + "ms");
            }
            fileWriter.close();
        }
        catch(Exception exception) {
            LOGGER.log(Level.WARNING, "Fail to write statistics to the file", exception);
        }
    }

    /**
     * Starts timer to record how long the program takes
     */
    public static void startTimer() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Stops timer when the program terminates
     */
    public static void stopTimer() {
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
    }

}