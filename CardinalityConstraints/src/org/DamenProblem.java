package org;

import java.util.ArrayList;

import com.microsoft.z3.Z3Exception;

/**
 * Ist eines der Testprobleme.
 * 
 * @author Tietjen
 * 
 */
public class DamenProblem {

  /*
   * Feld wird durchlaufen und einzelne Variablen werden vergeben. Es wird dann an den Solver
   * übergeben
   */
  public static ArrayList<Literal> solve(int k, Encoding encoding) throws Z3Exception {
    // Erstellt das Spielfeld
    ArrayList<Constraint> constraints = new ArrayList<>();
    // Reihen
    for (int row = 0; row < k; row++) {
      ArrayList<Literal> literals = new ArrayList<>();
      for (int column = 0; column < k; column++) {
        literals.add(new Literal(k * row + column, true));
      }
      constraints.add(new Constraint(Constraint.Type.SMALLEREQUALS, literals, 1));
    }
    // Spalten
    for (int column = 0; column < k; column++) {
      ArrayList<Literal> literals = new ArrayList<>();
      for (int row = 0; row < k; row++) {
        literals.add(new Literal(k * row + column, true));
      }
      constraints.add(new Constraint(Constraint.Type.SMALLEREQUALS, literals, 1));
    }
    // Diagonale 1
    int column = 0;
    int row = 0;
    for (int j = 0; j < k - 1; j++) {
      ArrayList<Literal> literals = new ArrayList<>();

      for (int i = 0; ((row + j + i) < k) && ((column + i) < k); i++) {
        literals.add(new Literal(k * (row + j + i) + (column + i), true));
      }
      constraints.add(new Constraint(Constraint.Type.SMALLEREQUALS, literals, 1));
      ArrayList<Literal> literalsNew = new ArrayList<>();
      for (int i = 0; ((column + j + i) < k) && ((row + i) < k); i++) {
        literalsNew.add(new Literal(k * (row + i) + (column + j + i), true));
      }
      constraints.add(new Constraint(Constraint.Type.SMALLEREQUALS, literalsNew, 1));
    }

    // Diagonale 2 (andere Richtung)
    column = k - 1;
    for (int j = 0; j < k - 1; j++) {
      ArrayList<Literal> literals = new ArrayList<>();

      for (int i = 0; ((row + j + i) < k) && ((column - i) < k); i++) {
        literals.add(new Literal(k * (row + j + i) + (column - i), true));
      }
      constraints.add(new Constraint(Constraint.Type.SMALLEREQUALS, literals, 1));
      ArrayList<Literal> literalsNew = new ArrayList<>();
      for (int i = 0; ((column - j - i) >= 0) && ((row + i) < k); i++) {
        literalsNew.add(new Literal(k * (row + i) + (column - j - i), true));
      }
      constraints.add(new Constraint(Constraint.Type.SMALLEREQUALS, literalsNew, 1));
    }

    // equals
    ArrayList<Literal> literals = new ArrayList<Literal>();
    for (int i = 0; i < k * k; i++) { // k^2 für gesamtes Feld
      literals.add(new Literal(i, true));
    }
    constraints.add(new Constraint(Constraint.Type.EQUALS, literals, k));

    Builder builder = new Builder();
    return builder.solve(constraints, k * k, encoding);
  }

}
