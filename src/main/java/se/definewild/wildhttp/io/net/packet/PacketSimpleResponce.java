package se.definewild.wildhttp.io.net.packet;

public class PacketSimpleResponce extends PacketResponce {

  final String msg;

  public PacketSimpleResponce(String msg) {
    super(DataType.HTML);
    this.msg = msg;
  }

  @Override
  public String Write() {
    return msg;
  }

}
