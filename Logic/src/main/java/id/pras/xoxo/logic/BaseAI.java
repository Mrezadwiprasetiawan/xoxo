package id.pras.xoxo.logic;

import java.util.ArrayList;
import java.util.Random;

public class BaseAI extends AI {
  private int enemyScore;
  private int selfScore;
  private final Random randGen = new Random(); // Inisiasi sekali untuk menghindari overhead

  public BaseAI(int[][] board, int role) {
    super(board, 3, 3);
    setRole(role);
  }

  public BaseAI(int[][] board, int sideSize, int winSize, int role) {
    super(board, sideSize, winSize);
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
              boolean gameOver = false;

              while (System.nanoTime() - startTime < timeout && !gameOver) {
                ArrayList<Coord> bestMoves = findBestMove(depth);
                if (!bestMoves.isEmpty()) {
                  int rand = randGen.nextInt(bestMoves.size());
                  setResult(bestMoves.get(rand).getX(), bestMoves.get(rand).getY());
                }

                // Cek apakah sudah game over (misalnya kemenangan, kekalahan, atau seri)
                int score = Evaluator.calcScore(board, winSize());
                gameOver =
                    getRole() == 1
                        ? Evaluator.calcScore(board, winSize()) == Integer.MAX_VALUE ? true : false
                        : Evaluator.calcScore(board, winSize()) == Integer.MIN_VALUE ? true : false;

                depth++;
              }
              setRunning(false);
            });
    worker.start();
  }

  private ArrayList<Coord> findBestMove(int depth) {
    int role = getRole();
    ArrayList<Coord> bestMoves = new ArrayList<>();
    int bestScore = (role == 1) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    for (Coord coord : possibleMoves(board)) {
      board[coord.getX()][coord.getY()] = role; // Ubah langsung tanpa clone
      int score = minimax(board, depth - 1, -role, Integer.MIN_VALUE, Integer.MAX_VALUE);
      board[coord.getX()][coord.getY()] = 0; // Undo move

      if ((role == 1 && score > bestScore) || (role == -1 && score < bestScore)) {
        bestScore = score;
        bestMoves.clear();
        bestMoves.add(coord);
      } else if (score == bestScore) {
        bestMoves.add(coord);
      }
    }
    bestMoves.trimToSize();

    return bestMoves;
  }

  private int minimax(int[][] board, int depth, int currentRole, int alpha, int beta) {
    int score = Evaluator.calcScore(board, winSize());
    if (depth == 0 || score == Integer.MAX_VALUE || score == Integer.MIN_VALUE) {
      return score; // Kembalikan skor jika game over atau kedalaman maksimum
    }

    int bestScore = (currentRole == 1) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    for (Coord coord : possibleMoves(board)) {
      board[coord.getX()][coord.getY()] = currentRole; // Ubah langsung tanpa clone
      int nextScore = minimax(board, depth - 1, -currentRole, alpha, beta);
      board[coord.getX()][coord.getY()] = 0; // Undo move

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
