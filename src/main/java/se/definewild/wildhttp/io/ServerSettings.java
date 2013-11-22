package se.definewild.wildhttp.io;

public class ServerSettings {

  private final SettingsOLD settings;

  public ServerSettings() {
    settings = new SettingsOLD("WildHTTP.properties");

    settings.getInt("port", 80);
  }

  public int getPort() {
    return settings.getInt("port", 80);
  }

}
