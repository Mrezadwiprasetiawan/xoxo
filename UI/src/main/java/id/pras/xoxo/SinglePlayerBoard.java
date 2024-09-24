package id.pras.xoxo;

import android.content.Context;
import android.view.MotionEvent;

public class SinglePlayerBoard extends Board {
  private AI ai;

  public SinglePlayerBoard(Context context) {
    super(context, 5, 3);
    setCurrentPlayer(Board.O);
    initAI(Board.X);
  }

  public SinglePlayerBoard(Context context, int sideSize, int winSize) {
    super(context, sideSize, winSize);
    setCurrentPlayer(Board.O);
    initAI(Board.X);
  }

  public SinglePlayerBoard(Context context, int sideSize, int winSize, int player) {
    super(context, sideSize, winSize);
    setCurrentPlayer(player);
    initAI(player == Board.O ? Board.X : Board.O);
  }

  public SinglePlayerBoard(Context context, int player) {
    super(context, 5, 3);
    setCurrentPlayer(player);
    initAI(player == Board.O ? Board.X : Board.O);
  }

  private void initAI(int role) {
    ai = new BaseAI(getBoard(), 8);
    ai.setRole(role);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    boolean parentChangePlayer = event.getAction() == 0 && super.onTouchEvent(event);
    if (parentChangePlayer) {
      int role = getCurrentPlayer();
      if (role == ai.getRole() && !(ai.IsRunning())) {
        ai.Start();
      }
      // implementasi ai selesai berfikir
      return true;
    }
    return super.onTouchEvent(event);
  }
}
