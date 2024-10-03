/**
 * Interface that defines essential methods for the Tic-Tac-Toe (XOXO) game board.
 * This interface provides methods to draw O and X symbols on the board, 
 * retrieve the current state of the board, and get details about the game such as 
 * the win condition and the current player.
 *
 * <p>Classes implementing this interface, such as {@link Board} and its implementations, 
 * are responsible for managing the game logic and rendering the board.</p>
 *
 * @see Board
 * @see SimpleBoard
 *
 * @author [M Reza Dwi Prasetiawan]
 * @version 1.0
 */
interface BoardInterface {

  /**
   * Draws an O symbol at the specified coordinates on the board.
   *
   * @param x the x-coordinate on the board grid
   * @param y the y-coordinate on the board grid
   */
  public void drawO(int x, int y);

  /**
   * Draws an X symbol at the specified coordinates on the board.
   *
   * @param x the x-coordinate on the board grid
   * @param y the y-coordinate on the board grid
   */
  public void drawX(int x, int y);

  /**
   * Retrieves the current state of the board, represented as a 2D byte array.
   *
   * @return the board state where each cell is either O, X, or empty (null)
   */
  public byte[][] getBoard();

  /**
   * Returns the win condition size for the game.
   *
   * @return the minimum number of symbols in a row required to win the game
   */
  public int getWinSize();

  /**
   * Returns the current win state of the game.
   *
   * @return {@code true} if a player has won the game, {@code false} otherwise
   */
  public boolean getWinState();

  /**
   * Returns the current player of the game.
   *
   * @return the player currently taking a turn (O or X)
   */
  public byte getCurrentPlayer();
}