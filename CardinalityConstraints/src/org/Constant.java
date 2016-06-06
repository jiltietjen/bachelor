package org;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

/**
 * Ist der Wrapper für den ALGO der BDDS.
 * 
 * @author Tietjen
 * 
 */
public class Constant extends Signal {

  private Boolean value;

  public Constant(Boolean value) {
    this.value = value;
    this.tseitinVar = counter++;
  }

  public Boolean isValue() {
    return value;
  }

  public void setValue(Boolean value) {
    this.value = value;
  }

  public void toZ3(Context ctx, Solver solver, int count) throws Z3Exception {
    if (encoded) {
      return;
    }
    encoded = true;
    if (value == false) {
      solver.add(ctx.mkNot(ctx.mkBoolConst("b" + tseitinVar + "_Niklasse_" + count)));
    } else {
      solver.add(ctx.mkBoolConst("b" + tseitinVar + "_Niklasse_" + count));
    }

  }


}
