package org;


import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public class KnuthBailleux extends Encoding {


  /* Beginn Bailleux nach Knuth (sequentiell)---------------------------------------------- */
  // static int n = 4;
  // static int r = 1;


  /*
   * Erstellt den Baum für n und r nur zur Überprüfung -> wird später gelöscht public static void
   * createTree() { int root = 1; int k1; int k2; System.out.println("Wurzel: " + root + ", "); for
   * (int k = 1; k < n; k++) { System.out.println("interner Knoten: " + k); k1 = 2 * k; k2 = 2 * k +
   * 1; System.out.println("Kind links: " + k1); System.out.println("Kind rechts: " + k2); } }
   */

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

  /* zweite Formel wird erstellt */
  public static void createFormulaSecond(Context ctx, Solver solver, int n, int r, int counter,
      ArrayList<Literal> literals) throws Z3Exception {
    ArrayList<BoolExpr> variablesSecond = new ArrayList<BoolExpr>();
    for (int i = 0; i <= calcT(2, n); i++) {
      int j = r + 1 - i;
      if (j >= 0 && j <= calcT(3, n)) {
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
      for (int i = 0; i <= calcT(2 * k, n); i++) {
        for (int j = 0; j <= calcT(2 * k + 1, n); j++) {
          if (i + j <= calcT(k, n) + 1 && i + j >= 1) {
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
            System.out.println("first " + variablesFirst);
            solver.add(ctx.mkOr((BoolExpr[]) variablesFirst.toArray(new BoolExpr[variablesFirst
                .size()])));

          }
        }
      }
    }
  }

  /* Esetzt Ausdrücke bei Erfüllung der Bedingungen durch x */
  private static BoolExpr makeVariable(int upperIndex, int lowerIndex, Context ctx, Solver solver,
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

  /* Ende Bailleux nach Knuth --------------------------------------------------------- */


  /*
   * // z3 Solver public static void solver() { Context ctx = null; try { ctx = new Context();
   * Solver solver = ctx.mkSolver("QF_LIA"); createFormulaFirst(ctx, solver);
   * createFormulaSecond(ctx, solver); System.out.println(solver); if (solver.check() ==
   * Status.UNSATISFIABLE) { System.out.println("UNSAT"); } else { System.out.println("SAT"); Model
   * m = solver.getModel(); System.out.println("Model is" + m); } solver.dispose(); } catch
   * (Z3Exception ex) { ex.printStackTrace(); } finally { ctx.dispose(); } }
   */
  @Override
  public void encode(ArrayList<Literal> literals, int r, int counter, Solver solver, Context ctx)
      throws Z3Exception {
    createFormulaFirst(ctx, solver, literals.size(), r, counter, literals);
    createFormulaSecond(ctx, solver, literals.size(), r, counter, literals);
  }
}
