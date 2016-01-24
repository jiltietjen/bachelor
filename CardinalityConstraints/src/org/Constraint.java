package org;

import java.util.ArrayList;

public class Constraint {
  public enum Type {
    EQUALS, SMALLEREQUALS, GREATEREQUALS
  }

  private ArrayList<Variable> variables = new ArrayList<>();
  private Type type;

  public Constraint(Type type, ArrayList<Variable> variables) {
    this.type = type;
    this.variables = variables;
  }

  public ArrayList<Variable> getVariables() {
    return variables;
  }

  public void setVariables(ArrayList<Variable> variables) {
    this.variables = variables;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }


}
