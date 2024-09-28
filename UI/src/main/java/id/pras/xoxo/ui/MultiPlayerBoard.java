package id.pras.xoxo.ui;

import android.content.Context;
import android.view.MotionEvent;
import id.pras.xoxo.logic.Evaluator;

public class MultiPlayerBoard extends SimpleBoard {
  public MultiPlayerBoard(Context context) {
    super(context, 3, 3);
  }

  public MultiPlayerBoard(Context context, int sideSize, int winSize) {
    super(context, sideSize, winSize);
  }

  public MultiPlayerBoard(Context context, int sideSize, int winSize, byte player) {
    super(context, sideSize, winSize, player);
  }

  public MultiPlayerBoard(Context context, int sideSize, int winSize, byte player, int bg) {
    super(context, sideSize, winSize, player);
    setColor(bg);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == 0) {
      boolean changePlayer = false;
      int boardX = (int) (event.getX() - getOffsetX()) / getCellSize();
      int boardY = (int) (event.getY() - getOffsetY()) / getCellSize();
      if (boardX >= 0 && boardX < getBoard().length && boardY >= 0 && boardY < getBoard().length) {
        changePlayer = setValue(boardX, boardY);
      }

      if (changePlayer) {
        if (Evaluator.isWin(getBoard(), getWinSize(), getCurrentPlayer())) {
          setWinState(true);
        }else{
          setCurrentPlayer(getCurrentPlayer() == Board.X ? Board.O : Board.X);
        }
        return true;
      } else {
        return false;
      }
    }
    return super.onTouchEvent(event);
  }

}
