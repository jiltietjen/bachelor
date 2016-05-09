package org;

import java.util.ArrayList;
import java.util.Random;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

public class SynthetischeTestfaelle {

  public static void main(String[] args) throws Z3Exception {
    for (int i = 0; i <= 100; i++) {
      solve(2000, new KnuthBailleux());
    }
  }

  private static Random randomGenerator = new Random();

  public static void solve(int n, Encoding encoding) throws Z3Exception {

    Context ctx = new Context();
    Solver solver = ctx.mkSolver("QF_LIA");
    ArrayList<Literal> lits = new ArrayList<>();
    ArrayList<Constraint> filledVariables = new ArrayList<>();

    for (int i = 0; i <= n; i++) {
      lits.add(new Literal(i, true));
    }
    for (int i = 0; i < n / 10; i++) {
      int constraintSize = (int) (randomGenerator.nextInt((int) Math.sqrt(n)) + 0.5 * Math.sqrt(n));
      ArrayList<Literal> litsCopy = new ArrayList<>(lits);
      while (litsCopy.size() > constraintSize) {
        litsCopy.remove(randomGenerator.nextInt(litsCopy.size()));
      }
      filledVariables.add(new Constraint(Constraint.Type.SMALLEREQUALS, litsCopy, randomGenerator
          .nextInt(litsCopy.size() + 1)));
    }

    for (int j = 0; j < lits.size(); j++) {
      if (j % 100 == 0) {
        solver.add(lits.get(j).toZ3(ctx));
      }
    }


    for (int i = 0; i < filledVariables.size(); i++) {
      encoding.encode(filledVariables.get(i).getVariables(), filledVariables.get(i).getLimitR(), i,
          solver, ctx);
    }

    // System.out.println(solver);

    if (solver.check() == Status.UNSATISFIABLE) { // TODO Timeout abfangen
      TestBench.file("UNSAT");
    } else {
      TestBench.file("SAT");
    }
    // TODO memory aufräumen
    solver.reset();
    ctx.dispose();
  }
}
