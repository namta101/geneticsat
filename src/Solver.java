import java.util.ArrayList;

public class Solver {
    private int POPULATION_SIZE = 5;
    private ArrayList<Chromosome> population;
    private int[][] formula;
    private int numberOfVariables;

    public Solver(int[][] formula, int numberOfVariables) {
        population = new ArrayList<>();
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
        initialisePopulation();
    }

    private void initialisePopulation() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(new Chromosome(numberOfVariables));
        }
    }

    public void solve() {
        System.out.println("Hello Solver");
    }

}