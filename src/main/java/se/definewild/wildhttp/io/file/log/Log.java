package se.definewild.wildhttp.io.file.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import jline.ConsoleReader;

public class Log {

  private static Log current;

  public static Log getLog() {
    if (current != null)
      return current;

    System.out.println("Log failed to init. Aborting!");
    System.exit(-1);
    return null;
  }

  private final Logger log;

  private final ConsoleReader reader;

  public Log(final ConsoleReader reader) throws Exception {
    this.reader = reader;
    log = Logger.getLogger("WildHTTP");
    log.setUseParentHandlers(false);

    final FileHandler f = new FileHandler("WildHTTP.log", true);
    f.setLevel(Level.ALL);
    f.setFormatter(new LogFormatter(true));
    log.addHandler(f);
    final Handler h = new Handler() {

      @Override
      public void close() throws SecurityException {

      }

      @Override
      public void flush() {
        try {
          reader.flushConsole();
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void publish(LogRecord arg0) {
        if (reader != null)
          try {
            reader.printString("\r" + this.getFormatter().format(arg0));
            reader.redrawLine();
          } catch (final IOException e) {
            e.printStackTrace();
          }
        else
          System.out.print(this.getFormatter().format(arg0));
      }
    };
    h.setLevel(Level.ALL);
    h.setFormatter(new LogFormatter(false));
    log.addHandler(h);
    current = this;
  }

  public void Info(String msg) {
    log.log(Level.INFO, "[WildHTTP] " + msg);
    if (reader != null)
      try {
        reader.redrawLine();
      } catch (final IOException e) {
      }
  }

  public void Severe(String msg) {
    log.log(Level.SEVERE, "[WildHTTP] " + msg);
    if (reader != null)
      try {
        reader.redrawLine();
      } catch (final IOException e) {
      }
  }

  public void Warning(String msg) {
    log.log(Level.WARNING, "[WildHTTP] " + msg);
    if (reader != null)
      try {
        reader.redrawLine();
      } catch (final IOException e) {
      }
  }

}
