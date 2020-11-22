package src;

import java.util.Arrays;

public class Clause {
    private int[] variables;

    public Clause(int[] variables) {
        if (variables[variables.length - 1] == 0) {
            this.variables = Arrays.copyOf(variables, variables.length - 1); // Get rid of the 0 on the end
        } else {
            this.variables = variables;
        }
    }

    public int[] getVariables() {
        return variables;
    }

    // Get Unit clause + pure literal 
    // Return variable number of each one, then lock it on the chromosome level
}