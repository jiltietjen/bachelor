package org;

import java.util.ArrayList;

import com.microsoft.z3.Context;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

public class DamenProblem {


  public static void main(String[] args) {
    try {
      solve(8);
    } catch (Z3Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /*
   * Feld wird durchlaufen und einzelne Variablen werden vergeben. Es wird dann an den Solver
   * übergeben
   */
  public static void solve(int k) throws Z3Exception {
    Context ctx = new Context();
    Solver solver = ctx.mkSolver("QF_LIA");
    Encoding encoding = new KnuthBailleux(); // TODO Einstellungsmöglichkeit für verschiedene
                                             // Encodings
    Encoding encodingSinz = new KnuthSinz();

    // Erstellt das Spielfeld
    // Reihen
    for (int row = 0; row < k; row++) {
      ArrayList<String> variableNames = new ArrayList<>();
      for (int column = 0; column < k; column++) {
        variableNames.add("x_" + row + "_" + column);
      }
      encoding.encode(variableNames, 1, solver, ctx);
    }
    // Spalten
    for (int column = 0; column < k; column++) {
      ArrayList<String> variableNames = new ArrayList<>();
      for (int row = 0; row < k; row++) {
        variableNames.add("x_" + row + "_" + column);
      }
      encoding.encode(variableNames, 1, solver, ctx);
    }
    // Diagonale 1
    int column = 0;
    int row = 0;
    for (int j = 0; j < k - 1; j++) {
      ArrayList<String> variableNames = new ArrayList<>();

      for (int i = 0; ((row + j + i) < k) && ((column + i) < k); i++) {
        variableNames.add("x_" + (row + j + i) + "_" + (column + i));
      }
      encoding.encode(variableNames, 1, solver, ctx);
      variableNames = new ArrayList<>();
      for (int i = 0; ((column + j + i) < k) && ((row + i) < k); i++) {
        variableNames.add("x_" + (row + i) + "_" + (column + j + i));
      }
      encoding.encode(variableNames, 1, solver, ctx);
    }

    // Diagonale 2 (andere Richtung)
    column = k - 1;
    for (int j = 0; j < k - 1; j++) {
      ArrayList<String> variableNames = new ArrayList<>();

      for (int i = 0; ((row + j + i) < k) && ((column - i) < k); i++) {
        variableNames.add("x_" + (row + j + i) + "_" + (column - i));
      }
      encoding.encode(variableNames, 1, solver, ctx);
      variableNames = new ArrayList<>();
      for (int i = 0; ((column - j - i) < k) && ((row + i) < k); i++) {
        variableNames.add("x_" + (row + i) + "_" + (column - j - i));
      }
      encoding.encode(variableNames, 1, solver, ctx);
    }


    // wird in den Solver gegeben
    System.out.println(solver);
    if (solver.check() == Status.UNSATISFIABLE) { // TODO Timeout abfangen
      System.out.println("UNSAT");
    } else {
      System.out.println("SAT");
      Model m = solver.getModel();
      System.out.println("Model is" + m); // TODO Model zurückübersetzen in k Damen Problem
    }
    solver.dispose();
  }

}
