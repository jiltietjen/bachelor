package org;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;


public class SinzParallelTest {
  @Test
  public void testNumClausesVariables() throws Z3Exception {
    Context ctx = new Context();
    Solver solver = TestUtils.makeEncoding(2, 1, new SinzParallel(), ctx);
    assertEquals(solver.getNumAssertions(), 4);
    assertEquals(TestUtils.numVars(solver), 4);
    solver = TestUtils.makeEncoding(3, 1, new SinzParallel(), ctx);
    assertEquals(solver.getNumAssertions(), 8);
    assertEquals(TestUtils.numVars(solver), 5);
    solver = TestUtils.makeEncoding(4, 1, new SinzParallel(), ctx);
    assertEquals(solver.getNumAssertions(), 15);
    assertEquals(TestUtils.numVars(solver), 10);
    solver = TestUtils.makeEncoding(5, 1, new SinzParallel(), ctx);
    assertEquals(solver.getNumAssertions(), 19);
    assertEquals(TestUtils.numVars(solver), 11);
    solver = TestUtils.makeEncoding(3, 2, new SinzParallel(), ctx);
    assertEquals(solver.getNumAssertions(), 8);
    assertEquals(TestUtils.numVars(solver), 5);
    solver = TestUtils.makeEncoding(4, 2, new SinzParallel(), ctx);
    assertEquals(solver.getNumAssertions(), 15);
    assertEquals(TestUtils.numVars(solver), 10);
    solver = TestUtils.makeEncoding(5, 2, new SinzParallel(), ctx);
    assertEquals(solver.getNumAssertions(), 19);
    assertEquals(TestUtils.numVars(solver), 11);
    solver = TestUtils.makeEncoding(4, 3, new SinzParallel(), ctx);
    assertEquals(solver.getNumAssertions(), 14);
    assertEquals(TestUtils.numVars(solver), 10);
    solver = TestUtils.makeEncoding(5, 3, new SinzParallel(), ctx);
    assertEquals(solver.getNumAssertions(), 18);
    assertEquals(TestUtils.numVars(solver), 11);
  }



  @Test
  public void testVariablesSAT() throws Z3Exception {
    Context ctx = new Context();
    for (int n = 1; n < 5; n++) {
      for (int r = 0; r < n; r++) {
        for (boolean[] assignment = new boolean[n]; !TestUtils.checkAllTrue(assignment); TestUtils
            .nextAssignment(assignment)) {
          Solver solver = TestUtils.testVariables(n, r, new SinzParallel(), ctx, assignment);
          assertEquals(solver.check(), TestUtils.countTrues(assignment) <= r ? Status.SATISFIABLE
              : Status.UNSATISFIABLE);
          Solver solver1 = TestUtils.testVariablesNeg(n, r, new SinzParallel(), ctx, assignment);
          assertEquals(solver1.check(), TestUtils.countTrues(assignment) <= r ? Status.SATISFIABLE
              : Status.UNSATISFIABLE);
          if (TestUtils.checkAllTrue(assignment)) {
            break;
          }
        }
      }
    }
  }
}
