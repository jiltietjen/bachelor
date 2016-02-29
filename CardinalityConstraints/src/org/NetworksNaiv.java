package org;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public class NetworksNaiv {

  // Comporators aufrufen und sortieren
  private ArrayList<NetworkComparator> makeSortingNetwork(ArrayList<BoolExpr> inputs,
      ArrayList<BoolExpr> outputs) {
    return null;
  }


  public void encode(ArrayList<Literal> literals, int r, int counter, Solver solver, Context ctx)
      throws Z3Exception {}
}
