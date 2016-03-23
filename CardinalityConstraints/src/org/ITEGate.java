package org;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;


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
    public void toZ3(Context ctx, Solver solver, int counter) throws Z3Exception {

        String tVar = "b" + tseitinVar + "_Niklasse_" + counter;
        String in3Var = "b" + input3.getTseitinVar() + "_Niklasse_" + counter;
        String in2Var = "b" + input2.getTseitinVar() + "_Niklasse_" + counter;

        solver.add(ctx.mkOr(
                ctx.mkNot(ctx.mkBoolConst(tVar)),
                input1.toZ3(ctx),
                ctx.mkBoolConst(in3Var))
        );
        solver.add(ctx.mkOr(ctx.mkNot(ctx.mkBoolConst(tVar)), ctx.mkNot(input1.toZ3(ctx)), ctx.mkBoolConst(in2Var)));
        solver.add(
                ctx.mkOr(ctx.mkNot(ctx.mkBoolConst(tVar)),
                        ctx.mkBoolConst(in2Var)),
                ctx.mkBoolConst(in3Var));
        solver.add(ctx.mkOr(ctx.mkBoolConst(tVar),
                ctx.mkNot(input1.toZ3(ctx)),
                ctx.mkNot(ctx.mkBoolConst(in2Var))));
        solver.add(ctx.mkOr(ctx.mkBoolConst(tVar),
                input1.toZ3(ctx),
                ctx.mkNot(ctx.mkBoolConst(in3Var))));
        input2.toZ3(ctx, solver, counter);
        input3.toZ3(ctx, solver, counter);
        // TODO packt den Knoten zu oft in den Solver
    }

}
