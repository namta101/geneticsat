package src;

import java.util.ArrayList;

public class TestHelper {

    public static Formula createFormula() {
        Formula formula = new Formula();
        Clause clause1 = new Clause(new int[]{1,2,3,0});
        Clause clause2 = new Clause(new int[]{1,-2,3,0});
        Clause clause3 = new Clause(new int[]{1,2,-3,0});
        Clause clause4 = new Clause(new int[]{1,-2,-3,0});
        Clause clause5 = new Clause(new int[]{-1,2,3,0});
        Clause clause6 = new Clause(new int[]{-1,2,-3,0});
        Clause clause7 = new Clause(new int[]{-1,-2,3,0});

        formula.addClause(clause1);
        formula.addClause(clause2);
        formula.addClause(clause3);
        formula.addClause(clause4);
        formula.addClause(clause5);
        formula.addClause(clause6);
        formula.addClause(clause7);

        return formula;
    }

    public static Formula createUnsatisfiableFormula() {
        Formula formula = new Formula();
        Clause clause1 = new Clause(new int[]{1,2,3});
        Clause clause2 = new Clause(new int[]{1,-2,3});
        Clause clause3 = new Clause(new int[]{1,2,-3});
        Clause clause4 = new Clause(new int[]{1,-2,-3});
        Clause clause5 = new Clause(new int[]{-1,2,3});
        Clause clause6 = new Clause(new int[]{-1,-2,3});
        Clause clause7 = new Clause(new int[]{-1,2,-3});
        Clause clause8 = new Clause(new int[]{-1,-2,-3});

        formula.addClause(clause1);
        formula.addClause(clause2);
        formula.addClause(clause3);
        formula.addClause(clause4);
        formula.addClause(clause5);
        formula.addClause(clause6);
        formula.addClause(clause7);
        formula.addClause(clause8);

        return formula;
    }

    public static ArrayList<Chromosome> createNumberOfChromosomes(int numberOfChromosomes) {
            ArrayList<Chromosome> chromosomes = new ArrayList<>();
            Formula formula = createFormula();
            int numberOfVariables = 3;
            for (int i = 0; i < numberOfChromosomes; i++){
                chromosomes.add(new Chromosome(numberOfVariables, formula, createMutator()));
            }
            return chromosomes;
    }

    public static Mutator createMutator() {
        return new Mutator(createFormula());
    }
}
