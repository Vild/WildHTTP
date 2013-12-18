package se.definewild.wildhttp.io.net.packet.receive;

import java.util.HashMap;

import se.definewild.wildhttp.io.net.packet.PacketReceiver;

public class PacketReceiverGet extends PacketReceiver {

  private String acceptEncoding;
  private String acceptLanguage;
  private String file;
  private String host;

  private String ifModifiedSince;
  private String ifNoneMatch;
  private String referer;
  private String userAgent;

  public String getAcceptEncoding() {
    return acceptEncoding;
  }

  public String getAcceptLanguage() {
    return acceptLanguage;
  }

  public String getFile() {
    return file;
  }

  public String getHost() {
    return host;
  }

  public String getIfModifiedSince() {
    return ifModifiedSince;
  }

  public String getIfNoneMatch() {
    return ifNoneMatch;
  }

  public String getReferer() {
    return referer;
  }

  public String getUseragent() {
    return userAgent;
  }

  private String IntepreterHex(String str) {
    for (int i = 0; i < 0xFF; i++)
      if (i < 0x10)
        str = str.replace("%0" + Integer.toHexString(i).toUpperCase(),
            ((char) i) + "");
      else
        str = str.replace("%" + Integer.toHexString(i).toUpperCase(),
            ((char) i) + "");
    return str;
  }

  @Override
  public void Read(HashMap<String, String> header) {
    file = header.get("GET");
    if (file == null)
      file = "/ HTTP/1.1";
    else
      file = file.trim();
    file = IntepreterHex(file.substring(0, file.length() - "HTTP/1.1".length()
        - 1)); // TODO: Fix dirty GET path hack
    userAgent = header.get("User-Agent");
    if (userAgent == null)
      userAgent = "NULL";
    else
      userAgent = userAgent.trim();
    host = header.get("Host");
    if (host == null)
      host = "NULL";
    else
      host = host.trim();

    referer = header.get("Referer");
    acceptLanguage = header.get("Accept-Language");
    acceptEncoding = header.get("Accept-Encoding");
    ifModifiedSince = header.get("If-Modified-Since");
    ifNoneMatch = header.get("If-None-Match");
  }
}
