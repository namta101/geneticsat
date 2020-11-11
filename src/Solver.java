import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Solver {
    private int POPULATION_SIZE = 5;
    private double ELITISM_RATE = 0.2;
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
            System.out.println("This is attempt: " + attempt);
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
            population.get(i).mutate(); // prints false
            population.get(i).assignFitnessScore();
            // sortPopulationByFitnessValue();
        }
    }

    // Applies Uniform Crossover
    private void crossover() {

        int[] parentOneGenes = rouletteWheelSelection().getGenes();
        int[] parentTwoGenes = rouletteWheelSelection().getGenes();
        int lengthOfGenes = parentOneGenes.length;

        Chromosome offspring = new Chromosome();
        int[] offspringGenes = new int[lengthOfGenes];

        for (int i = 0; i < lengthOfGenes - 1; i += 2) {
            if (i == lengthOfGenes) {
                offspringGenes[i] = parentOneGenes[i];
                break;
            }
            offspringGenes[i] = parentOneGenes[i];
            offspringGenes[i + 1] = parentTwoGenes[i + 1];

        }

        //gives new genes to offspring genes, return    
    }

    // private ArrayList<Chromosome> createNewPopulation() {

    // }

    // Select a pair of chromosomes to crossover
    private Chromosome rouletteWheelSelection() {
        // create the roulette wheel
        double[] rouletteWheel = new double[POPULATION_SIZE];
        double rouletteTotal = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            double chromosomeFitness = population.get(i).getFitnessScore();
            rouletteTotal = rouletteTotal + chromosomeFitness;
            rouletteWheel[i] = rouletteTotal;
        }

        int indexOfChromosomeToChoose = 0;
        Random rand = new Random();
        double positionOnRouletteWheel = 1 + (rouletteTotal - 1 * rand.nextDouble()); // values from 1 to total
        for (int i = 0; i < rouletteWheel.length; i++) {
            if (rouletteWheel[i] <= positionOnRouletteWheel) {
                indexOfChromosomeToChoose = i;
                break;
            }
        }

        return population.get(indexOfChromosomeToChoose);

    }

    private double totalPopulationFitnessScore() {
        double total = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            total = total + population.get(i).getFitnessScore();
        }
        return total;
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
        // System.out.println(population.get(i).getFitnessScore());
        // }

    }

}