package se.definewild.wildhttp.io.file.script.token;

import java.io.EOFException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import se.definewild.wildhttp.io.file.script.token.Token.TokenTypes;

public class Tokenizer {

  private char c;

  private String[] data;

  private final Vector<Token> tokens;
  private int xFirstIndex;

  private int xIndex;
  private int yFirstIndex;
  private int yIndex;

  public Tokenizer(String data) throws ParseException {
    tokens = new Vector<>();
    xIndex = -1;
    yIndex = 0;
    xFirstIndex = 0;
    yFirstIndex = 0;
    makeTokens(data);
  }

  private void add(TokenTypes type) {
    add(type, null);
  }

  private void add(TokenTypes type, Object data) {
    tokens.add(new Token(type, xIndex, yIndex, xFirstIndex, yFirstIndex, data));
  }

  private void Back() throws EOFException {
    xIndex--;
    while (true)
      if (xIndex < 0) {
        yIndex--;
        if (yIndex < 0)
          throw new EOFException();
        xIndex = data[yIndex].length() - 1;
      } else
        break;
    c = data[yIndex].charAt(xIndex);
  }

  private void Expect(char ch) throws EOFException, ParseException {
    Next();
    if (c != ch)
      throw new ParseException("Expected token '" + ch + "' got '" + c
          + "' on line: " + yIndex + ":" + xIndex + ". Started on line: "
          + yFirstIndex + ":" + xFirstIndex + ".", -1);
  }

  public Vector<Token> getTokens() {
    return tokens;
  }

  private void makeTokens(String d) throws ParseException {
    data = d.split("\\r?\\n");
    String tmpstr;
    final ArrayList<Character> NON_VALID_CHAR = new ArrayList<>(Arrays.asList(
        '\0', ' ', '\t', '"', '\'', '(', ')', '{', '}', ',', '.', ';', '=', '+',
        '-', '*', '/', '<', '>', '&', '|', '%', '~', '^', '#'));

    try {
      NextSave();
      loop: while (true) {
        if (c == '/') {
          Next();
          if (c == '/')
            xIndex = data[yIndex].length() - 1;
          else if (c == '*') {
            Next();
            while (true)
              if (c == '*') {
                Next();
                if (c == '/')
                  break;
              } else
                Next();
          } else
            add(TokenTypes.DIV);

        } else if (c == '"') {
          tmpstr = "";
          while (true) {
            Next();
            if (c == '"') {
              add(TokenTypes.STRING, tmpstr);
              break;
            } else if (c == '\\') {
              Next();
              if (c == 't')
                tmpstr += '\t';
              else if (c == 'b')
                tmpstr += '\b';
              else if (c == 'n')
                tmpstr += '\n';
              else if (c == 'r')
                tmpstr += '\r';
              else if (c == 'f')
                tmpstr += '\f';
              else if (c == '\'')
                tmpstr += '\'';
              else if (c == '\"')
                tmpstr += '\"';
              else if (c == '\\')
                tmpstr += '\\';
              else
                throw new ParseException(
                    "Expected token 'ESCAPE SEQUENCES CODE' got '" + c
                        + "' on line: " + yIndex + ":" + xIndex
                        + ". Started on line: " + yFirstIndex + ":"
                        + xFirstIndex + ".", -1);
            } else
              tmpstr += c;
          }
        } else if (c == '\'') {
          Next();
          if (c == '\'')
            throw new ParseException("Expected token 'CHARACTER' got '" + c
                + "' on line: " + yIndex + ":" + xIndex + ". Started on line: "
                + yFirstIndex + ":" + xFirstIndex + ".", -1);
          if (c == '\\') {
            Next();
            if (c == 't')
              add(TokenTypes.CHAR, '\t');
            else if (c == 'b')
              add(TokenTypes.CHAR, '\b');
            else if (c == 'n')
              add(TokenTypes.CHAR, '\n');
            else if (c == 'r')
              add(TokenTypes.CHAR, '\r');
            else if (c == 'f')
              add(TokenTypes.CHAR, '\f');
            else if (c == '\'')
              add(TokenTypes.CHAR, '\'');
            else if (c == '\"')
              add(TokenTypes.CHAR, '\"');
            else if (c == '\\')
              add(TokenTypes.CHAR, '\\');
            else
              throw new ParseException(
                  "Expected token 'ESCAPE SEQUENCES CODE' got '" + c
                      + "' on line: " + yIndex + ":" + xIndex
                      + ". Started on line: " + yFirstIndex + ":" + xFirstIndex
                      + ".", -1);
          } else
            add(TokenTypes.CHAR, c);
          Expect('\'');
        } else if (c == '\0' || c == ' ' || c == '\t')
          ;
        else if (c == '(')
          add(TokenTypes.BEGIN_ARGS);
        else if (c == ')')
          add(TokenTypes.END_ARGS);
        else if (c == '{')
          add(TokenTypes.BEGIN_BODY);
        else if (c == '}')
          add(TokenTypes.END_BODY);
        else if (c == '.')
          add(TokenTypes.DOT);
        else if (c == ',')
          add(TokenTypes.COMMA);
        else if (c == ':')
          add(TokenTypes.COLON);
        else if (c == ';')
          add(TokenTypes.SEMICOLON);
        else if (c == '=')
          add(TokenTypes.EQUALS);
        else if (c == '+')
          add(TokenTypes.PLUS);
        else if (c == '-')
          add(TokenTypes.MINUS);
        else if (c == '*')
          add(TokenTypes.MUL);
        else if (c == '<')
          add(TokenTypes.LESS);
        else if (c == '>')
          add(TokenTypes.MORE);
        else if (c == '&')
          add(TokenTypes.AND);
        else if (c == '|')
          add(TokenTypes.OR);
        else if (c == '%')
          add(TokenTypes.MOD);
        else if (c == '~')
          add(TokenTypes.TILDA);
        else if (c == '^')
          add(TokenTypes.XOR);
        else if (c == '#')
          add(TokenTypes.HASHTAG);
        else if (Character.isDigit(c)) {
          boolean hex = false;
          final ArrayList<Character> hexchar = new ArrayList<>(Arrays.asList(
              '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
              'D', 'E', 'F'));
          double digit = Character.getNumericValue(c);
          int digitDot = -1;
          Next();
          if (c == 'x' || c == 'X') {
            hex = true;
            Next();
          }
          do {
            if (digitDot > -1)
              digitDot++;
            if (c == '.')
              if (digitDot == -1)
                digitDot = 0;
              else
                throw new ParseException("Expected token 'NUMBER' got '" + c
                    + "' on line: " + yIndex + ":" + xIndex
                    + ". Started on line: " + yFirstIndex + ":" + xFirstIndex
                    + ".", -1);
            else {
              if (hex)
                digit = digit * 16 + hexchar.indexOf(c);
              else if (hexchar.indexOf(c) > 9)
                throw new ParseException("Expected token 'NUMBER' got '" + c
                    + "' on line: " + yIndex + ":" + xIndex
                    + ". Started on line: " + yFirstIndex + ":" + xFirstIndex
                    + ".", -1);
              else
                digit = digit * 10 + hexchar.indexOf(c);
            }
            Next();
            c = Character.toUpperCase(c);
          } while (Character.isDigit(c) || c == '.');
          if (digitDot != -1)
            digit /= Math.pow((hex ? 16 : 10), digitDot);
          Back();
          add(TokenTypes.NUMBER, digit);
        } else {
          tmpstr = "";
          while (true) {
            if (NON_VALID_CHAR.contains(c)) {
              add(TokenTypes.DEFINITION, tmpstr);
              Back();
              break;
            } else
              tmpstr += c;
            try {
              Next();
            } catch (final EOFException e) {
              add(TokenTypes.DEFINITION, tmpstr);
              break loop;
            }
          }
        }
        try {
          NextSave();
        } catch (final EOFException e) {
          break loop;
        }
      }
    } catch (final EOFException e) {
      throw new ParseException("Expected token got EOF on line: " + yIndex
          + ":" + xIndex + ". Started on line: " + yFirstIndex + ":"
          + xFirstIndex + ".", -1);
    } catch (final Exception e) {
      throw e;
    }
  }

  private void Next() throws EOFException {
    xIndex++;
    while (true)
      if (xIndex >= data[yIndex].length()) {
        xIndex = 0;
        yIndex++;
        if (yIndex >= data.length)
          throw new EOFException();
      } else
        break;
    c = data[yIndex].charAt(xIndex);
  }

  private void NextSave() throws EOFException {
    Next();
    xFirstIndex = xIndex;
    yFirstIndex = yIndex;
  }

}
