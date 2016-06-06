package org;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

/**
 * Kodierung Sorting Networks mit Odd-Even-Mergesort
 * 
 * @author Tietjen
 * 
 */
public class NetworksOddEvenMergesort extends Encoding {

  // Comparators aufrufen und die inputs zu den comparators sortieren
  // legt fest, in welcher Reihe welcher Comparator ist
  private ArrayList<NetworkComparator> makeSortingNetwork(Context ctx, ArrayList<BoolExpr> inputs,
      ArrayList<BoolExpr> outputs, int counter, Solver solver) throws Z3Exception {
    ArrayList<NetworkComparator> result = new ArrayList<>();
    int[] countComp = new int[inputs.size()];
    result.addAll(oddEvenMergeSort(0, inputs.size(), countComp, ctx, counter));
    for (int i = 0; i < inputs.size(); i++) {
      solver.add(ctx.mkOr(ctx.mkNot(inputs.get(i)),
          ctx.mkBoolConst("b_" + 0 + "_" + (i) + "_NetworksMerge_" + counter)));
      solver.add(ctx.mkOr(inputs.get(i),
          ctx.mkNot(ctx.mkBoolConst("b_" + 0 + "_" + (i) + "_NetworksMerge_" + counter))));
      solver.add(ctx.mkOr(ctx.mkNot(outputs.get(i)),
          ctx.mkBoolConst("b_" + countComp[i] + "_" + i + "_NetworksMerge_" + counter)));
      solver.add(ctx.mkOr(outputs.get(i),
          ctx.mkNot(ctx.mkBoolConst("b_" + countComp[i] + "_" + i + "_NetworksMerge_" + counter))));
    }
    return result;
  }

  // http://www.iti.fh-flensburg.de/lang/algorithmen/sortieren/networks/oemen.htm
  // https://en.wikipedia.org/wiki/Batcher_odd%E2%80%93even_mergesort
  private ArrayList<NetworkComparator> oddEvenMergeSort(int lo, int n, int[] countComp,
      Context ctx, int counter) throws Z3Exception {
    ArrayList<NetworkComparator> result = new ArrayList<>();
    if (n > 1) {
      int m = n / 2;
      result.addAll(oddEvenMergeSort(lo, m, countComp, ctx, counter));
      result.addAll(oddEvenMergeSort(lo + m, m, countComp, ctx, counter));
      result.addAll(oddEvenMerge(lo, n, 1, counter, ctx, countComp));
    }
    return result;
  }

  /**
   * lo is the starting position and n is the length of the piece to be merged, r is the distance of
   * the elements to be compared
   * 
   * @throws Z3Exception
   */
  private ArrayList<NetworkComparator> oddEvenMerge(int lo, int n, int r, int counter, Context ctx,
      int[] countComp) throws Z3Exception {
    ArrayList<NetworkComparator> result = new ArrayList<>();
    int m = r * 2;
    if (m < n) {
      result.addAll(oddEvenMerge(lo, n, m, counter, ctx, countComp)); // even subsequence
      result.addAll(oddEvenMerge(lo + r, n, m, counter, ctx, countComp)); // odd subsequence
      for (int i = lo + r; i + r < lo + n; i += m) {

        BoolExpr input1 =
            ctx.mkBoolConst("b_" + countComp[i] + "_" + (i) + "_NetworksMerge_" + counter);
        BoolExpr input2 =
            ctx.mkBoolConst("b_" + countComp[i + r] + "_" + (i + r) + "_NetworksMerge_" + counter);
        countComp[i] += 1;
        countComp[i + r] += 1;
        BoolExpr output1 =
            ctx.mkBoolConst("b_" + countComp[i] + "_" + (i) + "_NetworksMerge_" + counter);
        BoolExpr output2 =
            ctx.mkBoolConst("b_" + countComp[i + r] + "_" + (i + r) + "_NetworksMerge_" + counter);


        result.add(new NetworkComparator(input1, output1, input2, output2));
      }
    } else {
      BoolExpr input1 =
          ctx.mkBoolConst("b_" + countComp[lo] + "_" + (lo) + "_NetworksMerge_" + counter);
      BoolExpr input2 =
          ctx.mkBoolConst("b_" + countComp[lo + r] + "_" + (lo + r) + "_NetworksMerge_" + counter);
      countComp[lo] += 1;
      countComp[lo + r] += 1;
      BoolExpr output1 =
          ctx.mkBoolConst("b_" + countComp[lo] + "_" + (lo) + "_NetworksMerge_" + counter);
      BoolExpr output2 =
          ctx.mkBoolConst("b_" + countComp[lo + r] + "_" + (lo + r) + "_NetworksMerge_" + counter);

      result.add(new NetworkComparator(input1, output1, input2, output2));
    }
    return result;
  }

  private static int power2(int m) {
    return 1 << m;
  }

  // braucht man für log(m), rundet auf
  private static int log2(int n) {
    if (n <= 0)
      throw new IllegalArgumentException();
    int log = 31 - Integer.numberOfLeadingZeros(n);
    if (power2(log) == n) {
      return log;
    } else {
      return log + 1;
    }
  }

  // Inputs sind nur 2er Potenzen. Nullen, um es auf die richtige Größe zu bekommen.
  public void encode(ArrayList<Literal> literals, int r, int counter, Solver solver, Context ctx)
      throws Z3Exception {
    ArrayList<BoolExpr> inputs = new ArrayList<>();
    int nextPowerOf2 = power2(log2(literals.size()));
    for (int i = 0; i < nextPowerOf2; i++) {
      if (i < literals.size()) {
        inputs.add(literals.get(i).toZ3(ctx));
      } else {
        inputs.add(ctx.mkBoolConst("FillingZero"));
      }
    }
    solver.add(ctx.mkNot(ctx.mkBoolConst("FillingZero")));
    ArrayList<BoolExpr> outputs = new ArrayList<>();
    for (int i = 0; i < nextPowerOf2; i++) {
      outputs.add(ctx.mkBoolConst("b_output_" + i + "_NetworksMerge_" + counter));
    }


    ArrayList<NetworkComparator> comparators =
        makeSortingNetwork(ctx, inputs, outputs, counter, solver);
    for (int i = 0; i < comparators.size(); i++) {
      comparators.get(i).toZ3(ctx, solver);
    }
    solver.add(ctx.mkNot(ctx.mkBoolConst("b_output_" + r + "_NetworksMerge_" + counter)));
  }
}
