import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Solver {
    private int POPULATION_SIZE = 300;
    private double ELITISM_RATE = 0.8;
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
        while (!solutionFound  ) {
            System.out.println("This is Population number: " + attempt);
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
        population = createNewPopulation();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.get(i).clearFitnessScore();
            population.get(i).mutate(); // prints false
            population.get(i).assignFitnessScore();
            // sortPopulationByFitnessValue();
        }

    }

    // Applies Uniform Crossover and returns breeded offspring
    private Chromosome crossover() {

        int[] parentOneGenes = rouletteWheelSelection().getGenes();
        int[] parentTwoGenes = rouletteWheelSelection().getGenes();
        int lengthOfGenes = parentOneGenes.length;

        Chromosome offspring = new Chromosome(numberOfVariables, formula);
        int[] offspringGenes = new int[lengthOfGenes];

        for (int i = 0; i < lengthOfGenes - 1; i += 2) {
            if (i == lengthOfGenes) {
                offspringGenes[i] = parentOneGenes[i];
                break;
            }
            offspringGenes[i] = parentOneGenes[i];
            offspringGenes[i + 1] = parentTwoGenes[i + 1];
        }

        offspring.intakeParentsGenes(offspringGenes);
        // System.out.println("Offspring: " + Arrays.toString(offspring.getGenes()));
        // System.out.println("Parent one: " + Arrays.toString(parentOneGenes));
        // System.out.println("Parent two: " + Arrays.toString(parentTwoGenes));


        return offspring;
    }

    private ArrayList<Chromosome> createNewPopulation() {

        ArrayList<Chromosome> newPopulation = new ArrayList<>();

        sortPopulationByFitnessValue();
        double individualsCloned = Math.ceil(ELITISM_RATE * (double) (POPULATION_SIZE));
        double individualsCreated = (double) POPULATION_SIZE - individualsCloned;

        for (int i = 0; i < individualsCloned; i++) {
            Chromosome clonedIndividual = population.get(i);
            newPopulation.add(clonedIndividual);
        }

        for (int i = 0; i < individualsCreated; i++) {
            Chromosome clonedIndividual = crossover();
            newPopulation.add(clonedIndividual);
        }

        return newPopulation;

    }

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
        double positionOnRouletteWheel = 1 + ((rouletteTotal - 1) * rand.nextDouble()); // values from 1 to total
        for (int i = 0; i < rouletteWheel.length; i++) {
            if (rouletteWheel[i] >= positionOnRouletteWheel) {
                indexOfChromosomeToChoose = i;
                break;
            }
        }

        // System.out.println("roulette wheel: " + Arrays.toString(rouletteWheel));
        // System.out.println("index: " + indexOfChromosomeToChoose);
        // System.out.println("position on roulette wheel: " + positionOnRouletteWheel);
        // System.out.println("total roulette wheel: " + rouletteTotal);


        // System.out.println("Chosen roulette: " + Arrays.toString((population.get(indexOfChromosomeToChoose).getGenes())));
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