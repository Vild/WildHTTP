package se.definewild.wildhttp.io.net.packet;

import java.util.HashMap;

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
  public void Read(String msg) {
    // 14:24:13 [INFO] [WildHTTP] Name: X-DNT-Cohort Value: 2013-8-28
    // 14:24:13 [INFO] [WildHTTP] Name: Accept-Language Value: en-US,en;q=0.5
    // 14:24:13 [INFO] [WildHTTP] Name: If-Modified-Since Value: to, 14 nov 2013 14:23:43 CET
    // 14:24:13 [INFO] [WildHTTP] Name: GET Value: / HTTP/1.1
    // 14:24:13 [INFO] [WildHTTP] Name: Host Value: localhost
    // 14:24:13 [INFO] [WildHTTP] Name: Accept-Encoding Value: gzip, deflate
    // 14:24:13 [INFO] [WildHTTP] Name: DNT Value: 1
    // 14:24:13 [INFO] [WildHTTP] Name: X-DNT-Version Value: 2.2.9.618 FF ffamo 10216
    // 14:24:13 [INFO] [WildHTTP] Name: X-DNT-OVersion Value: 2.2.9.618
    // 14:24:13 [INFO] [WildHTTP] Name: User-Agent Value: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0
    // 14:24:13 [INFO] [WildHTTP] Name: Connection Value: keep-alive
    // 14:24:13 [INFO] [WildHTTP] Name: Accept Value: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
    // 14:24:13 [INFO] [WildHTTP] Name: Cache-Control Value: max-age=0
    final HashMap<String, String> parts = new HashMap<>();
    final String[] split = msg.trim().split("\n");
    for (int i = 0; i < split.length; i++) {
      String name = split[i].substring(0, split[i].indexOf(" ")).trim();
      final String data = split[i].substring(split[i].indexOf(" ") + 1).trim();
      if (name.endsWith(":"))
        name = name.substring(0, name.length() - 1);
      parts.put(name, data);
    }

    file = parts.get("GET").trim();
    file = file.substring(0, file.length() - "HTTP/1.1".length() - 1); // TODO: Fix dirty GET path hack
    useragent = parts.get("User-Agent").trim();
    host = parts.get("Host").trim();
  }

}
