package org;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;


public class BailleuxTest {

  @Test
  public void testNumClausesVariables() throws Z3Exception {
    Solver solver = makeEncoding(2, 1);
    assertEquals(solver.getNumAssertions(), 1);
    solver.check();
    assertEquals(solver.getModel().getNumConsts(), 2);
    solver = makeEncoding(3, 1);
    assertEquals(solver.getNumAssertions(), 5);
    solver.check();
    assertEquals(solver.getModel().getNumConsts(), 5);
    solver = makeEncoding(4, 1);
    assertEquals(solver.getNumAssertions(), 9);
    solver.check();
    assertEquals(solver.getModel().getNumConsts(), 8);
    solver = makeEncoding(5, 1);
    assertEquals(solver.getNumAssertions(), 14);
    solver.check();
    assertEquals(solver.getModel().getNumConsts(), 12);
  }

  private Solver makeEncoding(int n, int r) throws Z3Exception {
    ArrayList<Literal> literals = new ArrayList<>();
    for (int i = 1; i <= n; i++) {
      literals.add(new Literal(i, true));
    }
    Encoding encoding = new KnuthBailleux();
    Context ctx = new Context();
    Solver solver = ctx.mkSolver("QF_LIA");
    encoding.encode(literals, r, 0, solver, ctx);
    return solver;
  }
}
