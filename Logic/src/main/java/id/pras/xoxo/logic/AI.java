package id.pras.xoxo.logic;

/**
 * Abstract class representing the AI logic for the XOXO game.
 * This class manages the AI's role, board state, and its decision-making process.
 * Subclasses must implement the {@link #thinking(long)} method to define AI's logic for deciding its move.
 * 
 * The class also provides mechanisms to start the AI's decision process and set timeouts for its calculations.
 *
 * <p>The AI operates on a board of variable size, and supports different win conditions
 * based on the number of consecutive pieces required for a victory.</p>
 *
 * @author [M Reza Dwi Prasetiawan]
 * @version 1.0
 */
public abstract class AI {
  
  /** Stores the result of AI's move, with X and Y coordinates. */
  private final int[] result = new int[2];

  /** Indicates whether the AI is currently running its decision process. */
  private volatile boolean isRunning = false;

  /** The current state of the board, where AI operates. */
  private volatile byte[][] board;

  /** The role of the AI, typically represented as 'X' or 'O'. */
  private byte role;

  /** The size of the board (number of rows/columns). */
  private int sideSize;

  /** The number of consecutive pieces required to win the game. */
  private int winSize;

  /** The maximum allowed time for AI to make its move, in nanoseconds. */
  private long timeout;
  

  /**
   * Constructs an AI with a specific board and win size.
   * Initializes the board and sets the win condition.
   *
   * @param board the board on which the AI operates
   * @param winSize the number of consecutive pieces required to win the game
   */
  public AI(byte[][] board, int winSize, byte role) {
    this.board = board.clone();
    this.sideSize = board.length;
    this.winSize = winSize;
    this.timeout = 2000_000_000;
    this.role = role;
  }
  
  /**
   * Sets the timeout for the AI's decision process.
   *
   * @param nanoTimeout the timeout duration in nanoseconds
   */
  public void setTimeout(long nanoTimeout) {
    if(nanoTimeout>=0x7fff_ffff_ffff_ffffL) return;
    this.timeout = nanoTimeout;
  }

  /**
   * Starts the AI's decision-making process and sets the AI to running state.
   * This method triggers the {@link #thinking(long)} method with the provided timeout.
   */
  public void Start() {
    setRunning(true);
    thinking(timeout);
  }

  /**
   * Abstract method to define AI's decision-making logic.
   * Subclasses must implement this method to define how the AI selects its next move.
   *
   * @param nanoTimeout the maximum time allowed for AI to make a move, in nanoseconds
   */
  protected abstract void thinking(long nanoTimeout);

  /**
   * Returns the role of the AI (X or O).
   *
   * @return the role of the AI
   */
  public byte getRole() {
    return this.role;
  }

  /**
   * Sets the role of the AI (X or O).
   *
   * @param role the role to assign to the AI
   */
  protected void setRole(byte role) {
    this.role = role;
  }

  /**
   * Returns the result of the AI's move, with X and Y coordinates.
   *
   * @return the AI's move as an array of two integers [X, Y]
   */
  public int[] getResult() {
    return this.result;
  }

  /**
   * Sets the result of the AI's move.
   *
   * @param X the X-coordinate of the move
   * @param Y the Y-coordinate of the move
   */
  protected void setResult(int X, int Y) {
    result[0] = X;
    result[1] = Y;
  }

  /**
   * Checks if the AI is currently running.
   *
   * @return true if the AI is running, false otherwise
   */
  public boolean IsRunning() {
    return this.isRunning;
  }

  /**
   * Sets the running state of the AI.
   *
   * @param isRunning the running state to set
   */
  protected void setRunning(boolean isRunning) {
    this.isRunning = isRunning;
  }

  /**
   * Returns the current state of the board on which the AI operates.
   *
   * @return the board as a 2D byte array
   */
  protected byte[][] getBoard() {
    return this.board;
  }

  /**
   * Sets the current state of the board.
   *
   * @param board the board to set, represented as a 2D byte array
   */
  public void setBoard(byte[][] board) {
    this.board = board.clone();
  }

  /**
   * Returns the size of the board (number of rows/columns).
   *
   * @return the size of the board
   */
  protected int sideSize() {
    return sideSize;
  }

  /**
   * Returns the number of consecutive pieces required to win the game.
   *
   * @return the number of consecutive pieces required to win
   */
  protected int winSize() {
    return winSize;
  }
  
}