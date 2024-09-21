package id.pras.xoxo;

import id.pras.xoxo.runtimeexception.UnsupportedFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class DataReader {
  @Deprecated
  public DataReader() {}

  public static Data[] read(String path) throws IOException {
    File file = new File(path);
    FileInputStream fis = new FileInputStream(file);
    if (!file.exists()) {
      throw new FileNotFoundException("File doesnt exist" + file.getAbsolutePath());
    }
    if (!(fis.read() == DataWriter.SIGN_D
        && fis.read() == DataWriter.SIGN_M
        && fis.read() == DataWriter.SIGN_Y)) {
      throw new UnsupportedFile("File is Not Supported");
    }
    int DataLen = fis.read();
    Data[] data = new Data[DataLen];
    for (int i = 0; i < DataLen; i++) {
      data[i].setScore(fis.read());
      int seqLen = fis.read();
      int[] seqData = new int[seqLen];
      for (int j = 0; j < seqLen; j++) {
        seqData[j] = fis.read();
      }
      data[i].setSequenceData(seqData);
    }
    return data;
  }
}
