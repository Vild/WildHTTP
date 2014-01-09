package se.definewild.wildhttp.io.net.packet.receive;

import java.util.HashMap;

import se.definewild.wildhttp.io.net.packet.PacketReceiver;

public class PacketReceiverGet extends PacketReceiver {

  private String acceptEncoding;
  private String acceptLanguage;
  private String file;
  private HashMap<String, String> get;

  private String host;
  private String ifModifiedSince;
  private String ifNoneMatch;
  private HashMap<String, String> post;
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

  public HashMap<String, String> getGet() {
    return get;
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

  public HashMap<String, String> getPost() {
    return post;
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
  public void Read(HashMap<String, String> header, HashMap<String, String> post) {
    this.header = header;
    this.post = post;

    if (header.get("GET") != null) {
      requestMode = RequestMode.GET;
      file = header.get("GET");
    } else if (header.get("HEAD") != null) {
      requestMode = RequestMode.HEAD;
      file = header.get("HEAD");
    } else if (header.get("POST") != null) {
      requestMode = RequestMode.POST;
      file = header.get("POST");
    }
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

    this.get = new HashMap<>();
    if (this.file.indexOf("?") != -1) {
      final String tmp[] = this.file.split("\\?");
      this.file = tmp[0];
      for (final String part : tmp[1].split("&")) {
        final String parttmp[] = part.split("=");
        if (parttmp.length == 1)
          this.get.put(parttmp[0], null);
        else
          this.get.put(parttmp[0], parttmp[1]);
      }
    }
  }
}
