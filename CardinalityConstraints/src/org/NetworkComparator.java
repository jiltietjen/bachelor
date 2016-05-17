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
  private int i1Row;
  private int i1Pos;
  private int i2Row;
  private int i2Pos;


  public NetworkComparator(BoolExpr input1, BoolExpr output1, BoolExpr input2, BoolExpr output2) {
    this.input1 = input1;
    this.setOutput1(output1);
    this.input2 = input2;
    this.output2 = output2;
  }

  public NetworkComparator(BoolExpr input1, BoolExpr output1, BoolExpr input2, BoolExpr output2,
      int i1Row, int i1Pos, int i2Row, int i2Pos) {
    this(input1, output1, input2, output2);
    this.setI1Pos(i1Pos);
    this.setI1Row(i1Row);
    this.setI2Pos(i2Pos);
    this.setI2Row(i2Row);
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

  public int getI1Row() {
    return i1Row;
  }

  public void setI1Row(int i1Row) {
    this.i1Row = i1Row;
  }

  public int getI1Pos() {
    return i1Pos;
  }

  public void setI1Pos(int i1Pos) {
    this.i1Pos = i1Pos;
  }

  public int getI2Row() {
    return i2Row;
  }

  public void setI2Row(int i2Row) {
    this.i2Row = i2Row;
  }

  public int getI2Pos() {
    return i2Pos;
  }

  public void setI2Pos(int i2Pos) {
    this.i2Pos = i2Pos;
  }



}
