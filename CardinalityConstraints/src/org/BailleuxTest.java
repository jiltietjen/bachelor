package org;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;


public class BailleuxTest {

  @Test
  public void test() throws Z3Exception {
    ArrayList<Literal> literals = new ArrayList<>();
    for (int i = 1; i < 3; i++) {
      literals.add(new Literal(i, true));
    }
    Encoding encoding = new KnuthBailleux();
    Context ctx = new Context();
    Solver solver = ctx.mkSolver("QF_LIA");
    encoding.encode(literals, 1, 0, solver, ctx);
    Solver solverOwn = ctx.mkSolver("QF_LIA");
    solverOwn.add(ctx.mkOr(ctx.mkNot(ctx.mkBoolConst("x" + "_" + 2)),
        ctx.mkNot(ctx.mkBoolConst("x" + "_" + 1))));
    Assert.assertArrayEquals(solver.getAssertions(), solverOwn.getAssertions());
  }
}
