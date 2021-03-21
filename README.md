
# Running the solver using an IDE (such as IntelliJ)

It is recommended to run the solver using an IDE, as this will take care of compilation/building the solver. 

1. Unzip the folder
2. Open up the folder in the chosen IDE
3. To build, you must have installed the junit-jupiter 5.42 library: which can be installed from the [Maven Repository](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api). (This is to build the unit tests, and is not necessary if you exclude the test folder from the build/run). (You will  likely be prompted and automatically be able to install this library from within the IDE.)
4. **MAKE SURE TO CHECK THE WORKING DIRECTORY*
 - The Working Directory should match to where the Main file is held: 
 - For example if the project is placed in directory *IdeaProjects*, then the likely directory needed is /Users/yourName/IdeaProjects/geneticsat/satsolver/src
 - Typically the working directory is by default  /Users/yourName/IdeaProjects/geneticsat/, so simply add on satsolver/src
5. Edit Program arguments as wanted (details below 'Arguments for Solver') 
6. Run the program



# Running the Solver from terminal

1. Unzip the folder
2. Navigate to the directory **geneticsat/satsolver/src/**
3. Compile all files using **javac src/*.java**
4. Run using java src/Main {Arguments}

## Arguments for the Solver

Firstly, there is the option of solving a given problem or letting the solver randomly generate the problem.

### Solving an existing/given problem

There are multiple examples given in the cnf folder of various problem sizes.
These examples are named X.Y.no.cnf, where X is the number of variables, Y the number of clauses. 
If you wish to submit your own problem, simply place the file inside this folder (the file must be in DIMACS format - look at the examples for help).

1. To run, simply type java src/Main {name of file}               
 - For example: *java src/Main 10.15.1.cnf*

**Note that the first argument must be the name of the file  - there are other arguments you can give that are explained further below**

### Solving a randomly generated problem 

If instead you wish to generate a new randomly generated problem: you must have the first argument to be '--new'

1. To run, simply type java src/Main --new {fileName} {numberOfVariables} {numberOfClauses}
 - For example: *java src/Main --new test.cnf 30 50*

**Note, these arguments must be in this exact order each time - there are other arguments you can give that are explained further below**

This will generate a random 3-SAT problem depending on the number of variables and clauses given, and immediately start solving it. It will also generate a file to hold this problem and the results which will be stored in the cnf folder under *fileName*

### Optional parameters

If you wish to adjust the configuration of the solver, there are multiple parameters you can change, which are listed in the table below:

| Key         | Appropriate Values |
| ----------- | ----------- |
| -ms (Parent Selection Method)          | roulettewheel OR rank       |
| -mc (Parent Crossover Method)   | uniform OR twopoint        |
| -mm (Mutation Method)   | Greedy OR Random        |
| -pop (Population size)   |  1 - 500        |
| -rm (Rate of Mutation)   | 0.001 - 0.999        |
| -re (Rate of Elitism)   |  0.001 - 0.999|

The default configuration is Rank Selection, Uniform Crossover, Random mutation, Pop. Size of 100, Mutation Rate of 0.1, Elitism Rate of 0.95. 
You are able to change any number of these configurations as you wish.

An example to do so: 

*java src/Main --new testOwnConfig.cnf 100 300 -rm 0.5 -pop 200* <br />
This would solve a newly generated problem with 100 variables, 300 clauses with adjusted rate of mutation being 0.5 and population size being 200. The other default settings will stay the same.


## Output

During solving, the solver will continously print out the current time taken and the generation number it is on (generation number resets on restart policy).
When the solver finds a solution, it will output this solution with the time taken to the user. If not, it will output the closest solution according to its fitness score with the fitness score.
If the solution is found, this will be appended to the problem file. The time taken for each run will also be appended.


