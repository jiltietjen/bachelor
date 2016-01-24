package org;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public class Naiv extends Encoding {

  public static void calculateNaiv(Context ctx, Solver solver) {
    ArrayList<BoolExpr> naiv = new ArrayList<BoolExpr>();
    for (int i = 0; i < naiv.size(); i++) {
      ArrayList<BoolExpr> formular = new ArrayList<BoolExpr>();
      if (naiv != formular) {
        System.out.println((naiv + " or " + formular) + " and ");
      }
    }

  }

  public void encode(ArrayList<String> variableNames, int r, Solver solver, Context ctx)
      throws Z3Exception {}
}
