package org;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public class Naiv extends Encoding {


  public ArrayList<ArrayList<Literal>> createSubsets(ArrayList<Literal> set, int limit,
      ArrayList<Literal> toBeAdded) {
    ArrayList<ArrayList<Literal>> result = new ArrayList<>();
    // fertig
    if (set.size() >= limit) {
      result.add(set);
      return result;
    }
    // In das neue Array wird immer das nächst-höhere Element reingepackt
    for (int i = 0; i < toBeAdded.size(); i++) {
      ArrayList<Literal> setCopy = new ArrayList<>(set);
      setCopy.add(toBeAdded.get(i));
      ArrayList<Literal> newToBeAdded = new ArrayList<>();
      for (int j = i + 1; j < toBeAdded.size(); j++) {
        newToBeAdded.add(toBeAdded.get(j));
      }
      result.addAll(createSubsets(setCopy, limit, newToBeAdded));
    }
    return result;
  }

  @Override
  // Wandelt das Array in Klauseln für den Z3 um
  public void encode(ArrayList<Literal> literals, int r, int counter, Solver solver, Context ctx)
      throws Z3Exception {

    if (literals.size() == 1) {
      if (r == 0) {
        solver.add(ctx.mkNot(literals.get(0).toZ3(ctx)));
      }
      return;
    }

    ArrayList<ArrayList<Literal>> clauses =
        createSubsets(new ArrayList<Literal>(), r + 1, literals);
    for (int i = 0; i < clauses.size(); i++) {
      BoolExpr[] clause = new BoolExpr[clauses.get(i).size()];
      for (int j = 0; j < clauses.get(i).size(); j++) {
        clause[j] = ctx.mkNot(clauses.get(i).get(j).toZ3(ctx));
      }
      solver.add(ctx.mkOr(clause));
    }

  }
}
