package se.definewild.wildhttp.io;

public class ServerSettings {

  private final Settings settings;

  public ServerSettings() {
    settings = new Settings("WildHTTP.properties");

    settings.getInt("port", 80);
    settings.getString("motd", "Super 1337 server");
  }

  public String getMotd() {
    return settings.getString("motd", "Super 1337 server");
  }

  public int getPort() {
    return settings.getInt("port", 80);
  }

}
