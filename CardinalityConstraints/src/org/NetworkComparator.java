package org;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public class NetworkComparator {

  private BoolExpr input1;
  private BoolExpr output1;
  private BoolExpr input2;
  private BoolExpr output2;


  public NetworkComparator(BoolExpr input1, BoolExpr output1, BoolExpr input2, BoolExpr output2) {
    this.input1 = input1;
    this.setOutput1(output1);
    this.input2 = input2;
    this.output2 = output2;
  }


  public BoolExpr getInput2() {
    return input2;
  }

  public void setInput2(BoolExpr input2) {
    this.input2 = input2;
  }

  public BoolExpr getOutput2() {
    return output2;
  }

  public void setOutput2(BoolExpr output2) {
    this.output2 = output2;
  }

  public BoolExpr getInput1() {
    return input1;
  }


  public void setInput1(BoolExpr input1) {
    this.input1 = input1;
  }

  public BoolExpr getOutput1() {
    return output1;
  }


  public void setOutput1(BoolExpr output1) {
    this.output1 = output1;
  }

  // logische Überlegung für den Comparator, so dass alle möglichen Fälle richtig behandelt werden
  public void toZ3(Context ctx, Solver solver) throws Z3Exception {
    solver.add(ctx.mkOr(ctx.mkNot(output2), output1));
    solver.add(ctx.mkOr(ctx.mkNot(input1), ctx.mkNot(input2), output2));
    solver.add(ctx.mkOr(input1, ctx.mkNot(output2)));
    solver.add(ctx.mkOr(input2, ctx.mkNot(output2)));
    solver.add(ctx.mkOr(input1, input2, ctx.mkNot(output1)));
    solver.add(ctx.mkOr(ctx.mkNot(input1), output1));
    solver.add(ctx.mkOr(ctx.mkNot(input2), output1));
  }

  public String toString() {
    return "inputs " + input1 + " " + input2 + " outputs " + output1 + " " + output2;
  }

}
