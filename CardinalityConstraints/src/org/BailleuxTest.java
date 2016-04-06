package org;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;


public class BailleuxTest {
  @Test
  public void testNumClausesVariables() throws Z3Exception {
    Context ctx = new Context();
    Solver solver = TestUtils.makeEncoding(2, 1, new KnuthBailleux(), ctx);
    assertEquals(solver.getNumAssertions(), 1);
    assertEquals(TestUtils.numVars(solver), 2);
    solver = TestUtils.makeEncoding(3, 1, new KnuthBailleux(), ctx);
    assertEquals(solver.getNumAssertions(), 4);
    assertEquals(TestUtils.numVars(solver), 4);
    solver = TestUtils.makeEncoding(4, 1, new KnuthBailleux(), ctx);
    assertEquals(solver.getNumAssertions(), 7);
    assertEquals(TestUtils.numVars(solver), 6);
    solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
    assertEquals(solver.getNumAssertions(), 10);
    assertEquals(TestUtils.numVars(solver), 8);
    solver = TestUtils.makeEncoding(3, 2, new KnuthBailleux(), ctx);
    assertEquals(solver.getNumAssertions(), 4);
    assertEquals(TestUtils.numVars(solver), 5);
    solver = TestUtils.makeEncoding(4, 2, new KnuthBailleux(), ctx);
    assertEquals(solver.getNumAssertions(), 8);
    assertEquals(TestUtils.numVars(solver), 8);
    solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
    assertEquals(solver.getNumAssertions(), 13);
    assertEquals(TestUtils.numVars(solver), 11);
    solver = TestUtils.makeEncoding(4, 3, new KnuthBailleux(), ctx);
    assertEquals(solver.getNumAssertions(), 7);
    assertEquals(TestUtils.numVars(solver), 8);
    solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
    assertEquals(solver.getNumAssertions(), 13);
    assertEquals(TestUtils.numVars(solver), 12);
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

  @Test
  public void testCalcT() throws Z3Exception {
    for (int n = 2; n < 1000; n++) {
      for (int t = 1; t < 2 * n; t++) {
        assertEquals(KnuthBailleux.calcT(t, n), KnuthBailleux.calcTWithoutRecursion(t, n));
      }
    }

    // n=2 r=1
    // Solver solver = TestUtils.testVariables(2, 1, new KnuthBailleux(), ctx);
    // x_1 = true
    // solver.add(ctx.mkBoolConst("x_1"));
    // solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
    // assertEquals(solver.check(), Status.SATISFIABLE);
    // solver = TestUtils.makeEncoding(2, 1, new KnuthBailleux(), ctx);
    // solver.add(ctx.mkBoolConst("x_2"));
    // solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
    // assertEquals(solver.check(), Status.SATISFIABLE);
    // solver = TestUtils.makeEncoding(2, 1, new KnuthBailleux(), ctx);
    // solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
    // solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
    // assertEquals(solver.check(), Status.SATISFIABLE);
    // solver = TestUtils.makeEncoding(2, 1, new KnuthBailleux(), ctx);
    // solver.add(ctx.mkBoolConst("x_1"));
    // solver.add(ctx.mkBoolConst("x_2"));
    // assertEquals(solver.check(), Status.UNSATISFIABLE);
    // n = 3 r = 1
    /*
     * solver = TestUtils.makeEncoding(3, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(3, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(3, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(3, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); assertEquals(solver.check(), Status.SATISFIABLE); solver
     * = TestUtils.makeEncoding(3, 1, new KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 1, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 1, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 1, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
     * assertEquals(solver.check(), Status.UNSATISFIABLE); // n = 4 r = 1 solver =
     * TestUtils.makeEncoding(4, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(4, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(4, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 1, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE); // n
     * = 5 r = 1 solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE);
     * 
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * 
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * 
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 1, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * 
     * // n = 3 r = 2 solver = TestUtils.makeEncoding(3, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); assertEquals(solver.check(), Status.SATISFIABLE); solver
     * = TestUtils.makeEncoding(3, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); assertEquals(solver.check(), Status.SATISFIABLE); solver
     * = TestUtils.makeEncoding(3, 2, new KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(3, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
     * assertEquals(solver.check(), Status.UNSATISFIABLE); // n = 4 r = 2 solver =
     * TestUtils.makeEncoding(4, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.SATISFIABLE); solver
     * = TestUtils.makeEncoding(4, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(4, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * assertEquals(solver.check(), Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE); // n
     * = 5 r = 2 solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE);
     * 
     * solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE); solver
     * = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
     * assertEquals(solver.check(), Status.SATISFIABLE);
     * 
     * solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * 
     * solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 2, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * 
     * // n = 4 r = 3 solver = TestUtils.makeEncoding(4, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.SATISFIABLE); solver
     * = TestUtils.makeEncoding(4, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.SATISFIABLE); solver
     * = TestUtils.makeEncoding(4, 3, new KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.SATISFIABLE); solver
     * = TestUtils.makeEncoding(4, 3, new KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(4, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkBoolConst("x_4")); assertEquals(solver.check(), Status.UNSATISFIABLE); // n
     * = 5 r = 3 solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE);
     * 
     * solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE); solver
     * = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
     * assertEquals(solver.check(), Status.SATISFIABLE);
     * 
     * solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.SATISFIABLE); solver
     * = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkBoolConst("x_5"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkNot(ctx.mkBoolConst("x_1")));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkBoolConst("x_5"));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_2"))); solver.add(ctx.mkBoolConst("x_3"));
     * solver.add(ctx.mkBoolConst("x_4")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_5")));
     * assertEquals(solver.check(), Status.SATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new
     * KnuthBailleux(), ctx); solver.add(ctx.mkBoolConst("x_1"));
     * solver.add(ctx.mkBoolConst("x_2")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_3")));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_4"))); solver.add(ctx.mkBoolConst("x_5"));
     * assertEquals(solver.check(), Status.SATISFIABLE);
     * 
     * solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_5"))); assertEquals(solver.check(),
     * Status.UNSATISFIABLE); solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_1"))); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_2")));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkNot(ctx.mkBoolConst("x_3"))); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkNot(ctx.mkBoolConst("x_4")));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     * solver = TestUtils.makeEncoding(5, 3, new KnuthBailleux(), ctx);
     * solver.add(ctx.mkBoolConst("x_1")); solver.add(ctx.mkBoolConst("x_2"));
     * solver.add(ctx.mkBoolConst("x_3")); solver.add(ctx.mkBoolConst("x_4"));
     * solver.add(ctx.mkBoolConst("x_5")); assertEquals(solver.check(), Status.UNSATISFIABLE);
     */

  }
}
