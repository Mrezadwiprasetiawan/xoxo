package id.pras.xoxo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class DataWriter {
  public static final int SIGN_D = 8;
  public static final int SIGN_M = 4;
  public static final int SIGN_Y = 2;
  public static final int CAFE = 0x0000CAFE;

  @Deprecated
  public DataWriter() {}

  public static boolean write(String path, Data[] data) throws IOException {
    File file = new File(path);
    if (!file.exists()) {
      if (!file.createNewFile()) {
        return false;
      }
    }
    FileOutputStream fos = new FileOutputStream(file);
    
    fos.write(SIGN_D);
    fos.write(SIGN_M);
    fos.write(SIGN_Y);
    fos.write(data.length);
    for (int i = 0; i < data.length; i++) {
      fos.write(data[i].getScore());
      fos.write(data[i].getSequenceData().length);
      for (int j = 0; j < data[i].getSequenceData().length; j++) {
        for (int dat : data[i].getSequenceData()) {
          fos.write(dat);
        }
      }
    }
    fos.flush();
    fos.close();
    return true;
  }
}
