package src;

import java.util.Arrays;

/**
 * Represents a clause in the SAT problem
 */
public class Clause {
    private final int[] variables;

    /**
     * Creates a clause by taking in an array of variables. Deals with DIMACS format.
     */
    public Clause(int[] variables) {
        try {
            if (variables[variables.length - 1] == 0) {
                this.variables = Arrays.copyOf(variables, variables.length - 1); // Get rid of the 0 on the end
            } else {
                this.variables = variables;
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Problem with inputted SAT file");
            throw e; // We still want to let the user know there is an error in their inputted file
                     // and thus not process the file
        }
    }

    /**
     * Returns the variables in the clause
     */
    public int[] getVariables() {
        return variables;
    }

}