package org;

import java.util.ArrayList;

public class Constraint {
  public enum Type {
    EQUALS, SMALLEREQUALS, GREATEREQUALS
  }

  private ArrayList<Literal> variables = new ArrayList<>();
  private Type type;
  private int limitR;

  public Constraint(Type type, ArrayList<Literal> variables, int limitR) {
    this.type = type;
    this.variables = variables;
    this.setLimitR(limitR);
  }

  public ArrayList<Literal> getVariables() {
    return variables;
  }

  public void setVariables(ArrayList<Literal> variables) {
    this.variables = variables;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public int getLimitR() {
    return limitR;
  }

  public void setLimitR(int limitR) {
    this.limitR = limitR;
  }

  public String toString() {
    String result = type == Type.SMALLEREQUALS ? "<=" : type == Type.EQUALS ? "==" : ">=";
    result = result + limitR + variables;
    return result;
  }


}
