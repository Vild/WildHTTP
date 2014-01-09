package se.definewild.wildhttp.io;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import se.definewild.wildhttp.io.SiteFile.DataType;
import se.definewild.wildhttp.io.SiteFile.HTTPCode;

public class SiteGetter {

  public static HashMap<String, File> cacheDirectory = new HashMap<>();

  public static void ClearCache() {
    cacheDirectory.clear();
  }

  private static File GetFilePath(String host, String file) {
    File dir;
    if (cacheDirectory.containsKey(host))
      dir = cacheDirectory.get(host);
    else {
      dir = new File("www/" + host);
      if (!dir.exists())
        dir = new File("www/general");
      cacheDirectory.put(host, dir);
    }
    return new File(dir.toString() + file);
  }

  public static File GetIndex(String host, File file) {
    return new File(file, "index.html");
  }

  public static SiteFile GetSite(String host, String file,
      HashMap<String, String> post, HashMap<String, String> get,
      HashMap<String, String> header, boolean sendData) {
    final File f = GetFilePath(host, file);

    try {
      if (f.isDirectory())
        return new SiteFile(GetIndex(host, f), post, get, header, sendData);
      else
        return new SiteFile(f, post, get, header, sendData);
    } catch (final Exception e) {
      if (f.isDirectory()) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<html>\n" + "<head>\n" + "<title>Directory listing of "
            + f.toString() + "</title>\n" + "</head>\n" + "<body>\n");
        for (final File x : f.listFiles()) {
          String url = file + "/" + x.getName();
          if (file.endsWith("/"))
            url = file + x.getName();
          else
            url = file + "/" + x.getName();
          if (x.isDirectory())
            sb.append("<a href=\"" + url + "\">Dir: " + url + "</a><br />\n");
          else
            sb.append("<a href=\"" + url + "\">File: " + url + "</a><br />\n");
        }

        sb.append("</body>\n" + "</html>");
        return new SiteFile(f, DataType.HTML, sb.toString().getBytes(),
            new Date(System.currentTimeMillis()), HTTPCode.OK, post, get, true);
      } else {
        final String x = "<p style='font-weight:bold;font-size:50px;'>Unknown file: "
            + file + "</p>";
        return new SiteFile(f, DataType.HTML, x.getBytes(), new Date(
            System.currentTimeMillis()), HTTPCode.NOT_FOUND, post, get, true);
      }
    }
  }

  public static String NeedsRedirection(String host, String file) {
    if (!file.endsWith("/") && GetFilePath(host, file).isDirectory())
      return file + "/";
    else
      return null;
  }
}
