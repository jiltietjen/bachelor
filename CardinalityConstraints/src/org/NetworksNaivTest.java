package org;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;


public class NetworksNaivTest {
  @Test
  public void test() throws Z3Exception {
    for (int i = 2; i < 20; i++) {
      for (int j = 1; j < i; j++) {
        Solver solver = TestUtils.makeEncoding(i, j, new NetworksNaiv(), new Context());
        System.out.println(i + " " + j);
        System.out.println(Arrays.toString(solver.getAssertions()));
        assertEquals(solver.getNumAssertions(), sum(i) * 7 + 1);
        assertEquals(TestUtils.numVars(solver), sum(i) * 2 + i);
      }
    }
  }

  private int sum(int n) {
    return n * (n - 1) / 2;
  }



  @Test
  public void testVariablesSAT() throws Z3Exception {
    Context ctx = new Context();
    for (int n = 2; n < 5; n++) {
      for (int r = 1; r < n; r++) {
        for (boolean[] assignment = new boolean[n]; !TestUtils.checkAllTrue(assignment); TestUtils
            .nextAssignment(assignment)) {
          Solver solver = TestUtils.testVariables(n, r, new KnuthBailleux(), ctx, assignment);
          assertEquals(solver.check(), TestUtils.countTrues(assignment) <= r ? Status.SATISFIABLE
              : Status.UNSATISFIABLE);
        }
      }
    }
  }

  /*
   * @Test public void testVariables() throws Z3Exception { Context ctx = new Context(); // n=2 r=1
   * Solver solver = TestUtils.makeEncoding(2, 1, new NetworksNaiv(), ctx); // x_1 = true
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(2, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(2, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(2, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); // n = 3 r = 1 solver =
   * TestUtils.makeEncoding(3, 1, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(3, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(3, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(3, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkBoolConst("x_3")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(3, 1, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); assertEquals(solver.check(), Status.UNSATISFIABLE); // n =
   * 4 r = 1 solver = TestUtils.makeEncoding(4, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(4, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(4, 1, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(4, 1, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(4, 1, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); // n = 5 r = 1 solver =
   * TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE);
   * 
   * solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE);
   * 
   * solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
   * 
   * solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 1, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
   * 
   * // n = 3 r = 2 solver = TestUtils.makeEncoding(3, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkBoolConst("x_3")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(3, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(3, 2, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); assertEquals(solver.check(), Status.UNSATISFIABLE); // n =
   * 4 r = 2 solver = TestUtils.makeEncoding(4, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(4, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(4, 2, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(4, 2, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(4, 2, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); // n = 5 r = 2 solver =
   * TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE);
   * 
   * solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.SATISFIABLE);
   * 
   * solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
   * 
   * solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 2, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
   * 
   * // n = 4 r = 3 solver = TestUtils.makeEncoding(4, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(4, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(4, 3, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(4, 3, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(4, 3, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); // n = 5 r = 3 solver =
   * TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE);
   * 
   * solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.SATISFIABLE);
   * 
   * solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE); solver =
   * TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
   * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE);
   * 
   * solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
   * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx);
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE); solver
   * = TestUtils.makeEncoding(5, 3, new NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1"));
   * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
   * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
   * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
   * NetworksNaiv(), ctx); solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
   * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
   * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
   * 
   * }
   */

}
