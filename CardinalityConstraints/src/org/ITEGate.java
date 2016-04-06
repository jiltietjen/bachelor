package org;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

// neuer Datentyp für den Algorithmus
public class ITEGate extends Signal {
  private Literal input1;
  private Signal input2;
  private Signal input3;

  public ITEGate(Literal input1, Signal input2, Signal input3) {
    this.input1 = input1;
    this.input2 = input2;
    this.input3 = input3;
    this.tseitinVar = counter++;
  }

  public Literal getInput1() {
    return input1;
  }

  public void setInput1(Literal input1) {
    this.input1 = input1;
  }

  public Signal getInput3() {
    return input3;
  }

  public void setInput3(Signal input3) {
    this.input3 = input3;
  }

  public Signal getInput2() {
    return input2;
  }

  public void setInput2(Signal input2) {
    this.input2 = input2;
  }

  // Erstellt aus dem Baum die Klauseln mit Hilfe von Tseitin. Jeder Knoten hat eine Variable t und
  // a ist ein Literal.
  public void toZ3(Context ctx, Solver solver, int count) throws Z3Exception {
    if (encoded) {
      return;
    }
    encoded = true;

    BoolExpr tVar = ctx.mkBoolConst("b" + tseitinVar + "_Niklasse_" + count);
    BoolExpr in3Var = ctx.mkBoolConst("b" + input3.getTseitinVar() + "_Niklasse_" + count);
    BoolExpr in1Var = input1.toZ3(ctx);
    BoolExpr in2Var = ctx.mkBoolConst("b" + input2.getTseitinVar() + "_Niklasse_" + count);

    solver.add(ctx.mkOr(ctx.mkNot(tVar), in1Var, in3Var)); // drin
    solver.add(ctx.mkOr(ctx.mkNot(tVar), ctx.mkNot(in1Var), in2Var)); // drin
    // solver.add(ctx.mkOr(ctx.mkNot(tVar), in2Var, in3Var)); verschlechtert die Performanz
    // solver.add(ctx.mkOr(tVar, ctx.mkNot(in2Var), ctx.mkNot(in3Var))); verschlechtert die
    // Performanz
    solver.add(ctx.mkOr(tVar, ctx.mkNot(in1Var), ctx.mkNot(in2Var))); // drin
    solver.add(ctx.mkOr(tVar, in1Var, ctx.mkNot(in3Var))); // drin
    input2.toZ3(ctx, solver, count);
    input3.toZ3(ctx, solver, count);
  }

}
