package id.pras.xoxo.ui;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

interface BoardInterface {
  public void drawO(int x, int y);
  public void drawX(int x, int y);
  public byte[][] getBoard();
  public int getWinSize();
  public boolean getWinState();
  public byte getCurrentPlayer();
}
