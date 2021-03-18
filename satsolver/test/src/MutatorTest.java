package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MutatorTest {
    private Formula formula;
    private Mutator mutator;

    public MutatorTest() {
        formula = TestHelper.createFormula();
        mutator = new Mutator(formula);
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

    @Test
    public void greedyMutation_doesNotDecreaseFitnessScore() {
        int[] preMutatedGenes = new int[]{1,1,0};
        int[] genesToMutate = new int[]{1,1,0};

        mutator.greedyMutation(genesToMutate);

        int preMutatedGenesFitnessScore = formula.getClausesMatched(preMutatedGenes);
        int postMutatedGenesFitnessScore = formula.getClausesMatched(genesToMutate);

        Assertions.assertTrue(postMutatedGenesFitnessScore>=preMutatedGenesFitnessScore);
    }

    @Test
    public void randomMutation_throwsError_SystemDoesNotCrash() {
        mutator.randomMutation(null);
    }

    @Test
    public void greedyMutation_throwsError_SystemDoesNotCrash() {
        mutator.greedyMutation(null);
    }

}
