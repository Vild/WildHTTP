package se.definewild.wildhttp.io.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import se.definewild.wildhttp.io.Log;
import se.definewild.wildhttp.io.net.packet.PacketReceiver;
import se.definewild.wildhttp.io.net.packet.PacketReceiverGet;
import se.definewild.wildhttp.io.net.packet.PacketResponce;

public class PacketHandler {

  private static final Log log = Log.getLog();

  public static HashMap<String, Class<?>> packets;

  static {
    packets = new HashMap<String, Class<?>>();
    packets.put("GET", PacketReceiverGet.class);
  }

  private static String formatDate(Date date) {
    return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(date);
  }

  public static PacketReceiver GetPacket(Socket socket) throws IOException {
    final DataInputStream input = new DataInputStream(socket.getInputStream());
    final int length = input.available();
    if (length == 0)
      return null;
    final byte[] bytes = new byte[length];
    input.readFully(bytes, 0, bytes.length);
    final String msg = new String(bytes);
    final String ID = msg.substring(0, msg.indexOf(' '));

    if (!packets.containsKey(ID)) {
      log.Severe("UNKNOWN PACKET ID:" + ID);
      return null;
    }

    try {
      final PacketReceiver a = (PacketReceiver) packets.get(ID).newInstance();
      a.Read(msg);
      return a;
    } catch (final Exception e) {
      log.Severe(e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  public static void SendPacket(Socket socket, PacketResponce packet)
      throws IOException {
    final DataOutputStream output = new DataOutputStream(
        socket.getOutputStream());
    final StringBuilder sb = new StringBuilder();
    final String data = packet.Write();
    // Header
    sb.append("HTTP/1.1 200 OK\r\n");
    sb.append("Date: " + formatDate(new Date(System.currentTimeMillis()))
        + "\r\n");
    sb.append("Server: WildHTTP\r\n");
    sb.append("Last-Modified: "
        + formatDate(new Date(System.currentTimeMillis())) + "\r\n");
    sb.append("Accept-Ranges: bytes\r\n");
    sb.append("Content-Length: " + data.length() + "\r\n");
    sb.append("Vary: Accept-Encoding\r\n");
    sb.append("Content-Type: " + packet.getDataType() + "\r\n");
    sb.append("\r\n");
    sb.append(data);
    sb.append("\r\n");

    output.write(sb.toString().getBytes());
    output.flush();
  }
}
