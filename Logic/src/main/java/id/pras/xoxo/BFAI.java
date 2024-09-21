package id.pras.xoxo;

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

  public void train(int i) {
    //implementasi kode pelatihan
    try{
      writeData(datas);
      this.hasData=true;
    }
    catch(IOException){
      this.hasData=false;
    }
  }

  @Override
  protected void Thinking() {
    setRunning(true);
    for (int i = 0; i < ThreadCount; i++) {
      new Thread(() -> {
        if (hasData) {
          // implementasi pembacaan data di sini
        } else {
          // implementasi kode bruteforcing di sini
        }
      }).start(); // Pastikan thread dijalankan
    }
    setRunning(false);
  }
  private void writeData(Data[] data){
     
  }
}