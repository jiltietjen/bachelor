package org;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

public class NiklasseBDDsTest {


  // Todo in die anderen kopieren
  @Test
  public void testVariablesSAT() throws Z3Exception {
    Context ctx = new Context();
    for (int n = 1; n < 5; n++) {
      for (int r = 0; r < n; r++) {
        for (boolean[] assignment = new boolean[n];; TestUtils.nextAssignment(assignment)) {
          Solver solver = TestUtils.testVariables(n, r, new NiklasseBDDs(), ctx, assignment);
          assertEquals(solver.toString(), solver.check(),
              TestUtils.countTrues(assignment) <= r ? Status.SATISFIABLE : Status.UNSATISFIABLE);
          Solver solver1 = TestUtils.testVariablesNeg(n, r, new NiklasseBDDs(), ctx, assignment);
          assertEquals(solver1.toString(), solver.check(),
              TestUtils.countTrues(assignment) <= r ? Status.SATISFIABLE : Status.UNSATISFIABLE);
          // solver.dispose();
          if (TestUtils.checkAllTrue(assignment)) {
            break;
          }
        }
      }
    }
  }
}
