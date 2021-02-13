package src;

import java.util.Arrays;

// This class represents a clause in the formula
public class Clause {
    private final int[] variables;

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

    public int[] getVariables() {
        return variables;
    }

    // Get Unit clause + pure literal 
    // Return variable number of each one, then lock it on the chromosome level
}