package org;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public abstract class Signal {

  protected static int counter = 0;
  protected int tseitinVar;


  public abstract void toZ3(Context ctx, Solver solver, int counter) throws Z3Exception;

  public int getTseitinVar() {
    return tseitinVar;
  }


}
