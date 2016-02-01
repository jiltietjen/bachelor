package org;

import java.util.ArrayList;

import org.junit.Test;

public class NaivTest {

  @Test
  public void test() {
    ArrayList<Literal> literals = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      literals.add(new Literal(i, true));
    }
    Naiv naiv = new Naiv();
    ArrayList<ArrayList<Literal>> result =
        naiv.createSubsets(new ArrayList<Literal>(), 3, literals);
    System.out.println(result);
    System.out.println(result.size());
  }
}
