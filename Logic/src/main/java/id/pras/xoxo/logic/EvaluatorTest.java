package id.pras.xoxo.logic;

public final class EvaluatorTest {
  private static final byte O=1;
  private static final byte X=-1;
  private static final byte N=0;
  private EvaluatorTest(){}
  public static void main(String[]args){
    byte[][] board={
      {O,N,N,N,N},
      {N,O,N,N,N},
      {N,N,O,N,N},
      {N,N,N,O,N},
      {N,N,N,N,O}
    };
    Evaluator.Sequence[] existSeqs=Evaluator.AllDirectionSequences(board,3);
    int count=0;
    System.out.println("Board = [");
    for(int x=0;x<board.length;x++){
      for(int y=0;y<board.length;y++) System.out.print(board[x][y]+", ");
      System.out.println();
    }
    System.out.println("]");
    for(Evaluator.Sequence seq:existSeqs){
      count++;
      System.out.println("blocked start : "+seq.getBlockedStart());
      System.out.println("blocked end : "+seq.getBlockedEnd());
      System.out.println("score : "+seq.score()+" with real score : "+ seq.realScore());
      System.out.print("direction : ");
      seq.getDirectType().print();
      System.out.println();
      
    }
    System.out.println("total sequences : "+ count);
  }
}
