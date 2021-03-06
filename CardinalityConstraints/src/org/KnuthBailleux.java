package org;


import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

/**
 * Kodierung Bailleux nach Knuth und Subklasse von Encoding
 * 
 * @author Tietjen
 * 
 */
public class KnuthBailleux extends Encoding {


  /* Zählt die Blätter unter den jeweiligen Knoten t */
  public static int calcT(int t, int n) {
    if (t >= n) {
      return 1;
    }
    /* überschreitet die Anzahl der möglichen Knoten */
    if (t > 2 * n - 1) {
      return 0;
    }
    /* berechnet die Anzahl der Blätter rekursiv für Knoten t */
    return calcT(2 * t, n) + calcT(2 * t + 1, n);
  }

  public static int calcT(int t, int n, int r) {
    if (r == 0) {
      return 1;
    }
    return Math.min(calcTWithoutRecursion(t, n), r);
  }


  /*
   * public static int calcTWithoutRecursion(int t, int n) { if (t >= n) { return 1; } // zählt die
   * Anzahl der Blätter int result = 0; // Blätter sind n bis 2*n for (int i = n; i < 2 * n; i++) {
   * // i wird um die Differenz von t-i geshiftet if (i >>> (Integer.numberOfLeadingZeros(t) -
   * Integer.numberOfLeadingZeros(i)) == t) { result++; } } return result; }
   */

  // Zählt die Anzahl der Blätter unter einem Knoten. Lesbarere (aber langsamere) Version im
  // Kommentar oben
  public static int calcTWithoutRecursion(int t, int n) {
    if (t >= n) {
      return 1;
    }
    int tShifted = t << (Integer.numberOfLeadingZeros(t) - Integer.numberOfLeadingZeros(n));
    int tMask = (1 << (Integer.numberOfLeadingZeros(t) - Integer.numberOfLeadingZeros(n))) - 1;
    int highLeafs = Math.max(0, (tShifted | tMask) - Math.max(n, tShifted) + 1);
    int lowLeafs =
        Math.max(0, Math.min(2 * n, (tShifted << 1) + ((tMask << 1) + 2)) - (tShifted << 1));
    return highLeafs + lowLeafs;
  }

  /* zweite Formel wird erstellt */
  public static void createFormulaSecond(Context ctx, Solver solver, int n, int r, int counter,
      ArrayList<Literal> literals) throws Z3Exception {
    ArrayList<BoolExpr> variablesSecond = new ArrayList<BoolExpr>();
    for (int i = 0; i <= calcT(2, n, r); i++) {
      int j = r + 1 - i;
      if (j >= 0 && j <= calcT(3, n, r)) {
        // makeLiteral(not b^2_i or not b^3_j)
        // wird in x, bzw b umgewandelt in makeVariable
        variablesSecond = new ArrayList<>();
        BoolExpr formularSecondFVariable =
            makeVariable(2, i, ctx, solver, true, n, r, counter, literals);
        BoolExpr formularSecondSVariable =
            makeVariable(3, j, ctx, solver, true, n, r, counter, literals);
        if (formularSecondFVariable != null) {
          variablesSecond.add(formularSecondFVariable);
        }
        if (formularSecondSVariable != null) {
          variablesSecond.add(formularSecondSVariable);
        }
        solver.add(ctx.mkOr((BoolExpr[]) variablesSecond.toArray(new BoolExpr[variablesSecond
            .size()])));
      }
    }
  }

  /* erste Formel wird erstellt */
  public static void createFormulaFirst(Context ctx, Solver solver, int n, int r, int counter,
      ArrayList<Literal> literals) throws Z3Exception {
    ArrayList<BoolExpr> variablesFirst = new ArrayList<BoolExpr>();
    for (int k = 2; k < n; k++) {
      // t2k in echtzeit berechnen
      for (int i = 0; i <= calcT(2 * k, n, r); i++) {
        for (int j = 0; j <= calcT(2 * k + 1, n, r); j++) {
          if (i + j <= calcT(k, n, r) + 1 && i + j >= 1) {
            // not b^2k_i or not b^2k+1_j or b^k_i+j
            // in x, bzw b umwandeln in makeVariable
            variablesFirst = new ArrayList<>();
            BoolExpr formularFirstFVariable =
                makeVariable(2 * k, i, ctx, solver, true, n, r, counter, literals);
            BoolExpr formularFirstSVariable =
                makeVariable(2 * k + 1, j, ctx, solver, true, n, r, counter, literals);
            BoolExpr formularFirstTVariable =
                makeVariable(k, i + j, ctx, solver, false, n, r, counter, literals);
            if (formularFirstFVariable != null) {
              variablesFirst.add(formularFirstFVariable);
            }
            if (formularFirstSVariable != null) {
              variablesFirst.add(formularFirstSVariable);
            }
            if (formularFirstTVariable != null) {
              variablesFirst.add(formularFirstTVariable);
            }
            solver.add(ctx.mkOr((BoolExpr[]) variablesFirst.toArray(new BoolExpr[variablesFirst
                .size()])));

          }
        }
      }
    }
  }

  /* Esetzt Ausdrücke bei Erfüllung der Bedingungen durch x */
  public static BoolExpr makeVariable(int upperIndex, int lowerIndex, Context ctx, Solver solver,
      boolean not, int n, int r, int counter, ArrayList<Literal> literals) throws Z3Exception {
    // ist lowerIndex 0 oder r+1 soll der Ausdruck entfernt werden
    // ersetzt b mit upperindex k > n durch x mit upperindex -n+1

    if (lowerIndex == 0 && not) {
      return null;
    }
    if (lowerIndex == r + 1 && !not) {
      return null;
    }
    if (lowerIndex == 1 && upperIndex >= n) {
      // x_upperIndex-n+1
      if (not) {
        // +1 entfällt durch die 0. Stelle in der Liste
        return ctx.mkNot(literals.get(upperIndex - n).toZ3(ctx));
      }
      return literals.get(upperIndex - n).toZ3(ctx);
    } else {
      // b^upperIndex_lowerIndex
      // ansonsten wird b nicht verändert
      if (not) {
        return ctx.mkNot(ctx.mkBoolConst("b" + upperIndex + "_" + lowerIndex + "_Bailleux_"
            + counter));
      }
      return ctx.mkBoolConst("b" + upperIndex + "_" + lowerIndex + "_Bailleux_" + counter);
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
