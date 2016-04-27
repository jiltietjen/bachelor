package org;

import java.util.ArrayList;
import java.util.Random;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

public class synthetischeTestfaelle {

  static int sat = 0;
  static int unsat = 0;

  public static void main(String[] args) throws Z3Exception {
    for (int i = 0; i <= 100; i++) {
      solve(2000);
    }
    System.out.println(sat + " " + unsat);
  }

  private static Random randomGenerator = new Random();

  public static void solve(int n) throws Z3Exception {

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

    Encoding encoding = new NetworksOddEvenMergesort();
    for (Constraint c : filledVariables) {
      System.out.println(c);
    }
    for (int i = 0; i < filledVariables.size(); i++) {
      encoding.encode(filledVariables.get(i).getVariables(), filledVariables.get(i).getLimitR(), i,
          solver, ctx);
    }

    // System.out.println(solver);

    if (solver.check() == Status.UNSATISFIABLE) { // TODO Timeout abfangen
      System.out.println("UNSAT");
      unsat++;
    } else {
      System.out.println("SAT");
      sat++;
    }
    // TODO memory aufräumen
    solver.reset();
    ctx.dispose();
  }
}
