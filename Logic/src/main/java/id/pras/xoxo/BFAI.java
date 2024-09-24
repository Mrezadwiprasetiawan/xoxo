package id.pras.xoxo;

import java.io.IOException;

// AI untuk membruteforce setiap langkah yang mungkin dan menyimpannya dalam file data
public class BFAI extends AI {
  private String dataPath;
  private int ThreadCount;
  private boolean hasData = false;

  public BFAI(int[][] board) {
    this(board, 3, 3);
    setThreadCount(1);
  }

  public BFAI(int[][] board, int sideSize, int winSize) {
    super(board, sideSize, winSize);
  }

  public void setThreadCount(int ThreadCount) {
    this.ThreadCount = ThreadCount;
  }

  public void train(long timeout) {
    long startTime = System.currentTimeMillis();
    while (System.currentTimeMillis() - startTime <= timeout) {
      // implementasi kode pelatihan disini
    }
  }

  @Override
  protected void Thinking() {
    setRunning(true);
    for (int i = 0; i < ThreadCount; i++) {
      new Thread(
              () -> {
                if (hasData) {
                  // implementasi pembacaan data di sini
                } else {
                  // implementasi kode bruteforcing di sini
                }
              })
          .start(); // Pastikan thread dijalankan
    }
    setRunning(false);
  }

  private void writeData(Data[] data) {}
}
