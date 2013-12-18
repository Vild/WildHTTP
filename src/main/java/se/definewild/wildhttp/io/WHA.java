package se.definewild.wildhttp.io;

import java.util.Random;
import java.util.Vector;

public class WHA {
  public static byte[] CleanUp(byte[] data) {
    final Vector<Byte> ret = new Vector<Byte>();
    for (int i = 0; i < data.length; i++)
      if (data[i] < 33)
        ret.add((byte) (data[i] + 33));
      else if (data[i] > 126 && data[i] - 33 < 126)
        ret.add((byte) (data[i] - 33));
      else
        ret.add(data[i]);

    final byte[] x = new byte[ret.size()];
    for (int i = 0; i < ret.size(); i++)
      x[i] = ret.get(i).byteValue();
    return x;
  }

  public static byte[] MixChars(byte[] str) {
    final byte[] ret = new byte[str.length];
    final Random rand = new Random(0xBEEFBEEF);
    final Vector<Byte> chars = new Vector<>();
    for (final byte c : str)
      chars.add(c);

    for (int i = 0; i < chars.size(); i++) {
      final int x = rand.nextInt(chars.size());
      ret[i] = chars.elementAt(x);
      chars.remove(x);
    }

    return ret;
  }

  public static String RandomChar(long seed, int length) {
    String ret = "";
    final Random rand = new Random(seed);

    for (int i = 0; i < length; i++) {
      final int x = rand.nextInt('z' - 'a' + 10);
      if (x < 10)
        ret += x;
      else
        ret += 'a' + (x - 10);
    }

    return ret;
  }

  public static byte[] RemoveEnd(byte[] data, int length) {
    final byte newdata[] = new byte[length];
    int i = 0;
    for (i = 0; i < length && i < data.length; i++)
      newdata[i] = data[i];
    for (; i < length; i++)
      newdata[i] = (byte) '0';
    return newdata;
  }

  public static byte[] WHA0(byte[] data) {
    try {
      final Vector<Byte> ret = new Vector<Byte>();
      for (final char c : RandomChar(0xBEEFB00B, 32).toCharArray())
        ret.add((byte) c);
      long all = data.length;
      for (final byte c : data)
        all += c;

      ret.add(Long.toString(all, 24).getBytes()[0]);
      ret.add(Long.toString(all * 2, 24).getBytes()[0]);
      ret.add(Long.toString(all * 4, 24).getBytes()[0]);
      ret.add(Long.toString(all * 8, 24).getBytes()[0]);

      for (int i = 0; i < Math.pow(data.length, 2) / 2; i++)
        ret.add(data[(Math.abs(i * data.length * 0x1337) % data.length)]);

      final byte[] x = new byte[ret.size()];
      for (int i = 0; i < ret.size(); i++)
        x[i] = ret.get(i).byteValue();
      return RemoveEnd(CleanUp(MixChars(x)), 32);
    } catch (final Exception e) {
      e.printStackTrace();
      return new byte[32];
    }
  }
}
