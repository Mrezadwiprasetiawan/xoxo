package id.pras.xoxo.logic;

import java.io.File;
import java.io.IOException;

// AI untuk membruteforce setiap langkah yang mungkin dan menyimpannya dalam file data
public class BFAI extends BaseAI {
  private String dataPath;
  private int ThreadCount;
  private boolean hasData = false;

  public BFAI(int[][] board, int role) {
    this(board, 3, 3, role);
    setThreadCount(1);
  }

  public BFAI(int[][] board, int sideSize, int winSize, int role) {
    super(board, sideSize, winSize, role);
  }

  public void train(long timeout) {
    long startTime = System.nanoTime();
    while (System.nanoTime() - startTime <= timeout) {}

    this.hasData = true;
  }

  @Override
  protected void thinking(long timeout) {
    setRunning(true);
    for (int i = 0; i < ThreadCount; i++) {
      new Thread(
              () -> {
                long startTime = System.nanoTime();
                while (System.nanoTime() - startTime <= timeout) {
                  if (hasData) {
                    //
                  } else {
                    train(5 * 1000_000);
                  }
                }
              })
          .start(); // Pastikan thread dijalankan
    }
    setRunning(false);
  }

  private void writeData(Data[] data, String filename) {}

  public String getDataPath() {
    return this.dataPath;
  }

  public void setDataPath(String dataPath) {
    this.dataPath = dataPath;
  }

  public int getThreadCount() {
    return this.ThreadCount;
  }

  public void setThreadCount(int ThreadCount) {
    this.ThreadCount = ThreadCount;
  }

  public boolean deleteData(String filename) throws IOException {
    File dataFile = new File(dataPath + "/" + filename);
    if (dataFile.exists() && dataFile.isFile() && dataFile.delete()) return true;
    return false;
  }
}
