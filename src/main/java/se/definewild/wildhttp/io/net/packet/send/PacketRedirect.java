package se.definewild.wildhttp.io.net.packet.send;

import java.util.Date;

import se.definewild.wildhttp.io.net.packet.PacketResponce;

public class PacketRedirect extends PacketResponce {

  private String location;

  public PacketRedirect(String location) {
    this.location = location;
  }

  @Override
  public String Write() {
    StringBuffer sb = new StringBuffer();
    sb.append("HTTP/1.1 301 Moved Permanently\r\n");
    sb.append("Location: " + location + "\r\n");
    sb.append("Date: " + formatDate(new Date(System.currentTimeMillis())) + "\r\n");
    sb.append("Server: WildHTTP" + "\r\n");
    return sb.toString();
  }

}
