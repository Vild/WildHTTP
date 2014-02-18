package se.definewild.wildhttp.io.file.script;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import jline.ConsoleReader;
import jline.Terminal;
import se.definewild.wildhttp.io.file.log.Log;
import se.definewild.wildhttp.io.file.script.token.Tokenizer;

public class TestScript {

  private static String getContent(File file) {
    try {
      final DataInputStream in = new DataInputStream(new FileInputStream(file));
      final byte[] buffer = new byte[in.available()];
      in.read(buffer);
      in.close();
      return new String(buffer);
    } catch (final Exception e) {
      return null;
    }
  }

  public static void main(String[] args) throws Exception {
    boolean noJLine = java.lang.management.ManagementFactory.getRuntimeMXBean()
        .getInputArguments().toString().contains("-agentlib:jdwp");

    for (final String arg : args)
      if (arg.toLowerCase().contains("-nojline"))
        noJLine = true;

    ConsoleReader reader;

    if (!noJLine) {
      Terminal.setupTerminal();

      try {
        reader = new ConsoleReader();
      } catch (final IOException e) {
        Logger.getLogger("WildHTTP").severe(
            "Failed to make an instante of jline.ConsoleReader, aborting!");
        reader = null;
      }
    } else
      reader = null;

    new Log(reader);

    final String text = getContent(new File("test.ws"));

    final Tokenizer a = new Tokenizer(text);
    for (int i = 0; i < a.getTokens().size(); i++)
      Log.getLog().Info(i + ": " + a.getTokens().get(i).toString());
  }
}
