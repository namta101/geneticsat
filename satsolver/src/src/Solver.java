package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Solver {
    private Population population;
    private final ArrayList<Clause> formula;
    private final int numberOfVariables;
    private boolean solutionFound;
    private long startTime;
    private final long upperTimeLimit = 10000;
    private int generationNumber = 1;

    public Solver(ArrayList<Clause> formula, int numberOfVariables) {
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
        population = new Population(formula, numberOfVariables);
        solutionFound = false;
    }

    public void solve() {
        startTimer();
        population.initialisePopulation();
        solutionFound = population.isSatisfied();
        if (solutionFound) {
            printSatisfyingSolution();
        }
        while(!solutionFound) {
            generationNumber++;
            System.out.println("This is Generation number: " + generationNumber);
            population.nextPopulation();
            solutionFound = population.isSatisfied();
            if (solutionFound) {
                printSatisfyingSolution();
            }
            if (upperTimeLimitReached()) {
                printCurrentMostSatisfyingSolution();
                break;
            }
        }

    }

    private void printSatisfyingSolution() {
        System.out.println("Solution satisfied: " + Arrays.toString(population.getCurrentMostSatisfyingSolution()));
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

    private boolean upperTimeLimitReached() {
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
        return (System.currentTimeMillis() - startTime) > upperTimeLimit;
    }

}