package se.definewild.wildhttp.io.file.script.statement;

import java.util.ArrayList;

import se.definewild.wildhttp.io.file.script.statement.Variable.VariableType;

public class Function {

  private final ArrayList<Variable> arguments;
  private final String name;
  private VariableType type;
  private final Scope scope;

  public Function(String name, VariableType type, Scope parent) {
    this.arguments = new ArrayList<>();
    this.name = name;
    this.type = type;
    this.scope = new Scope(parent);
  }

  public void Add(Statement statement) {
    scope.Add(statement);
  }

  public void AddArgument(Variable variable) {
    arguments.add(variable);
  }

  public ArrayList<Variable> getArguments() {
    return arguments;
  }

  public String getName() {
    return name;
  }

  public VariableType getType() {
    return type;
  }

  public Scope getScope() {
    return scope;
  }

}
