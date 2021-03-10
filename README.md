# Running the Solver from terminal

1. Unzip the file
2. Navigate to the directory **geneticsat/satsolver/src/**
3. Compile all files using **javac src/*.java**
4. Run using java src/Main {Arguments}

## Arguments for the Solver

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




