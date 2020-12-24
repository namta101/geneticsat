package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;

public class SATGeneratorTest {

    public SATGeneratorTest() {

    }

    @Test
    public void createEmptyFile() {
        System.out.println(System.getProperty("user.dir"));
        File myObj = new File(System.getProperty("user.dir") + "/satsolver/cnf/" + "test123" + ".cnf");
        try {
            myObj.createNewFile();
            System.out.println("Path" + myObj.getAbsolutePath());
        } catch (IOException exception) {
            System.out.println("Fail to create file");
            exception.printStackTrace();
        }
    }

    @Test
    public void writeSATProblemToFile() {
        try{
            int numberOfVariables = 10;
            int numberOfClauses = 30;
            PrintStream fileWriter = new PrintStream(System.getProperty("user.dir") + "/satsolver/cnf/" + "test123" + ".cnf");
            fileWriter.println("p cnf " + numberOfVariables + " " + numberOfClauses);

            Random rand = new Random();
            for(int i = 0; i<numberOfClauses; i++){
                int numberOfVariablesInClause = rand.nextInt(3) + 1;
                for(int j= 0; j<numberOfVariablesInClause; j++) {
                    fileWriter.print(rand.nextInt(numberOfVariables) + 1  + " ");
                }
                fileWriter.print("\n");
            }
            fileWriter.close();
        } catch (IOException exception) {
            System.out.println("Fail to write to file");
            exception.printStackTrace();
        }
    }


}
