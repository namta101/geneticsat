package src;

public class ParentCrossover {
    private Formula formula;
    private int numberOfVariables;
    private GACombination.Crossover crossoverMethod;
    private Mutator mutator;

    public ParentCrossover(Formula formula, int numberOfVariables, Mutator mutator) {
        this.formula = formula;
        this.numberOfVariables = numberOfVariables;
        this.mutator = mutator;
        crossoverMethod = GACombination.Crossover.Uniform;
    }

    public Chromosome crossover(int[] parentOneGenes, int[] parentTwoGenes, int lengthOfGenes) {
        switch (crossoverMethod) {
            case Uniform:
                return uniformCrossover(parentOneGenes, parentTwoGenes, lengthOfGenes);

        }
        return uniformCrossover(parentOneGenes, parentTwoGenes, lengthOfGenes);
    }

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
        return offspring;
    }


}
