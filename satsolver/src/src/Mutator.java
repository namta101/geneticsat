package src;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for the stage of Mutation in the genetic algorithm
 */
public class Mutator {
    private final GACombination.Mutation mutationMethod;
    private final Formula formula;
    private static final Logger LOGGER = Logger.getLogger(Mutator.class.getName());

    public Mutator(Formula formula) {
        this.formula = formula;
        mutationMethod = GACombination.Mutation.Random;
    }

    /**
     * Mutates a given array of genes
     */
    public void mutate(int[] genes) {
        switch (mutationMethod.name()) {
            case "Random": randomMutation(genes);
            break;
            case "Greedy": greedyMutation(genes);
            break;
            default: LOGGER.log(Level.WARNING, "Error choosing mutation method, using default Random implementation");
                randomMutation(genes);
        }
    }

    /**
     *  Mutates the genes by randomly flipping a gene
      */
    public void randomMutation(int[] genes) {
        try {
            Random rand = new Random();
            int upperBound = genes.length - 1;
            int positionToMutate = rand.nextInt(upperBound);
            if (genes[positionToMutate] == 0) {
                genes[positionToMutate] = 1;
            } else {
                genes[positionToMutate] = 0;
            }
            LOGGER.log(Level.FINEST, "Undergoing random mutation");
        } catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Failure to random mutate", exception);
        }

    }

    /**
     * Mutates a gene by going along the line of genes and flipping a gene only if it improves the fitness score
     */
    public void greedyMutation(int[] genes) {
        try{
            for (int i = 0; i<genes.length; i++) {
             double preMutateFitnessScore = formula.getClausesMatched(genes);
                int[] postMutatedGenes;
             postMutatedGenes = genes.clone();
             if (postMutatedGenes[i] == 0) {
                    postMutatedGenes[i] = 1;
                } else {
                 postMutatedGenes[i] = 0;
             }
                double postMutateFitnessScore = formula.getClausesMatched(postMutatedGenes);
                if(postMutateFitnessScore>preMutateFitnessScore) {
                    if (genes[i] == 0) {
                     genes[i] = 1;
                    } else {
                        genes[i] = 0;
                    }
                 }
            }
            LOGGER.log(Level.FINEST, "Undergoing greedy mutation");
        }
        catch(Exception exception) {
            LOGGER.log(Level.WARNING,"Failure to greedy mutate", exception);
        }
    }

}
