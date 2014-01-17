package se.definewild.wildhttp.io.file;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;

import se.definewild.wildhttp.utils.WHA;

public class SiteFile {

  public enum DataType {
    AAC("audio/x-aac"), ATOM("application/atom+xml"), CAF("audio/x-caf"), CRX(
        "application/x-chrome-extension"), CSS("text/css"), CSV("text/csv"), DEB(
        "application/x-deb"), DOWNLOAD("application/octet-stream"), DVI(
        "application/x-dvi"), FLV("video/x-flv"), GIF("image/gif"), GZIP(
        "application/gzip"), HTML("text/html"), JAVASCRIPT("text/javascript"), JPEG(
        "image/jpeg"), JSON("application/json"), LATEX("application/x-latex"), MARKDOWN(
        "text/x-markdown"), MKV("video/x-matroska"), MOV("video/quicktime"), MP3(
        "audio/mpeg"), MP4("video/mp4"), MPEG("video/mpeg"), OGG("video/ogg"), PDF(
        "application/pdf"), PJPEG("image/pjpeg"), PNG("image/png"), POSTSCRIPT(
        "application/postscript"), RAR("application/x-rar-compressed"), RDF(
        "application/rdf+xml"), RSS("application/rss+xml"), SEVEN_ZIP(
        "application/x-7z-compressed"), SOAP("application/soap+xml"), SVG(
        "image/svg+xml"), SWF("application/x-shockwave-flash"), TAR(
        "application/x-tar"), TEXT("text/plain"), TFF("application/x-font-ttf"), TIFF(
        "image/tiff"), VCARD("text/vcard"), WEBM("video/webm"), WMV(
        "video/x-ms-wmv"), WOFF("application/font-woff"), XCF("image/x-xcf"), XHTML(
        "application/xhtml+xml"), XML("text/xml"), XPI(
        "application/x-xpinstall"), ZIP("application/zip");

    private String type;

    DataType(String type) {
      this.type = type;
    }

    @Override
    public String toString() {
      return type;
    }
  }

  public enum HTTPCode {
    NOT_FOUND("404 Not Found"), NOT_MODIFIED("304 Not Modified"), OK("200 OK");
    private String code;

    HTTPCode(String code) {
      this.code = code;
    }

    @Override
    public String toString() {
      return code;
    }
  }

  private static HashMap<File, byte[]> cacheFile = new HashMap<>();

  private byte[] content;
  private final String etag;
  private final File file;
  private final HashMap<String, String> get;
  private final HashMap<String, String> header;
  private final HTTPCode httpCode;

  private final Date lastModified;
  private final HashMap<String, String> post;

  private final DataType type;

  public SiteFile(File file, DataType type, byte[] content, Date lastModified,
      HTTPCode httpCode, HashMap<String, String> post,
      HashMap<String, String> get, boolean sendData) {
    this.post = post;
    this.get = get;
    this.header = new HashMap<>();

    this.file = file;
    this.type = type;
    if (sendData)
      this.content = content;
    if (this.content == null)
      this.content = new byte[0];
    this.lastModified = lastModified;
    this.httpCode = httpCode;
    this.etag = new String(WHA.WHA0(this.content));
  }

  public SiteFile(File file, HashMap<String, String> post,
      HashMap<String, String> get, HashMap<String, String> header,
      boolean sendData) throws FileNotFoundException {
    if (!file.exists())
      throw new FileNotFoundException(file.getPath());

    this.post = post;
    this.get = get;
    this.header = header;

    this.file = file;
    this.type = getDataType(file);
    if (sendData)
      if (cacheFile.containsKey(file))
        this.content = cacheFile.get(file);
      else {
        this.content = getContent(file);
        cacheFile.put(file, this.content);
      }
    else
      this.content = new byte[0];

    this.lastModified = new Date(file.lastModified());
    this.httpCode = HTTPCode.OK;
    this.etag = null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final SiteFile other = (SiteFile) obj;
    if (content == null) {
      if (other.content != null)
        return false;
    } else if (!content.equals(other.content))
      return false;
    if (file == null) {
      if (other.file != null)
        return false;
    } else if (!file.equals(other.file))
      return false;
    if (httpCode != other.httpCode)
      return false;
    if (lastModified == null) {
      if (other.lastModified != null)
        return false;
    } else if (!lastModified.equals(other.lastModified))
      return false;
    if (type != other.type)
      return false;
    return true;
  }

  public byte[] getContent() {
    // TODO: fix replace -> scripting
    if (file.getName().endsWith(".html"))
      return ReplacePostGet(new String(content)).getBytes();
    else
      return content;
  }

  private byte[] getContent(File file) {
    try {
      final DataInputStream in = new DataInputStream(new FileInputStream(file));
      final byte[] buffer = new byte[in.available()];
      in.read(buffer);
      in.close();
      return buffer;
    } catch (final Exception e) {
      return new byte[0];
    }
  }

  private DataType getDataType(File file) {
    final String part[] = file.getName().split("\\.");
    final String ext = part[part.length - 1];

    if (ext.equalsIgnoreCase("html") || ext.equalsIgnoreCase("htm"))
      return DataType.HTML;
    else if (ext.equalsIgnoreCase("js"))
      return DataType.JAVASCRIPT;
    else if (ext.equalsIgnoreCase("css"))
      return DataType.CSS;
    else if (ext.equalsIgnoreCase("csv"))
      return DataType.CSV;
    else if (ext.equalsIgnoreCase("xml"))
      return DataType.XML;
    else if (ext.equalsIgnoreCase("vcard"))
      return DataType.VCARD;

    else if (ext.equalsIgnoreCase("mpeg"))
      return DataType.MPEG;
    else if (ext.equalsIgnoreCase("mp4"))
      return DataType.MP4;
    else if (ext.equalsIgnoreCase("ogg"))
      return DataType.OGG;
    else if (ext.equalsIgnoreCase("mov"))
      return DataType.MOV;
    else if (ext.equalsIgnoreCase("webm"))
      return DataType.WEBM;
    else if (ext.equalsIgnoreCase("mkv"))
      return DataType.MKV;
    else if (ext.equalsIgnoreCase("wmv"))
      return DataType.WMV;
    else if (ext.equalsIgnoreCase("flv"))
      return DataType.FLV;
    else if (ext.equalsIgnoreCase("mp3"))
      return DataType.MP3;

    else if (ext.equalsIgnoreCase("gif"))
      return DataType.GIF;
    else if (ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("jpg"))
      return DataType.JPEG;
    else if (ext.equalsIgnoreCase("pjpeg"))
      return DataType.PJPEG;
    else if (ext.equalsIgnoreCase("png"))
      return DataType.PNG;
    else if (ext.equalsIgnoreCase("svg"))
      return DataType.SVG;
    else if (ext.equalsIgnoreCase("tiff"))
      return DataType.TIFF;

    else if (ext.equalsIgnoreCase("atom"))
      return DataType.ATOM;
    else if (ext.equalsIgnoreCase("json"))
      return DataType.JSON;
    else if (ext.equalsIgnoreCase("pdf"))
      return DataType.PDF;
    else if (ext.equalsIgnoreCase("ps"))
      return DataType.POSTSCRIPT;
    else if (ext.equalsIgnoreCase("rdf"))
      return DataType.RDF;
    else if (ext.equalsIgnoreCase("rss"))
      return DataType.RSS;
    else if (ext.equalsIgnoreCase("soap"))
      return DataType.SOAP;
    else if (ext.equalsIgnoreCase("woff"))
      return DataType.WOFF;
    else if (ext.equalsIgnoreCase("xhtml") || ext.equalsIgnoreCase("xhtm"))
      return DataType.XHTML;
    else if (ext.equalsIgnoreCase("zip"))
      return DataType.ZIP;
    else if (ext.equalsIgnoreCase("gzip") || ext.equalsIgnoreCase("gz"))
      return DataType.GZIP;

    else if (ext.equalsIgnoreCase("7z") || ext.equalsIgnoreCase("7zip"))
      return DataType.SEVEN_ZIP;
    else if (ext.equalsIgnoreCase("crx"))
      return DataType.CRX;
    else if (ext.equalsIgnoreCase("deb"))
      return DataType.DEB;
    else if (ext.equalsIgnoreCase("dvi"))
      return DataType.DVI;
    else if (ext.equalsIgnoreCase("tff"))
      return DataType.TFF;
    else if (ext.equalsIgnoreCase("latex"))
      return DataType.LATEX;
    else if (ext.equalsIgnoreCase("rar") || ext.equalsIgnoreCase("r00"))
      return DataType.RAR;
    else if (ext.equalsIgnoreCase("swf"))
      return DataType.SWF;
    else if (ext.equalsIgnoreCase("tar"))
      return DataType.TAR;
    else if (ext.equalsIgnoreCase("xpi"))
      return DataType.XPI;
    else if (ext.equalsIgnoreCase("aac"))
      return DataType.AAC;
    else if (ext.equalsIgnoreCase("cad"))
      return DataType.CAF;
    else if (ext.equalsIgnoreCase("xcf"))
      return DataType.XCF;
    else if (ext.equalsIgnoreCase("markdown") || ext.equalsIgnoreCase("md"))
      return DataType.MARKDOWN;

    else
      return DataType.TEXT;
  }

  public String getETag() {
    if (etag != null)
      return etag;
    else
      return new String(WHA.WHA0(content));
  }

  public File getFile() {
    return file;
  }

  public HTTPCode getHttpCode() {
    return httpCode;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public DataType getType() {
    return type;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((content == null) ? 0 : content.hashCode());
    result = prime * result + ((file == null) ? 0 : file.hashCode());
    result = prime * result + ((httpCode == null) ? 0 : httpCode.hashCode());
    result = prime * result
        + ((lastModified == null) ? 0 : lastModified.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  private String ReplacePostGet(String content) {
    String buf = content.replaceAll("%POST%", post.toString());
    buf = buf.replaceAll("%GET%", get.toString());
    buf = buf
        .replaceAll("%USER-AGENT%",
            header.containsKey("User-Agent") ? header.get("User-Agent")
                : "UNKNOWN");
    return buf;
  }

  @Override
  public String toString() {
    return "SiteFile [content.length=" + content.length + ", etag=" + etag
        + ", file=" + file + ", httpCode=" + httpCode + ", lastModified="
        + lastModified + ", type=" + type + "]";
  }

}
