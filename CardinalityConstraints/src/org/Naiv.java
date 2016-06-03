package org;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public class Naiv extends Encoding {


  // Iterator gibt die nächste Teilmenge von der Menge 1-n
  public int encode(ArrayList<Literal> literals, int r, int counter, Solver solver, Context ctx)
      throws Z3Exception {

    // binomialkoeffizient n über k
      return nCk(n,k);
    }

    for (LexicographicIterator iter = new LexicographicIterator(n, r + 1); iter.hasNext();) {
      // index ist die nächste Teilmenge
      int[] indexes = iter.next();
      BoolExpr[] clause = new BoolExpr[r + 1];
      for (int i = 0; i < r + 1; i++) {
        // gibt das Literals für den Index aus
        clause[i] = ctx.mkNot(literals.get(indexes[i]).toZ3(ctx));
      }
      solver.add(ctx.mkOr(clause));
    }
  }


  /* Übernommen von org.apache.commons.math3.util.Combinations.java */



  /**
   * Lexicographic combinations iterator.
   * <p>
   * Implementation follows Algorithm T in <i>The Art of Computer Programming</i> Internet Draft
   * (PRE-FASCICLE 3A), "A Draft of Section 7.2.1.3 Generating All Combinations</a>, D. Knuth, 2004.
   * </p>
   * <p>
   * The degenerate cases {@code k == 0} and {@code k == n} are NOT handled by this implementation.
   * If constructor arguments satisfy {@code k == 0} or {@code k >= n}, no exception is generated,
   * but the iterator is empty.
   * </p>
   * 
   */
  private static class LexicographicIterator implements Iterator<int[]> {
    /** Size of subsets returned by the iterator */
    private final int k;

    /**
     * c[1], ..., c[k] stores the next combination; c[k + 1], c[k + 2] are sentinels.
     * <p>
     * Note that c[0] is "wasted" but this makes it a little easier to follow the code.
     * </p>
     */
    private final int[] c;

    /** Return value for {@link #hasNext()} */
    private boolean more = true;

    /** Marker: smallest index such that c[j + 1] > j */
    private int j;

    /**
     * Construct a CombinationIterator to enumerate k-sets from n.
     * <p>
     * NOTE: If {@code k === 0} or {@code k >= n}, the Iterator will be empty (that is,
     * {@link #hasNext()} will return {@code false} immediately.
     * </p>
     * 
     * @param n size of the set from which subsets are enumerated
     * @param k size of the subsets to enumerate
     */
    LexicographicIterator(int n, int k) {
      this.k = k;
      c = new int[k + 3];
      if (k == 0 || k >= n) {
        more = false;
        return;
      }
      // Initialize c to start with lexicographically first k-set
      for (int i = 1; i <= k; i++) {
        c[i] = i - 1;
      }
      // Initialize sentinels
      c[k + 1] = n;
      c[k + 2] = 0;
      j = k; // Set up invariant: j is smallest index such that c[j + 1] > j
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext() {
      return more;
    }

    /**
     * {@inheritDoc}
     */
    public int[] next() {
      if (!more) {
        throw new NoSuchElementException();
      }
      // Copy return value (prepared by last activation)
      final int[] ret = new int[k];
      System.arraycopy(c, 1, ret, 0, k);

      // Prepare next iteration
      // T2 and T6 loop
      int x = 0;
      if (j > 0) {
        x = j;
        c[j] = x;
        j--;
        return ret;
      }
      // T3
      if (c[1] + 1 < c[2]) {
        c[1]++;
        return ret;
      } else {
        j = 2;
      }
      // T4
      boolean stepDone = false;
      while (!stepDone) {
        c[j - 1] = j - 2;
        x = c[j] + 1;
        if (x == c[j + 1]) {
          j++;
        } else {
          stepDone = true;
        }
      }
      // T5
      if (j > k) {
        more = false;
        return ret;
      }
      // T6
      c[j] = x;
      j--;
      return ret;
    }

    /**
     * Not supported.
     */
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  // aus Performanz-Gründen auskommentiert
  // public ArrayList<ArrayList<Literal>> createSubsets(ArrayList<Literal> set, int limit,
  // ArrayList<Literal> toBeAdded) {
  // ArrayList<ArrayList<Literal>> result = new ArrayList<>();
  // // fertig
  // if (set.size() >= limit) {
  // result.add(set);
  // return result;
  // }
  // // In das neue Array wird immer das nächst-höhere Element reingepackt
  // for (int i = 0; i < toBeAdded.size(); i++) {
  // ArrayList<Literal> setCopy = new ArrayList<>(set);
  // setCopy.add(toBeAdded.get(i));
  // ArrayList<Literal> newToBeAdded = new ArrayList<>();
  // for (int j = i + 1; j < toBeAdded.size(); j++) {
  // newToBeAdded.add(toBeAdded.get(j));
  // }
  // result.addAll(createSubsets(setCopy, limit, newToBeAdded));
  // }
  // return result;
  // }

  // @Override
  // // Wandelt das Array in Klauseln für den Z3 um
  // public void encode(ArrayList<Literal> literals, int r, int counter, Solver solver, Context ctx)
  // throws Z3Exception {
  //
  // if (literals.size() == 1) {
  // if (r == 0) {
  // solver.add(ctx.mkNot(literals.get(0).toZ3(ctx)));
  // }
  // return;
  // }
  //
  // ArrayList<ArrayList<Literal>> clauses =
  // createSubsets(new ArrayList<Literal>(), r + 1, literals);
  // for (int i = 0; i < clauses.size(); i++) {
  // BoolExpr[] clause = new BoolExpr[clauses.get(i).size()];
  // for (int j = 0; j < clauses.get(i).size(); j++) {
  // clause[j] = ctx.mkNot(clauses.get(i).get(j).toZ3(ctx));
  // }
  // solver.add(ctx.mkOr(clause));
  // }

  // }
}
