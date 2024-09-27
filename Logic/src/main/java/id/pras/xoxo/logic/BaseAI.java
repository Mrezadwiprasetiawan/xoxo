package id.pras.xoxo.logic;

import java.util.ArrayList;
import java.util.Random;

public class BaseAI extends AI {
  private final Random randGen = new Random();
  private int bestScore;
  private ArrayList<Coord> minimaxMove=new ArrayList<>();

  public BaseAI(int[][] board, int role) {
    super(board, 3);
    setRole(role);
  }

  public BaseAI(int[][] board, int winSize, int role) {
    super(board, winSize);
    setRole(role);
  }

  @Override
  protected void thinking(long timeout) {
    setRunning(true);
    Thread worker =
        new Thread(
            () -> {
              int depth = 1;
              long startTime = System.nanoTime();

              while (System.nanoTime() - startTime < timeout) { // Perbaiki kondisi loop
                ArrayList<Coord> bestMoves = findBestMove(depth, startTime, timeout);

                if (!bestMoves.isEmpty()) {
                  int rand = randGen.nextInt(bestMoves.size());
                  setResult(bestMoves.get(rand).getX(), bestMoves.get(rand).getY());
                }

                System.out.println("current result"+getResult()[0]+","+getResult()[1]+ "with depth"+depth);
                depth++;
              }
        
              System.out.println("set Running to false");
              setRunning(false);
            });
    worker.start();
  }

  private ArrayList<Coord> findBestMove(int depth, long startTime, long timeout) {
    int role = getRole();
    ArrayList<Coord> bestMoves = new ArrayList<>();
    int maxScore = (role == 1) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    // debugging
    int counter = 0;

    //reset bestScore to currentRealScore
    bestScore=Evaluator.calcScore(board,winSize());
    
    
    for (Coord coord : possibleMoves(board)) {
      counter++;
      System.out.println("current delay" + (System.nanoTime() - startTime));
      // debugging
      System.out.println("Amount of loops =" + counter);

      board[coord.getX()][coord.getY()] = role; // Ubah langsung tanpa clone
      int realScore=Evaluator.calcScore(board,winSize());
      int score =
          minimax(
              board, depth - 1, -role, realScore, Integer.MIN_VALUE, Integer.MAX_VALUE, startTime, timeout);
      
      board[coord.getX()][coord.getY()] = 0; // Undo move
      
      // debugging
      System.out.println("Score =" + score);
      System.out.println("Current Evaluated Move =" + coord.getX() + "," + coord.getY());

      board[coord.getX()][coord.getY()] = 0; // Undo move

      if ((role == 1 && score > bestScore) || (role == -1 && score < bestScore)) {

        // debugging
        System.out.println("bestScore transition from " + bestScore + " to" + score);

        bestScore = score;

        // debugging
        System.out.println("Starting Clearing prevBestMove Array\n prevBestArray =\n");
        for (Coord coord2 : bestMoves) System.out.println(coord2.getX() + "," + coord.getY());

        bestMoves.clear();
        bestMoves.add(coord);

        // debugging
        System.out.println("Add current coord =" + coord.getX() + "," + coord.getY());
      } else if (score == bestScore) {
        // debugging
        System.out.println("Add current coord =" + coord.getX() + "," + coord.getY());
        bestMoves.add(coord);
      }
    }
    bestMoves.trimToSize();

    return bestMoves;
  }

  public int getBestScore() {
    return this.bestScore;
  }

  private int minimax(
      int[][] board,
      int depth,
      int currentRole,
      int prevScore,
      int alpha,
      int beta,
      long startTime,
      long timeout) {
    int score = prevScore+Evaluator.calcScore(board, winSize());
    if (depth == 0
        || score == Integer.MAX_VALUE
        || score == Integer.MIN_VALUE
        || possibleMoves(board).isEmpty()
        || System.nanoTime() - startTime >= timeout) {
      return score; // Kembalikan skor jika game over atau kedalaman maksimum
    }

    int bestScore = (currentRole == 1) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    for (Coord coord : possibleMoves(board)) {
      board[coord.getX()][coord.getY()] = currentRole; // Ubah langsung tanpa clone
      int nextScore = minimax(board, depth - 1, -currentRole, score, alpha, beta, startTime, timeout);

      if (currentRole == 1) {
        bestScore = Math.max(bestScore, nextScore);
        alpha = Math.max(alpha, bestScore);
      } else {
        bestScore = Math.min(bestScore, nextScore);
        beta = Math.min(beta, bestScore);
      }

      // Pruning
      if (beta <= alpha) {
        break;
      }
    }

    return bestScore;
  }

  private ArrayList<Coord> possibleMoves(int[][] board) {
    ArrayList<Coord> result = new ArrayList<>();
    for (int y = 0; y < board.length; y++) {
      for (int x = 0; x < board.length; x++) {
        if (board[x][y] == 0) result.add(new Coord(x, y));
      }
    }
    result.trimToSize();
    return result;
  }
}
