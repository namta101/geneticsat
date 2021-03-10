
# Running the solver using an IDE (such as IntelliJ)

It is recommended to run the solver using an IDE, as this will take care of compilation/building the solver. 

1. Unzip the folder
2. Open up the folder in the chosen IDE
3. To build, you must have installed the junit-jupiter 5.42 library: which can be installed from the Maven Repository (this is to run the unit tests).
4. **MAKE SURE TO CHECK THE WORKING DIRECTORY**
 - The Working Directory should match to where the Main file is held: 
 - For example if the project is placed in directory *IdeaProjects*, then the likely directory needed is /Users/yourName/IdeaProjects/geneticsat/satsolver/src
 - Typically the working directory is by default  /Users/nam/IdeaProjects/geneticsat/, so simply add on satsolver/src
5. Edit Program arguments as wanted (details below 'Arguments for Solver') 
6. Run the program



# Running the Solver from terminal

1. Unzip the folder
2. Navigate to the directory **geneticsat/satsolver/src/**
3. Compile all files using **javac src/*.java**
4. Run using java src/Main {Arguments}

## Arguments for the Solver

There are two sets of arguments you can give to the solver. 1 Argument if there is already a given problem or 3 Arguments if you wish to generate a new problem to solve. 

### Solving an existing/given problem

There are multiple examples given in the cnf folder of various problem sizes.
These examples are named X.Y.no.cnf, where X is the number of variables, Y the number of clauses. 
If you wish to submit your own problem, simply place the file inside this folder (the file must be in DIMACS format - look at the examples for help)

1. To run, simply type java src/Main {name of file}
 - For example: *java src/Main 10.15.1.cnf*

### Solving a randomly generated problem 

If instead you wish to generate a new randomly generated problem:

1. To run, simply type java src/Main {fileName} {numberOfVariables} {numberOfClauses}
 - For example: *java src/Main test.cnf 30 50*

This will generate a random 3-SAT problem depending on the number of variables and clauses given, and immediately start solving it. It will also generate a file to hold this problem and the results which will be stored in the cnf folder under *fileName*


