package se.definewild.wildhttp.io.file.script.statement;

import java.awt.Window.Type;
import java.io.EOFException;
import java.text.ParseException;
import java.util.Vector;

import se.definewild.wildhttp.io.file.script.statement.Statement.StatementType;
import se.definewild.wildhttp.io.file.script.statement.Variable.VariableType;
import se.definewild.wildhttp.io.file.script.token.Token;
import se.definewild.wildhttp.io.file.script.token.Token.TokenTypes;

public class Statementizer {

  private Scope currentScope;
  private int firstIndex;
  private int index;
  private final Vector<Statement> statements;
  private Token t;
  private Vector<Token> tokens;

  public Statementizer(Vector<Token> tokens) throws ParseException {
    statements = new Vector<>();
    index = -1;
    makeStatements(tokens);
  }

  private void add(Token token, StatementType type, Object data) {
    currentScope.Add(new Statement(token, type, data));
  }

  private void Back() throws EOFException {
    index--;
    if (index < 0)
      throw new EOFException();

    t = tokens.get(index);
  }

  public Vector<Statement> getStatements() {
    return statements;
  }

  private void makeStatements(Vector<Token> tokens) throws ParseException {
    this.tokens = tokens;
    currentScope = new Scope(null);
    Object tmp;
    String type;
    String name;
    try {
      NextSave();
      while (true) {
        if (t.getType() == TokenTypes.DEFINITION) {
          tmp = t.getData();
          Next();
          if (t.getType() == TokenTypes.DEFINITION) {
            type = (String) tmp;
            name = (String) t.getData();
            if (t.getType() == TokenTypes.SEMICOLON)
              add(t, StatementType.VarDef,
                  new Variable(name, VariableType.Get(type), t.getData()));
            else if (t.getType() == TokenTypes.EQUALS) {
              Next();
              add(t, StatementType.VarDef, parseValue(type, name));
            }
          } else if (t.getType() == TokenTypes.EQUALS)
            Next();
        } else
          throw new ParseException("Expected token got '" + t.getType()
              + "' on line: " + index + ". Started on line: " + firstIndex
              + ".", -1);
        NextSave();
      }
    } catch (final EOFException e) {
      throw new ParseException("Expected token got EOF on line: " + index
          + ". Started on line: " + firstIndex + ".", -1);
    } catch (final Exception e) {
      throw e;
    }
  }

  private void Next() throws EOFException {
    index++;
    if (index >= tokens.size())
      throw new EOFException();

    t = tokens.get(index);
  }

  private void NextSave() throws EOFException {
    Next();
    firstIndex = index;
  }

  private Function parseFunction(String type, String name)
      throws ParseException, EOFException {
    final Token tf = t;
    final Function func = new Function(name, VariableType.Get(type),
        currentScope);
    Next();
    if (t.getType() != TokenTypes.BEGIN_ARGS)
      throw new ParseException("Expected 'BEGIN_ARGS' got '" + t.getType()
          + "' on line: " + index + ". Started on line: " + firstIndex + ".",
          -1);
    Next();

    if (t.getType() != TokenTypes.DEFINITION)
      throw new ParseException("Expected 'DEFINITION' got '" + t.getType()
          + "' on line: " + index + ". Started on line: " + firstIndex + ".",
          -1);
    do {
      if (t.getType() != TokenTypes.DEFINITION)
        throw new ParseException("Expected 'DEFINITION' got '" + t.getType()
            + "' on line: " + index + ". Started on line: " + firstIndex + ".",
            -1);
      type = (String) t.getData();
      Next();
      if (t.getType() != TokenTypes.DEFINITION)
        throw new ParseException("Expected 'DEFINITION' got '" + t.getType()
            + "' on line: " + index + ". Started on line: " + firstIndex + ".",
            -1);
      name = (String) t.getData();
      Next();

      if (t.getType() == TokenTypes.EQUALS)
        Next();

      func.AddArgument(new Variable(name, VariableType.Get(type), null));
    } while (t.getType() == TokenTypes.COLON);

    if (t.getType() != TokenTypes.END_ARGS)
      throw new ParseException("Expected 'END_ARGS' got '" + t.getType()
          + "' on line: " + index + ". Started on line: " + firstIndex + ".",
          -1);
    Next();

    if (t.getType() != TokenTypes.BEGIN_BODY)
      throw new ParseException("Expected 'BEGIN_BODY' got '" + t.getType()
          + "' on line: " + index + ". Started on line: " + firstIndex + ".",
          -1);

    // Body
    Next();
    if (t.getType() != TokenTypes.END_BODY)
      throw new ParseException("Expected 'END_BODY' got '" + t.getType()
          + "' on line: " + index + ". Started on line: " + firstIndex + ".",
          -1);

    add(tf, StatementType.VarDef,
        new Variable(name, VariableType.GetFunction(type), func));
    return func;
  }

  private Variable parseValue(String type, String name) throws ParseException {
    if (t.getType() == TokenTypes.NUMBER)
      return new Variable(name, VariableType.Get(type), t.getData());
    else if (t.getType() == TokenTypes.CHAR)
      return new Variable(name, VariableType.Get(type), t.getData());
    else if (t.getType() == TokenTypes.STRING)
      return new Variable(name, VariableType.Get(type), t.getData());
    else if (t.getType() == TokenTypes.DEFINITION)
      return new Variable(name, VariableType.GetVariable(type), t.getData());
    else
      throw new ParseException("Unknown value: " + t.getType(), -1);
  }

}
