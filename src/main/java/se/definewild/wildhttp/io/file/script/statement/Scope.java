package se.definewild.wildhttp.io.file.script.statement;

import java.util.Vector;

public class Scope {

  private final Scope parent;
  private final Vector<Statement> statements;

  public Scope(Scope parent) {
    this.parent = parent;
    this.statements = new Vector<>();
  }

  public void Add(Statement statement) {
    statements.add(statement);
  }

  public Scope getParent() {
    return parent;
  }

  public Vector<Statement> getStatements() {
    return statements;
  }

}
