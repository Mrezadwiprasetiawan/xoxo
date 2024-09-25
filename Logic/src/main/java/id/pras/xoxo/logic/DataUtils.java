package id.pras.xoxo.logic;

import id.pras.xoxo.runtimeexception.UnsupportedFile;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public final class DataUtils {
  private static final String SIGN_NAME = "XOXO";
  public static final int VERSION = 2;

  private DataUtils() {}

  public static void write(Data[] datas, String path, String filename) throws IOException {
    write(datas, path + "/" + filename);
  }

  public static void write(Data[] datas, String path) throws IOException {
    try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(path))) {
      dos.writeByte(VERSION);
      dos.writeUTF(SIGN_NAME);
      dos.writeInt(datas.length);

      for (Data data : datas) {
        dos.writeInt(data.getBoardSideSize());
        dos.writeInt(data.getWinSize());
        dos.writeInt(data.getScore());
        dos.writeInt(data.getSequenceData().length);
        for (int xoxo : data.getSequenceData()) dos.writeInt(xoxo);
      }
    }
  }

  public static Data[] read(String path, String filename) throws IOException {
    return read(path + "/" + filename);
  }

  public static Data[] read(String path) throws IOException {
    DataInputStream dis = new DataInputStream(new FileInputStream(path));
    if (!(dis.readByte() == VERSION && dis.readUTF() == SIGN_NAME)) {
      throw new UnsupportedFile("Versi tidak sesuai atau File tidak didukung");
    }
    Data[] result = new Data[dis.readInt()];
    for (int i = 0; i < result.length; i++) {
      int boardSideSize = dis.readInt();
      int winSize = dis.readInt();
      int score = dis.readInt();
      int[] seqData = new int[dis.readInt()];
      for (int j = 0; j < seqData.length; j++) seqData[j] = dis.readInt();
      result[i] = new Data(boardSideSize, winSize, score, seqData);
    }
    return result;
  }
}
