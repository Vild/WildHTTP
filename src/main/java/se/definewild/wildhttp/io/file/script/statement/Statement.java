package se.definewild.wildhttp.io.file.script.statement;

import se.definewild.wildhttp.io.file.script.token.Token;

public class Statement {

  public enum StatementType {
    VarDef, VarSet;
  }

  private final Object data;
  private final Token token;
  private final StatementType type;

  public Statement(Token token, StatementType type, Object data) {
    this.token = token;
    this.type = type;
    this.data = data;
  }

  public Object getData() {
    return data;
  }

  public Token getToken() {
    return token;
  }

  public StatementType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Statement [type=" + type + ", data=" + data + "]";
  }

}
