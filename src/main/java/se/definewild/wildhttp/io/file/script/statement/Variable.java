package se.definewild.wildhttp.io.file.script.statement;

import java.text.ParseException;
import java.util.HashMap;

public class Variable {

  public enum VariableType {
    CHAR, FUNCTION, NUMBER, STRING, VARIABLE, VOID;
    public static VariableType Get(String type) {
      for (final VariableType t : values())
        if (t.name().equalsIgnoreCase(type))
          return t;
      return null;
    }

    public static VariableType GetFunction(String type) {
      final VariableType tmp = FUNCTION;
      tmp.varType = VariableType.Get(type);
      return tmp;
    }

    public static VariableType GetVariable(String type) {
      final VariableType tmp = VARIABLE;
      tmp.varType = VariableType.Get(type);
      return tmp;
    }

    private VariableType varType = null;

    public VariableType getVarType() {
      return varType;
    }

  }

  private static HashMap<VariableType, Class<?>> valid = new HashMap<>();
  static {
    valid.put(VariableType.VOID, Void.class);
    valid.put(VariableType.STRING, String.class);
    valid.put(VariableType.CHAR, Character.class);
    valid.put(VariableType.NUMBER, Double.class);
    valid.put(VariableType.VARIABLE, String.class);
    valid.put(VariableType.FUNCTION, Function.class);

  }
  private final Object data;
  private boolean isFunction;
  private final String name;

  private final VariableType type;

  public Variable(String name, VariableType type, Object data)
      throws ParseException {
    this.name = name;
    this.type = type;
    this.data = data;
    if (type == VariableType.VOID && data != null)
      throw new ParseException("The type 'VOID' can't have a value", -1);
    if (type == VariableType.FUNCTION)
      isFunction = true;

    if (!data.getClass().equals(valid.get(type)))
      throw new ParseException("The type '" + type
          + "' must have a value of the type '" + type + "' not '"
          + data.getClass().getName() + "'", -1);
  }

  public Object getData() {
    return data;
  }

  public String getName() {
    return name;
  }

  public VariableType getType() {
    return type;
  }

  public boolean IsFunction() {
    return isFunction;
  }
}
