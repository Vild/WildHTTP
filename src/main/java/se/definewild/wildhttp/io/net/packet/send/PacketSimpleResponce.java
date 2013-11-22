package se.definewild.wildhttp.io.net.packet.send;

import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import se.definewild.wildhttp.io.SiteFile;
import se.definewild.wildhttp.io.net.packet.PacketResponce;

public class PacketSimpleResponce extends PacketResponce {

  private HashMap<String, String> header;
  private SiteFile file;

  public PacketSimpleResponce(HashMap<String, String> header, SiteFile file) {
    if (header == null)
      this.header = new HashMap<>();
    else
      this.header = header;
    this.file = file;
    add("Date", formatDate(new Date(System.currentTimeMillis())));
    add("Server", "WildHTTP");
    add("Last-Modified", formatDate(file.getLastModified()));
    add("Accept-Ranges", "bytes");
    add("Content-Length", file.getContent().length() + "");
    add("Vary", "Accept-Encoding");
    add("Content-Type", file.getType().toString());
  }

  private void add(String key, String value) {
    if (!header.containsKey(key))
      header.put(key, value);
  }

  @Override
  public String Write() {
    final StringBuilder sb = new StringBuilder();
    // Header
    sb.append("HTTP/1.1 " + file.getHttpCode().toString() + "\r\n");
    for (Entry<String, String> item : header.entrySet())
      sb.append(item.getKey() + ": " + item.getValue() + "\r\n");

    sb.append("\r\n");
    sb.append(file.getContent());
    sb.append("\r\n");
    return sb.toString();
  }

}
