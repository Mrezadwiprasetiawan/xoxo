package id.pras.xoxo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public abstract class Board extends View implements BoardInterface {

  public static final byte NULL = 0;
  public static final byte O = 1;
  public static final byte X = -1;

  private final int PADDING_PX = 50; // Default Board Padding in pixel
  private final byte[][] board;
  private byte player;
  private final int sideSize;
  private int cellSize;
  private float offsetX;
  private float offsetY;
  private final int winSize;
  private boolean winState = false;
  private Canvas canvas;

  public Board(Context context, int sideSize, int winSize) {
    this(context, sideSize, winSize, O);
  }

  public Board(Context context, int sideSize, int winSize, byte firstPlayer) {
    super(context);
    if (sideSize == 0) throw new BoardException(BoardException.ZERO_SIDE_SIZE_MSG);
    if (winSize > sideSize) throw new BoardException(BoardException.WIN_SIZE_EXCEPTION_MSG);
    if (player==NULL) throw new BoardException(BoardException.ZERO_ROLE_EXCEPTION);
    this.sideSize = sideSize;
    this.board = new byte[sideSize][sideSize];
    this.winSize = winSize;
    setCurrentPlayer(firstPlayer);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    this.canvas = canvas;
    calcOffsetAndCellSize();
    drawBoard();
  }

  protected void calcOffsetAndCellSize() {
    int widthPx = getWidth();
    int heightPx = getHeight();

    // Memghiting cellSize berdasarkan minimum dimensi dari view
    cellSize = Math.min(widthPx - PADDING_PX, heightPx - PADDING_PX) / sideSize;

    // Menghitung offset berdasarkan cellSize
    offsetX = (widthPx - (cellSize * sideSize)) / 2;
    offsetY = (heightPx - (cellSize * sideSize)) / 2;
  }

  private void drawBoard() {
    // Menggambar background berdasarkan offset
    drawBackground(canvas);
    // Draw the grid
    for (int i = 0; i <= sideSize; i++) {
      float posX = offsetX + i * cellSize;
      float posY = offsetY + i * cellSize;
      drawBoardLine(canvas, posX, offsetY, posX, offsetY + sideSize * cellSize);
      drawBoardLine(canvas, offsetX, posY, offsetX + sideSize * cellSize, posY);
    }

    // Draw the X and O on the board
    for (int x = 0; x < sideSize; x++) {
      for (int y = 0; y < sideSize; y++) {
        if (board[x][y] != NULL) {
          if (board[x][y] == X) {
            drawX(x, y);
          } else if (board[x][y] == O) {
            drawO(x, y);
          }
        }
      }
    }
  }

  // menggambar latar belakang harus diimplementasikan oleh kelas turunan
  protected abstract void drawBackground(Canvas canvas);

  /*
   * draw O & draw X method
   * membutuhkan argumen index x dan y dari papan mana yang akan diset
   * perhatikan bahwa metode ini hanya bisa dipanggil saat onDraw telah benar benar dipanggil setidaknya satu kali
   * pengecualian akan dilemparkan jika mencoba memanggil metode ini tanpa menunggu onDraw event dipanggil
   */
  public void drawO(int x, int y) {
    if (canvas == null)
      throw new RuntimeException("Cant draw O/X before Canvas initialized on onDraw event");
    drawO(canvas, x, y);
  }

  @Override
  public void drawX(int x, int y) {
    if (canvas == null)
      throw new RuntimeException("Cant draw O/X before Canvas initialized on onDraw event");
    drawX(canvas, x, y);
  }

  // Kelas turunan harus mengimplementasikan metode ini
  protected abstract void drawO(Canvas canvas, int x, int y);

  protected abstract void drawX(Canvas canvas, int x, int y);

  // menggambar garis kustom untuk papan
  protected abstract void drawBoardLine(Canvas canvas, float x0, float y0, float x1, float y1);

  protected boolean setValue(int x, int y, byte role) {
    if (x < 0 || y < 0 || x >= sideSize || y >= sideSize) return false;
    if (board[x][y] == NULL) {
      board[x][y] = role;

      // memanggil metode invalidate() agar board digambar ulang
      invalidate();
      return true;
    }
    return false;
  }

  protected boolean setValue(int x, int y) {
    return setValue(x, y, player);
  }

  // mengembalikan array dari papan saat ini
  @Override
  public byte[][] getBoard() {
    return this.board;
  }

  // mengembalikan ukuran kemenangan minimum
  @Override
  public int getWinSize() {
    return this.winSize;
  }

  // hanya kelas turunan yang dapat menset status kemenangan
  protected void setWinState(boolean winState) {
    this.winState = winState;
  }

  // mengembalikan status kemenangan
  @Override
  public boolean getWinState() {
    return this.winState;
  }

  public int getCellSize() {
    return this.cellSize;
  }

  public float getOffsetX() {
    return this.offsetX;
  }

  public float getOffsetY() {
    return this.offsetY;
  }

  // hanya kelas turunan yang dapat menset nilai player
  protected void setCurrentPlayer(byte player) {
    this.player = player;
  }

  // mengembalikan player saat ini
  @Override
  public byte getCurrentPlayer() {
    return player;
  }

  public class BoardException extends RuntimeException {

    public static final String ZERO_ROLE_EXCEPTION="Role cant be 0";
    public static final String NOT_SQUARE_MSG = "Board not Square";
    public static final String WIN_SIZE_EXCEPTION_MSG = " Winsize greater than board size!";
    public static final String ZERO_SIDE_SIZE_MSG = "You cant create Board with zero side size!";

    private BoardException(String msg) {
      super(msg);
    }
  }
}
