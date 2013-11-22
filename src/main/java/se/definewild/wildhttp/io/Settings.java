package se.definewild.wildhttp.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Settings {

  public class SettingsData {
    private String name;
    private Object value;
    private String comment;

    public SettingsData(String name, Object value, String comment) {
      this.name = name;
      this.value = value;
      this.comment = comment;
    }

    public String GetName() {
      return name;
    }

    public void SetName(String name) {
      this.name = name;
    }

    public Object GetValue() {
      return value;
    }

    public void SetValue(Object value) {
      this.value = value;
    }

    public String GetComment() {
      return comment;
    }

    public void SetComment(String comment) {
      this.comment = comment;
    }

  }

  private File file;
  private ArrayList<SettingsData> entries;

  public Settings(File file) {
    this.file = file;
    this.entries = new ArrayList<>();

    Load();
  }

  public void Load() {
    String content = getContent().trim();
    if (content != null)
      entries = new ArrayList<>();

    String split[] = content.split("\n");
    for (int i = 0; i < split.length; i++)
      split[i] = split[i].trim();

    for (int i = 0; i < split.length; i++) {
      try {
        String line[] = split[i].split("#");
        String tmp[];
        String comment = "";
        String data = "";

        if (line.length > 1)
          for (int j = 1; j < line.length; j++)
            if (j > 1)
              comment += line[j] + "#";
            else
              comment += line[j];

        tmp = line[0].split(":");
        for (int j = 1; j < tmp.length; j++)
          if (j > 1)
            data += tmp[j] + ":";
          else
            data += tmp[j];
        entries.add(new SettingsData(tmp[0], data, comment));
      } catch (Exception e) {
      }
    }

  }

  public void Save() {

  }

  private String getContent() {
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

}
