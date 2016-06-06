package org;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;


/**
 * Oberklasse von Constant und ITEGate (für den Algo von BDDs)
 * 
 * @author Tietjen
 * 
 */
public abstract class Signal {

  protected static int counter = 0;
  protected int tseitinVar;
  protected boolean encoded = false;


  public abstract void toZ3(Context ctx, Solver solver, int counter) throws Z3Exception;

  public int getTseitinVar() {
    return tseitinVar;
  }


}
