package org;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

public class NetworksOwnSortingTest {

  // @Test
  // public void test() throws Z3Exception {
  // for (int i = 2; i < 10; i++) {
  // for (int j = 1; j < i; j++) {
  // Solver solver = TestUtils.makeEncoding(i, j, new NetworksOddEvenMergesort(), new Context());
  // System.out.println(i + " " + j);
  // System.out.println(Arrays.toString(solver.getAssertions()));
  // assertEquals(solver.getNumAssertions(), sum(i) * 7 + 1);
  // assertEquals(TestUtils.numVars(solver), sum(i) * 2 + i);
  // }
  // }
  // }


  @Test
  public void testVariablesSAT() throws Z3Exception {
    Context ctx = new Context();
    for (int n = 1; n < 5; n++) {
      for (int r = 0; r < n; r++) {
        for (boolean[] assignment = new boolean[n]; !TestUtils.checkAllTrue(assignment); TestUtils
            .nextAssignment(assignment)) {

          System.out.println(n + " " + r + " Assignment " + Arrays.toString(assignment));
          Solver solver = TestUtils.testVariables(n, r, new NetworksOwnSorting(), ctx, assignment);
          System.out.println(solver);
          assertEquals(solver.check(), TestUtils.countTrues(assignment) <= r ? Status.SATISFIABLE
              : Status.UNSATISFIABLE);
          Solver solver1 =
              TestUtils.testVariablesNeg(n, r, new NetworksOwnSorting(), ctx, assignment);
          System.out.println(solver1);
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
