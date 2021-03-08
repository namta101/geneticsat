package src;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is responsible for initialising the population which kicks off the genetic algorithm.
 * It is also responsible for holding the upper time limit of how long to solve for, and deals with the restart policy
 */
public class Solver {
    private Population population;
    private final Formula formula;
    private final int numberOfVariables;
    private Mutator mutator;
    private boolean solutionFound;
    private int[] satisfyingSolution;
    private long startTime;
    private long restartTimeTracker;
    private final long upperTimeLimit = 300000;
    private long timeBeforeEachRestart = 400000;
    private int unimprovedGenerationsBeforeRestart = 100;
    private int unimprovedGenerationsCount = 1;
    private int generationNumber = 1;
    private int numberOfRestarts = 0;
    private int totalGenerationsPassed = 0;

    public Solver(Formula formula, int numberOfVariables) {
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
        mutator = new Mutator(formula);
        population = new Population(formula, numberOfVariables, mutator);
        solutionFound = false;
    }

    /**
     * Starts the timers and if a solution is found then return the solution or return an empty int[]
     */
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

    /**
     * Moves on to the next population (one iteration of the genetic algorithm)
     */
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

    /**
     * Restarts algorithm but saves 3/4 of the current population on restart, and randomly generates the remaining
     * portion
     */
    public void restartAlgorithm() {

        population.sortPopulationByFitnessValue(population.getChromosomes());
        ArrayList<Chromosome> chromosomes = new ArrayList<>();
        int numberOfIndividualsToSave = (int)Math.floor(Population.POPULATION_SIZE/1.33);

        // Take every other chromosome, if more than half is saved, then loop back to the top again
        int x = 0;
        int y = 1;
        for(int i = 0; i<numberOfIndividualsToSave-1; i++) {
            if(x >= Population.POPULATION_SIZE){
                chromosomes.add(population.getChromosomes().get(y));
                y = y + 2;
            } else {
                chromosomes.add(population.getChromosomes().get(x));
                x = x + 2;
            }
        }

        int numberOfIndividualsToCreate = Population.POPULATION_SIZE - chromosomes.size();
        for(int i = 0; i<numberOfIndividualsToCreate;i++) {
            chromosomes.add(new Chromosome(numberOfVariables, formula, mutator));
        }

        population.setChromosomes(chromosomes);
        System.out.println("Restarting algorithm");
        generationNumber = 0;
        unimprovedGenerationsCount = 0;
        resetRestartTimer();
    }

    /**
     * The previous restart algorithm which generated a completely new population
     */
    public void previousRestartAlgorithm() {
        System.out.println("Restarting algorithm");
        population.clearPopulation();
        population.initialisePopulation();
        generationNumber = 0;
        unimprovedGenerationsCount = 0;
        resetRestartTimer();
    }

    /**
     * Prints out the satisfying solution (will be null if one is not found yet)
     */
    private void printSatisfyingSolution() {
        System.out.println("Solution satisfied: " + Arrays.toString(population.getSatisfyingSolution()));
    }

    /**
     * Prints out the current most satisfying solution
     */
    private void printCurrentMostSatisfyingSolution() {
        int[] currentFittestSolution = population.getCurrentMostSatisfyingSolution();
        double currentFitnessSolutionFitnessScore = population.getCurrentMostSatisfyingSolutionFitnessScore();
        System.out.println("Fittest solution found: " + Arrays.toString(currentFittestSolution) + " \n"
                + "Fitness score of: " + currentFitnessSolutionFitnessScore);
    }

    /**
     * Starts timer to track how long the solver attempts to solve the problem
     */
    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Returns whether we should restart the algorithm, currently done on unimproved generation count and time taken
     */

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

    /**
     * Resets the restart timer. Used when restarting algorithm
     */
    public void resetRestartTimer() {this.restartTimeTracker = System.currentTimeMillis();}

    /**
     * Returns the time of the restartTimeTracker
     */
    public long getRestartTimeTracker() {
        return restartTimeTracker;
    }

    /**
     * Returns whether the solver has reached the upper time limit and should terminate
     */
    public boolean upperTimeLimitReached() {
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
        return (System.currentTimeMillis() - startTime) > upperTimeLimit;
    }

    /**
     * Returns generation number
     */
    public int getGenerationNumber() {
        return generationNumber;
    }

    /**
     * Returns the start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Returns the total number of restarts occurred so far
     */
    public int getNumberOfRestarts() {
        return numberOfRestarts;
    }

    /**
     * Returns the total number of generations passed
     */
    public int getTotalGenerationsPassed() {
        return totalGenerationsPassed;
    }

    /**
     * Returns the number of unimproved generations so far
     */
    public int getUnimprovedGenerationsBeforeRestart() {
        return unimprovedGenerationsBeforeRestart;
    }

    /**
     * Returns how long we give the solver before we restart the algorithm
     */
    public long getTimeBeforeEachRestart() {
        return timeBeforeEachRestart;
    }

    /**
     * Sets the time before restarting to max
     */
    public void setRestartTimerToMax() {
        timeBeforeEachRestart = 999999999;
    }

    /**
     * Sets the number of unimproved generations before restarting to max
     */
    public void setUnimprovedGenerationsBeforeRestartToMax(){
        unimprovedGenerationsBeforeRestart = 999999999;
    }

}


