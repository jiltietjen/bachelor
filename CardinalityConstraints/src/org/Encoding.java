package org;

import java.util.ArrayList;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public abstract class Encoding {

  public abstract void encode(ArrayList<String> variableNames, int r, Solver solver, Context ctx)
      throws Z3Exception;

}