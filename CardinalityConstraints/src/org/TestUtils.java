package org;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public class TestUtils {


  public static Solver makeEncoding(int n, int r, Encoding encoding, Context ctx)
      throws Z3Exception {
    ArrayList<Literal> literals = new ArrayList<>();
    for (int i = 1; i <= n; i++) {
      literals.add(new Literal(i, true));
    }
    Solver solver = ctx.mkSolver("QF_LIA");
    encoding.encode(literals, r, 0, solver, ctx);
    return solver;
  }

  public static int numVars(Solver solver) throws Z3Exception {
    BoolExpr[] exprs = solver.getAssertions();
    ArrayList<BoolExpr> vars = new ArrayList<>();
    for (int i = 0; i < exprs.length; i++) {
      // get.Args Literale
      Expr[] exp = exprs[i].getArgs();
      for (int j = 0; j < exp.length; j++) {
        BoolExpr expr = (BoolExpr) exp[j];
        if (expr.isNot()) {
          expr = (BoolExpr) expr.getArgs()[0];
        }
        if (!vars.contains(expr)) {
          vars.add(expr);
        }
      }
    }
    return vars.size();
  }

}
