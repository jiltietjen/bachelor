package org;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

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

  public void toZ3(Context ctx, Solver solver, int counter) throws Z3Exception {
    if (value == false) {
      solver.add(ctx.mkNot(ctx.mkBoolConst("b" + tseitinVar + "_Niklasse_" + counter)));
    } else {
      solver.add(ctx.mkBoolConst("b" + tseitinVar + "_Niklasse_" + counter));
    }

  }


}
