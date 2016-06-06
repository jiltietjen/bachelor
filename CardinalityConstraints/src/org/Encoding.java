package org;

import java.util.ArrayList;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

/**
 * Stellt die Beziehung zu den Kodierungen her.
 * 
 * @author Tietjen
 * 
 */
public abstract class Encoding {

  public abstract void encode(ArrayList<Literal> literals, int r, int counter, Solver solver,
      Context ctx) throws Z3Exception;

}
