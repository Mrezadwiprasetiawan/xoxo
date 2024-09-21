package id.pras.xoxo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public abstract class Board extends View {
  public static final int NULL_HANDLE=0;
  public static final int X=1;
  public static final int O=2;

  private final int PADDING_PX = 50; // Tambahkan padding yang lebih besar
  private Paint paint = new Paint();
  private int[][] board;
  private int player;
  private int sideSize;
  private int cellSize;
  private int offsetX;
  private int offsetY;
  private int winSize;
  private boolean winState = false;

  public Board(Context context, int sideSize, int winSize) {
    super(context);
    if (sideSize < winSize) {
      throw new IllegalArgumentException(
          "winSize > sideSize\t" + "winSize :" + winSize + " sideSize :" + sideSize);
    }
    this.sideSize = sideSize;
    this.winSize = winSize;
    board = new int[sideSize][sideSize];
    paint.setStrokeWidth(4);
	  setCurrentPlayer(O);
  }

  public boolean setValue(int x, int y, int xoxo) {
    if (board[x][y] == NULL_HANDLE) {
      board[x][y] = xoxo;
      invalidate();
      return true; // Redraw the view after updating the value
    }
    return false;
  }

  public int[][] getBoard() {
    return board;
  }

  public int getSideSize() {
    return sideSize;
  }

  public int getWinSize() {
    return winSize;
  }

  protected void drawX(Canvas canvas, int x, int y) {
    // Tentukan ukuran margin untuk memperkecil X
    int margin = cellSize / 5;

    // Hitung startX dan startY dengan menambahkan margin
    int startX = offsetX + x * cellSize + margin;
    int startY = offsetY + y * cellSize + margin;

    // Tentukan endX dan endY dengan mengurangi margin dari cellSize
    int endX = offsetX + (x + 1) * cellSize - margin;
    int endY = offsetY + (y + 1) * cellSize - margin;

    paint.setColor(Color.RED);
    paint.setStrokeWidth(8);

    // Gambar dua garis diagonal
    canvas.drawLine(startX, startY, endX, endY, paint);
    canvas.drawLine(endX, startY, startX, endY, paint);
  }

  protected void drawO(Canvas canvas, int x, int y) {
    int startX = offsetX + x * cellSize;
    int startY = offsetY + y * cellSize;
    paint.setColor(Color.BLUE);
    paint.setStrokeWidth(8);
    paint.setStyle(Paint.Style.STROKE);
    canvas.drawCircle(startX + cellSize / 2, startY + cellSize / 2, cellSize / 4f, paint);
  }

  protected void drawBoard(Canvas canvas) {
    int widthPx = getWidth();
    int heightPx = getHeight();

    // Calculate cellSize based on the minimum dimension of the view
    cellSize = Math.min(widthPx - PADDING_PX, heightPx - PADDING_PX) / sideSize;

    // Calculate offsets to center the board
    offsetX = (widthPx - (cellSize * sideSize)) / 2;
    offsetY = (heightPx - (cellSize * sideSize)) / 2;

    // Draw the grid
    paint.setColor(Color.BLACK);
    paint.setStrokeWidth(4);
    for (int i = 0; i <= sideSize; i++) {
      float posX = offsetX + i * cellSize;
      float posY = offsetY + i * cellSize;
      canvas.drawLine(posX, offsetY, posX, offsetY + sideSize * cellSize, paint);
      canvas.drawLine(offsetX, posY, offsetX + sideSize * cellSize, posY, paint);
    }

    // Draw the X and O on the board
    for (int x = 0; x < sideSize; x++) {
      for (int y = 0; y < sideSize; y++) {
        if (board[x][y] != NULL_HANDLE) {
          if (board[x][y] == X) {
            drawX(canvas, x, y);
          } else if (board[x][y] == O) {
            drawO(canvas, x, y);
          }
        }
      }
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawBoard(canvas);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == 0) {
      boolean changePlayer = false;
      int boardX = (int) (event.getX() - offsetX) / cellSize;
      int boardY = (int) (event.getY() - offsetY) / cellSize;
      if (boardX >= 0 && boardX < sideSize && boardY >= 0 && boardY < sideSize) {
        changePlayer = setValue(boardX, boardY, player);
      }

      if (changePlayer) {
        setCurrentPlayer(player == X ? O : X);
				return true;
      }else{
				return false;
			}
    }
    return super.onTouchEvent(event);
  }

  public boolean getWinState() {
    return winState;
  }

  protected void setWinState(boolean winState) {
    this.winState = winState;
  }

  public int getCurrentPlayer() {
    return player;
  }

  protected void setCurrentPlayer(int player) {
    this.player = player;
  }
}
