package se.definewild.wildhttp.io.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import se.definewild.wildhttp.io.Log;

public class Server implements Runnable {

  public final static ArrayList<Client> Clients = new ArrayList<Client>();

  private final Log log = Log.getLog();
  private final int port;
  private ServerSocket server;

  private final Thread thread;

  public Server(int port) {
    this.port = port;
    this.thread = new Thread(this, "Server Listener");
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
        log.Info("Got new client from: "
            + client.getInetAddress().getHostAddress());
        Clients.add(new Client(client));
      } catch (final IOException e) {
      }
    }
  }

  public void Start() {
    try {
      server = new ServerSocket(port);
      server.setSoTimeout(1000);
      this.thread.start();
      log.Info("The server is started on port " + port + ".");
    } catch (final IOException e) {
      log.Severe("Failed to start a server listener");
      e.printStackTrace();
    }
  }

  public void Stop() {
    try {
      server.close();
      this.thread.interrupt();
      WaitToEnd();
    } catch (final IOException e) {
    }
  }

  public void WaitToEnd() {
    try {
      this.thread.join();
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }
}
