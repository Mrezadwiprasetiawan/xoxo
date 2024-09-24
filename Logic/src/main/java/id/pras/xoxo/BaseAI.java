package id.pras.xoxo;

import java.util.ArrayList;
import java.util.Random;

public class BaseAI extends AI {
  private int ThreadCount;
  private int enemyScore;
  private int selfScore;

  public BaseAI(int[][] board) {
    super(board, 3, 3);
    ThreadCount = 1;
  }

  public BaseAI(int[][] board, int ThreadCount) {
    super(board, 3, 3);
    this.ThreadCount = ThreadCount;
  }

  public BaseAI(int[][] board, int sideSize, int winSize) {
    super(board, sideSize, winSize);
    this.ThreadCount = ThreadCount;
  }

  @Override
  protected void Thinking() {
    setRunning(true);
    for (int i = 0; i < ThreadCount; i++) {
      new Thread(
              () -> {
                ArrayList<Coord> bestMoves = findBestMove();
                Random randGen = new Random();
                int rand = randGen.nextInt(bestMoves.size());
                setResult(bestMoves.get(rand).X, bestMoves.get(rand).Y);
              })
          .start();
    }
    setRunning(false);
  }

  private ArrayList<Coord> findBestMove() {
    ArrayList<Coord> result = new ArrayList<>();
    return result;
  }

  private int selfBestScore(int[][] board, int winSize) {
    return 0;
  }

  private int enemyBestScore(int[][] board, int winSize) {
    return 0;
  }
}
