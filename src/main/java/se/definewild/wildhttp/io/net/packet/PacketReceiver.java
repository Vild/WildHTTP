package se.definewild.wildhttp.io.net.packet;

import java.util.HashMap;

public abstract class PacketReceiver {

  public enum RequestMode {
    GET, HEAD, POST;
  }

  protected HashMap<String, String> header;
  protected RequestMode requestMode;

  public HashMap<String, String> getHeader() {
    return header;
  }

  public RequestMode getRequestMode() {
    return requestMode;
  }

  public abstract void Read(HashMap<String, String> header,
      HashMap<String, String> post);
}
