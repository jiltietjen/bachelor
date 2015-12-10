package org;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public class KnuthSinz {
  /* Beginn Sinz nach Knuth ----------------------------------------------------------- */

  public static void createFormulaFirstSinz(Context ctx, Solver solver, int n, int r)
      throws Z3Exception {
    for (int k = 1; k <= r; k++) {
      for (int j = 1; j <= n - r; j++) {
        solver.add(ctx.mkOr(ctx.mkNot(makeVariableSinz(k, j, ctx, n)),
            makeVariableSinz(k, j + 1, ctx, n)));
      }
    }
  }

  public static void createFormulaSecondSinz(Context ctx, Solver solver, int n, int r)
      throws Z3Exception {
    for (int k = 0; k <= r; k++) {
      for (int j = 1; j <= n - r; j++) {
        // x?
        solver.add(ctx.mkOr(ctx.mkNot(makeVariableSinz(1, j + k, ctx, n)),
            ctx.mkNot(makeVariableSinz(-k, j, ctx, n)), makeVariableSinz(k + 1, j, ctx, n)));
      }
    }
  }

  // TODO gucken, wie Variablen ersetzt werden
  private static BoolExpr makeVariableSinz(int upperIndex, int lowerIndex, Context ctx, int n)
      throws Z3Exception {
    // ersetzt b mit upperindex k > n durch x mit upperindex -n+1
    if (lowerIndex == 1 && upperIndex >= n) {
      // x_upperIndex-n+1
      return ctx.mkBoolConst("x" + (upperIndex - n + 1));
    } else {
      // b^upperIndex_lowerIndex
      // ansonsten wird b nicht verändert
      return ctx.mkBoolConst("b" + upperIndex + "_" + lowerIndex);
    }
  }


  /* Ende Sinz nach Knuth ------------------------------------------------------------- */


}
