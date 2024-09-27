package id.pras.xoxo.ui;
import android.graphics.Canvas;

public interface BoardInterface {
  public void drawO(Canvas canvas, int x, int y);
  public void drawX(Canvas canvas, int x, int y);
  public void drawBoard(Canvas canvas);
  public boolean setValue(int x, int y);
  
}
