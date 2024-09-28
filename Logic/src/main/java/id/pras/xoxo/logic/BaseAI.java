package id.pras.xoxo.logic;

import java.util.ArrayList;
import java.util.Random;

public class BaseAI extends AI {
  private final Random randGen = new Random();
  private int bestScore;
  private ArrayList<Coord> minimaxMove=new ArrayList<>();

  public BaseAI(byte[][] getBoard(), byte role) {
    super(getBoard(), 3);
    setRole(role);
  }

  public BaseAI(byte[][] getBoard(), int winSize, byte role) {
    super(getBoard(), winSize);
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
    bestScore=Evaluator.calcScore(getBoard()(),winSize());
    
    
    for (Coord coord : possibleMoves(getBoard()())) {
      counter++;
      System.out.println("current delay" + (System.nanoTime() - startTime));
      // debugging
      System.out.println("Amount of loops =" + counter);

      getBoard()[coord.getX()][coord.getY()] = getRole();
      int realScore=Evaluator.calcScore(getBoard()(),winSize());
      int score =
          minimax(
              getBoard(), depth - 1, -role, realScore, Integer.MIN_VALUE, Integer.MAX_VALUE, startTime, timeout);
      
      getBoard()[coord.getX()][coord.getY()] = 0; // Undo move
      
      // debugging
      System.out.println("Score =" + score);
      System.out.println("Current Evaluated Move =" + coord.getX() + "," + coord.getY());

      getBoard()[coord.getX()][coord.getY()] = 0; // Undo move

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
      int[][] getBoard(),
      int depth,
      int currentRole,
      int prevScore,
      int alpha,
      int beta,
      long startTime,
      long timeout) {
    int score = prevScore+Evaluator.calcScore(getBoard(), winSize());
    if (depth == 0
        || score == Integer.MAX_VALUE
        || score == Integer.MIN_VALUE
        || possibleMoves(getBoard()).isEmpty()
        || System.nanoTime() - startTime >= timeout) {
      return score; // Kembalikan skor jika game over atau kedalaman maksimum
    }

    int bestScore = (currentRole == 1) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    for (Coord coord : possibleMoves(getBoard())) {
      getBoard()[coord.getX()][coord.getY()] = currentRole;
      int nextScore = minimax(getBoard(), depth - 1, -currentRole, score, alpha, beta, startTime, timeout);

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

  private ArrayList<Coord> possibleMoves(byte[][] getBoard()) {
    ArrayList<Coord> result = new ArrayList<>();
    for (int y = 0; y < getBoard().length; y++) {
      for (int x = 0; x < getBoard().length; x++) {
        if (getBoard()[x][y] == 0) result.add(new Coord(x, y));
      }
    }
    result.trimToSize();
    return result;
  }
}
