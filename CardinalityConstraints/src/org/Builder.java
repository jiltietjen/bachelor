package org;

import java.util.ArrayList;

import org.Constraint.Type;

import com.microsoft.z3.Context;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

public class Builder {

  public ArrayList<Literal> solve(ArrayList<Constraint> constraints, int numVariables)
      throws Z3Exception {
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
              new Constraint(Type.SMALLEREQUALS, new ArrayList<Literal>(), numVariables
                  - constraint.getLimitR());
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
    Context ctx = new Context();
    Solver solver = ctx.mkSolver("QF_LIA");
    Encoding encoding = new Naiv();
    for (Constraint c : leqConstraints) {
      System.out.println(c);
    }
    for (int i = 0; i < leqConstraints.size(); i++) {
      encoding.encode(leqConstraints.get(i).getVariables(), leqConstraints.get(i).getLimitR(), i,
          solver, ctx);
    }

    System.out.println(solver);
    if (solver.check() == Status.UNSATISFIABLE) { // TODO Timeout abfangen
      System.out.println("UNSAT");
    } else {
      System.out.println("SAT");
      Model m = solver.getModel();
      System.out.println("Model is" + m); // TODO Model zurückübersetzen in k Damen Problem
    }
    solver.dispose();
    return null;
  }

}
