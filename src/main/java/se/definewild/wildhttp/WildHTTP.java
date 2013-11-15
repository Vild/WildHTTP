package se.definewild.wildhttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import jline.ConsoleReader;
import jline.Terminal;
import se.definewild.wildhttp.io.Log;
import se.definewild.wildhttp.io.ServerSettings;
import se.definewild.wildhttp.io.net.Server;

public class WildHTTP implements Runnable {
  public static void main(String[] args) {
    try {
      new WildHTTP(args).run();
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  private final BufferedReader in = new BufferedReader(new InputStreamReader(
      System.in));
  private final Log log;

  public boolean noJLine;
  public ConsoleReader reader;
  private Server server;
  public ServerSettings serverSettings;

  public WildHTTP(String[] args) throws Exception {
    noJLine = java.lang.management.ManagementFactory.getRuntimeMXBean()
        .getInputArguments().toString().contains("-agentlib:jdwp");

    for (final String arg : args)
      if (arg.toLowerCase().contains("-nojline"))
        noJLine = true;

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

    log = new Log(reader);
    serverSettings = new ServerSettings();
  }

  private void commandline() {
    String command = null;
    while (server.IsRunning()) {
      command = ReadLine();
      if (command != null) {
        command = command.trim();
        if (command.equalsIgnoreCase("help"))
          log.Info("Help - help menu :P\nStop - stops the server");
        else if (command.equalsIgnoreCase("reload"))
          serverSettings = new ServerSettings();
        else if (command.equalsIgnoreCase("stop"))
          server.Stop();
      }
    }
  }

  public String ReadLine() {
    while (true)
      try {
        if (noJLine)
          return in.readLine();
        else
          return reader.readLine(">", null);
      } catch (final IOException e) {
      }
  }

  @Override
  public void run() {
    log.Info("WildHTTP is initializing...");

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        server.Stop();
        Log.getLog().Info("Server is stopped");
      }
    });

    log.Info("Starting the server...");
    server = new Server(serverSettings.getPort());
    server.Start();
    if (!noJLine) {
      reader.setBellEnabled(false);
      reader.setUseHistory(true);
    }

    commandline();
  }
}
