package se.definewild.wildhttp.io.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import se.definewild.wildhttp.io.Log;

public class Server implements Runnable {

  public class clientCleanup extends Thread {

    public final long KILL_TIME = 1 * 1000;

    public clientCleanup() {
      super("Client Cleanup");
    }

    @Override
    public void run() {
      while (!isInterrupted())
        try {
          for (int i = 0; i < Clients.size(); i++) {
            Client client = Clients.get(i);
            if (client != null && client.socket != null)
              synchronized (client) {
                if (client.socket.isClosed()
                    || System.currentTimeMillis() - client.Created > KILL_TIME) {
                  client.WaitPacket.interrupt();
                  client.WaitPacket.join();

                  Clients.remove(client);
                  client.WaitPacket = null;
                  client.socket = null;
                  client = null;
                  i--; // Because we removed one
                }
              }
          }
          sleep(100);
        } catch (final InterruptedException e) {
          return;
        } catch (final Exception e) {
        }
    }
  }

  public final static ArrayList<Client> Clients = new ArrayList<Client>();

  private final clientCleanup cleanup;
  private final Log log = Log.getLog();
  private final int port;

  private ServerSocket server;
  private final Thread thread;

  public Server(int port) {
    this.port = port;
    this.thread = new Thread(this, "Server Listener");
    this.cleanup = new clientCleanup();
  }

  public boolean IsRunning() {
    return this.thread != null && this.thread.isAlive()
        && !this.thread.isInterrupted();
  }

  @Override
  public void run() {
    while (true) {
      if (server == null || server.isClosed())
        return;

      try {
        final Socket client = server.accept();
        Clients.add(new Client(client));
      } catch (final SocketTimeoutException e) {
      } catch (final IOException e) {
      }
    }
  }

  public void Start() {
    try {
      server = new ServerSocket(port);
      server.setSoTimeout(1000);
      this.thread.start();
      this.cleanup.start();
      log.Info("The server is started on port " + port + ".");
    } catch (final IOException e) {
      log.Severe("Failed to start a server listener");
      e.printStackTrace();
      System.exit(-1);
    }
  }

  public void Stop() {
    try {
      server.close();
      this.thread.interrupt();
      this.cleanup.interrupt();
      WaitToEnd();
    } catch (final IOException e) {
    }
  }

  public void WaitToEnd() {
    try {
      this.thread.join();
      this.cleanup.join();
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }
}
