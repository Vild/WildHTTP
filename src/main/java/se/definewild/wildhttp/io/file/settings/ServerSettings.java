package se.definewild.wildhttp.io.file.settings;

import java.io.File;

public class ServerSettings {

  private final Settings settings;

  public ServerSettings() {
    settings = new Settings(new File("WildHTTP.properties"));

    settings.SetIfNotExists("port", "80",
        "The port number that the server will run on");
  }

  public int getPort() {
    return settings.GetInt("port", 80);
  }

}
