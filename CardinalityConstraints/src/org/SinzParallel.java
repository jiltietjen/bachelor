package org;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public class SinzParallel extends Encoding {

  /* Sinz parallel --------------------------------------------------------------------- */
  private ArrayList<BoolExpr> makeCounter(Context ctx, Solver solver, int counter,
      int circuitCounter, ArrayList<BoolExpr> literals) throws Z3Exception {
    int size = literals.size();
    // Sonderfall, wenn eine Hälfte 0 oder 1 Eingänge hat, gibt es keine Änderung
    if (size <= 1) {
      return literals;
    }

    // m aus dem Paper
    // Paper [log n]
    int m = log2(size);
    // power2(m) = 2 ^m, Indizies 0 - m-1 wegen der Liste
    ArrayList<BoolExpr> firstHalf = new ArrayList<>(literals.subList(0, power2(m) - 1));
    // 2. Hälfte 2^m - 1
    ArrayList<BoolExpr> secondHalf =
        new ArrayList<>(literals.subList(power2(m) - 1, literals.size() - 1));
    System.out.println(counter + " m " + m + " m^2 " + power2(m) + " firstHalf " + firstHalf
        + " SecondHalf " + secondHalf);
    // Hälften in die Counter (rekursiv bis keine Subcounter mehr gebildet werden können)
    ArrayList<BoolExpr> firstOutputs =
    // CircuitCounter ist Zähler für die circuits
        makeCounter(ctx, solver, counter, 2 * circuitCounter, firstHalf);
    ArrayList<BoolExpr> secondOutputs =
        makeCounter(ctx, solver, counter, 2 * circuitCounter + 1, secondHalf);
    ArrayList<BoolExpr> result = new ArrayList<>();
    int i = 0;
    BoolExpr carryIn = literals.get(literals.size() - 1);
    // Neue BoolExpr für die Ausgänge der Addierer
    for (; i < secondOutputs.size(); i++) {
      BoolExpr sum = ctx.mkBoolConst("s_SinzPar_" + i + "_" + circuitCounter + "_" + counter);
      BoolExpr carry = ctx.mkBoolConst("c_SinzPar_" + i + "_" + circuitCounter + "_" + counter);
      result.add(sum);
      // dann in die Voll-Addierer für 2. Hälfte
      makeFullAdder(solver, ctx, firstOutputs.get(i), secondOutputs.get(i), carryIn, carry, sum);
      carryIn = carry;
    }
    for (; i < firstOutputs.size(); i++) {
      BoolExpr sum = ctx.mkBoolConst("s_SinzPar_" + i + "_" + circuitCounter + "_" + counter);
      BoolExpr carry = ctx.mkBoolConst("c_SinzPar_" + i + "_" + circuitCounter + "_" + counter);
      result.add(sum);
      // Halbaddierer für erste Hälfte
      makeHalfAdder(solver, ctx, firstOutputs.get(i), carryIn, carry, sum);
      carryIn = carry;
    }
    // Letztes Carry links vom Bild
    result.add(carryIn);
    return result;
  }

  // Adder aus dem Paper
  private void makeHalfAdder(Solver solver, Context ctx, BoolExpr aIn, BoolExpr carryIn,
      BoolExpr carry, BoolExpr sum) throws Z3Exception {
    solver.add(ctx.mkOr(aIn, ctx.mkNot(carryIn), sum));
    solver.add(ctx.mkOr(ctx.mkNot(aIn), carryIn, sum));
    solver.add(ctx.mkOr(ctx.mkNot(aIn), ctx.mkNot(carryIn), carry));

    // Evtl Verbesserung der Performanz durch häufigere Unit-Propagation. Nochmal mit richtiger
    // Benchmark testen.
    // solver.add(ctx.mkOr(ctx.mkNot(aIn), carryIn, sum));
  }

  // Adder aus dem Paper
  private void makeFullAdder(Solver solver, Context ctx, BoolExpr aIn, BoolExpr bIn,
      BoolExpr carryIn, BoolExpr carry, BoolExpr sum) throws Z3Exception {
    solver.add(ctx.mkOr(aIn, bIn, ctx.mkNot(carryIn), sum));
    solver.add(ctx.mkOr(aIn, ctx.mkNot(bIn), carryIn, sum));
    solver.add(ctx.mkOr(ctx.mkNot(aIn), bIn, carryIn, sum));
    solver.add(ctx.mkOr(ctx.mkNot(aIn), ctx.mkNot(bIn), ctx.mkNot(carryIn), sum));
    solver.add(ctx.mkOr(ctx.mkNot(aIn), ctx.mkNot(bIn), carry));
    solver.add(ctx.mkOr(ctx.mkNot(aIn), ctx.mkNot(carryIn), carry));
    solver.add(ctx.mkOr(ctx.mkNot(bIn), ctx.mkNot(carryIn), carry));

    // Evtl Verbesserung der Performanz durch häufigere Unit-Propagation. Nochmal mit richtiger
    // Benchmark testen.
    // solver.add(ctx.mkOr(ctx.mkNot(aIn), carry, sum));

  }

  // 2^m Linksshifter für die 2-er potenzen (bsp 2^0=1, 2^1=2...)
  private static int power2(int m) {
    return 1 << m;
  }

  // braucht man für log(m)
  private static int log2(int n) {
    if (n <= 0)
      throw new IllegalArgumentException();
    return 31 - Integer.numberOfLeadingZeros(n);
  }

  // Vergleich mit r
  private void makeComparator(Context ctx, Solver solver, int r, ArrayList<BoolExpr> expressions)
      throws Z3Exception {
    ArrayList<BoolExpr> result = new ArrayList<>();
    for (int i = 0; i < expressions.size(); i++) {
      // bitweises Und für das letzte Bit, damit das letzte Bit isoliert ist
      int lowestBit = r & 1;
      // Erstellt Literal
      if (lowestBit == 0) {
        result.add(ctx.mkNot(expressions.get(i)));
      } else {
        // Hinzufügen des Literals zu allen Klauseln
        for (int j = 0; j < result.size(); j++) {
          result.set(j, ctx.mkOr(result.get(j), ctx.mkNot(expressions.get(i))));
        }
      }
      r = r >>> 1;
    }
    for (int i = 0; i < result.size(); i++) {
      solver.add(result.get(i));
    }
  }

  @Override
  public void encode(ArrayList<Literal> literals, int r, int counter, Solver solver, Context ctx)
      throws Z3Exception {
    ArrayList<BoolExpr> expressions = new ArrayList<>();
    for (int i = 0; i < literals.size(); i++) {
      // Macht Literals zu BoolExpr
      expressions.add(literals.get(i).toZ3(ctx));
    }
    ArrayList<BoolExpr> outputs = makeCounter(ctx, solver, counter, 1, expressions);
    makeComparator(ctx, solver, r, outputs);
  }

  /* Sinz parallel Ende ------------------------------------------------------------------ */

}
