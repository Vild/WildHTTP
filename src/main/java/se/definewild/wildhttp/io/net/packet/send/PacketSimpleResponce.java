package se.definewild.wildhttp.io.net.packet.send;

import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import se.definewild.wildhttp.WildHTTP;
import se.definewild.wildhttp.io.SiteFile;
import se.definewild.wildhttp.io.net.packet.PacketResponce;

public class PacketSimpleResponce extends PacketResponce {

  private final SiteFile file;
  private HashMap<String, String> header;

  public PacketSimpleResponce(HashMap<String, String> header, SiteFile file) {
    if (header == null)
      this.header = new HashMap<>();
    else
      this.header = header;
    this.file = file;
    add("Date", formatDate(new Date(System.currentTimeMillis())));
    add("Server", WildHTTP.FULLNAME);
    add("Last-Modified", formatDate(file.getLastModified()));
    add("Accept-Ranges", "bytes");
    add("Content-Length", file.getContent().length + "");
    add("Vary", "Accept-Encoding");
    add("Content-Type", file.getType().toString() + "; charset=UTF-8");
    add("Connection", "keep-alive");
    add("ETag", file.getETag());
  }

  private void add(String key, String value) {
    if (!header.containsKey(key))
      header.put(key, value);
  }

  @Override
  public String toString() {
    return "PacketSimpleResponce [file=" + file + ", header=" + header + "]";
  }

  @Override
  public byte[] Write() {
    final StringBuilder sb = new StringBuilder();
    // Header
    sb.append("HTTP/1.1 " + file.getHttpCode().toString() + "\r\n");
    for (final Entry<String, String> item : header.entrySet())
      sb.append(item.getKey() + ": " + item.getValue() + "\r\n");

    sb.append("\r\n");
    final byte[] b = new byte[sb.length() + file.getContent().length];

    for (int i = 0; i < sb.length(); i++)
      b[i] = sb.toString().getBytes()[i];

    for (int i = 0; i < file.getContent().length; i++)
      b[i + sb.length()] = file.getContent()[i];
    return b;
  }

}
