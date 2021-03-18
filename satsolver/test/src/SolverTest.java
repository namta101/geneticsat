package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SolverTest {
    private final Formula formula = TestHelper.createFormula();
    private final Formula unsatisfiableFormula = TestHelper.createUnsatisfiableFormula();
    private final int numberOfVariables = 3;
    private final Solver solver;
    private final Solver solverWithUnsatisfiableInstance;

    public SolverTest(){
        solver = new Solver(formula, numberOfVariables);
        solverWithUnsatisfiableInstance = new Solver(unsatisfiableFormula, numberOfVariables);
    }

    @Test
    public void upperTimeLimitReached_timeLimitReachedNotReached_returnsFalse() {
        solver.startTimer();
        boolean result = solver.upperTimeLimitReached();
        Assertions.assertFalse(result);
    }

    @Test
    public void upperTimeLimitReached_timeLimitReachedReached_returnsTrue() throws InterruptedException {
        solver.startTimer();
        Thread.sleep(400000);
        boolean result = solver.upperTimeLimitReached();
        Assertions.assertTrue(result);
    }

    @Test
    public void resetRestartPolicyCounters_onMethodCall_ResetsCounters(){
        solver.solve();
        solver.resetRestartPolicyCounters();
        Assertions.assertTrue(solver.getRestartTimeTracker() - System.currentTimeMillis() < 1000);
        Assertions.assertEquals(0, solver.getUnimprovedGenerationsCount());
        Assertions.assertEquals(0, solver.getGenerationNumber());
    }

    @Test
    public void resetTimeTracker_callingMethod_setsTimerTo0() throws InterruptedException {
        solver.solve();
        Thread.sleep(50);
        Assertions.assertTrue(solver.getRestartTimeTracker() > 10000);
        solver.resetRestartTimer();
        Assertions.assertTrue(System.currentTimeMillis() - solver.getRestartTimeTracker() < 10);
    }

    @Test
    public void processAlgorithm_incrementsGenerationNumber() {

        int firstGeneration = solver.getGenerationNumber();
        solver.solve();
        solver.processAlgorithm();
        int secondGeneration =  solver.getGenerationNumber();
        Assertions.assertEquals(firstGeneration + 1, secondGeneration );
    }

    /// Note this test only works on SAT problems with one satisfying solution
    @Test
    public void solve_returnsCorrectSolution() {
        int[] solution = solver.solve();
        Assertions.assertArrayEquals(new int[]{1,1,1}, solution);
    }

    @Test
    public void restartAlgorithm_resetsGenerationNumber(){
        solver.solve();
        solver.processAlgorithm();
        Assertions.assertNotEquals(0, solver.getGenerationNumber());
        solver.restartAlgorithm();
        Assertions.assertEquals(0, solver.getGenerationNumber());

    }

    // This tests the generations unimproved for count restart
    @Test
    public void shouldRestartAlgorithm_restartsOnUnimprovedGenerationCountLimitReached(){
        solverWithUnsatisfiableInstance.setRestartTimerToMax();
        solverWithUnsatisfiableInstance.solve();

        int generationNumber = solverWithUnsatisfiableInstance.getGenerationNumber();
        int numberOfRestarts = solverWithUnsatisfiableInstance.getNumberOfRestarts();
        int totalNumberOfGenerations = solverWithUnsatisfiableInstance.getTotalGenerationsPassed();

        int generationsUnimprovedBeforeRestart = (totalNumberOfGenerations - generationNumber) / numberOfRestarts;
        Assertions.assertEquals(solverWithUnsatisfiableInstance.getUnimprovedGenerationsBeforeRestart(), generationsUnimprovedBeforeRestart);
    }

    @Test
    public void shouldRestartAlgorithm_restartsOnRestartTimerLimitReached(){
        solverWithUnsatisfiableInstance.setUnimprovedGenerationsBeforeRestartToMax();
        solverWithUnsatisfiableInstance.solve();
        int numberOfRestarts = solverWithUnsatisfiableInstance.getNumberOfRestarts();
        long timeBeforeEachReset = solverWithUnsatisfiableInstance.getTimeBeforeEachRestart();

        long totalTimeRun = System.currentTimeMillis() - solverWithUnsatisfiableInstance.getStartTime();
        long timeAfterLatestReset = totalTimeRun -  (numberOfRestarts * timeBeforeEachReset);

        Assertions.assertTrue(timeAfterLatestReset < timeBeforeEachReset);

    }

    @Test
    public void previousRestartAlgorithm_resetsGenerationNumberAndResetTimer(){
        solverWithUnsatisfiableInstance.solve();
        solverWithUnsatisfiableInstance.previousRestartAlgorithm();
        Assertions.assertEquals(0, solverWithUnsatisfiableInstance.getGenerationNumber());
        Assertions.assertTrue(System.currentTimeMillis() - solverWithUnsatisfiableInstance.getRestartTimeTracker() < 1000);
    }

}