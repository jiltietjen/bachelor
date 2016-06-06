package org;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;


public class KnuthSinzTest {
  @Test
  public void testNumClausesVariables() throws Z3Exception {
    Context ctx = new Context();
    Solver solver = TestUtils.makeEncoding(2, 1, new KnuthSinz(), ctx);
    assertEquals(solver.getNumAssertions(), 2);
    assertEquals(TestUtils.numVars(solver), 3);
    solver = TestUtils.makeEncoding(3, 1, new KnuthSinz(), ctx);
    assertEquals(solver.getNumAssertions(), 5);
    assertEquals(TestUtils.numVars(solver), 5);
    solver = TestUtils.makeEncoding(4, 1, new KnuthSinz(), ctx);
    assertEquals(solver.getNumAssertions(), 8);
    assertEquals(TestUtils.numVars(solver), 7);
    solver = TestUtils.makeEncoding(5, 1, new KnuthSinz(), ctx);
    assertEquals(solver.getNumAssertions(), 11);
    assertEquals(TestUtils.numVars(solver), 9);
    solver = TestUtils.makeEncoding(3, 2, new KnuthSinz(), ctx);
    assertEquals(solver.getNumAssertions(), 3);
    assertEquals(TestUtils.numVars(solver), 5);
    solver = TestUtils.makeEncoding(4, 2, new KnuthSinz(), ctx);
    assertEquals(solver.getNumAssertions(), 8);
    assertEquals(TestUtils.numVars(solver), 8);
    solver = TestUtils.makeEncoding(5, 2, new KnuthSinz(), ctx);
    assertEquals(solver.getNumAssertions(), 13);
    assertEquals(TestUtils.numVars(solver), 11);
    solver = TestUtils.makeEncoding(4, 3, new KnuthSinz(), ctx);
    assertEquals(solver.getNumAssertions(), 4);
    assertEquals(TestUtils.numVars(solver), 7);
    solver = TestUtils.makeEncoding(5, 3, new KnuthSinz(), ctx);
    assertEquals(solver.getNumAssertions(), 11);
    assertEquals(TestUtils.numVars(solver), 11);
  }


  @Test
  public void testVariablesSAT() throws Z3Exception {
    Context ctx = new Context();
    for (int n = 1; n < 5; n++) {
      for (int r = 0; r < n; r++) {
        for (boolean[] assignment = new boolean[n]; !TestUtils.checkAllTrue(assignment); TestUtils
            .nextAssignment(assignment)) {
          Solver solver = TestUtils.testVariables(n, r, new KnuthSinz(), ctx, assignment);
          assertEquals(solver.check(), TestUtils.countTrues(assignment) <= r ? Status.SATISFIABLE
              : Status.UNSATISFIABLE);
          Solver solver1 = TestUtils.testVariablesNeg(n, r, new KnuthSinz(), ctx, assignment);
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
