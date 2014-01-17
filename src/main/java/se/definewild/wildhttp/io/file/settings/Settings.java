package se.definewild.wildhttp.io.file.settings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Settings {

  public class SettingsData {
    private String comment;
    private String name;
    private String value;

    public SettingsData(String name, String value, String comment) {
      this.name = name;
      this.value = value;
      this.comment = comment;
    }

    public String GetComment() {
      return comment;
    }

    public String GetName() {
      return name;
    }

    public String GetValue() {
      return value;
    }

    public void SetComment(String comment) {
      this.comment = comment;
    }

    public void SetName(String name) {
      this.name = name;
    }

    public void SetValue(String value) {
      this.value = value;
    }

  }

  private ArrayList<SettingsData> entries;
  private final File file;

  public Settings(File file) {
    this.file = file;
    this.entries = new ArrayList<>();

    Load();
  }

  public boolean Exists(String name) {
    if (name == null)
      return false;

    for (final SettingsData x : entries)
      if (x.name.equalsIgnoreCase(name))
        return true;
    return false;
  }

  public SettingsData Get(String name) {
    SettingsData data = null;
    for (final SettingsData x : entries)
      if (x.name.equalsIgnoreCase(name))
        data = x;
    return data;
  }

  public SettingsData Get(String name, String val) {
    SettingsData data = null;
    for (final SettingsData x : entries)
      if (x.name.equalsIgnoreCase(name))
        data = x;
    if (data != null)
      return data;
    entries.add(data = new SettingsData(name, val, null));
    Save();
    return data;
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

  public int GetInt(String name, int val) {
    final String data = Get(name, val + "").value;
    try {
      return Integer.parseInt(data);
    } catch (final Exception e) {
      Set(name, val + "");
      return val;
    }
  }

  public void Load() {
    String content = getContent();

    if (content == null)
      return;

    entries = new ArrayList<>();

    content = content.trim();

    final String split[] = content.trim().split("\n");
    for (int i = 0; i < split.length; i++)
      split[i] = split[i].trim();

    for (int i = 0; i < split.length; i++)
      try {
        final String line[] = split[i].split("#");
        String tmp[];
        String comment = "";
        String data = "";

        // line[0] will be the "name: value" part MANDATORY
        // line[1-...] will be the comment part OPTIONAL
        if (line.length > 1)
          for (int j = 1; j < line.length; j++)
            if (j > 1)
              comment += line[j] + "#";
            else
              comment += line[j];

        // tmp[0] will be the name part MANDATORY
        // tmp[1-...] will be the value part MANDATORY

        tmp = line[0].split(":");
        for (int j = 1; j < tmp.length; j++)
          if (j > 1)
            data += tmp[j] + ":";
          else
            data += tmp[j];

        tmp[0] = tmp[0].trim();
        data = data.trim();
        comment = comment.trim();

        if (tmp[0].equals(""))
          tmp[0] = null;
        if (data.equals(""))
          data = null;
        if (comment.equals(""))
          comment = null;

        entries.add(new SettingsData(tmp[0], data, comment));
      } catch (final Exception e) {
      }

  }

  // TODO: add comments on their own row
  /*
   * public void AddComment(String id, String comment) { if (!Exists(id)) Set(id, null, comment); }
   */

  public void Save() {
    final StringBuilder sb = new StringBuilder();
    for (final SettingsData data : entries) {
      if (data.value != null) {
        sb.append(data.name + ": " + data.value);
        if (data.comment != null)
          sb.append(" ");
      }
      if (data.comment != null)
        sb.append("#" + data.comment);

      sb.append("\n");
    }

    try {
      final DataOutputStream out = new DataOutputStream(new FileOutputStream(
          file));
      out.write(sb.toString().getBytes());
      out.close();
    } catch (final Exception e) {
    }
  }

  public String Set(String name, String val) {
    return Set(name, val, null);
  }

  public String Set(String name, String val, String comment) {
    SettingsData data = null;
    if (Exists(name))
      data = Get(name);
    if (data != null)
      data.value = val;
    else
      entries.add(new SettingsData(name, val, comment));
    Save();
    return val;
  }

  public String SetIfNotExists(String name, String val) {
    return SetIfNotExists(name, val, null);
  }

  public String SetIfNotExists(String name, String val, String comment) {
    if (!Exists(name))
      return Set(name, val, comment);

    return Get(name).value;
  }

}
