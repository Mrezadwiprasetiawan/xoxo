package id.pras.xoxo.logic;

public class BaseAI extends AI{
  
  
  /** Thread that handle thinking logic*/
  private Thread[] thinkers;
  
  public BaseAI(byte[][] board, int winSize, byte role){
    super(board,winSize,role);
    thinkers=new Thread[1];
  }
  
  @Override
  protected void thinking(long nanoTimeout) {
    long startTime= System.nanoTime();
    for(Thread thinker: thinkers){
      thinker=new Thread(()->{
        while(true){
          break;
        }
      });
      thinker.start();
    }
    setRunning(false);
  }
  
}
