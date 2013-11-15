package se.definewild.wildhttp.io.net.packet;

public abstract class PacketResponce {

  public enum DataType {
    HTML, TEXT;
  }

  private final DataType datatype;

  public PacketResponce(DataType datatype) {
    this.datatype = datatype;
  }

  public String getDataType() {
    switch (datatype) {
    case HTML:
      return "text/html";
    case TEXT:
    default:
      return "text/plain";
    }
  }

  public abstract String Write();
}
