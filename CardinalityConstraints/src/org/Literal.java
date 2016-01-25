package org;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Z3Exception;

public class Literal {

  private int index;
  private boolean positive;

  public Literal(int index, boolean positive) {
    this.index = index;
    this.positive = positive;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public boolean isPositive() {
    return positive;
  }

  public void setPositive(boolean positive) {
    this.positive = positive;
  }

  public BoolExpr toZ3(Context ctx) throws Z3Exception {
    BoolExpr result = ctx.mkBoolConst("x_" + index);
    if (!positive) {
      result = ctx.mkNot(result);
    }
    return result;
  }
}
