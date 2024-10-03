package id.pras.xoxo.logic;

import java.util.Scanner;

/**
 * Represents a single-player game of XOXO (Tic-Tac-Toe) where the player competes 
 * against an AI opponent.
 * 
 * <p>This class manages the game board, player turns, and interacts with the AI 
 * to make moves. The player can choose their role (X or O) and the AI will 
 * automatically take the other role.</p>
 * 
 * <p>Public methods available:</p>
 * <ul>
 *   <li>{@link #SinglePlayerGame(int, int, int)}: Constructor to initialize the 
 *       game with specified board size, winning size, and player role.</li>
 *   <li>{@link #setAItimeout(int)}: Sets the timeout for the AI in seconds.</li>
 *   <li>{@link #setAItimeout(long)}: Sets the timeout for the AI in nanoseconds.</li>
 *   <li>{@link #startGame()}: Starts the game loop, allowing player and AI moves.</li>
 *   <li>{@link #main(String[])}: The main method to run the game with optional 
 *       command-line arguments for AI timeout.</li>
 * </ul>
 * 
 * <p>This class is designed to work with an AI implementation that adheres to 
 * the expected interface.</p>
 * 
 * <p>Note: The player can input their move via the console, and the AI will make 
 * its move automatically based on its logic.</p>
 * 
 * @version: 1.5
 * 
 * @author [M Reza Dwi Prasetiawan]
 */
public class SinglePlayerGame {
  private static final byte O = 1;
  private static final byte X = -1;
  private static final byte EMPTY = 0;

  private AI ai;
  private byte[][] board;
  private byte currentPlayer;
  private boolean isProcessingMove = false;
  private int winSize;
  private int sideSize;
  private long currTimeout;
  /**
   * Constructs a SinglePlayerGame instance with specified board size, 
   * winning size, and player role.
   * 
   * @param sideSize the size of the game board
   * @param winSize  the number of marks in a row needed to win
   * @param role     the role of the player (X or O)
   */
  public SinglePlayerGame(int sideSize, int winSize, byte role) {
    this.sideSize = sideSize;
    this.winSize = winSize;
    this.board = new byte[sideSize][sideSize];
    this.currentPlayer = role;
    initAI(role == O ? X : O);
  }

  /**
   * Initializes the AI with the specified role.
   * 
   * @param role the role of the AI (X or O)
   */
  private void initAI(byte role) {
    ai = new BaseAI(board, winSize, role);
    ai.setTimeout(1000_000); // Set initial timeout to 1 second
  }

  /**
   * Sets the AI timeout in seconds.
   * 
   * @param second the timeout in seconds
   */
  public void setAItimeout(int second) {
    currTimeout = second * 1_000_000_000L; // Convert to nanoseconds
    ai.setTimeout(currTimeout);
  }

  /**
   * Sets the AI timeout in nanoseconds.
   * 
   * @param nanoSec the timeout in nanoseconds
   */
  public void setAItimeout(long nanoSec) {
    currTimeout = nanoSec;
    ai.setTimeout(nanoSec);
  }

  /**
   * Starts the game loop, allowing player and AI to take turns making moves.
   * 
   * <p>The game continues until a player wins or an invalid move is made.</p>
   */
  public void startGame() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      printBoard();
      System.out.println("Skor saat ini :");
      printScore();

      if (isWin(currentPlayer)) {
        System.out.println("Player " + (currentPlayer == X ? "X" : "O") + " wins!");
        break;
      }

      if (currentPlayer == ai.getRole()) {
        System.out.println("AI is thinking...");
        isProcessingMove = true;
        ai.Start();
        while (ai.IsRunning()) {
          print("thinking"+".".repeat((int)(System.currentTimeMillis()%5)));
          try {
            Thread.sleep(10);
          }catch(InterruptedException err){
            err.printStackTrace();
          }
        }

        System.out.println("AI selesai berfikir");
        int[] result = ai.getResult();
        if (!setValue(result[0], result[1], ai.getRole())) {
          System.out.println("AI memilih posisi yang sudah diisi");
          System.out.println("menambah timeout 1 detik");
          setAItimeout(currTimeout + 1_000_000_000); // Increase timeout by 1 second
          continue;
        }
        currentPlayer = (currentPlayer == X ? O : X);
        isProcessingMove = false;
      } else {
        System.out.println(
            "Player " + (currentPlayer == X ? "X" : "O") + ", masukkan langkahmu (baris dan kolom): ");
        int row = scanner.nextInt();
        int col = scanner.nextInt();

        if (!setValue(row - 1, col - 1, currentPlayer)) {
          System.out.println("Posisi tidak valid, coba lagi.");
          continue;
        }

        currentPlayer = (currentPlayer == X ? O : X);
      }
    }

    scanner.close();
  }

  /**
   * Sets the value of the specified cell on the board for the given player.
   * 
   * @param row    the row index of the cell
   * @param col    the column index of the cell
   * @param player the player making the move
   * @return true if the move is successful; false otherwise
   */
  private boolean setValue(int row, int col, byte player) {
    if (row < 0 || row >= sideSize || col < 0 || col >= sideSize || board[row][col] != EMPTY) {
      return false;
    }
    board[row][col] = player;
    return true;
  }

  /**
   * Prints the current state of the game board to the console.
   */
  private void printBoard() {
    for (int i = 0; i < sideSize; i++) {
      for (int j = 0; j < sideSize; j++) {
        char symbol = (board[i][j] == X) ? 'X' : (board[i][j] == O) ? 'O' : '.';
        System.out.print(symbol + " ");
      }
      System.out.println();
    }
  }

  /**
   * Prints the current score of the game to the console.
   */
  private void printScore() {
    System.out.println(Evaluator.calcScore(board, winSize));
  }

  /**
   * Checks if the specified player has won the game.
   * 
   * @param player the player to check for victory
   * @return true if the player has won; false otherwise
   */
  private boolean isWin(byte player) {
    return Evaluator.isWin(board, winSize, player);
  }
  
  private static void print(String s){
    System.out.print("\33[2K\33[G"+s);
  }

  /**
   * The main method to run the game.
   * 
   * <p>Accepts optional command-line arguments to configure AI timeout.</p>
   * 
   * @param args command-line arguments for configuring the game
   */
  public static void main(String[] args) {
    int sideSize = 7;
    int winSize = 5;
    byte playerRole = O; // Player bisa memilih menjadi X atau O

    SinglePlayerGame game = new SinglePlayerGame(sideSize, winSize, playerRole);
    if (args.length != 0) {
      for (int i = 0; i < args.length; i++) {
        if (args[i].equals("-n") && i + 1 < args.length) {
          game.setAItimeout(Long.parseLong(args[++i]));
        } else if (args[i].equals("-s") && i + 1 < args.length) {
          game.setAItimeout(Integer.parseInt(args[++i]));
        }
      }
    }
    game.startGame();
  }
}