package se.definewild.wildhttp.io.file.script;

import java.io.EOFException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import se.definewild.wildhttp.io.file.script.Token.Types;

public class Tokenizer {

  private Vector<Token> tokens;

  private int xIndex;
  private int yIndex;

  private int xFirstIndex;
  private int yFirstIndex;
  private char c;

  public Tokenizer(String data) throws ParseException {
    tokens = new Vector<>();
    xIndex = -1;
    yIndex = 0;
    xFirstIndex = 0;
    yFirstIndex = 0;
    makeTokens(data);
  }

  private void makeTokens(String d) throws ParseException {
    String[] data = d.split("\\r?\\n");
    String tmpstr;
    final ArrayList<Character> NON_VALID_CHAR = new ArrayList<>(Arrays.asList(
        '\0', ' ', '\t', '"', '\'', '(', ')', '{', '}', ',', '.', '+', '-',
        '*', '/', '<', '>', '&', '|'));

    try {
      NextSave(data);
      loop: while (true) {
        if (c == '/') {
          Next(data);
          if (c == '/')
            xIndex = data[yIndex].length() - 1;
          else if (c == '*') {
            Next(data);
            while (true)
              if (c == '*') {
                Next(data);
                if (c == '/')
                  break;
              } else
                Next(data);
          } else
            tokens.add(new Token(Types.DIV));

        } else if (c == '"') {
          tmpstr = "";
          while (true) {
            Next(data);
            if (c == '"') {
              tokens.add(new Token(Types.STRING, tmpstr));
              break;
            } else
              tmpstr += c;
          }
        } else if (c == '\'') {
          Next(data);
          tokens.add(new Token(Types.CHAR, c));
          Expect(data, '\'');
        } else if (c == '\0' || c == ' ' || c == '\t')
          ;
        else if (c == '(')
          tokens.add(new Token(Types.BEGIN_ARGS));
        else if (c == ')')
          tokens.add(new Token(Types.END_ARGS));
        else if (c == '{')
          tokens.add(new Token(Types.BEGIN_BODY));
        else if (c == '}')
          tokens.add(new Token(Types.END_BODY));
        else if (c == '.')
          tokens.add(new Token(Types.DOT));
        else if (c == ',')
          tokens.add(new Token(Types.COMMA));
        else if (c == ':')
          tokens.add(new Token(Types.COLON));
        else if (c == ';')
          tokens.add(new Token(Types.SEMICOLON));
        else if (c == '+')
          tokens.add(new Token(Types.PLUS));
        else if (c == '-')
          tokens.add(new Token(Types.MINUS));
        else if (c == '*')
          tokens.add(new Token(Types.MUL));
        else if (c == '<')
          tokens.add(new Token(Types.LESS));
        else if (c == '>')
          tokens.add(new Token(Types.MORE));
        else if (c == '&')
          tokens.add(new Token(Types.AND));
        else if (c == '|')
          tokens.add(new Token(Types.OR));
        else if (Character.isDigit(c)) {
          int digit = 0;
          while (Character.isDigit(c)) {
            digit = digit * 10 + Character.getNumericValue(c);
            Next(data);
          }
          Back(data);
          tokens.add(new Token(Types.NUMBER, digit));
        } else {
          tmpstr = "";
          while (true) {
            if (NON_VALID_CHAR.contains(c)) {
              tokens.add(new Token(Types.DEFINITION, tmpstr));
              Back(data);
              break;
            } else
              tmpstr += c;
            try {
              Next(data);
            } catch (EOFException e) {
              tokens.add(new Token(Types.DEFINITION, tmpstr));
              break loop;
            }
          }
        }
        try {
          NextSave(data);
        } catch (EOFException e) {
          break loop;
        }
      }
    } catch (EOFException e) {
      throw new ParseException("Expected token got EOF on line: " + yIndex
          + ":" + xIndex + ". Started on line: " + yFirstIndex + ":"
          + xFirstIndex + ".", -1);
    } catch (Exception e) {
      throw e;
    }
  }

  private void Back(String[] data) throws EOFException {
    xIndex--;
    while (true) {
      if (xIndex < 0) {
        yIndex--;
        if (yIndex < 0)
          throw new EOFException();
        xIndex = data[yIndex].length() - 1;
      } else
        break;
    }
    c = data[yIndex].charAt(xIndex);
  }

  private void Next(String[] data) throws EOFException {
    xIndex++;
    while (true) {
      if (xIndex >= data[yIndex].length()) {
        xIndex = 0;
        yIndex++;
        if (yIndex >= data.length)
          throw new EOFException();
      } else
        break;
    }
    c = data[yIndex].charAt(xIndex);
  }

  private void NextSave(String[] data) throws EOFException {
    Next(data);
    xFirstIndex = xIndex;
    yFirstIndex = yIndex;
  }

  private void Expect(String[] data, char ch) throws EOFException,
      ParseException {
    Next(data);
    if (c != ch)
      throw new ParseException("Expected token '" + ch + "' got '" + c
          + "' on line: " + yIndex + ":" + xIndex + ". Started on line: "
          + yFirstIndex + ":" + xFirstIndex + ".", -1);
  }

  public Vector<Token> getTokens() {
    return tokens;
  }

}
