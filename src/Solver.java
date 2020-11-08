import java.util.ArrayList;
import java.util.Arrays;

public class Solver {
    private int POPULATION_SIZE = 1;
    private ArrayList<Chromosome> population;
    private ArrayList<Clause> formula;
    private int numberOfVariables;
    private boolean solutionFound;
    private int[] satisfyingSolution;

    public Solver(ArrayList<Clause> formula, int numberOfVariables) {
        population = new ArrayList<>();
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
        solutionFound = false;
        initialisePopulation();
        solutionFound = isSatisfied();
        if (solutionFound) {
            printSatisfyingSolution(satisfyingSolution);
        }
        int attempt = 2;
        while (!solutionFound && attempt < 10) {
            System.out.println("This is atttempt: " + attempt);
            nextPopulation();
            solutionFound = isSatisfied();
            if (solutionFound) {
                printSatisfyingSolution(satisfyingSolution);
            }
            attempt++;
        }
    }

    private boolean isSatisfied() {
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).getFitnessScore() == formula.size()) {
                satisfyingSolution = population.get(i).getGenes();
                return true;
            }
        }
        return false;
    }

    private void printSatisfyingSolution(int[] satisfyingSolution) {
        System.out.println("Solution satisfied: " + Arrays.toString(satisfyingSolution));
    }

    private void initialisePopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(new Chromosome(numberOfVariables, formula));
        }
    }

    private void nextPopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.get(i).clearFitnessScore();
            population.get(i).mutate();
            population.get(i).assignFitnessScore();
            sortPopulationByFitnessValue();
        }
    }

    // Sorts the populattion by their fitness score in descending order
    private void sortPopulationByFitnessValue() {
        population.sort((Chromosome c1, Chromosome c2) -> {
            if (c1.getFitnessScore() > c2.getFitnessScore())
                return -1;
            if (c1.getFitnessScore() < c2.getFitnessScore())
                return 1;
            return 0;

        });

        // for (int i = 0; i < POPULATION_SIZE; i++) {
        //     System.out.println(population.get(i).getFitnessScore());
        // }

    }

}