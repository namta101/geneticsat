import java.util.ArrayList;
import java.util.Arrays;


public class Solver {
    private int POPULATION_SIZE = 1;
    private ArrayList<Chromosome> population;
    private ArrayList<Clause> formula;
    private int numberOfVariables;

    public Solver(ArrayList<Clause> formula, int numberOfVariables) {
        population = new ArrayList<>();
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
        boolean solutionFound = false;
        initialisePopulation();
        for (int i = 0; i < population.size(); i++) {
            if (population.get(i).getFitnessScore() == formula.size()) {
                System.out.println("Success!");
                System.out.println((Arrays.toString(population.get(i).getGenes())));
                solutionFound = true;
                break;
            }
        }
        if(!solutionFound) {
            System.out.println("Solution not found");
            nextPopulation();
        }
    }

    private void initialisePopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(new Chromosome(numberOfVariables, formula));
        }
    }

    private void nextPopulation() {

    }

}