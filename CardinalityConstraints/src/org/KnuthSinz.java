package org;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

/**
 * Kodierung Sinz sequentiell nach Knuth
 * 
 * @author Tietjen
 * 
 */
public class KnuthSinz extends Encoding {

  // erstellt die erste Formel von Knuth
  public static void createFormulaFirst(Context ctx, Solver solver, int n, int r, int counter,
      ArrayList<Literal> literals) throws Z3Exception {
    ArrayList<BoolExpr> variablesFirst = new ArrayList<BoolExpr>();
    for (int k = 1; k <= r; k++) {
      for (int j = 1; j < n - r; j++) {
        variablesFirst = new ArrayList<>();
        BoolExpr formularFirstFVariable =
            makeVariableSinz(k, j, ctx, solver, true, n, r, counter, literals, false);
        BoolExpr formularFirstSVariable =
            makeVariableSinz(k, j + 1, ctx, solver, false, n, r, counter, literals, false);
        if (formularFirstFVariable != null) {
          variablesFirst.add(formularFirstFVariable);
        }
        if (formularFirstSVariable != null) {
          variablesFirst.add(formularFirstSVariable);
        }
        solver
            .add(ctx.mkOr((BoolExpr[]) variablesFirst.toArray(new BoolExpr[variablesFirst.size()])));

      }
    }
  }


  /* zweite Formel wird erstellt */
  public static void createFormulaSecond(Context ctx, Solver solver, int n, int r, int counter,
      ArrayList<Literal> literals) throws Z3Exception {
    ArrayList<BoolExpr> variablesSecond = new ArrayList<BoolExpr>();
    for (int k = 0; k <= r; k++) {
      for (int j = 1; j <= n - r; j++) {
        variablesSecond = new ArrayList<>();
        BoolExpr formularSecondFVariable =
            makeVariableSinz(1, j + k, ctx, solver, true, n, r, counter, literals, true);
        BoolExpr formularSecondSVariable =
            makeVariableSinz(k, j, ctx, solver, true, n, r, counter, literals, false);
        BoolExpr formularSecondTVariable =
            makeVariableSinz(k + 1, j, ctx, solver, false, n, r, counter, literals, false);
        if (formularSecondFVariable != null) {
          variablesSecond.add(formularSecondFVariable);
        }
        if (formularSecondSVariable != null) {
          variablesSecond.add(formularSecondSVariable);
        }
        if (formularSecondTVariable != null) {
          variablesSecond.add(formularSecondTVariable);
        }
        solver.add(ctx.mkOr((BoolExpr[]) variablesSecond.toArray(new BoolExpr[variablesSecond
            .size()])));

      }
    }
  }

  private static BoolExpr makeVariableSinz(int upperIndex, int lowerIndex, Context ctx,
      Solver solver, boolean not, int n, int r, int counter, ArrayList<Literal> literals, boolean x)
      throws Z3Exception {
    if (upperIndex == 0 && not) {
      return null;
    }
    if (upperIndex == r + 1 && !not) {
      return null;
    }
    if (x) {
      if (not) {
        // -1 liste x beginnt bei 1 und nicht bei 0
        return ctx.mkNot(literals.get(lowerIndex - 1).toZ3(ctx));
      }
      throw new NullPointerException();
    } else {
      if (not) {
        return ctx.mkNot(ctx.mkBoolConst("b" + upperIndex + "_" + lowerIndex + "_Sinz_" + counter));
      }
      return ctx.mkBoolConst("b" + upperIndex + "_" + lowerIndex + "_Sinz_" + counter);
    }
  }


  @Override
  public void encode(ArrayList<Literal> literals, int r, int counter, Solver solver, Context ctx)
      throws Z3Exception {

    if (literals.size() == 1) {
      if (r == 0) {
        solver.add(ctx.mkNot(literals.get(0).toZ3(ctx)));
      }
      return;
    }
    createFormulaFirst(ctx, solver, literals.size(), r, counter, literals);
    createFormulaSecond(ctx, solver, literals.size(), r, counter, literals);

  }

}
