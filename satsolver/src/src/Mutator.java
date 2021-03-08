package src;

import java.util.Random;

/**
 * This class is responsible for the stage of Mutation in the genetic algorithm
 */
public class Mutator {
    private final GACombination.Mutation mutationMethod;
    private final Formula formula;

    public Mutator(Formula formula) {
        this.formula = formula;
        mutationMethod = GACombination.Mutation.Random;
    }

    public void mutate(int[] genes) {
        switch (mutationMethod.name()) {
            case "Random": randomMutation(genes);
            break;
            case "Greedy": greedyMutation(genes);
            break;
            default: System.out.println("Error choosing mutation method, using default Random implementation");
                     randomMutation(genes);

        }
    }

    // Mutates the genes by randomly flipping a gene
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
        } catch (Exception e) {
            System.out.println("Failure to random mutate");
            System.out.println("Error: " + e);
        }

    }

    // Mutates a gene by going alone the line of genes and flipping a gene only if it improves the fitness score
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
        }
        catch(Exception e) {
            System.out.println("Failure to greedy mutate");
            System.out.println("Error: " + e);
        }
    }

}
