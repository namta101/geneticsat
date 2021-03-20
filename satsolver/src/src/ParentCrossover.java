package src;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for the stage of Parent Crossover in the genetic algorithm
  */
public class ParentCrossover {
    private final Formula formula;
    private final int numberOfVariables;
    private final GACombination.Crossover crossoverMethod;
    private final Mutator mutator;
    private static final Logger LOGGER = Logger.getLogger(ParentCrossover.class.getName());


    public ParentCrossover(Formula formula, int numberOfVariables, Mutator mutator) {
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
        this.mutator = mutator;
        crossoverMethod = GACombination.Crossover.Uniform;
    }

    /**
     * Crossovers a pair of genes, if fails to do so, then will return a newly created chromosome
     */
    public Chromosome crossover(int[] parentOneGenes, int[] parentTwoGenes, int lengthOfGenes) {
        try{
        switch (crossoverMethod.name()) {
            case "Uniform":
                return uniformCrossover(parentOneGenes, parentTwoGenes, lengthOfGenes);
            case "TwoPoint":
                return twoPointCrossover(parentOneGenes, parentTwoGenes, lengthOfGenes);
            default:
                LOGGER.log(Level.WARNING,"Error in choosing crossover method, using default Uniform implementation");
                return uniformCrossover(parentOneGenes, parentTwoGenes, lengthOfGenes);
        }
        }
        catch(Exception exception) {
            LOGGER.log(Level.WARNING, "Error crossing over parents, returning first parent", exception);
            Chromosome defaultChromosome = new Chromosome(lengthOfGenes, formula, mutator);
            defaultChromosome.intakeParentsGenes(parentOneGenes);
            return defaultChromosome;
        }
    }

    /**
     * Alternates choosing bits from both the parents' genes
     */
    public Chromosome uniformCrossover(int[] parentOneGenes, int[] parentTwoGenes, int lengthOfGenes) {
        Chromosome offspring = new Chromosome(numberOfVariables, formula, mutator);
        int[] offspringGenes = new int[lengthOfGenes];

        for (int i = 0; i < lengthOfGenes - 1; i += 2) {
            offspringGenes[i] = parentOneGenes[i];
            offspringGenes[i + 1] = parentTwoGenes[i + 1];
        }
        if(lengthOfGenes%2 == 1) {
            offspringGenes[lengthOfGenes-1] = parentOneGenes[lengthOfGenes-1]; // Make sure genes are full
        }

        offspring.intakeParentsGenes(offspringGenes);

        LOGGER.log(Level.FINEST, "Undergoing Uniform crossover");
        return offspring;
    }

    /**
     * Randomly chooses two pivot points to select portions of each parents' genes
     */
    public Chromosome twoPointCrossover(int[] parentOneGenes, int[] parentTwoGenes, int lengthOfGenes) {
        Chromosome offspring = new Chromosome(numberOfVariables, formula, mutator);
        int[] offspringGenes = new int[lengthOfGenes];


        Random rand = new Random();
        int pointOne = rand.nextInt(lengthOfGenes-1);
        int pointTwo = rand.nextInt(lengthOfGenes-1);

        if(shouldCrossover(pointOne, pointTwo, lengthOfGenes)){
            for (int i = 0; i < pointOne; i++) {
                offspringGenes[i] = parentOneGenes[i];
            }
            for (int i = pointOne; i < pointTwo; i++) {
                offspringGenes[i] = parentTwoGenes[i];
            }
            for (int i = pointTwo; i < lengthOfGenes; i++) {
                offspringGenes[i] = parentOneGenes[i];
            }

            offspring.intakeParentsGenes(offspringGenes);
        } else {
            offspring.intakeParentsGenes(parentOneGenes);
        }

        LOGGER.log(Level.FINEST, "Undergoing Two-point crossover");
        return offspring;

    }

    /**
     * Returns whether the crossover length would be between 20% and 80%
     */
    public boolean shouldCrossover(int pointOne, int pointTwo, int lengthOfGenes) {
        if (lengthOfGenes < 10 && lengthOfGenes > 0) { // Allow crossover 100% for exceptional cases (very small problem size)
            return true;
        }
        try {
            if (lengthOfGenes<=0) {
                throw new Exception();
            }
            int lengthOfCrossover = Math.abs(pointOne - pointTwo);
            int bottomBoundary = (int) Math.round(lengthOfGenes * 0.2);
            int topBoundary = (int) Math.round(lengthOfGenes * 0.8);
            if (lengthOfCrossover < bottomBoundary) {
                return false;
            }
            if (lengthOfCrossover > topBoundary) {
                return false;
            }
        } catch(Exception exception) {
            LOGGER.log(Level.WARNING,"Fail to check length of crossover, returning false meaning chromosomes will not crossover",
                    exception);
            return false;
        }
        return true;
    }


}
