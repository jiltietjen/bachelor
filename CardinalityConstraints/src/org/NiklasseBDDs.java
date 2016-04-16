package org;

import java.util.ArrayList;
import java.util.HashMap;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public class NiklasseBDDs extends Encoding {

  // Constraints in BDD darstellen. Aus BDD Klauseln erstellen. BDDs ist ein Schaltkreis von
  // if-then-else-Gattern, die zu
  // Klauseln durch Tseitin-Transformation geformt werden mit Extra-Variablen.

  // Algo aus Paper S. 11
  private Signal buildBDD(ArrayList<Literal> ps, int r, int size, int sum, int materialLeft,
      HashMap<PairInteger, Signal> memo) {
    if (sum >= r) {
      Constant constant = new Constant(false);
      return constant;
    } else if (sum + materialLeft < r) {
      Constant constant = new Constant(true);
      return constant;
    }
    PairInteger key = new PairInteger(size, sum);
    if (memo.get(key) == null) {
      size--;
      materialLeft -= 1;// cs[size];
      int hiSum;
      if (ps.get(size).isPositive()) {
        hiSum = sum;
      } else {
        hiSum = sum + 1; // cs[size];
      }
      int loSum;
      if (ps.get(size).isPositive()) {
        loSum = sum + 1; // cs[size];
      } else {
        loSum = sum;
      }
      Signal hiResult = buildBDD(ps, r, size, hiSum, materialLeft, memo);
      Signal loResult = buildBDD(ps, r, size, loSum, materialLeft, memo);
      Literal lit = ps.get(size);
      if (!lit.isPositive()) {
        lit = new Literal(lit.getIndex(), true);
      }
      ITEGate ite = new ITEGate(lit, loResult, hiResult); // TODO:tauschen
      memo.put(key, ite);
    }
    return memo.get(key);
  }

  public void encode(ArrayList<Literal> literals, int r, int counter, Solver solver, Context ctx)
      throws Z3Exception {
    if (literals.size() == 1) {
      if (r == 0) {
        solver.add(ctx.mkNot(literals.get(0).toZ3(ctx)));
      }
      return;
    }

    Signal root =
        buildBDD(literals, r + 1, literals.size(), 0, literals.size(),
            new HashMap<PairInteger, Signal>());
    root.toZ3(ctx, solver, counter);
    solver.add(ctx.mkBoolConst("b" + root.getTseitinVar() + "_Niklasse_" + counter));
  }

}
