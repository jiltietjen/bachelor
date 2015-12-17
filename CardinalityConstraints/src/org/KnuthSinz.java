package org;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public class KnuthSinz extends Encoding {
  /* Beginn Sinz nach Knuth ----------------------------------------------------------- */

  public static void createFormulaFirst(Context ctx, Solver solver, int n, int r,
      ArrayList<String> variableNames) throws Z3Exception {
    ArrayList<BoolExpr> variablesFirst = new ArrayList<BoolExpr>();
    for (int k = 1; k <= r; k++) {
      for (int j = 1; j <= n - r; j++) {
        variablesFirst = new ArrayList<>();
        BoolExpr formularFirstFVariable =
            makeVariableSinz(k, j, ctx, solver, true, n, r, variableNames, false);
        BoolExpr formularFirstSVariable =
            makeVariableSinz(k, j + 1, ctx, solver, true, n, r, variableNames, false);
        if (formularFirstFVariable != null) {
          variablesFirst.add(formularFirstFVariable);
        }
        if (formularFirstSVariable != null) {
          variablesFirst.add(formularFirstSVariable);
        }
        System.out.println("first " + variablesFirst);
        solver
            .add(ctx.mkOr((BoolExpr[]) variablesFirst.toArray(new BoolExpr[variablesFirst.size()])));

      }
    }
  }


  /* zweite Formel wird erstellt */
  public static void createFormulaSecond(Context ctx, Solver solver, int n, int r,
      ArrayList<String> variableNames) throws Z3Exception {
    ArrayList<BoolExpr> variablesSecond = new ArrayList<BoolExpr>();
    for (int k = 0; k <= r; k++) {
      for (int j = 1; j <= n - r; j++) {
        variablesSecond = new ArrayList<>();
        BoolExpr formularSecondFVariable =
            makeVariableSinz(1, j + k, ctx, solver, true, n, r, variableNames, true);
        BoolExpr formularSecondSVariable =
            makeVariableSinz(k, j, ctx, solver, true, n, r, variableNames, false);
        BoolExpr formularSecondTVariable =
            makeVariableSinz(k + 1, j, ctx, solver, true, n, r, variableNames, false);
        if (formularSecondFVariable != null) {
          variablesSecond.add(formularSecondFVariable);
        }
        if (formularSecondSVariable != null) {
          variablesSecond.add(formularSecondSVariable);
        }
        if (formularSecondTVariable != null) {
          variablesSecond.add(formularSecondSVariable);
        }
        solver.add(ctx.mkOr((BoolExpr[]) variablesSecond.toArray(new BoolExpr[variablesSecond
            .size()])));

      }
    }
  }

  private static BoolExpr makeVariableSinz(int upperIndex, int lowerIndex, Context ctx,
      Solver solver, boolean not, int n, int r, ArrayList<String> variableNames, boolean x)
      throws Z3Exception {

    String difference = "Bailleux";

    if (upperIndex == 0 && not) {
      return null;
    }
    if (upperIndex == r + 1 && !not) {
      return null;
    }
    if (x) {
      if (not) {
        // -1 liste x beginnt bei 1 und nicht bei 0
        return ctx.mkNot(ctx.mkBoolConst(variableNames.get(lowerIndex - 1)));
      }
      throw new NullPointerException();
    } else {
      if (not) {
        return ctx.mkNot(ctx.mkBoolConst("b" + upperIndex + "_" + lowerIndex + "_" + difference));
      }
      return ctx.mkBoolConst("b" + upperIndex + "_" + lowerIndex + "_" + difference);
    }
  }


  @Override
  public void encode(ArrayList<String> variableNames, int r, Solver solver, Context ctx)
      throws Z3Exception {
    // TODO Auto-generated method stub

  }


  /* Ende Sinz nach Knuth ------------------------------------------------------------- */


}
