import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Chromosome {

    private int numberOfGenes;
    private double fitnessScore;
    private int[] genes;

    public Chromosome(int numberOfGenes) {
        this.numberOfGenes = numberOfGenes;
        genes = new int[numberOfGenes];
        for (int i = 0; i < numberOfGenes; i++) {
            if (Math.random() < 0.5) {
                genes[i] = 0;
            } else {
                genes[i] = 1;
            }
        }
        System.out.println(Arrays.toString(genes));

    }

    public void calculateFitnessScore() {
        this.fitnessScore = 50;
    }

    public double getFitnessScore() {
        return fitnessScore;
    }

}