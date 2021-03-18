package src;

import java.util.ArrayList;

/**
 * Represents the SAT problem as a formula - created from the SAT file
  */
public class Formula {
    private final ArrayList<Clause> clauses;

    public Formula() {
        clauses = new ArrayList<>();
    }

    /**
     * Returns the number of clauses matched by comparing the genes inputted with the formula
     */
    public int getClausesMatched(int[] genes) {
        try {
            int numberOfClausesMatched = 0;
            for (Clause clause : clauses) {
                int[] variables = clause.getVariables();
                boolean isClauseSatisfied = false;
                for (int var : variables) {
                    if (var > 0) { // If the variable is positive, and the binary string bit that connects to that  variable is 1,
                        if (genes[var - 1] == 1) { // then the clause is satisfied. (The variable 1 connects to the first bit of the string)
                            isClauseSatisfied = true;
                            break;
                        }
                    } else { //
                        if (genes[Math.abs(var + 1)] == 0) { // If the variable is negative, then a variable -1, we will need to connect to
                            isClauseSatisfied = true; // the first bit of the string. If the first bit of the string is 0, then clause satisfied
                            break;                    //(If variable is -5 then it will connect to the 4th bit of the string)
                        }
                    }
                }
                if (isClauseSatisfied) {
                    numberOfClausesMatched++;
                }
            }
            return numberOfClausesMatched;
        } catch(Exception e) {
            System.out.println("Error checking clauses matched, assigning 0");
            System.out.println("Error message:" + e);
            return 0;
        }
    }


    /**
     * Adds a clause to the formula
     */
    public void addClause(Clause clause) {
        clauses.add(clause);
    }

    /**
     * Returns the number of clauses in the formula
     * */
    public int getSize() {
        return clauses.size();
    }



}

