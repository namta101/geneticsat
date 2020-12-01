package src;

import java.util.Random;

public class Mutator {
    private GACombination.Mutation mutationMethod;
    private Formula formula;

    public Mutator(Formula formula) {
        this.formula = formula;
        mutationMethod = GACombination.Mutation.Greedy;
    }

    public void mutate(int[] genes) {
        switch (mutationMethod.name()) {
            case "Random": randomMutation(genes);
            break;
            case "Greedy": greedyMutation(genes);
            break;
        }
    }

    public void randomMutation(int[] genes) {
        Random rand = new Random();
        int upperBound = genes.length - 1;
        int positionToMutate = rand.nextInt(upperBound);
        if (genes[positionToMutate] == 0) {
            genes[positionToMutate] = 1;
        } else {
            genes[positionToMutate] = 0;
        }
    }

    public void greedyMutation(int[] genes) {
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



}
