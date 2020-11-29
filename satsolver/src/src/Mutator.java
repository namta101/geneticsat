package src;

import java.util.Random;

public class Mutator {
    private GACombination.Mutation mutationMethod;

    public Mutator() {
        mutationMethod = GACombination.Mutation.Random;
    }

    public void mutate(int[] genes) {
        switch (mutationMethod) {
            case Random: randomMutation(genes);
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



}
