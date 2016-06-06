package org;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

/**
 * einige Methoden ausgelagert für das Testen der Kodierungen
 * 
 * @author Tietjen
 * 
 */
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


  public static Solver makeEncodingNeg(int n, int r, Encoding encoding, Context ctx)
      throws Z3Exception {
    ArrayList<Literal> literals = new ArrayList<>();
    for (int i = 1; i <= n; i++) {
      literals.add(new Literal(i, false));
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


  public static Solver testVariables(int n, int r, Encoding encoding, Context ctx,
      boolean[] assignment) throws Z3Exception {
    Solver solver = makeEncoding(n, r, encoding, ctx);
    for (int i = 0; i < assignment.length; i++) {
      if (assignment[i]) {
        solver.add(ctx.mkBoolConst("x_" + (i + 1)));
      } else {
        solver.add(ctx.mkNot(ctx.mkBoolConst("x_" + (i + 1))));
      }
    }
    return solver;
  }


  public static Solver testVariablesNeg(int n, int r, Encoding encoding, Context ctx,
      boolean[] assignment) throws Z3Exception {
    Solver solver = makeEncodingNeg(n, r, encoding, ctx);
    for (int i = 0; i < assignment.length; i++) {
      if (!assignment[i]) {
        solver.add(ctx.mkBoolConst("x_" + (i + 1)));
      } else {
        solver.add(ctx.mkNot(ctx.mkBoolConst("x_" + (i + 1))));
      }
    }
    return solver;
  }


  public static boolean checkAllTrue(boolean[] assignment) {
    for (int i = 0; i < assignment.length; i++) {
      if (!assignment[i]) {
        return false;
      }
    }
    return true;
  }

  public static void nextAssignment(boolean[] assignment) {
    for (int i = 0; i < assignment.length; i++) {
      if (!assignment[i]) {
        assignment[i] = true;
        return;
      } else {
        assignment[i] = false;
      }
    }
  }

  public static int countTrues(boolean[] assignment) {
    int counter = 0;
    for (int i = 0; i < assignment.length; i++) {
      if (assignment[i]) {
        counter++;
      }
    }
    return counter;
  }

}
