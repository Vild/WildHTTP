package se.definewild.wildhttp.io.net.packet.receive;

import java.util.HashMap;

import se.definewild.wildhttp.io.net.packet.PacketReceiver;

public class PacketReceiverGet extends PacketReceiver {

  private String file;
  private String host;
  private String useragent;

  public String getFile() {
    return file;
  }

  public String getHost() {
    return host;
  }

  public String getUseragent() {
    return useragent;
  }

  @Override
  public void Read(HashMap<String, String> header) {
    file = header.get("GET").trim();
    file = file.substring(0, file.length() - "HTTP/1.1".length() - 1); // TODO: Fix dirty GET path hack
    useragent = header.get("User-Agent").trim();
    host = header.get("Host").trim();
  }

}
