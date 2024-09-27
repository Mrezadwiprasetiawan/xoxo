package id.pras.xoxo.ui;

import android.content.Context;
import android.view.MotionEvent;

public class MultiPlayerBoard extends Board {
  public MultiPlayerBoard(Context context) {
    super(context, 3, 3);
  }

  public MultiPlayerBoard(Context context, int sideSize, int winSize) {
    super(context, sideSize, winSize);
  }

  public MultiPlayerBoard(Context context, int sideSize, int winSize, int player) {
    super(context, sideSize, winSize);
    setCurrentPlayer(player);
  }

  public MultiPlayerBoard(Context context, int sideSize, int winSize, int player, int bg) {
    super(context, sideSize, winSize);
    setCurrentPlayer(player);
    setBackgroundColor(bg);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    boolean parentChangePlayer = event.getAction() == 0 && super.onTouchEvent(event);
    return super.onTouchEvent(event);
  }

  public void setFirstPlayer(int player) {
    setCurrentPlayer(player);
  }
}
