package se.definewild.wildhttp.io;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import se.definewild.wildhttp.io.SiteFile.DataType;
import se.definewild.wildhttp.io.SiteFile.HTTPCode;

public class SiteGetter {

  public static HashMap<String, SiteFile> cacheFile = new HashMap<>();
  public static HashMap<String, File> cacheDirectory = new HashMap<>();

  public static void ClearCache() {
    cacheFile.clear();
    cacheDirectory.clear();
  }

  private static File GetFilePath(String host, String file) {
    File dir;
    if (cacheDirectory.containsKey(host))
      dir = cacheDirectory.get(host);
    else {
      dir = new File("www/" + host);
      if (!dir.exists()) {
        dir = new File("www/general");
      }
      cacheDirectory.put(host, dir);
    }

    if (file.endsWith("/"))
      file += "index.html";
    return new File(dir.toString() + file);
  }

  public static String NeedsRedirection(String host, String file) {
    if (!file.endsWith("/") && new File("www/" + host + file).isDirectory())
      return file + "/";
    else
      return null;
  }

  public static SiteFile GetSite(String host, String file) {
    final File f = GetFilePath(host, file);
    SiteFile sitefile;

    if (cacheFile.containsKey(f.toString()))
      return cacheFile.get(f.toString());

    try {
      sitefile = new SiteFile(f);
    } catch (Exception e) {
      sitefile = new SiteFile(f, DataType.HTML,
          "<p style='font-weight:bold;font-size:50px;'>Unknown file: " + file
              + "</p>", new Date(System.currentTimeMillis()),
          HTTPCode.NOT_FOUND);
    }
    cacheFile.put(f.toString(), sitefile);
    return sitefile;
  }
}
