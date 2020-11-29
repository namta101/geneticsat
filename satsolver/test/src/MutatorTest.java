package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MutatorTest {
    private Mutator mutator;

    public MutatorTest() {
        mutator = new Mutator();
    }

    @Test
    public void randomSelectionMutate_ranOnce_AltersOneGene() {
        int[] preMutatedGenes = new int[]{1,1,1};
        int [] genesToMutate = new int[]{1,1,1};
        mutator.randomMutation(genesToMutate);

        int numberOfGenesMutated = 0;
        for(int i=0; i<preMutatedGenes.length; i++){
            if(preMutatedGenes[i] != genesToMutate[i]) {
                numberOfGenesMutated++;
            }
        }
        Assertions.assertEquals(1, numberOfGenesMutated);
    }
}
