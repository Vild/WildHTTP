package se.definewild.wildhttp.io.net.packet;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class PacketResponce {

  protected static String formatDate(Date date) {
    return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(date);
  }

  @Override
  public String toString() {
    return "PacketResponce []";
  }

  public abstract byte[] Write();

}
