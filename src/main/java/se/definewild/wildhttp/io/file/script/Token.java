package se.definewild.wildhttp.io.file.script;

public class Token {

  public enum Types {
    STRING, CHAR, NUMBER, DEFINITION, BEGIN_ARGS, END_ARGS, BEGIN_BODY, END_BODY, DOT, COMMA, COLON, SEMICOLON, PLUS, MINUS, MUL, DIV, LESS, MORE, AND, OR;
  }

  private Types type;
  private Object data;

  public Token(Types type) {
    this(type, null);
  }

  public Token(Types type, Object data) {
    this.type = type;
    this.data = data;
  }

  public Types getType() {
    return type;
  }

  public Object getData() {
    return data;
  }

  @Override
  public String toString() {
    return "Token [type=" + type + ", data=" + data + "]";
  }

}
