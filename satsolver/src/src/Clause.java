package src;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a clause in the SAT problem
 */
public class Clause {
    private final int[] variables;
    private static final Logger LOGGER = Logger.getLogger(Clause.class.getName());

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
        } catch(Exception exception) {
            LOGGER.log(Level.SEVERE, "Failure to create clause", exception);
            throw exception;
        }
    }

    /**
     * Returns the variables in the clause
     */
    public int[] getVariables() {
        return variables;
    }

}