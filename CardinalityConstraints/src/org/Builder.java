package org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.Constraint.Type;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

public class Builder {

  protected static final Object creation_lock = new Object();

  public ArrayList<Literal> solve(ArrayList<Constraint> constraints, int numVariables,
      Encoding encoding) throws Z3Exception {
    TestBench.file("starting preprocessing");
    TestBench.measureTime();
    ArrayList<Constraint> leqConstraints = new ArrayList<>();
    for (int i = 0; i < constraints.size(); i++) {
      Constraint constraint = constraints.get(i);
      switch (constraint.getType()) {
        case EQUALS:
          ArrayList<Literal> literals = new ArrayList<>();
          for (Literal l : constraint.getVariables()) {
            literals.add(new Literal(l));
          }
          Constraint newConstraint =
              new Constraint(Type.SMALLEREQUALS, literals, constraint.getLimitR());
          leqConstraints.add(newConstraint);
          // kein Break, da greaterequals und smallerequals benutzt werden sollen
        case GREATEREQUALS:
          Constraint leqConstraint =
              new Constraint(Type.SMALLEREQUALS, new ArrayList<Literal>(), constraint
                  .getVariables().size() - constraint.getLimitR());
          for (int j = 0; j < constraint.getVariables().size(); j++) {
            leqConstraint.getVariables().add(constraint.getVariables().get(j));
            leqConstraint.getVariables().get(j)
                .setPositive(!leqConstraint.getVariables().get(j).isPositive());
          }
          leqConstraints.add(leqConstraint);
          break;
        case SMALLEREQUALS:
          leqConstraints.add(constraint);
          break;
      }
    }
    Map<String, String> params = new HashMap<>();
    params.put("timeout", Integer.toString(TestBench.TIMEOUT));
    Context ctx = new Context(params);
    Solver solver = ctx.mkSolver("QF_LIA");
    // Params p = ctx.mkParams();
    // p.add("T:TIMEOUT", 2000);
    // System.out.println(p);
    // solver.setParameters(p);
    for (int i = 0; i < leqConstraints.size(); i++) {
      if (leqConstraints.get(i).getVariables().size() > leqConstraints.get(i).getLimitR()) {
        encoding.encode(leqConstraints.get(i).getVariables(), leqConstraints.get(i).getLimitR(), i,
            solver, ctx);
      }
    }

    ArrayList<Literal> modelLiterals = null;
    TestBench.file("preprocessing done. Starting the solver.");
    TestBench.measureTime();
    TestBench.file("Number of clauses: " + solver.getNumAssertions());
    // long time = System.currentTimeMillis();
    Status stat = solver.check();

    solver.dispose();
    ctx.dispose();
    System.gc();

    TestBench.file(stat.toString());
    TestBench.measureTime();
    // if (stat == Status.UNSATISFIABLE) { // TODO Timeout abfangen
    // TestBench.file("UNSAT");
    // TestBench.measureTime();
    // } else {
    // System.out.println(System.currentTimeMillis() - time);
    // TestBench.file("SAT");
    // TestBench.measureTime();
    // Model m = solver.getModel();
    // modelLiterals = new ArrayList<>();
    //
    // // Ermöglicht das Übergeben der x-Werte für die GUI
    // for (int i = 0; i < numVariables; i++) {
    // // Retrieves the interpretation (the assignment) of x in the model
    // Expr val = m.getConstInterp(ctx.mkBoolConst("x_" + i));
    // if (val.isTrue()) {
    // modelLiterals.add(new Literal(i, true));
    // } else if (val.isFalse()) {
    // modelLiterals.add(new Literal(i, false));
    // } else {
    // throw new IllegalArgumentException();
    // }
    // }
    // }
    return modelLiterals;
  }
}
