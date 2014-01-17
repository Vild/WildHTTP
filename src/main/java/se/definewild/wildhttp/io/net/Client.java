package se.definewild.wildhttp.io.net;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;

import se.definewild.wildhttp.io.file.SiteFile;
import se.definewild.wildhttp.io.file.SiteGetter;
import se.definewild.wildhttp.io.file.SiteFile.HTTPCode;
import se.definewild.wildhttp.io.file.log.Log;
import se.definewild.wildhttp.io.net.packet.PacketReceiver;
import se.definewild.wildhttp.io.net.packet.PacketResponce;
import se.definewild.wildhttp.io.net.packet.receive.PacketReceiverGet;
import se.definewild.wildhttp.io.net.packet.send.PacketRedirect;
import se.definewild.wildhttp.io.net.packet.send.PacketSimpleResponce;

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
        } catch (final Exception e) {
          client.Close();
          return;
        }
    }
  }

  public long Created;

  public Log log = Log.getLog();

  public Socket socket;
  public waitPacket WaitPacket;

  public Client(Socket socket) {
    this.socket = socket;
    this.WaitPacket = new waitPacket(this);
    this.WaitPacket.start();
    this.Created = System.currentTimeMillis();
  }

  public void Close() {
    this.WaitPacket.interrupt();

    try {
      this.socket.close();
    } catch (final IOException e) {
    }
    Server.Clients.remove(this);
  }

  public PacketReceiver GetPacket() throws Exception {
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
    final String to = SiteGetter.NeedsRedirection(packet.getHost(),
        packet.getFile());
    try {
      if (to != null)
        SendPacket(new PacketRedirect(to));
      else {
        final SiteFile file = SiteGetter.GetSite(packet.getHost(),
            packet.getFile(), packet.getPost(), packet.getGet(),
            packet.getHeader(),
            packet.getRequestMode() != PacketReceiver.RequestMode.HEAD);

        if ((packet.getIfModifiedSince() != null && packet.getIfModifiedSince()
            .equals(
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(
                    file.getLastModified()).toString()))
            && (packet.getIfNoneMatch() != null && packet.getIfNoneMatch()
                .equals(file.getETag())))
          SendPacket(new PacketSimpleResponce(null, new SiteFile(
              file.getFile(), file.getType(), "".getBytes(),
              file.getLastModified(), HTTPCode.NOT_MODIFIED, packet.getPost(),
              packet.getGet(), true)));
        else
          SendPacket(new PacketSimpleResponce(null, file));
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public void SendPacket(PacketResponce packet) {
    try {
      log.Info("sent: " + packet.toString());
      PacketHandler.SendPacket(socket, packet);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

}
