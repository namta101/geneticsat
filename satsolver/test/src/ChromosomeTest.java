package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class ChromosomeTest {
    Chromosome chromosome;
    public ChromosomeTest() {
        ArrayList<Clause> formula = createFormula();
        chromosome = new Chromosome(3, formula);
    }

    public ArrayList<Clause> createFormula() {
        ArrayList<Clause> formula = new ArrayList<>();
        Clause clause1 = new Clause(new int[]{-1,2,-3,0});
        Clause clause2 = new Clause(new int[]{1,2,-3,0});
        Clause clause3 = new Clause(new int[]{-1,2,3,0});
        formula.add(clause1);
        formula.add(clause2);
        formula.add(clause3);
        return formula;
    }

    @BeforeEach
    void setUp() {
        chromosome.intakeParentsGenes(new int[]{0,1,0});
    }

    @Test
    public void initialiseGenes_ValidFormula_genesLengthEqualsClausesInFormula() {
        Assertions.assertEquals(3,chromosome.getGenes().length);
    }

    @Test
    public void getClausesMatched_validClauseChromosome_returnsCorrectNumberOfClausesMatched() {
        int clausesMatched = chromosome.getClausesMatched();
        Assertions.assertEquals(3, clausesMatched);
    }

    @Test
    public void randomSelectionMutate_ranOnce_AltersOneGene() {
        int[] preMutatedGenes = new int[]{0,1,0}; // taken from setUpMethod - replicates actual gene
        chromosome.randomSelectionMutate();
        int[] postMutatedGenes = chromosome.getGenes();

        int numberOfGenesMutated = 0;
        for(int i=0; i<preMutatedGenes.length; i++){
            if(preMutatedGenes[i] != postMutatedGenes[i]) {
                numberOfGenesMutated++;
            }
        }

        Assertions.assertEquals(1, numberOfGenesMutated);
    }

    @Test
    public void clearFitnessScore_SetsChromosomeFitnessScoreTo0() {
        double initialFitnessScore = chromosome.getFitnessScore();
        chromosome.clearFitnessScore();
        Assertions.assertEquals(0, chromosome.getFitnessScore());
        Assertions.assertNotEquals(initialFitnessScore, chromosome.getFitnessScore());
    }








}