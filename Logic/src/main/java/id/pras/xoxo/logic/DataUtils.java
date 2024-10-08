package id.pras.xoxo.logic;

import id.pras.xoxo.logic.RuntimeException.UnsupportedFile;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Utility class for reading and writing game data.
 * 
 * <p>This class provides methods to write and read {@link Data} objects
 * to and from a file, ensuring the data is saved and retrieved 
 * with the correct format and versioning.</p>
 * 
 * <p>Public methods available:</p>
 * <ul>
 *   <li>{@link #write(Data[], String, String)}: Method to write an array 
 *       of Data objects to a specified file.</li>
 *   <li>{@link #write(Data[], String)}: Method to write an array 
 *       of Data objects to a specified path.</li>
 *   <li>{@link #read(String, String)}: Method to read an array of Data 
 *       objects from a specified file.</li>
 *   <li>{@link #read(String)}: Method to read an array of Data 
 *       objects from a specified path.</li>
 * </ul>
 * 
 * <p>This class is declared as final to prevent inheritance.</p>
 * 
 * @author M Reza Dwi Prasetiawan
 * @version 1.1
 */
public final class DataUtils {
  private static final String SIGN_NAME = "XOXO";
  public static final int VERSION = 2;

  private DataUtils() {}

  /**
   * Writes an array of Data objects to a specified file.
   * 
   * @param datas   the array of Data objects to write
   * @param path    the path where the file will be saved
   * @param filename the name of the file to write to
   * @throws IOException if an I/O error occurs
   */
  public static void write(Data[] datas, String path, String filename) throws IOException {
    write(datas, path + "/" + filename);
  }

  /**
   * Writes an array of Data objects to a specified path.
   * 
   * @param datas the array of Data objects to write
   * @param path  the path where the file will be saved
   * @throws IOException if an I/O error occurs
   */
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

  /**
   * Reads an array of Data objects from a specified file.
   * 
   * @param path     the path where the file is located
   * @param filename the name of the file to read from
   * @return an array of Data objects read from the file
   * @throws IOException if an I/O error occurs
   */
  public static Data[] read(String path, String filename) throws IOException {
    return read(path + "/" + filename);
  }

  /**
   * Reads an array of Data objects from a specified path.
   * 
   * @param path the path where the file is located
   * @return an array of Data objects read from the file
   * @throws IOException if an I/O error occurs
   * @throws UnsupportedFile if the file version or signature does not match
   */
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