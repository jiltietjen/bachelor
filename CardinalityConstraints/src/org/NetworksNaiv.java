package org;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Z3Exception;

public class NetworksNaiv extends Encoding {

  // Comporators aufrufen und sortieren
  // http://hoytech.github.io/sorting-networks/ --> zeigt das SortingNetwork mit den Comparators
  private ArrayList<NetworkComparator> makeSortingNetwork(Context ctx, ArrayList<BoolExpr> inputs,
      ArrayList<BoolExpr> outputs, int counter) throws Z3Exception {
    ArrayList<NetworkComparator> result = new ArrayList<>();
    if (inputs.size() == 2) {
      result
          .add(new NetworkComparator(inputs.get(0), outputs.get(0), inputs.get(1), outputs.get(1)));
      return result;
    }
    for (int i = 0; i < inputs.size() - 1; i++) {
      BoolExpr input1;
      if (i == 0) {
        input1 = inputs.get(0);
      } else {
        input1 =
            ctx.mkBoolConst("b_" + inputs.size() + "_" + (2 * i - 1) + "_NetworksNaiv_" + counter);
      }
      BoolExpr input2 = inputs.get(i + 1);
      BoolExpr output1 =
          ctx.mkBoolConst("b_" + inputs.size() + "_" + (2 * i) + "_NetworksNaiv_" + counter);
      BoolExpr output2;
      if (i != inputs.size() - 2) {
        output2 =
            ctx.mkBoolConst("b_" + inputs.size() + "_" + (2 * i + 1) + "_NetworksNaiv_" + counter);
      } else {
        output2 = outputs.get(outputs.size() - 1);
      }
      result.add(new NetworkComparator(input1, output1, input2, output2));
    }
    ArrayList<BoolExpr> nextInputs = new ArrayList<>();
    for (int i = 0; i < inputs.size() - 1; i++) {
      nextInputs.add(ctx.mkBoolConst("b_" + inputs.size() + "_" + (2 * i) + "_NetworksNaiv_"
          + counter));
    }
    ArrayList<BoolExpr> nextOutputs = new ArrayList<>();
    for (int i = 0; i < outputs.size() - 1; i++) {
      nextOutputs.add(outputs.get(i));
    }
    result.addAll(makeSortingNetwork(ctx, nextInputs, nextOutputs, counter));
    return result;
  }


  public void encode(ArrayList<Literal> literals, int r, int counter, Solver solver, Context ctx)
      throws Z3Exception {
    ArrayList<BoolExpr> inputs = new ArrayList<>();
    for (int i = 0; i < literals.size(); i++) {
      inputs.add(literals.get(i).toZ3(ctx));
    }
    ArrayList<BoolExpr> outputs = new ArrayList<>();
    for (int i = 0; i < literals.size(); i++) {
      outputs
          .add(ctx.mkBoolConst("b_" + (inputs.size() + 1) + "_" + i + "_NetworksNaiv_" + counter));
    }
    ArrayList<NetworkComparator> comparators = makeSortingNetwork(ctx, inputs, outputs, counter);
    System.out.println(comparators);
    for (int i = 0; i < comparators.size(); i++) {
      comparators.get(i).toZ3(ctx, solver);
    }
    solver.add(ctx.mkNot(ctx.mkBoolConst("b_" + (inputs.size() + 1) + "_" + r + "_NetworksNaiv_"
        + counter)));
  }
}
