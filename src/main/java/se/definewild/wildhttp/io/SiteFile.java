package se.definewild.wildhttp.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

public class SiteFile {

  public enum DataType {
    TEXT("text/plain"), HTML("text/html"), JAVASCRIPT("text/javascript"), CSS(
        "text/css"), CSV("text/csv"), XML("text/xml"), VCARD("text/vcard"), MPEG(
        "video/mpeg"), MP4("video/mp4"), OGG("video/ogg"), MOV(
        "video/quicktime"), WEBM("video/webm"), MKV("video/x-matroska"), WMV(
        "video/x-ms-wmv"), FLV("video/x-flv"), MP3("audio/mpeg"), GIF(
        "image/gif"), JPEG("image/jpeg"), PJPEG("image/pjpeg"), PNG("image/png"), SVG(
        "image/svg+xml"), TIFF("image/tiff"), ATOM("application/atom+xml"), JSON(
        "application/json"), DOWNLOAD("application/octet-stream"), PDF(
        "application/pdf"), POSTSCRIPT("application/postscript"), RDF(
        "application/rdf+xml"), RSS("application/rss+xml"), SOAP(
        "application/soap+xml"), WOFF("application/font-woff"), XHTML(
        "application/xhtml+xml"), ZIP("application/zip"), GZIP(
        "application/gzip"), SEVEN_ZIP("application/x-7z-compressed"), CRX(
        "application/x-chrome-extension"), DEB("application/x-deb"), DVI(
        "application/x-dvi"), TFF("application/x-font-ttf"), LATEX(
        "application/x-latex"), RAR("application/x-rar-compressed"), SWF(
        "application/x-shockwave-flash"), TAR("application/x-tar"), XPI(
        "application/x-xpinstall"), AAC("audio/x-aac"), CAF("audio/x-caf"), XCF(
        "image/x-xcf"), MARKDOWN("text/x-markdown");

    private String type;

    DataType(String type) {
      this.type = type;
    }

    public String toString() {
      return type;
    }
  }

  public enum HTTPCode {
    OK("200 OK"), NOT_FOUND("404 Not Found");
    private String code;

    HTTPCode(String code) {
      this.code = code;
    }

    public String toString() {
      return code;
    }
  }

  private File file;
  private DataType type;
  private String content;
  private Date lastModified;
  private HTTPCode httpCode;

  public SiteFile(File file) throws FileNotFoundException {
    if (!file.exists())
      throw new FileNotFoundException(file.getPath());

    this.file = file;
    this.type = getDataType(file);
    this.content = getContent(file);
    this.lastModified = new Date(file.lastModified());
    this.httpCode = HTTPCode.OK;
  }

  public SiteFile(File file, DataType type, String content, Date lastModified,
      HTTPCode httpCode) {
    this.file = file;
    this.type = type;
    this.content = content;
    this.lastModified = lastModified;
    this.httpCode = httpCode;
  }

  private DataType getDataType(File file) {
    String part[] = file.getName().split("\\.");
    String ext = part[part.length - 1];

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
    else if (ext.equalsIgnoreCase("markdown") || ext.equalsIgnoreCase("mk"))
      return DataType.MARKDOWN;

    else
      return DataType.TEXT;
  }

  private String getContent(File file) {
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

  public File getFile() {
    return file;
  }

  public DataType getType() {
    return type;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public String getContent() {
    return content;
  }

  public HTTPCode getHttpCode() {
    return httpCode;
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SiteFile other = (SiteFile) obj;
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

}
