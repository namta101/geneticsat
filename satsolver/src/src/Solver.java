package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Solver {
    private Population population;
    private final Formula formula;
    private final int numberOfVariables;
    private boolean solutionFound;
    private int[] satisfyingSolution;
    private long startTime;
    private long restartTimeTracker;
    private final long upperTimeLimit = 5000;
    private final long timeBeforeEachRestart = 100000;
    private int generationNumber = 1;

    public Solver(Formula formula, int numberOfVariables) {
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
        population = new Population(formula, numberOfVariables);
        solutionFound = false;
    }

    // If solution found return else return empty int[]
    public int[] solve() {
        startTimer();
        resetRestartTracker();
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
        resetRestartTracker();
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

    private void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    private boolean shouldRestartAlgorithm() {
        return (System.currentTimeMillis() - restartTimeTracker) > timeBeforeEachRestart;
    }

    private void resetRestartTracker() {this.restartTimeTracker = System.currentTimeMillis();}

    private boolean upperTimeLimitReached() {
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
        return (System.currentTimeMillis() - startTime) > upperTimeLimit;
    }

}