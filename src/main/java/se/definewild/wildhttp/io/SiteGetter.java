package se.definewild.wildhttp.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class SiteGetter {

  public static String GetContent(File file) {
    try {
      final DataInputStream in = new DataInputStream(new FileInputStream(file));
      final byte[] buffer = new byte[in.available()];
      in.read(buffer);
      in.close();
      return new String(buffer);
    } catch (final Exception e) {
      return null;
    }
  }

  public static File GetSite(String host, String file) {
    Log.getLog().Info("host: " + host + " file: " + file);
    if (file.endsWith("/"))
      file += "index.html";

    return new File("www/" + host + "/" + file);
  }

}
