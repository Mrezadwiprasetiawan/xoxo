package id.pras.xoxo.logic;

public abstract class AI {
  private final int[] result = new int[2];
  private boolean isRunning = false;
  protected int[][] board;
  private int role;
  private int sideSize;
  private int winSize;
  private long timeout;

  public AI(int[][] board, int winSize) {
    this.board = board;
    this.sideSize = board.length;
    this.winSize = winSize;
  }
  
  public void setTimeout(long nanoTimeout){
    this.timeout=nanoTimeout;
  }

  public void Start() {
    setRunning(true);
    thinking(timeout);
  }

  protected abstract void thinking(long timeoutNano);


  public int getRole() {
    return this.role;
  }

  protected void setRole(int role) {
    this.role = role;
  }

  public int[] getResult() {
    return this.result;
  }

  protected void setResult(int X, int Y) {
    result[0] = X;
    result[1] = Y;
  }

  public boolean IsRunning() {
    return this.isRunning;
  }

  protected void setRunning(boolean isRunning) {
    this.isRunning = isRunning;
  }

  protected int[][] getBoard() {
    return this.board;
  }

  public void setBoard(int[][] board) {
    this.board = board;
  }

  protected int sideSize() {
    return sideSize;
  }

  protected int winSize() {
    return winSize;
  }
}
