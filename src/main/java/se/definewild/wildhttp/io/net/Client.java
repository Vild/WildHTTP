package se.definewild.wildhttp.io.net;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import se.definewild.wildhttp.io.Log;
import se.definewild.wildhttp.io.SiteGetter;
import se.definewild.wildhttp.io.net.packet.PacketReceiver;
import se.definewild.wildhttp.io.net.packet.PacketReceiverGet;
import se.definewild.wildhttp.io.net.packet.PacketResponce;
import se.definewild.wildhttp.io.net.packet.PacketSimpleResponce;

public class Client {

  class waitPacket extends Thread {

    private final Client client;

    public waitPacket(Client client) {
      this.client = client;
    }

    @Override
    public void run() {
      while (!isInterrupted())
        try {
          client.HandlePacket(PacketHandler.GetPacket(client.socket));
        } catch (final IOException e) {
        }
    }
  }

  public int KeepAlive_Hash;
  public Log log = Log.getLog();

  public Socket socket;

  private final waitPacket WaitPacket;

  public Client(Socket socket) {
    this.socket = socket;
    this.WaitPacket = new waitPacket(this);
    this.WaitPacket.start();
  }

  public void Close() {
    this.WaitPacket.interrupt();

    try {
      this.socket.close();
    } catch (final IOException e) {
    }
    Server.Clients.remove(this);
  }

  public PacketReceiver GetPacket() throws IOException {
    return PacketHandler.GetPacket(socket);
  }

  public void HandlePacket(PacketReceiver packet) {
    if (packet == null)
      return;
    if (packet instanceof PacketReceiverGet)
      RecivedPacket((PacketReceiverGet) packet);
    else
      RecivedPacket(packet);
  }

  public void RecivedPacket(PacketReceiver packet) {
    log.Severe("UNKNOWN PACKET: " + packet.getClass().getName());
  }

  public void RecivedPacket(PacketReceiverGet packet) {
    final File file = SiteGetter.GetSite(packet.getHost(), packet.getFile());
    final String site = SiteGetter.GetContent(file).replaceAll("%USER-AGENT%",
        packet.getUseragent());
    if (site == null)
      SendPacket(new PacketSimpleResponce(
          "<p style='font-weight:bold;font-size:50px;'>Unknown file: "
              + file.getAbsolutePath() + "</p>"));
    else
      SendPacket(new PacketSimpleResponce(site));
    Close();
  }

  public void SendPacket(PacketResponce packet) {
    try {
      PacketHandler.SendPacket(socket, packet);
    } catch (final Exception e) {
    }
  }

}
