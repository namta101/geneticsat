package src;
import java.util.Arrays;

public class Solver {
    private Population population;
    private final Formula formula;
    private final int numberOfVariables;
    private boolean solutionFound;
    private int[] satisfyingSolution;
    private long startTime;
    private long restartTimeTracker;
    private final long upperTimeLimit = 300000;
    private long timeBeforeEachRestart = 400000;
    private final int generationsBeforeRestart = 1000000;
    private int unimprovedGenerationsBeforeRestart = 9999999;
    private int unimprovedGenerationsCount = 1;
    private int generationNumber = 1;
    private int numberOfRestarts = 0;
    private int totalGenerationsPassed = 0;

    public Solver(Formula formula, int numberOfVariables) {
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
        population = new Population(formula, numberOfVariables);
        solutionFound = false;
    }

    // If solution found return else return empty int[]
    public int[] solve() {
        startTimer();
        resetRestartTimer();
        population.initialisePopulation();
        solutionFound = population.isSatisfied();
        if (solutionFound) {
            satisfyingSolution = population.getSatisfyingSolution();
            printSatisfyingSolution();
        }
        while(!solutionFound) {
           processAlgorithm();
           if(shouldRestartAlgorithm()) {
               restartAlgorithm();
               numberOfRestarts++;
           }
            if (upperTimeLimitReached()) {
                printCurrentMostSatisfyingSolution();
                break;
            }
        }
        if(solutionFound){
            return satisfyingSolution;
        } else {
            return new int[0];
        }
    }

    public void processAlgorithm(){
        generationNumber++;
        totalGenerationsPassed++;
        System.out.println("This is Generation number: " + generationNumber);
        population.nextPopulation();
        solutionFound = population.isSatisfied();
        if (solutionFound) {
            printSatisfyingSolution();
            satisfyingSolution = population.getSatisfyingSolution();
        }
    }

    public void restartAlgorithm() {
        System.out.println("Restarting algorithm");
        population.clearPopulation();
        population.initialisePopulation();
        generationNumber = 0;
        unimprovedGenerationsCount = 0;
        resetRestartTimer();
    }

    private void printSatisfyingSolution() {
        System.out.println("Solution satisfied: " + Arrays.toString(population.getSatisfyingSolution()));
    }


    private void printCurrentMostSatisfyingSolution() {
        int[] currentFittestSolution = population.getCurrentMostSatisfyingSolution();
        double currentFitnessSolutionFitnessScore = population.getCurrentMostSatisfyingSolutionFitnessScore();
        System.out.println("Fittest solution found: " + Arrays.toString(currentFittestSolution) + " \n"
                + "Fitness score of: " + currentFitnessSolutionFitnessScore);
    }

    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    public boolean shouldRestartAlgorithm() {
        if (population.getGenerationImproved()){
            unimprovedGenerationsCount = 0;
        } else {
            unimprovedGenerationsCount++;

        }
        boolean timeLimitPassed = (System.currentTimeMillis() - restartTimeTracker) > timeBeforeEachRestart;
        boolean unimprovedGenerationsPassed = unimprovedGenerationsCount >= unimprovedGenerationsBeforeRestart;
        return timeLimitPassed || unimprovedGenerationsPassed;
    }

    public void resetRestartTimer() {this.restartTimeTracker = System.currentTimeMillis();}

    public long getRestartTimeTracker() {
        return restartTimeTracker;
    }

    public boolean upperTimeLimitReached() {
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
        return (System.currentTimeMillis() - startTime) > upperTimeLimit;
    }

    public int getGenerationNumber() {
        return generationNumber;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getNumberOfRestarts() {
        return numberOfRestarts;
    }

    public int getTotalGenerationsPassed() {
        return totalGenerationsPassed;
    }

    public int getUnimprovedGenerationsBeforeRestart() {
        return unimprovedGenerationsBeforeRestart;
    }

    public long getTimeBeforeEachRestart() {
        return timeBeforeEachRestart;
    }

    public void setRestartTimerToMax() {
        timeBeforeEachRestart = 999999999;
    }

    public void setUnimprovedGenerationsBeforeRestartToMax(){

        unimprovedGenerationsBeforeRestart = 999999999;
    }

}


