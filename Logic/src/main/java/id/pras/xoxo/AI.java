package id.pras.xoxo;

public abstract class AI {
  private final int[] result = new int[2];
  private boolean isRunning = false;
  protected int[][] board;
  private int role;
  private final int NULL_HANDLE = 0;
  private final int X = 1;
  private final int O = 2;
  private int sideSize;
  private int winSize;

  public AI(int[][] board,int sideSize, int winSize) {
    this.board = board;
	  this.sideSize=sideSize;
	  this.winSize=winSize;
  }

  public void Start() {
    setRunning(true);
    Thinking();
  }

  protected abstract void Thinking();

  public int NULL_HANDLE() {
    return this.NULL_HANDLE;
  }

  public int X() {
    return this.X;
  }

  public int O() {
    return this.O;
  }

  public int getRole() {
    return this.role;
  }

  protected void setRole(int role) {
    this.role = role;
  }

  public int[] getResult() {
    return this.result;
  }
  
  protected void setResult(int X, int Y){
		result[0]=X;
		result[1]=Y;
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

  protected void setBoard(int[][] board) {
    this.board = board;
  }
  
  protected int sideSize(){
		return sideSize;
	}
	
	protected int winSize(){
		return winSize;
	}
}
