package org;

/**
 * neuer Datentyp für Algo von Integer und Integer
 * 
 * @author Tietjen
 * 
 */
public class PairInteger {
  private int i1;
  private int i2;


  public PairInteger(int i1, int i2) {
    this.i1 = i1;
    this.i2 = i2;
  }

  public int getI1() {
    return i1;
  }

  public void setI1(int i1) {
    this.i1 = i1;
  }

  public int getI2() {
    return i2;
  }

  public void setI2(int i2) {
    this.i2 = i2;
  }

  public boolean equals(Object other) {
    if (other == null || !(other instanceof PairInteger)) {
      return false;
    } else {
      PairInteger otherPair = (PairInteger) other;
      return this.i1 == otherPair.i1 && this.i2 == otherPair.i2;
    }
  }

  public int hashCode() {
    return i1 * 1 << 16 + i2;
  }

}
