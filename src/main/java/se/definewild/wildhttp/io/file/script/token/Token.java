package se.definewild.wildhttp.io.file.script.token;

public class Token {

  public enum TokenTypes {
    AND, BEGIN_ARGS, BEGIN_BODY, CHAR, COLON, COMMA, DEFINITION, DIV, DOT, END_ARGS, END_BODY, EQUALS, HASHTAG, LESS, MINUS, MOD, MORE, MUL, NUMBER, OR, PLUS, SEMICOLON, STRING, TILDA, XOR;
  }

  private final Object data;
  private final TokenTypes type;
  private final int xFirstIndex;
  private final int xIndex;
  private final int yFirstIndex;
  private final int yIndex;

  public Token(TokenTypes type, int xIndex, int yIndex, int xFirstIndex,
      int yFirstIndex) {
    this(type, xIndex, yIndex, xFirstIndex, yFirstIndex, null);
  }

  public Token(TokenTypes type, int xIndex, int yIndex, int xFirstIndex,
      int yFirstIndex, Object data) {
    this.type = type;
    this.data = data;
    this.xIndex = xIndex;
    this.yIndex = yIndex;
    this.xFirstIndex = xFirstIndex;
    this.yFirstIndex = yFirstIndex;
  }

  public Object getData() {
    return data;
  }

  public TokenTypes getType() {
    return type;
  }

  public int getXFirstIndex() {
    return xFirstIndex;
  }

  public int getXIndex() {
    return xIndex;
  }

  public int getYFirstIndex() {
    return yFirstIndex;
  }

  public int getYIndex() {
    return yIndex;
  }

  @Override
  public String toString() {
    return "Token [type=" + type + ", data=" + data + ", xIndex=" + xIndex
        + ", yIndex=" + yIndex + ", xFirstIndex=" + xFirstIndex
        + ", yFirstIndex=" + yFirstIndex + "]";
  }

}
