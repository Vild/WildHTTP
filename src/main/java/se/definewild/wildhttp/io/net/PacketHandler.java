package se.definewild.wildhttp.io.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import se.definewild.wildhttp.io.Log;
import se.definewild.wildhttp.io.net.packet.PacketReceiver;
import se.definewild.wildhttp.io.net.packet.PacketResponce;
import se.definewild.wildhttp.io.net.packet.receive.PacketReceiverGet;

public class PacketHandler {

  private static final Log log = Log.getLog();

  public static HashMap<String, Class<?>> packets;

  static {
    packets = new HashMap<String, Class<?>>();
    packets.put("GET", PacketReceiverGet.class);
    packets.put("HEAD", PacketReceiverGet.class);
    packets.put("POST", PacketReceiverGet.class);
  }

  public static PacketReceiver GetPacket(Socket socket) throws Exception {
    if (socket.isClosed())
      throw new Exception();
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
      final HashMap<String, String> parts = new HashMap<>();
      final HashMap<String, String> post = new HashMap<>();
      final String[] split = msg.trim().split("\n");
      for (int i = 0; i < split.length; i++)
        if (split[i].indexOf(" ") != -1) {
          String name = split[i].substring(0, split[i].indexOf(" ")).trim();
          final String data = split[i].substring(split[i].indexOf(" ") + 1)
              .trim();
          if (name.endsWith(":"))
            name = name.substring(0, name.length() - 1);
          parts.put(name, data);
        } else if (split[i].contains("=")) {
          final String data[] = split[i].split("&");
          for (final String p : data) {
            final String tmp[] = p.split("=");
            if (tmp.length == 1)
              post.put(tmp[0], null);
            else
              post.put(tmp[0], tmp[1]);
          }
        }
      a.Read(parts, post);
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
    output.write(packet.Write());
    output.flush();
    output.close();
  }
}
