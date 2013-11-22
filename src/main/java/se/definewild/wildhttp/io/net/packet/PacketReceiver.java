package se.definewild.wildhttp.io.net.packet;

import java.util.HashMap;

public abstract class PacketReceiver {

  public abstract void Read(HashMap<String, String> header);
}
